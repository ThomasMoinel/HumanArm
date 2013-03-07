package appdev.regression;

import Jama.Matrix;
import appdev.ArmState;

/**
 * Locally Weighted Regression
 * 
 * @author moinel
 * 
 */
public class LWR implements Regression {

	@Override
	public Matrix doRegression(ArmState query, ArmState[] vicinity) {
		int dim_x = query.getDimmension();
		int dim_y = query.getCommand().getColumnDimension();
		int nbVector = vicinity.length;
		// init martix
		Matrix xq = new Matrix(query.toArrayVector(), 1);
		Matrix X = new Matrix(nbVector, dim_x);
		Matrix w = new Matrix(nbVector, dim_x, 1.0);
		Matrix wX;
		Matrix Y = new Matrix(nbVector, dim_y);

		for (int i = 0; i < nbVector; i++) {
			double[] vec = vicinity[i].toArrayVector();
			for (int j = 0; j < dim_x; j++) {
				X.set(i, j, vec[j]);
			}
			Y.setMatrix(i, i, 0, dim_y - 1, vicinity[i].getCommand());

			double d_i = X.getMatrix(i, i, 0, dim_x - 1).minusEquals(xq)
					.norm2();
			d_i = f(d_i);
			w.setMatrix(i, i, 0, dim_x - 1, new Matrix(1, dim_x, d_i));
		}
		wX = X.arrayTimes(w).transpose();

		Matrix Lambda = getLambdaMatrix(dim_x, dim_x, 0.0001);
		Matrix P = (wX.times(X).plus(Lambda)).inverse();
		Matrix beta = P.times(wX).times(Y);
		Matrix yq = xq.times(beta);

		return yq;
	}

	/**
	 * Tiny random diagonal matrix to perform the inverse.
	 * The value range is [0:lambda^2]
	 * 
	 * @param m rows
	 * @param n cols
	 * @param lambda scale
	 * @return a random diagonal matrix
	 */
	public Matrix getLambdaMatrix(int m, int n, double lambda) {
		return Matrix.identity(m, n).arrayTimesEquals(
				Matrix.random(m, n).times(lambda * lambda));
	}

	/**
	 * Weight distance function
	 * 
	 * @param d distance to the query point
	 * @return the weight of this point 
	 */
	public double f(double d) {
		return 1/d/d/d;
	}
}
