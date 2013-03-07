package appdev.regression;

import Jama.Matrix;
import appdev.ArmState;

/**
 * Selectionne l'état le plus près, le premier du tableau vicinity.
 * vicinity étant trier par proximité
 * 
 * @author moinel
 *
 */
public class NearestNeighborRegression implements Regression {

	@Override
	public Matrix doRegression(ArmState query, ArmState[] vicinity) {
		return vicinity[0].getCommand();
	}

}
