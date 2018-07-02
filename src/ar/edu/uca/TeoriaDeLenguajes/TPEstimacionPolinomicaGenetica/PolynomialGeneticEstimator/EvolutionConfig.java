package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

public class EvolutionConfig implements Cloneable {
	private int poblationSize;
	private float probabilityOfCrossOver;
	private float probabilityOfMutation;
	private Function<Integer, Float> mutationDeltaByIteration;
	
	/**
	 * @post Crea una configuración de evolución
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
	 * @post Devuelve el tamaño de la población
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
	 * @post Devuelve la probabilidad de mutación
	 */
	public float getProbabilityOfMutation() {
		return this.probabilityOfMutation;
	}
	
	/**
	 * @post Devuelve el delta de mutación por iteración
	 */
	public Function<Integer, Float> getMutationDeltaByIteration() {
		return this.mutationDeltaByIteration;
	}
}
