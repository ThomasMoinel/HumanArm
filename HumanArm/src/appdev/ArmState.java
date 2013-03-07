package appdev;

import utils.JamaU;
import Jama.Matrix;


public class ArmState {

	/** position angulaire */
	public Matrix q;
	
	/** vitesse angulaire */
	public Matrix dq;

	/** commande */
	public Matrix com;

	/** deplacement de l'extremité du bras */
	public Matrix dx;

	/** Compétance */
	public double competence;
	
	public ArmState() {
		super();
		this.q = new Matrix(1,2);
		this.dq = new Matrix(1,2);
		this.com = new Matrix(1,8);
		this.dx = new Matrix(1,3);
		this.competence = .0;
	}
	
	public ArmState(Matrix q, Matrix dq) {
		super();
		assert(q.getRowDimension() == 1 && q.getColumnDimension() == 2);
		assert(dq.getRowDimension() == 1 && dq.getColumnDimension() == 2);
		this.q = q;
		this.dq = dq;
		this.com = new Matrix(1,8);
		this.dx = new Matrix(1,3);
		this.competence = .0;
	}
	
	public ArmState(Matrix q, Matrix dq, Matrix dx) {
		super();
		assert(q.getRowDimension() == 1 && q.getColumnDimension() == 2);
		assert(dq.getRowDimension() == 1 && dq.getColumnDimension() == 2);
		assert(dx.getRowDimension() == 1 && dx.getColumnDimension() == 3);
		this.q = q;
		this.dq =dq;
		this.com = new Matrix(1,8);
		this.dx = dx;
		this.competence = .0;
		}

	public ArmState(Matrix q, Matrix dq, Matrix com, Matrix dx) {
		super();
		assert(q.getRowDimension() == 1 && q.getColumnDimension() == 2);
		assert(dq.getRowDimension() == 1 && dq.getColumnDimension() == 2);
		assert(com.getRowDimension() == 1 && com.getColumnDimension() == 8);
		assert(dx.getRowDimension() == 1 && dx.getColumnDimension() == 3);
		this.q = q;
		this.dq = dq;
		this.com = com;
		this.dx = dx;
		this.competence = .0;
	}

	public ArmState(Matrix q, Matrix dq, Matrix com, Matrix dx, double competance) {
		super();
		assert(q.getRowDimension() == 1 && q.getColumnDimension() == 2);
		assert(dq.getRowDimension() == 1 && dq.getColumnDimension() == 2);
		assert(com.getRowDimension() == 1 && com.getColumnDimension() == 8);
		assert(dx.getRowDimension() == 1 && dx.getColumnDimension() == 3);
		this.q = q;
		this.dq = dq;
		this.com = com;
		this.dx = dx;
		this.competence = competance;
	}



	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ArmState))
			return false;
		ArmState s = (ArmState) o;
		return q.equals(s.q) && dq.equals(dq) && com.equals(s.com) && dx.equals(dx);
	}

	/**
	 * Transforme l'état (3 vecteur) en un vecteur combiné. Cette méthode est
	 * utiliser pour retrouver les plus proches voisins.
	 * 
	 * @return double[] vecteur combiné de l'état
	 */
	public double[] toArrayVector() {
		double[] v = new double[getDimmension()];
		int i_v = 0;
		for (int i = 0; i < q.getColumnDimension(); i++, i_v++)
			v[i_v] = q.get(0, i);
		for (int i = 0; i < dq.getColumnDimension(); i++, i_v++)
			v[i_v] = dq.get(0, i);
		for (int i = 0; i < dx.getColumnDimension(); i++, i_v++)
			v[i_v] = dx.get(0, i);

		return v;
	}

	/**
	 * Retourne la dimension du vecteur de l'état actuel renvoyé par la méthode
	 * toArrayVector()
	 * 
	 * @return dimension du vecteur d'état
	 */
	public int getDimmension() {
		return q.getColumnDimension() + dq.getColumnDimension()
				+ dx.getColumnDimension();
	}

	public String toString() {
		return JamaU.vecToString(q) + " " + JamaU.vecToString(dq) + " " + JamaU.vecToString(com) + " "
				+ JamaU.vecToString(dx) + competence;
	}
	
	public Matrix getCommand() {
		return com;
	}
}
