package appdev.cmdGen;

import Jama.Matrix;

public class CmdGenRnd implements CommandGenerator {

	@Override
	public Matrix generate(int dim, int nb, double min, double max) {
		Matrix m = new Matrix(1, dim);

		for (int curr_nb = 0; curr_nb < nb; curr_nb++) {
			int i = (int) (Math.floor(Math.random() * dim));
			m.set(0, i, Math.random() * (max - min) + min);
		}
		return m;
	}
}
