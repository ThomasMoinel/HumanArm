package appdev.regression;

import Jama.Matrix;
import appdev.ArmState;

public class JacobianRegression implements Regression {

	@Override
	public Matrix doRegression(ArmState query, ArmState[] vicinity) {
		Matrix J = getAnaliticJacobian(query.q);
		Matrix J_inv = J.inverse();
		Matrix dthau = J_inv.times(query.dx.transpose()).transpose();

		return dthau;
	}

	public Matrix getAnaliticJacobian(Matrix thau) {
		// TODO: Changer la dimension de J, 2 en 8 ?
		Matrix J = new Matrix(3, 2, 0.0);
		J.set(0, 0,
				-0.30 * Math.sin(thau.get(0, 0)) - 0.35
						* Math.sin(thau.get(0, 0) + thau.get(0, 1)));
		J.set(1, 0,
				0.30 * Math.cos(thau.get(0, 0)) + 0.35
						* Math.cos(thau.get(0, 0) + thau.get(0, 1)));
		J.set(0, 1, -0.35 * Math.sin(thau.get(0, 0) + thau.get(0, 1)));
		J.set(1, 1, 0.35 * Math.cos(thau.get(0, 0) + thau.get(0, 1)));
		return J;
	}
}
