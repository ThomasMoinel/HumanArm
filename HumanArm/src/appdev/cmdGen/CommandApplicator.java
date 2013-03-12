package appdev.cmdGen;

import model.CompleteArm;
import Jama.Matrix;

public class CommandApplicator {
	public void applyCommand(CompleteArm arm, Matrix command, double dt) {
		arm.applyCommand(command, dt);
	}
}
