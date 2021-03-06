package com.gameplay.TeamSelection.PlayerSelection;

import com.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * @author prashitpatel
 */
class ForwardCategoryTest {
	IPlayerCategory forwards = new ForwardCategory(Arrays.asList(Constants.PLAYERS[0]));

	@Test
	void notNullTest() {
		assertNotNull(forwards);
	}
}