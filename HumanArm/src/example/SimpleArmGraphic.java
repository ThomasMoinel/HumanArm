package example;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;

import javax.swing.JFrame;

import model.CompleteArm;
import viewer.JArm2D;
import viewer.JArmLabel;
import Jama.Matrix;
import appdev.cmdGen.CommandApplicator;
import appdev.cmdGen.SimpleCommandApplicator;
import appdev.lowlevel.GenericLLCmd;
import appdev.lowlevel.LowLevelCommand;
import appdev.lowlevel.SSAlgorithm;
import appdev.regression.JacobianRegression;
import appdev.regression.LWR;
import appdev.regression.NearestNeighborRegression;

/**
 * Bras simplifie controle par variation des angles des membres directement.
 * Cette variation est calculer avec la Jacobienne analitique.
 * 
 * @author moinel
 * 
 */
public class SimpleArmGraphic extends Observable {

	LowLevelCommand _llcmd;

	/** But haut niveau */
	Matrix _goal;
	double GOAL_PRECISION_2 = 0.01;

	/** Fenetre principale de l'application */
	JFrame _frame;
	/** Le bras complet */
	CompleteArm _arm = new CompleteArm();
	/** Pour l'afficher */
	JArm2D _jArm;
	JArmLabel _jInfo;

	public SimpleArmGraphic() {

		_llcmd = new GenericLLCmd(_arm, new JacobianRegression());
		// _llcmd = new SSAlgorithm(_arm, new LWR(),new SimpleCommandApplicator());
		// _llcmd = new SSAlgorithm(_arm, new NearestNeighborRegression(),new SimpleCommandApplicator());

		_goal = genereGoalAleatoir();

		// Setup in resting position
		_arm.setup(0.0, Math.toRadians(45));

		// Setup window
		_frame = new JFrame("Simple Arm - Analitics Jacobian");
		_frame.setSize(600, 600);
		_frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		_frame.setLayout(new BorderLayout());

		_jArm = new JArm2D(_arm.getArm());
		System.out.println("MemSize=" + _jArm.getMemorySize());
		_frame.add(_jArm, BorderLayout.CENTER);
		_frame.add(_jArm.getControlPanel(), BorderLayout.SOUTH);
		_jInfo = new JArmLabel(_arm.getArm());
		_frame.add(_jInfo, BorderLayout.NORTH);

		_arm.getArm().addObserver(_jArm);
		_arm.getArm().addObserver(_jInfo);
		this.addObserver(_jArm);
		setChanged();
		notifyObservers(_goal);

		_frame.setVisible(true);
	}

	public void start() {
		_arm.getArm().setBounded(false);

		while (true) {
			step();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Matrix genereGoalAleatoir() {
		Matrix but = new Matrix(1, 3);
		double r = (0.65 - 0.05) * Math.random() + 0.05;
		double theta = 2 * Math.PI * Math.random();
		but.set(0, 0, r * Math.cos(theta));
		but.set(0, 1, r * Math.sin(theta));
		return but;
	}

	public void majGoal() {
		double d_goal = _arm.getArm().getArmEndPointMatrix().minusEquals(_goal)
				.norm2();
		if (d_goal < GOAL_PRECISION_2) {
			_goal = genereGoalAleatoir();
			setChanged();
			notifyObservers(_goal);
		}
	}

	protected void step() {

		majGoal();

		_llcmd.reachGoal(_goal, 1, 0.01);

		System.out.println(_arm.toString());

	}

	public static void main(String[] args) {
		SimpleArmGraphic app = new SimpleArmGraphic();
		app.start();
	}
}
