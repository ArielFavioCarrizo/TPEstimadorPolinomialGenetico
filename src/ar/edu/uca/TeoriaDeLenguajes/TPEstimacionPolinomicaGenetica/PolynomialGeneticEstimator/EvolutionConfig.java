package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

public class EvolutionConfig implements Cloneable {
	private int poblationSize;
	private float probabilityOfCrossOver;
	private float probabilityOfMutation;
	private Function<Integer, Float> mutationDeltaByIteration;
	
	/**
	 * @post Crea una configuraci�n de evoluci�n
	 */
	public EvolutionConfig(
			int poblationSize,
			float probabilityOfCrossOver,
			float probabilityOfMutation,
			Function<Integer, Float> mutationDeltaByIteration) {
		if ( poblationSize <= 1 ) {
			throw new IllegalArgumentException("Invalid poblation size");
		}
		
		Common.checkProbabilityValue("probabilityOfCrossOver", probabilityOfCrossOver);
		Common.checkProbabilityValue("probabilityOfMutation", probabilityOfMutation);
		Common.checkNotNull("mutationDeltaByIteration", mutationDeltaByIteration);
		
		this.poblationSize = poblationSize;
		this.probabilityOfCrossOver = probabilityOfCrossOver;
		this.probabilityOfMutation = probabilityOfMutation;
		this.mutationDeltaByIteration = mutationDeltaByIteration;
	}
	
	/**
	 * @post Devuelve el tama�o de la poblaci�n
	 */
	public int getPoblationSize() {
		return this.poblationSize;
	}
	
	/**
	 * @post Devuelve la probabilidad de crossover
	 */
	public float getProbabilityOfCrossOver() {
		return this.probabilityOfCrossOver;
	}
	
	/**
	 * @post Devuelve la probabilidad de mutaci�n
	 */
	public float getProbabilityOfMutation() {
		return this.probabilityOfMutation;
	}
	
	/**
	 * @post Devuelve el delta de mutaci�n por iteraci�n
	 */
	public Function<Integer, Float> getMutationDeltaByIteration() {
		return this.mutationDeltaByIteration;
	}
}
