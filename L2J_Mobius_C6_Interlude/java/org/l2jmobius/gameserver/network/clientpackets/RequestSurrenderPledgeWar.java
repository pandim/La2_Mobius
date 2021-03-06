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
package org.l2jmobius.gameserver.network.clientpackets;

import java.util.logging.Logger;

import org.l2jmobius.gameserver.datatables.sql.ClanTable;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

public class RequestSurrenderPledgeWar extends GameClientPacket
{
	private static Logger LOGGER = Logger.getLogger(RequestSurrenderPledgeWar.class.getName());
	
	private String _pledgeName;
	private Clan _clan;
	private PlayerInstance _player;
	
	@Override
	protected void readImpl()
	{
		_pledgeName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		_player = getClient().getPlayer();
		if (_player == null)
		{
			return;
		}
		
		_clan = _player.getClan();
		if (_clan == null)
		{
			return;
		}
		
		final Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (clan == null)
		{
			_player.sendMessage("No such clan.");
			_player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		LOGGER.info("RequestSurrenderPledgeWar by " + getClient().getPlayer().getClan().getName() + " with " + _pledgeName);
		
		if (!_clan.isAtWarWith(clan.getClanId()))
		{
			_player.sendMessage("You aren't at war with this clan.");
			_player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		SystemMessage msg = new SystemMessage(SystemMessageId.YOU_HAVE_SURRENDERED_TO_THE_S1_CLAN);
		msg.addString(_pledgeName);
		_player.sendPacket(msg);
		_player.deathPenalty(false);
		ClanTable.getInstance().deleteClanWars(_clan.getClanId(), clan.getClanId());
	}
}
