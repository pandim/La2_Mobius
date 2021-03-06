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
package org.l2jmobius.gameserver.instancemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.datatables.SkillTable;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.WorldObject;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.instance.PlayerInstance;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.entity.siege.Castle;
import org.l2jmobius.gameserver.model.entity.siege.Siege;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;

public class SiegeManager
{
	private static final Logger LOGGER = Logger.getLogger(SiegeManager.class.getName());
	
	public static final SiegeManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private int _attackerMaxClans = 500; // Max number of clans
	private int _attackerRespawnDelay = 20000; // Time in ms. Changeable in siege.config
	private int _defenderMaxClans = 500; // Max number of clans
	private int _defenderRespawnDelay = 10000; // Time in ms. Changeable in siege.config
	
	// Siege settings
	private Map<Integer, List<SiegeSpawn>> _artefactSpawnList;
	private Map<Integer, List<SiegeSpawn>> _controlTowerSpawnList;
	
	private int _controlTowerLosePenalty = 20000; // Time in ms. Changeable in siege.config
	private int _flagMaxCount = 1; // Changeable in siege.config
	private int _siegeClanMinLevel = 4; // Changeable in siege.config
	private int _siegeLength = 120; // Time in minute. Changeable in siege.config
	
	private boolean _teleport_to_siege = false;
	private boolean _teleport_to_siege_town = false;
	
	private SiegeManager()
	{
		load();
	}
	
	public void addSiegeSkills(PlayerInstance character)
	{
		character.addSkill(SkillTable.getInstance().getInfo(246, 1), false);
		character.addSkill(SkillTable.getInstance().getInfo(247, 1), false);
	}
	
	/**
	 * Return true if character summon<BR>
	 * <BR>
	 * @param creature The Creature of the creature can summon
	 * @param isCheckOnly
	 * @return
	 */
	public boolean checkIfOkToSummon(Creature creature, boolean isCheckOnly)
	{
		if ((creature == null) || !(creature instanceof PlayerInstance))
		{
			return false;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
		PlayerInstance player = (PlayerInstance) creature;
		Castle castle = CastleManager.getInstance().getCastle(player);
		
		if ((castle == null) || (castle.getCastleId() <= 0))
		{
			sm.addString("You must be on castle ground to summon this");
		}
		else if (!castle.getSiege().getIsInProgress())
		{
			sm.addString("You can only summon this during a siege.");
		}
		else if ((player.getClanId() != 0) && (castle.getSiege().getAttackerClan(player.getClanId()) == null))
		{
			sm.addString("You can only summon this as a registered attacker.");
		}
		else
		{
			return true;
		}
		
		if (!isCheckOnly)
		{
			player.sendPacket(sm);
		}
		
		return false;
	}
	
	public boolean checkIsRegisteredInSiege(Clan clan)
	{
		
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (checkIsRegistered(clan, castle.getCastleId()) && (castle.getSiege() != null) && castle.getSiege().getIsInProgress())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true if the clan is registered or owner of a castle<BR>
	 * <BR>
	 * @param clan The Clan of the player
	 * @param castleid
	 * @return
	 */
	public boolean checkIsRegistered(Clan clan, int castleid)
	{
		if (clan == null)
		{
			return false;
		}
		
		if (clan.getHasCastle() > 0)
		{
			return true;
		}
		
		boolean register = false;
		
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM siege_clans where clan_id=? and castle_id=?");
			statement.setInt(1, clan.getClanId());
			statement.setInt(2, castleid);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next())
			{
				register = true;
				break;
			}
			
			rs.close();
			statement.close();
		}
		catch (Exception e)
		{
			LOGGER.info("Exception: checkIsRegistered(): " + e.getMessage());
			e.printStackTrace();
		}
		return register;
	}
	
	public void removeSiegeSkills(PlayerInstance character)
	{
		character.removeSkill(SkillTable.getInstance().getInfo(246, 1));
		character.removeSkill(SkillTable.getInstance().getInfo(247, 1));
	}
	
	private final void load()
	{
		LOGGER.info("Initializing SiegeManager");
		InputStream is = null;
		try
		{
			is = new FileInputStream(new File(Config.SIEGE_CONFIG_FILE));
			Properties siegeSettings = new Properties();
			siegeSettings.load(is);
			
			// Siege setting
			_attackerMaxClans = Integer.decode(siegeSettings.getProperty("AttackerMaxClans", "500"));
			_attackerRespawnDelay = Integer.decode(siegeSettings.getProperty("AttackerRespawn", "30000"));
			_controlTowerLosePenalty = Integer.decode(siegeSettings.getProperty("CTLossPenalty", "20000"));
			_defenderMaxClans = Integer.decode(siegeSettings.getProperty("DefenderMaxClans", "500"));
			_defenderRespawnDelay = Integer.decode(siegeSettings.getProperty("DefenderRespawn", "20000"));
			_flagMaxCount = Integer.decode(siegeSettings.getProperty("MaxFlags", "1"));
			_siegeClanMinLevel = Integer.decode(siegeSettings.getProperty("SiegeClanMinLevel", "4"));
			_siegeLength = Integer.decode(siegeSettings.getProperty("SiegeLength", "120"));
			
			// Siege Teleports
			_teleport_to_siege = Boolean.parseBoolean(siegeSettings.getProperty("AllowTeleportToSiege", "false"));
			_teleport_to_siege_town = Boolean.parseBoolean(siegeSettings.getProperty("AllowTeleportToSiegeTown", "false"));
			
			// Siege spawns settings
			_controlTowerSpawnList = new HashMap<>();
			_artefactSpawnList = new HashMap<>();
			
			for (Castle castle : CastleManager.getInstance().getCastles())
			{
				List<SiegeSpawn> _controlTowersSpawns = new ArrayList<>();
				
				for (int i = 1; i < 0xFF; i++)
				{
					String _spawnParams = siegeSettings.getProperty(castle.getName() + "ControlTower" + i, "");
					
					if (_spawnParams.length() == 0)
					{
						break;
					}
					
					StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
					
					try
					{
						final int x = Integer.parseInt(st.nextToken());
						final int y = Integer.parseInt(st.nextToken());
						final int z = Integer.parseInt(st.nextToken());
						final int npc_id = Integer.parseInt(st.nextToken());
						final int hp = Integer.parseInt(st.nextToken());
						
						_controlTowersSpawns.add(new SiegeSpawn(castle.getCastleId(), x, y, z, 0, npc_id, hp));
					}
					catch (Exception e)
					{
						LOGGER.warning("Error while loading control tower(s) for " + castle.getName() + " castle.");
					}
				}
				
				List<SiegeSpawn> _artefactSpawns = new ArrayList<>();
				
				for (int i = 1; i < 0xFF; i++)
				{
					String _spawnParams = siegeSettings.getProperty(castle.getName() + "Artefact" + i, "");
					
					if (_spawnParams.length() == 0)
					{
						break;
					}
					
					StringTokenizer st = new StringTokenizer(_spawnParams.trim(), ",");
					
					try
					{
						final int x = Integer.parseInt(st.nextToken());
						final int y = Integer.parseInt(st.nextToken());
						final int z = Integer.parseInt(st.nextToken());
						final int heading = Integer.parseInt(st.nextToken());
						final int npc_id = Integer.parseInt(st.nextToken());
						
						_artefactSpawns.add(new SiegeSpawn(castle.getCastleId(), x, y, z, heading, npc_id));
					}
					catch (Exception e)
					{
						LOGGER.warning("Error while loading artefact(s) for " + castle.getName() + " castle.");
					}
				}
				
				_controlTowerSpawnList.put(castle.getCastleId(), _controlTowersSpawns);
				_artefactSpawnList.put(castle.getCastleId(), _artefactSpawns);
			}
		}
		catch (Exception e)
		{
			LOGGER.warning("Error while loading siege data.");
			e.printStackTrace();
			
		}
		finally
		{
			
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<SiegeSpawn> getArtefactSpawnList(int _castleId)
	{
		if (_artefactSpawnList.containsKey(_castleId))
		{
			return _artefactSpawnList.get(_castleId);
		}
		return null;
	}
	
	public List<SiegeSpawn> getControlTowerSpawnList(int _castleId)
	{
		if (_controlTowerSpawnList.containsKey(_castleId))
		{
			return _controlTowerSpawnList.get(_castleId);
		}
		return null;
	}
	
	public int getAttackerMaxClans()
	{
		return _attackerMaxClans;
	}
	
	public int getAttackerRespawnDelay()
	{
		return _attackerRespawnDelay;
	}
	
	public int getControlTowerLosePenalty()
	{
		return _controlTowerLosePenalty;
	}
	
	public int getDefenderMaxClans()
	{
		return _defenderMaxClans;
	}
	
	public int getDefenderRespawnDelay()
	{
		return _defenderRespawnDelay;
	}
	
	public int getFlagMaxCount()
	{
		return _flagMaxCount;
	}
	
	public Siege getSiege(WorldObject activeObject)
	{
		return getSiege(activeObject.getX(), activeObject.getY(), activeObject.getZ());
	}
	
	public Siege getSiege(int x, int y, int z)
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (castle.getSiege().checkIfInZone(x, y, z))
			{
				return castle.getSiege();
			}
		}
		return null;
	}
	
	public int getSiegeClanMinLevel()
	{
		return _siegeClanMinLevel;
	}
	
	public int getSiegeLength()
	{
		return _siegeLength;
	}
	
	public List<Siege> getSieges()
	{
		final List<Siege> _sieges = new ArrayList<>();
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			_sieges.add(castle.getSiege());
		}
		return _sieges;
	}
	
	/**
	 * @return the _teleport_to_siege
	 */
	public boolean is_teleport_to_siege_allowed()
	{
		return _teleport_to_siege;
	}
	
	/**
	 * @return the _teleport_to_siege_town
	 */
	public boolean is_teleport_to_siege_town_allowed()
	{
		return _teleport_to_siege_town;
	}
	
	public class SiegeSpawn
	{
		Location _location;
		private final int _npcId;
		private final int _heading;
		private final int _castleId;
		private int _hp;
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id)
		{
			_castleId = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npcId = npc_id;
		}
		
		public SiegeSpawn(int castle_id, int x, int y, int z, int heading, int npc_id, int hp)
		{
			_castleId = castle_id;
			_location = new Location(x, y, z, heading);
			_heading = heading;
			_npcId = npc_id;
			_hp = hp;
		}
		
		public int getCastleId()
		{
			return _castleId;
		}
		
		public int getNpcId()
		{
			return _npcId;
		}
		
		public int getHeading()
		{
			return _heading;
		}
		
		public int getHp()
		{
			return _hp;
		}
		
		public Location getLocation()
		{
			return _location;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final SiegeManager INSTANCE = new SiegeManager();
	}
}
