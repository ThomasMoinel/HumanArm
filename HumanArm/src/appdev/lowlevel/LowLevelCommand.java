package appdev.lowlevel;

import model.CompleteArm;
import Jama.Matrix;
import appdev.regression.Regression;

public abstract class LowLevelCommand {

	protected CompleteArm _arm;
	
	protected Regression _regression;
	
	public LowLevelCommand(CompleteArm arm, Regression regression) {
		this._arm = arm;
		this._regression = regression;
	}

	public void setRegression(Regression regression) {
		this._regression = regression;
	}

	public Regression getRegression() {
		return _regression;
	}

	public abstract Matrix reachGoal(Matrix goal, int nbSubGoal,
			double precision);
}
