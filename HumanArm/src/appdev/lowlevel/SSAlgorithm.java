package appdev.lowlevel;

import java.text.DecimalFormat;

import model.CompleteArm;
import utils.dataStructures.MaxHeap;
import utils.dataStructures.trees.thirdGenKD.KdTree;
import utils.dataStructures.trees.thirdGenKD.SquareEuclideanDistanceFunction;
import Jama.Matrix;
import appdev.ArmState;
import appdev.cmdGen.CmdGenRnd;
import appdev.cmdGen.CommandGenerator;
import appdev.regression.Regression;


/**
 * Shifting Setpoints Algorithm (SSA)
 * 
 * from "Robot Juggling: An Implementation of Memory-based Leanring"
 * by Schaal & Atkeson
 * 
 * @author moinel
 *
 */
public class SSAlgorithm extends LowLevelCommand {
	/** Decimal formating */
	public final static DecimalFormat df5_4 = new DecimalFormat( "0.000" );
	
	/** Force maximal d'une micro action */
	double MAX_FORCE = 0.01;
	double MIN_FORCE = -0.01;
	
	/** Seuil d'activation de la phase d'exploration */
	double E_MAX = 0.005;

	/* http://home.wlu.edu/~levys/software/kd/ */
	KdTree<ArmState> _data;
	
	/** Distance du prochain sous but */
	double VELOCITY = 0.015;
	
	int MAX_NEIGHBORS = 20;
	int nb_micro_action = 20;
	
	double _dt = 0.025;
	
	CommandGenerator _generator = new CmdGenRnd();
	
	public SSAlgorithm(CompleteArm model, Regression reg) {
		super(model, reg);
		
		ArmState zero = new ArmState();
		this._data = new KdTree<ArmState>(zero.getDimmension());
		exploration(20);
	}
	
	public void add(ArmState s) {
		_data.addPoint(s.toArrayVector(), s);
	}
	
	public int getSize() {
		return _data.size();
	}
	
	public ArmState[] getNearestStates(ArmState sToFind) {
		if (_data.size() <= 0) 
			return null;
		
		MaxHeap<ArmState> entries = _data.findNearestNeighbors(sToFind.toArrayVector(), MAX_NEIGHBORS, new SquareEuclideanDistanceFunction());
		ArmState [] states = new ArmState[entries.size()];

		for (int i = entries.size()-1; i >= 0; i--) {
			ArmState s = entries.getMax();
			states[i] = s;
			entries.removeMax();
		}

		return states;
	}

	public CommandGenerator getGenerator() {
		return _generator;
	}

	public void setGenerator(CommandGenerator _generator) {
		this._generator = _generator;
	}

	
	public Matrix reachGoal(Matrix goal, final int nb_exploration, double e_max) {
		
		// génération de sous buts
		Matrix nextSubGoal_dx = null;
		for (int i=0; i < nb_exploration; i++) {
			Matrix x = _arm.getArm().getArmEndPointMatrix();
			nextSubGoal_dx = goal.minus(x);
			nextSubGoal_dx.timesEquals(VELOCITY / nextSubGoal_dx.norm2());
			
			// calcule le mouvement à effectuer
			Matrix commande = computeCommande(_arm.getArm().getArmPos(), _arm.getArm().getArmSpeed(), nextSubGoal_dx);
			ArmState s = applyCommand(commande);
			this.add(s); // ajoute le mouvement effectué
			
			
			double error = nextSubGoal_dx.minus(_arm.getArm().getArmEndPointMatrix().minus(x)).norm2();
			if (error > E_MAX) {
				exploration(nb_exploration);
				
				//return nextSubGoal_dx;
			}
		}
		System.out.println(" Nb data= " + getSize());
		return nextSubGoal_dx;
	}

	public void exploration(final int nb_exploration) {
		for (int i=0; i < nb_exploration; i++) {
			Matrix commande = _generator.generate(2, 1, MIN_FORCE, MAX_FORCE);
			
			ArmState s = applyCommand(commande);
			this.add(s);
		}
	}

	public Matrix computeCommande(Matrix q, Matrix qd, Matrix dx) {
		ArmState qState = new ArmState(q.copy(),qd.copy(), dx);
		ArmState[] vicinity = getNearestStates(qState);
		
		Matrix commande;
		try {
			commande = _regression.doRegression(qState, vicinity);
		}catch(RuntimeException re) {
			commande = _generator.generate(2, 1, MIN_FORCE, MAX_FORCE);
		}
		
		return commande;
	}

	private ArmState applyCommand(Matrix command) {
		Matrix q = _arm.getArm().getArmPos().copy();
		Matrix dq = _arm.getArm().getArmSpeed().copy();
		Matrix dx = _arm.getArm().getArmEndPointMatrix();
		
		//_arm.applyCommand(command, _dt);
		applyCommandSimple(command);
		
		dx = _arm.getArm().getArmEndPointMatrix().minusEquals(dx);

		ArmState s = new ArmState(q, dq, command, dx);
		return s;
	}

	private void applyCommandSimple(Matrix command) {

		Matrix angle_courant = _arm.getArm().getArmPos().plus(command);

		_arm.setup(angle_courant.get(0, 0) % (2.0 * Math.PI),
				angle_courant.get(0, 1) % (2.0 * Math.PI));
	}
}
	
