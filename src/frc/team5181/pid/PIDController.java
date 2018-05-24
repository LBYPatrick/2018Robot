package frc.team5181.pid;

import frc.team5181.tasking.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PIDController implements Task {
	
	private PIDControllerState state = PIDControllerState.CONSTRUCTING;
	
	private void assertState(String method, PIDControllerState state) {
		if (!this.state.equals(state)) {
			throw new PIDControllerException(method +
					" is not available whent the state is " +
					this.state.toString() +
					". The state has to be " +
					state.toString() +
					" to use that."
			);
		}
	}

	/**
	 * Get the current state of this PIDController.
	 * @return Current state of this PIDController
	 */
	public PIDControllerState getState() {
		return this.state;
	}

	@Override
	public boolean nextStep() {
		this.update();
		return this.state.equals(PIDControllerState.ENDED);
	}

	// ---------- Source ----------
	
	private enum PIDControllerSourceMode {
		
		/** Neither .addSource() nor .setSource() is called. */
		UNDECIDED,
		
		/** .setSource() is called so it is locked in SINGLE_SOURCE mode. */
		SINGLE_SOURCE,
		
		/** .addSource() is called so it is locked in MULTIPLE_SOURCE mode. */
		MULTIPLE_SOURCE

	}
	
	private PIDControllerSourceMode sourceMode = PIDControllerSourceMode.UNDECIDED;
	private List<PIDSource> sources = null;
	private PIDSource source = null;

	/**
	 * Add the given source into source list of this PIDController. Calling this
	 * method will set the source mode into MULTIPLE_SOURCE.
	 * @param newSource The source that is going to added to the source list
	 * @return Self for chaining purposes
	 */
	public PIDController addSource(PIDSource... newSource) {
		this.assertState(".addSource()", PIDControllerState.CONSTRUCTING);
		
		if (this.sourceMode.equals(PIDControllerSourceMode.SINGLE_SOURCE)) {
			throw new PIDControllerException("Once .setSource() is called, there can be ONLY ONE source.");
		}
		this.sourceMode = PIDControllerSourceMode.MULTIPLE_SOURCE;
		
		if (this.sources == null) {
			this.sources = new ArrayList<>();
		}
		this.sources.addAll(Arrays.asList(newSource));
		
		return this;
	}
	
	/**
	 * Set the given source to the only source of this PIDController. Calling this
	 * method will set the source mode into SINGLE_SOURCE.
	 * @param newSource The source that is going to become the only source of this
	 * PIDController.
	 * @return Self for chaining purposes
	 */
	public PIDController setSource(PIDSource newSource) {
		this.assertState(".setSource()", PIDControllerState.CONSTRUCTING);
		
		if (!this.sourceMode.equals(PIDControllerSourceMode.UNDECIDED)) {
			throw new PIDControllerException("There are already other source(s) registered in this PIDController.");
		}
		this.sourceMode = PIDControllerSourceMode.SINGLE_SOURCE;
		
		this.source = newSource;
		
		return this;
	}
	
	// ---------- Blending Strategy ----------
	
	private enum PIDControllerBlendingStrategy {
		
		/** Neither .useReducer() nor .useCombiner() is called. */
		UNDECIDED,
		
		/** .useReducer() is called so it is locked in REDUCER strategy. */
		REDUCER,
		
		/** .useCombiner() is called so it is locked in COMBINER strategy. */
		COMBINER

	}
	
	private PIDControllerBlendingStrategy blendingStrategy = PIDControllerBlendingStrategy.UNDECIDED;
	private PIDReducer reducer;
	private PIDCombiner combiner;
	
	/**
	 * Let this PIDController use the given reducer as the strategy to blend multiple
	 * sources into one. Only available when the source mode is MULTIPLE_SOURCE.
	 * @param reducer The reducer which is going to be used.
	 * @return Self for chaining purposes
	 */
	public PIDController useReducer(PIDReducer reducer) {
		this.assertState(".useReducer()", PIDControllerState.CONSTRUCTING);
		
		if (this.sourceMode != PIDControllerSourceMode.MULTIPLE_SOURCE) {
			throw new PIDControllerException("You will only need to use reducer when there are multiple sources.");
		}
		
		if (this.blendingStrategy != PIDControllerBlendingStrategy.UNDECIDED) {
			throw new PIDControllerException("Blending strategy already declared before.");
		}
		
		this.blendingStrategy = PIDControllerBlendingStrategy.REDUCER;
		this.reducer = reducer;
		
		return this;
	}
	
	/**
	 * Let this PIDController use the given combiner as the strategy to blend multiple
	 * sources into one. Only available when the source mode is MULTIPLE_SOURCE.
	 * @param combiner The combiner which is going to be used.
	 * @return Self for chaining purposes
	 */
	public PIDController useCombiner(PIDCombiner combiner) {
		this.assertState(".useCombiner()", PIDControllerState.CONSTRUCTING);
		
		if (this.sourceMode != PIDControllerSourceMode.MULTIPLE_SOURCE) {
			throw new PIDControllerException("You will only need to use combiner when there are multiple sources.");
		}
		
		if (this.blendingStrategy != PIDControllerBlendingStrategy.UNDECIDED) {
			throw new PIDControllerException("Blending strategy already declared before.");
		}
		
		this.blendingStrategy = PIDControllerBlendingStrategy.COMBINER;
		this.combiner = combiner;
		
		return this;
	}
	
	// ---------- Translator ----------
	
	private PIDTranslator translator;
	
	/**
	 * Let this PIDController use the given translator to translate the sensor's
	 * readings.
	 * @param translator The translator which is going to be used.
	 * @return Self for chaining purposes
	 */
	public PIDController useTranslator(PIDTranslator translator) {
		this.assertState(".useTranslator()", PIDControllerState.CONSTRUCTING);
		
		if (this.translator != null) {
			throw new PIDControllerException("Translator already declared before.");
		}
		
		this.translator = translator;
		
		return this;
	}

	// ---------- Output ----------

	private List<PIDTarget> targets = new ArrayList<>();

	public PIDController outputTo(PIDTarget target) {
		this.assertState(".outputTo()", PIDControllerState.CONSTRUCTING);
		targets.add(target);
		return this;
	}

	public PIDController outputTo(PIDTarget target, PIDTranslator translator) {
		this.outputTo(new PIDWrappedTarget(target, translator));
		return this;
	}

	// ---------- Constants ----------

	// TODO: Find better default constant values
	private double proportionalConstant = 0.5;
	private double integralConstant = 0;
	private double differentialConstant = 0.1;

	/**
	 * Set proportional constant for the PID process.
	 * @param proportionalConstant Proportional constant for the PID process.
	 */
	public PIDController withProportionalConstant(double proportionalConstant) {
		this.proportionalConstant = proportionalConstant;
		return this;
	}

	/**
	 * Set integral constant for the PID process.
	 * @param integralConstant Integral constant for the PID process.
	 * @return 
	 */
	public PIDController withIntegralConstant(double integralConstant) {
		this.integralConstant = integralConstant;
		return this;
	}

	/**
	 * Set derivative constant for the PID process.
	 * @param derivativeConstant Derivative constant for the PID process.
	 * @return 
	 */
	public PIDController withDerivativeConstant(double derivativeConstant) {
		this.differentialConstant = derivativeConstant;
		return this;
	}

	public PIDController withPIDConstant(double pConstant, double iConstant, double dConstant){
		this.proportionalConstant = pConstant;
		this.integralConstant = iConstant;
		this.differentialConstant = dConstant;
		return this;
	}

	public PIDController withPIDConstantPreset(PIDConstantPreset preset){
		this.proportionalConstant = preset.getP();
		this.integralConstant = preset.getI();
		this.differentialConstant = preset.getD();
		return this;
	}

	// ---------- Never End ----------

	private boolean neverEnd = false;
	public PIDController neverEnd(boolean neverEnd) {
		this.neverEnd = neverEnd;
		return this;
	}

	// ---------- Reset ----------

	/**
	 * Reset all accumulated values. (Integrals, time, etc...)
	 */
	public void reset() {
		this.lastTime = this.getTimeFromNano();
		this.cumulativeError = 0;
		this.lastError = this.computeError();
	}

	// ---------- Acceptable Error ----------

	private double acceptableError = 0.1;
	public PIDController withAcceptableError(double acceptableError) {
		this.acceptableError = acceptableError;
		return this;
	}

	// ---------- Updating ----------

	private double computeError() {
		double reading;
		if (this.sourceMode.equals(PIDControllerSourceMode.MULTIPLE_SOURCE)) {
			if (this.blendingStrategy.equals(PIDControllerBlendingStrategy.REDUCER)) {
				double accumulator = 0;
				int index = 0;
				for (PIDSource source : this.sources) {
					accumulator = this.reducer.reduce(accumulator, source.pidYield(), index++);
				}
				reading = accumulator;
			} else if (this.blendingStrategy.equals(PIDControllerBlendingStrategy.COMBINER)) {
				List<Double> readings = new ArrayList<>();
				for (PIDSource source : this.sources) {
					readings.add(source.pidYield());
				}
				reading = combiner.combine(readings);
			} else {
				throw new PIDControllerException("When there are multiple sources, you need to use either .useCombiner() or .useReducer() to specify a blending strategy.");
			}
		} else if (this.sourceMode.equals(PIDControllerSourceMode.SINGLE_SOURCE)) {
			reading = this.source.pidYield();
		} else {
			throw new PIDControllerException("You need at least one source to get PID working.");
		}
		if (this.translator != null) {
			reading = this.translator.translate(reading);
		}
		return reading;
	}

	private void adjustWith(double amount) {
		for (PIDTarget target : this.targets) {
			target.pidAdjust(amount);
		}
	}

	private double lastError;
	private double cumulativeError;

	private void updateWithTime(double deltaTime) {
		if (this.state.equals(PIDControllerState.CONSTRUCTING)) {
			this.state = PIDControllerState.RUNNING;
		}
		if (this.state.equals(PIDControllerState.ENDED)) {
			return;
		}
		double error = this.computeError();
		if (Double.isInfinite(error)) {
			return;
		}
		if (Math.abs(error) < this.acceptableError) {
			if (this.neverEnd) {
				this.reset();
			} else {
				this.state = PIDControllerState.ENDED;
				this.adjustWith(0);
				return;
			}
		}

		// Trapezoid rule calculating integral.
		this.cumulativeError = this.cumulativeError + (this.lastError + error) * deltaTime / 2;

		// Calculating differential.
		double deltaError = (error - this.lastError) / deltaTime;

		double adjust = error * this.proportionalConstant + this.cumulativeError * this.integralConstant + deltaError * this.differentialConstant;

		this.lastError = error;

		this.adjustWith(adjust);
	}

	private double lastTime = 0;

	private double getTimeFromNano() {
		return System.nanoTime() / 1000000000.0;
	}

	/**
	 * Gets reading from all sources, calculates the "best" strategy to get to the
	 * goal, and tells all targets to do so.
	 * <br/>
	 * Calling this method will also bring the current PIDController into RUNNING
	 * state.
	 * <br/>
	 * Calling this will do nothing if the current state is ENDED.
	 * <br/>
	 * It will call interior reset instead first time called.
	 * @return Self for chaining purposes
	 */
	public PIDController update() {
		if (this.lastTime == 0) {
			this.reset();
		} else {
			double newTime = this.getTimeFromNano();
			updateWithTime(newTime - this.lastTime);
			this.lastTime = newTime;
		}
		return this;
	}

	// ---------- Revive ----------

	/**
	 * Reset all accumulated values and brings the state back to RUNNING.
	 * Can only be called when the state is ENDED.
	 * @return Self for chaining purposes
	 */
	public PIDController revive() {
		this.assertState(".revive()", PIDControllerState.ENDED);
		this.reset();
		this.state = PIDControllerState.RUNNING;
		return this;
	}
	
	@Override
	public String toString() {
		return "State: " + this.state.name() + "\n" +
				"Constants: P " + this.proportionalConstant + " I " + this.integralConstant + " D " + this.differentialConstant + "\n" +
				"Error: " + this.lastError + "/" + this.acceptableError;
	}

}
