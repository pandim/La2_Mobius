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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.commons.network.PacketWriter;
import org.l2jmobius.gameserver.model.PartyMatchRoom;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.network.OutgoingPackets;

/**
 * @author Gnacik
 */
public class PartyMatchDetail implements IClientOutgoingPacket
{
	private final PartyMatchRoom _room;
	
	/**
	 * @param player
	 * @param room
	 */
	public PartyMatchDetail(PlayerInstance player, PartyMatchRoom room)
	{
		_room = room;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PARTY_ROOM_INFO.writeId(packet);
		packet.writeD(_room.getId());
		packet.writeD(_room.getMaxMembers());
		packet.writeD(_room.getMinLvl());
		packet.writeD(_room.getMaxLvl());
		packet.writeD(_room.getLootType());
		packet.writeD(_room.getLocation());
		packet.writeS(_room.getTitle());
		packet.writeH(59064);
		return true;
	}
}
