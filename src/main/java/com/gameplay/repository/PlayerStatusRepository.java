package com.gameplay.repository;

//import com.utils.LogService;
import com.databaseOperations.DatabaseConnection;
import com.exceptions.PlayerNotFoundException;
import com.gameplay.entity.PlayerEntity;
import com.utils.Converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Jay Patel
 */
public class PlayerStatusRepository implements IPlayerStatusRepository {

	public static final String FETCH_ALL_PLAYER_IN_TEAM_QUERY = "SELECT * FROM player_status p WHERE p.club_id = ?";
	public static final String FETCH_PLAYER_BY_ID_QUERY = "SELECT * FROM player_status WHERE player_id = ?";
	public static final String DELETE_ALL_PLAYERS_QUERY = "DELETE FROM player_status";
	public static final String COPY_PLAYERS_DATA_QUERY = "INSERT INTO player_status (SELECT * FROM players)";
	public static final String FETCH_ALL_PLAYER_QUERY = "SELECT * FROM player_status";

	private Connection databaseConnection;
//	private LogService logService;
	private PreparedStatement statement;

	public PlayerStatusRepository() {
		try {
//			logService = new LogService();
			databaseConnection = DatabaseConnection.getInstance().getConnection();
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public List<PlayerEntity> fetchAllPlayers(Integer teamId) {
		try {
			statement = databaseConnection.prepareStatement(FETCH_ALL_PLAYER_IN_TEAM_QUERY);
			statement.setString(1, Converter.convertToTeamIdString(teamId));
			ResultSet resultSet = statement.executeQuery();
			List<PlayerEntity> playerList = Converter.resultSetToEntityList(resultSet, PlayerEntity.class);
			return playerList;
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	@Override
	public PlayerEntity fetchPlayer(Integer playerId) {
		try {
			statement = databaseConnection.prepareStatement(FETCH_PLAYER_BY_ID_QUERY);
			statement.setString(1, Converter.convertToPlayerIdString(playerId));
			ResultSet resultSet = statement.executeQuery();
			List<PlayerEntity> players = Converter.resultSetToEntityList(resultSet, PlayerEntity.class);
			if (players.isEmpty()) {
				throw new PlayerNotFoundException();
			}
			return players.get(0);
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean deleteAllPlayers() {
		try {
			statement = databaseConnection.prepareStatement(DELETE_ALL_PLAYERS_QUERY);
			statement.executeUpdate();
			return Boolean.TRUE;
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean copyFromPlayerToPlayerStatus() {
		try {
			statement = databaseConnection.prepareStatement(COPY_PLAYERS_DATA_QUERY);
			statement.executeUpdate();
			return Boolean.TRUE;
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return Boolean.FALSE;
	}

	@Override
	public List<PlayerEntity> fetchAllPlayers() {
		try {
			statement = databaseConnection.prepareStatement(FETCH_ALL_PLAYER_QUERY);
			ResultSet resultSet = statement.executeQuery();
			return Converter.resultSetToEntityList(resultSet, PlayerEntity.class);
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean savePlayer(PlayerEntity player) {
		try {
			statement = databaseConnection.prepareStatement(Converter.convertToUpdateQuery(player));
			statement.executeUpdate();
			return Boolean.TRUE;
		} catch (Exception e) {
//			logService.log(Level.SEVERE, e.getMessage());
		}
		return Boolean.FALSE;
	}
}
