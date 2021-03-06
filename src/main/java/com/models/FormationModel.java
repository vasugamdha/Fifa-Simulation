package com.models;

import com.utils.Constants;
//import com.utils.LogService;
import com.exceptions.FormationInvalidException;

import java.util.List;
import java.util.logging.Level;

/**
 * @author prashitpatel
 */
public class FormationModel {
	int defenders;
	int midfielders;
	int defensiveMidfielders;
	int attackingMidfielders;
	int forwards;
	public FormationType type;
	public boolean extendedFormation;
//	LogService logService = new LogService();
	public FormationModel(int defenders, int midfielders, int forwards, FormationType type) {
		try{
			checkFormationValidity(defenders, midfielders, forwards);
			this.extendedFormation = false;
			this.defenders = defenders;
			this.midfielders = midfielders;
			this.forwards = forwards;
			this.type = type;
		} catch (FormationInvalidException e) {
//			logService.log(Level.SEVERE, "ERROR! " + e);
		}
	}

	public FormationModel(int defenders, int defensiveMidfielders, int attackingMidfielders, int forwards, FormationType type) {
		try{
			checkFormationValidity(defenders, defensiveMidfielders, attackingMidfielders, forwards);

			this.extendedFormation = true;
			this.defenders = defenders;
			this.defensiveMidfielders = defensiveMidfielders;
			this.attackingMidfielders = attackingMidfielders;
			this.forwards = forwards;
			this.type = type;
		} catch (FormationInvalidException e) {
//			logService.log(Level.SEVERE, "ERROR! " + e);
		}
	}

	private void checkFormationValidity(int defenders, int midfielders, int forwards) throws FormationInvalidException {
		if(defenders >= Constants.DEFENDERS_MIN && defenders <= Constants.DEFENDERS_MAX && midfielders >= Constants.MIDFIELDERS_MIN
				&& midfielders <= Constants.MIDFIELDERS_MAX && forwards >= Constants.FORWARDS_MIN && forwards <= Constants.FORWARDS_MAX &&
				(defenders+midfielders+forwards) == Constants.FORMATION_SUM) {
			return;
		} else {
			throw new FormationInvalidException();
		}
	}

	private void checkFormationValidity(int defenders, int defensiveMidfielders, int attackingMidfielders, int forwards) throws FormationInvalidException {
		if(defenders >= Constants.DEFENDERS_MIN && defenders <= Constants.DEFENDERS_MAX && defensiveMidfielders >= Constants.DEFENSIVE_MIDFIELDERS_MIN
				&& defensiveMidfielders <= Constants.DEFENSIVE_MIDFIELDERS_MAX && attackingMidfielders >= Constants.ATTACKING_MIDFIELDERS_MIN &&
				attackingMidfielders <= Constants.ATTACKING_MIDFIELDERS_MAX && forwards >= Constants.FORWARDS_MIN && forwards <= Constants.FORWARDS_MAX &&
				(defenders+defensiveMidfielders+attackingMidfielders+forwards) == Constants.FORMATION_SUM) {
			return;
		} else {
			throw new FormationInvalidException();
		}
	}

	@Override
	public String toString() {
		return extendedFormation ? defenders+"-"+defensiveMidfielders+"-"+attackingMidfielders+"-"+forwards :
				defenders+"-"+midfielders+"-"+forwards;
	}

	public List<Integer> getFormation() {
		return extendedFormation ? List.of(defenders, defensiveMidfielders, attackingMidfielders, forwards) :
				List.of(defenders, midfielders, forwards);
	}
}

