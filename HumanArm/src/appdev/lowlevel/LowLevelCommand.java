package appdev.lowlevel;

import model.CompleteArm;
import Jama.Matrix;
import appdev.cmdGen.CommandApplicator;
import appdev.regression.Regression;

public abstract class LowLevelCommand {

	protected CompleteArm _arm;

	protected Regression _regression;

	protected CommandApplicator _applicator;

	public CommandApplicator getApplicator() {
		return _applicator;
	}

	public void setApplicator(CommandApplicator applicator) {
		this._applicator = applicator;
	}

	public LowLevelCommand(CompleteArm arm, Regression regression,
			CommandApplicator applicator) {
		this._arm = arm;
		this._regression = regression;
		this._applicator = applicator;
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
