package com.gameplay.UserInput;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import com.gameplay.controller.TeamSelectionController;
import com.gameplay.UserInput.validation.TeamValidation;
import com.gameplay.controller.IPlayerStatusController;
import com.gameplay.controller.ITeamController;
import com.gameplay.controller.IUserPlayersController;
import com.gameplay.controller.IUserTeamController;
import com.gameplay.entity.PlayerEntity;
import com.gameplay.repository.PlayerStatusRepository;
import com.io.IErrorStream;
import com.io.IInputStream;
import com.io.IOutputStream;
import com.io.StandardErrorStream;
import com.io.StandardOutputStream;
import com.models.ClubModel;
import com.models.StartOrResumeOptions;
import com.models.UserTeamModel;

/**
 * @author Jay Patel
 */
public class UserInputFunctionTest {

	private IOutputStream outputStreamMock = StandardOutputStream.getInstance();
	private IInputStream inputStreamMock = Mockito.mock(IInputStream.class);
	private IErrorStream errorStreamMock = StandardErrorStream.getInstance();
	private IUserTeamController userTeamControllerMock = Mockito.mock(IUserTeamController.class);
	private ITeamController teamControllerMock = Mockito.mock(ITeamController.class);
	private IPlayerStatusController playerStatusControllerMock = Mockito.mock(IPlayerStatusController.class);
	private IUserPlayersController userPlayersControllerMock = Mockito.mock(IUserPlayersController.class);
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	private TeamValidation teamValidationMock = Mockito.mock(TeamValidation.class);

	private IUserInputFunction userInputFunction = new UserInputFunction();

	@BeforeEach
	public void init() throws Exception {
		FieldSetter teamController = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("teamController"));
		teamController.set(teamControllerMock);

		FieldSetter playerStatusController = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("playerStatusController"));
		playerStatusController.set(playerStatusControllerMock);

		FieldSetter userTeamController = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("userTeamController"));
		userTeamController.set(userTeamControllerMock);

		FieldSetter userPlayerController = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("userPlayersController"));
		userPlayerController.set(userPlayersControllerMock);

		FieldSetter outputStreamNew = new FieldSetter(outputStreamMock,
				StandardOutputStream.class.getDeclaredField("printStream"));
		outputStreamNew.set(new PrintStream(outputStreamCaptor));

		FieldSetter outputStream = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("outputStream"));
		outputStream.set(outputStreamMock);

		FieldSetter inputStream = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("inputStream"));
		inputStream.set(inputStreamMock);

		FieldSetter errorStreamNew = new FieldSetter(errorStreamMock,
				StandardErrorStream.class.getDeclaredField("printStream"));
		errorStreamNew.set(new PrintStream(outputStreamCaptor));

		FieldSetter errorStream = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("errorStream"));
		errorStream.set(errorStreamMock);

		FieldSetter teamValidation = new FieldSetter(userInputFunction,
				UserInputFunction.class.getDeclaredField("teamValidation"));
		teamValidation.set(teamValidationMock);
	}

	@Test
	public void resumePreviousGameTest() {
		UserTeamModel userTeamModel = new UserTeamModel();
		userTeamModel.setSeasonPlayed(1);
		Mockito.when(userTeamControllerMock.fetchUserTeamModel()).thenReturn(userTeamModel);
		Mockito.when(inputStreamMock.readInteger()).thenReturn(3,
				StartOrResumeOptions.RESUME_PREVIOUS_GAME.getOption());
		assertTrue(userInputFunction.teamSelection(), "teamSelection() method is not working as expected.");
	}

	@Test
	public void startNewGameWithoutPlayerSelectionTest() {
		UserTeamModel userTeamModel = new UserTeamModel();
		userTeamModel.setSeasonPlayed(0);
		ClubModel club = new ClubModel(1, "name", null, 0);
		Mockito.when(userTeamControllerMock.fetchUserTeamModel()).thenReturn(userTeamModel);
		Mockito.when(teamControllerMock.fetchAllTeams()).thenReturn(Arrays.asList(club));
		Mockito.when(inputStreamMock.readInteger()).thenReturn(1, 1, 3, 2);
		Mockito.when(userTeamControllerMock.deleteUserTeamModel()).thenReturn(Boolean.TRUE);
		Mockito.when(userTeamControllerMock.setUserTeam(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(teamControllerMock.isTeamIdExists(Mockito.any())).thenReturn(Boolean.FALSE, Boolean.TRUE);
		Mockito.when(userTeamControllerMock.customizePlayingXI(Mockito.any())).thenReturn(Boolean.TRUE);
		assertTrue(userInputFunction.teamSelection(), "teamSelection() method is not working as expected.");
	}

	@Test
	public void startNewGameWithPlayerSelectionTest() {
		TeamSelectionController teamSelectionController = new TeamSelectionController("Liverpool", "Manchester United");
		PlayerStatusRepository playerStatusRepository = new PlayerStatusRepository();
		UserTeamModel userTeamModel = new UserTeamModel();
		userTeamModel.setSeasonPlayed(1);
		ClubModel club = new ClubModel(11, "name", null, 0);
		List<PlayerEntity> players = teamSelectionController.getSquads().get(0).getPlaying11().entrySet().stream()
				.map(entrySet -> playerStatusRepository.fetchPlayer(entrySet.getKey().getPlayerId()))
				.collect(Collectors.toList());
		Mockito.when(userTeamControllerMock.fetchUserTeamModel()).thenReturn(userTeamModel);
		Mockito.when(teamControllerMock.fetchAllTeams()).thenReturn(Arrays.asList(club));
		Mockito.when(inputStreamMock.readInteger()).thenReturn(1, 1, 1, 1, 2, 5);
		Mockito.when(userTeamControllerMock.deleteUserTeamModel()).thenReturn(Boolean.TRUE);
		Mockito.when(userTeamControllerMock.setUserTeam(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(teamControllerMock.isTeamIdExists(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(userTeamControllerMock.customizePlayingXI(Mockito.any())).thenReturn(Boolean.TRUE);
		Mockito.when(playerStatusControllerMock.fetchPlayer(Mockito.any())).thenReturn(players.get(0), players.get(1),
				players.get(2), players.get(3), players.get(4), players.get(5), players.get(6), players.get(7),
				players.get(8), players.get(9), players.get(10), players.get(0));
		Mockito.when(teamValidationMock.isTeamValid(Mockito.any())).thenReturn(Boolean.TRUE);
		assertTrue(userInputFunction.teamSelection(), "teamSelection() method is not working as expected.");
	}
}
