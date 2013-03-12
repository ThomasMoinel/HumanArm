package appdev.cmdGen;

import model.CompleteArm;
import Jama.Matrix;

public class SimpleCommandApplicator extends CommandApplicator {

	@Override
	public void applyCommand(CompleteArm arm, Matrix command, double dt) {
		Matrix angle_courant = arm.getArm().getArmPos().plus(command);

		arm.setup(angle_courant.get(0, 0) % (2.0 * Math.PI),
				angle_courant.get(0, 1) % (2.0 * Math.PI));
	}
}
