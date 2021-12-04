package com.gameplay.UserInput;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.database_operations.DatabaseImport;
import com.models.ClubModel;

/**
 * @author Jay Patel
 */
public class TeamRepository implements ITeamRepository {

	@Override
	public List<ClubModel> fetchAllTeams() {
		return Arrays.asList(DatabaseImport.getClubs());
	}

	@Override
	public Boolean isTeamIdExists(Integer teamId) {
		return Arrays.asList(DatabaseImport.getClubs()).stream().map(club -> club.getClubId())
				.collect(Collectors.toList()).contains(teamId);
	}
}
