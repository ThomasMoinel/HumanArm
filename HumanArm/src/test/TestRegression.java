package test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Jama.Matrix;
import appdev.ArmState;
import appdev.regression.LWR;
import appdev.regression.NearestNeighborRegression;
import appdev.regression.Regression;

/**
 * Genere en sortie standart des données de régression pouvant être affichees 
 * avec gnuplot ou autre
 * 
 * @author moinel
 *
 */
public class TestRegression {

	static DecimalFormat df5_4 = new DecimalFormat("0.000");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Regression lwr = new LWR();
		Regression nearest = new NearestNeighborRegression();

		TestRegression app = new TestRegression();

		app.add(10);

		ArmState query = new ArmState();

		System.out.println("#x y nearest LWR");
		for (double x = 0.0; x < 1.001; x += 0.01) {

			query.q.set(0, 0, x);

			Matrix yp_nea = app.regression(query, nearest);
			Matrix yp_lwr = app.regression(query, lwr);
			double y_real = app.hyperbole(x);
			System.out.println(x + " " + y_real + " " + yp_nea.get(0, 0) + " "
					+ yp_lwr.get(0, 0));
		}

	}

	ArrayList<ArmState> lstates;

	public TestRegression() {
		this.lstates = new ArrayList<ArmState>();
	}

	public void add(int nb_point) {
		for (int i = 0; i < nb_point; i++) {
			double x = Math.random();
			ArmState s = new ArmState();
			s.q.set(0, 0, x);
			s.com.set(0, 0, hyperbole(x));
			lstates.add(s);
		}
	}

	public double hyperbole(double x) {
		return (8 * Math.pow(x - 0.5, 3) + 1) / 2;
	}

	public double parabole(double x) {
		return -4.0 * x * x + 4 * x;
	}

	public Matrix regression(ArmState query, Regression reg) {
		ArmState[] array = new ArmState[lstates.size()];
		return reg.doRegression(query, lstates.toArray(array));
	}

	public void print() {
		for (ArmState s : lstates) {
			System.out.println(df5_4.format(s.q.get(0, 0)) + " -> "
					+ df5_4.format(s.com.get(0, 0)));
		}
	}
}
