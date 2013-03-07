package appdev.regression;

import Jama.Matrix;
import appdev.ArmState;

public interface Regression {
	/**
	 * Fonction de regression locale.
	 * 
	 * @param query, l'état voulu
	 * @param vicinity, la liste des états connus avoisinants
	 * @return L'état issu de la regression
	 */
	Matrix doRegression(ArmState query, ArmState[] vicinity);
}
