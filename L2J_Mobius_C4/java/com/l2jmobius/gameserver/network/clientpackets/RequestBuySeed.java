/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jmobius.gameserver.network.clientpackets;

import com.l2jmobius.Config;
import com.l2jmobius.gameserver.datatables.ItemTable;
import com.l2jmobius.gameserver.instancemanager.CastleManager;
import com.l2jmobius.gameserver.instancemanager.CastleManorManager;
import com.l2jmobius.gameserver.instancemanager.CastleManorManager.SeedProduction;
import com.l2jmobius.gameserver.model.L2ItemInstance;
import com.l2jmobius.gameserver.model.L2Object;
import com.l2jmobius.gameserver.model.actor.instance.L2ManorManagerInstance;
import com.l2jmobius.gameserver.model.actor.instance.L2PcInstance;
import com.l2jmobius.gameserver.model.entity.Castle;
import com.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import com.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jmobius.gameserver.network.serverpackets.StatusUpdate;
import com.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import com.l2jmobius.gameserver.templates.L2Item;
import com.l2jmobius.gameserver.util.Util;

/**
 * Format: cdd[dd] c // id (0xC4) d // manor id d // seeds to buy [ d // seed id d // count ]
 * @author l3x
 */
public class RequestBuySeed extends L2GameClientPacket
{
	private static final String _C__C4_REQUESTBUYSEED = "[C] C4 RequestBuySeed";
	
	private int _count;
	private int _manorId;
	private int[] _items; // size _count * 2
	
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		_count = readD();
		
		if ((_count > 500) || ((_count * 8) < _buf.remaining())) // check values
		{
			_count = 0;
			return;
		}
		
		_items = new int[_count * 2];
		
		for (int i = 0; i < _count; i++)
		{
			final int itemId = readD();
			_items[(i * 2) + 0] = itemId;
			final long cnt = readD();
			if ((cnt > Integer.MAX_VALUE) || (cnt < 1))
			{
				_count = 0;
				_items = null;
				return;
			}
			_items[(i * 2) + 1] = (int) cnt;
		}
	}
	
	@Override
	public void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getManor().tryPerformAction("BuySeed"))
		{
			return;
		}
		
		if (_count < 1)
		{
			sendPacket(new ActionFailed());
			return;
		}
		
		L2Object target = player.getTarget();
		
		if (!(target instanceof L2ManorManagerInstance))
		{
			target = player.getLastFolkNPC();
		}
		
		if (!(target instanceof L2ManorManagerInstance))
		{
			return;
		}
		
		long totalPrice = 0;
		int slots = 0;
		int totalWeight = 0;
		
		final Castle castle = CastleManager.getInstance().getCastleById(_manorId);
		
		for (int i = 0; i < _count; i++)
		{
			final int seedId = _items[(i * 2) + 0];
			final int count = _items[(i * 2) + 1];
			int price = 0;
			int residual = 0;
			
			final SeedProduction seed = castle.getSeed(seedId, CastleManorManager.PERIOD_CURRENT);
			price = seed.getPrice();
			residual = seed.getCanProduce();
			
			if (price <= 0)
			{
				return;
			}
			
			if (residual < count)
			{
				return;
			}
			
			totalPrice += count * price;
			
			final L2Item template = ItemTable.getInstance().getTemplate(seedId);
			totalWeight += count * template.getWeight();
			if (!template.isStackable())
			{
				slots += count;
			}
			else if (player.getInventory().getItemByItemId(seedId) == null)
			{
				slots++;
			}
		}
		
		if (totalPrice > Integer.MAX_VALUE)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + Integer.MAX_VALUE + " adena worth of goods.", Config.DEFAULT_PUNISH);
			return;
		}
		
		if (!player.getInventory().validateWeight(totalWeight))
		{
			sendPacket(new SystemMessage(SystemMessage.WEIGHT_LIMIT_EXCEEDED));
			return;
		}
		
		if (!player.getInventory().validateCapacity(slots))
		{
			sendPacket(new SystemMessage(SystemMessage.SLOTS_FULL));
			return;
		}
		
		// Charge buyer
		if ((totalPrice < 0) || !player.reduceAdena("Buy", (int) totalPrice, target, false))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
			return;
		}
		
		// Adding to treasury for Manor Castle
		castle.addToTreasuryNoTax((int) totalPrice);
		
		// Proceed the purchase
		final InventoryUpdate playerIU = new InventoryUpdate();
		for (int i = 0; i < _count; i++)
		{
			final int seedId = _items[(i * 2) + 0];
			int count = _items[(i * 2) + 1];
			if (count < 0)
			{
				count = 0;
			}
			
			// Update Castle Seeds Amount
			final SeedProduction seed = castle.getSeed(seedId, CastleManorManager.PERIOD_CURRENT);
			seed.setCanProduce(seed.getCanProduce() - count);
			if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
			{
				CastleManager.getInstance().getCastleById(_manorId).updateSeed(seed.getId(), seed.getCanProduce(), CastleManorManager.PERIOD_CURRENT);
			}
			
			// Add item to Inventory and adjust update packet
			final L2ItemInstance item = player.getInventory().addItem("Buy", seedId, count, player, target);
			
			if (item.getCount() > count)
			{
				playerIU.addModifiedItem(item);
			}
			else
			{
				playerIU.addNewItem(item);
			}
			
			// Send Char Buy Messages
			SystemMessage sm = null;
			sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
			sm.addItemName(seedId);
			sm.addNumber(count);
			player.sendPacket(sm);
		}
		
		// Send update packets
		player.sendPacket(playerIU);
		
		final StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
	}
	
	@Override
	public String getType()
	{
		return _C__C4_REQUESTBUYSEED;
	}
}