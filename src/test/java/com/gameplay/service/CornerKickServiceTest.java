package com.gameplay.service;

import com.Constants;
import com.models.PlayerModel;
import com.models.PlayingPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

/**
 * @author vasugamdha
 */

public class CornerKickServiceTest {
    HashMap<PlayerModel, PlayingPosition> team1 = new HashMap<>();
    HashMap<PlayerModel,PlayingPosition> team2 = new HashMap<>();

    public CornerKickServiceTest(){
        team1.put(Constants.PLAYERS[0], PlayingPosition.FORWARD);
        team1.put(Constants.PLAYERS[12], PlayingPosition.MIDFIELDER);
        team1.put(Constants.PLAYERS[20], PlayingPosition.DEFENDER);
        team1.put(Constants.PLAYERS[39], PlayingPosition.GOALKEEPER);

        team2.put(Constants.PLAYERS[1], PlayingPosition.FORWARD);
        team2.put(Constants.PLAYERS[8], PlayingPosition.MIDFIELDER);
        team2.put(Constants.PLAYERS[13], PlayingPosition.DEFENDER);
        team2.put(Constants.PLAYERS[18], PlayingPosition.GOALKEEPER);
    }

    @Test
    public void getSetPieceTypeTest(){
        IKickService cornerKick = new CornerKickService(team1,team2);
        assertEquals("List12",cornerKick.getSetPiece().getClass().getSimpleName());
    }
}