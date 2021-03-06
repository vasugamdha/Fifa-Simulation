package com.gameplay.UserInput;

import com.gameplay.controller.TeamSelectionController;
import com.gameplay.entity.PlayerEntity;
import com.gameplay.repository.PlayerStatusRepository;
import com.io.*;
import com.utils.Constants;
import com.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jay Patel
 */
public class AddPlayerTest {

	private IOutputStream outputStreamMock = StandardOutputStream.getInstance();
	private IInputStream inputStreamMock = Mockito.mock(IInputStream.class);
	private IErrorStream errorStreamMock = StandardErrorStream.getInstance();
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	private AddPlayer addPlayer = new AddPlayer();

	@BeforeEach
	public void init() throws Exception {

		FieldSetter outputStreamNew = new FieldSetter(outputStreamMock,
				StandardOutputStream.class.getDeclaredField("printStream"));
		outputStreamNew.set(new PrintStream(outputStreamCaptor));

		FieldSetter outputStream = new FieldSetter(addPlayer, AddPlayer.class.getDeclaredField("outputStream"));
		outputStream.set(outputStreamMock);

		FieldSetter inputStream = new FieldSetter(addPlayer, AddPlayer.class.getDeclaredField("inputStream"));
		inputStream.set(inputStreamMock);

		FieldSetter errorStreamNew = new FieldSetter(errorStreamMock,
				StandardErrorStream.class.getDeclaredField("printStream"));
		errorStreamNew.set(new PrintStream(outputStreamCaptor));

		FieldSetter errorStream = new FieldSetter(addPlayer, AddPlayer.class.getDeclaredField("errorStream"));
		errorStream.set(errorStreamMock);

	}

	@Test
	public void executeSelectionPlayerNotFoundTest() {
		TeamSelectionController teamSelectionController = new TeamSelectionController(Constants.CLUBS[0], Constants.CLUBS[1]);
		PlayerStatusRepository playerStatusRepository = new PlayerStatusRepository();
		List<PlayerEntity> players = teamSelectionController.getSquads().get(0).getPlaying11().entrySet().stream()
				.map(entrySet -> playerStatusRepository.fetchPlayer(entrySet.getKey().getPlayerId()))
				.collect(Collectors.toList());
		Mockito.when(inputStreamMock.readInteger()).thenReturn(1);
		assertFalse(addPlayer.executeSelection(Arrays.asList(players.get(0)), players),
				"addPlayer() method is not working as expected");
	}

	@Test
	public void executeSelectionPlayerAlreadySelectedTest() {
		TeamSelectionController teamSelectionController = new TeamSelectionController(Constants.CLUBS[0], Constants.CLUBS[1]);
		PlayerStatusRepository playerStatusRepository = new PlayerStatusRepository();
		List<PlayerEntity> players = teamSelectionController.getSquads().get(0).getPlaying11().entrySet().stream()
				.map(entrySet -> playerStatusRepository.fetchPlayer(entrySet.getKey().getPlayerId()))
				.collect(Collectors.toList());
		Mockito.when(inputStreamMock.readInteger())
				.thenReturn(Converter.convertToPlayerIdInteger(players.get(0).getPlayerId()));
		assertFalse(addPlayer.executeSelection(Arrays.asList(players.get(0)), players),
				"addPlayer() method is not working as expected");
	}

	@Test
	public void executeSelectionValidTest() {
		TeamSelectionController teamSelectionController = new TeamSelectionController(Constants.CLUBS[0], Constants.CLUBS[1]);
		PlayerStatusRepository playerStatusRepository = new PlayerStatusRepository();
		List<PlayerEntity> players = teamSelectionController.getSquads().get(0).getPlaying11().entrySet().stream()
				.map(entrySet -> playerStatusRepository.fetchPlayer(entrySet.getKey().getPlayerId()))
				.collect(Collectors.toList());
		Mockito.when(inputStreamMock.readInteger())
				.thenReturn(Converter.convertToPlayerIdInteger(players.get(1).getPlayerId()));
		assertTrue(addPlayer.executeSelection(new ArrayList<>(Arrays.asList(players.get(0))), players),
				"addPlayer() method is not working as expected");
	}
}
