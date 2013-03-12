package appdev.cmdGen;

import Jama.Matrix;

public interface CommandGenerator {
	/**
	 * Generate a random command
	 * 
	 * @param dim dimension of the command vector 
	 * @param nb number of dimension to generate
	 * @param min value for random generation
	 * @param max value for random generation
	 * @return a command vector of random command
	 */
	Matrix generate(int dim, int nb, double min, double max);
}
