package appdev.lowlevel;

import model.CompleteArm;
import Jama.Matrix;
import appdev.ArmState;
import appdev.regression.Regression;

public class GenericLLCmd extends LowLevelCommand {

	static double VELOCITY = 0.005;

	public GenericLLCmd(CompleteArm arm, Regression regression) {
		super(arm, regression);
	}

	@Override
	public Matrix reachGoal(Matrix goal, int nbSubGoal, double precision) {
		Matrix subGoal_dx = goal;
		for (int i_subGoal = 0; i_subGoal < nbSubGoal; i_subGoal++) {
			subGoal_dx = goal.minus(_arm.getArm().getArmEndPointMatrix());
			subGoal_dx.timesEquals(VELOCITY / subGoal_dx.norm2());

			ArmState sgoal = new ArmState(_arm.getArm());
			sgoal.dx = subGoal_dx;
			Matrix command = _regression.doRegression(sgoal, null);

			applyCommand(command);
		}
		return subGoal_dx;
	}

	public void applyCommand(Matrix command) {

		Matrix angle_courant = _arm.getArm().getArmPos().plus(command);

		_arm.setup(angle_courant.get(0, 0) % (2.0 * Math.PI),
				angle_courant.get(0, 1) % (2.0 * Math.PI));
	}
}
