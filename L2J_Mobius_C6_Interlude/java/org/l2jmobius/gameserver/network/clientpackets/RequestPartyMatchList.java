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

import org.l2jmobius.gameserver.model.PartyMatchRoom;
import org.l2jmobius.gameserver.model.PartyMatchRoomList;
import org.l2jmobius.gameserver.model.PartyMatchWaitingList;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ExPartyRoomMember;
import org.l2jmobius.gameserver.network.serverpackets.PartyMatchDetail;

/**
 * author: Gnacik Packetformat Rev650 cdddddS
 */
public class RequestPartyMatchList extends GameClientPacket
{
	private static final Logger LOGGER = Logger.getLogger(RequestPartyMatchList.class.getName());
	
	private int _roomid;
	private int _membersmax;
	private int _lvlmin;
	private int _lvlmax;
	private int _loot;
	private String _roomtitle;
	
	@Override
	protected void readImpl()
	{
		_roomid = readD();
		_membersmax = readD();
		_lvlmin = readD();
		_lvlmax = readD();
		_loot = readD();
		_roomtitle = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final PlayerInstance _player = getClient().getPlayer();
		if (_player == null)
		{
			return;
		}
		
		if (_roomid > 0)
		{
			final PartyMatchRoom _room = PartyMatchRoomList.getInstance().getRoom(_roomid);
			if (_room != null)
			{
				LOGGER.info("PartyMatchRoom #" + _room.getId() + " changed by " + _player.getName());
				_room.setMaxMembers(_membersmax);
				_room.setMinLvl(_lvlmin);
				_room.setMaxLvl(_lvlmax);
				_room.setLootType(_loot);
				_room.setTitle(_roomtitle);
				
				for (PlayerInstance _member : _room.getPartyMembers())
				{
					if (_member == null)
					{
						continue;
					}
					
					_member.sendPacket(new PartyMatchDetail(_player, _room));
					_member.sendPacket(SystemMessageId.PARTY_ROOM_REVISED);
				}
			}
		}
		else
		{
			final int _maxid = PartyMatchRoomList.getInstance().getMaxId();
			
			final PartyMatchRoom _room = new PartyMatchRoom(_maxid, _roomtitle, _loot, _lvlmin, _lvlmax, _membersmax, _player);
			
			LOGGER.info("PartyMatchRoom #" + _maxid + " created by " + _player.getName());
			
			// Remove from waiting list, and add to current room
			PartyMatchWaitingList.getInstance().removePlayer(_player);
			PartyMatchRoomList.getInstance().addPartyMatchRoom(_maxid, _room);
			
			if (_player.isInParty())
			{
				for (PlayerInstance ptmember : _player.getParty().getPartyMembers())
				{
					if (ptmember == null)
					{
						continue;
					}
					if (ptmember == _player)
					{
						continue;
					}
					
					ptmember.setPartyRoom(_maxid);
					
					_room.addMember(ptmember);
				}
			}
			
			_player.sendPacket(new PartyMatchDetail(_player, _room));
			_player.sendPacket(new ExPartyRoomMember(_player, _room, 1));
			
			_player.sendPacket(SystemMessageId.PARTY_ROOM_CREATED);
			
			_player.setPartyRoom(_maxid);
			_player.broadcastUserInfo();
		}
	}
}