package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

import javafx.util.Pair;

import java.util.Arrays;

public final class PolynomialGeneticEstimator {
	private float[] originalImageValues;
	
	private float minX;
	private float maxX;
	
	private int numberOfDomainSamples;
	
	/**
	 * @post Crea un estimador con la función objetivo de reales a reales,
	 * 		 el intervalo de estimación, y la cantidad de muestras deseadas
	 */
	public PolynomialGeneticEstimator(Function<Float, Float> function, float minX, float maxX, int numberOfSamples) {
		if ( function == null ) {
			throw new NullPointerException();
		}
		
		if ( numberOfSamples <= 0 ) {
			throw new IllegalArgumentException("Expected positive number of samples");
		}
		
		if ( !(maxX > minX) ) {
			throw new IllegalArgumentException("Expected maxX > minX");
		}
		
		this.minX = minX;
		this.maxX = maxX;
		this.numberOfDomainSamples = numberOfSamples;
		
		this.originalImageValues = this.imageSamples(function);
	}
	
	/**
	 * @post Devuelve el número de muestras de la imagen
	 * 		 de la función especificada
	 */
    private float[] imageSamples(Function<Float, Float> function) {
    	float[] imageSamples = new float[this.numberOfDomainSamples];
    	
    	for ( int i = 0; i<this.numberOfDomainSamples; i++ ) {
    		imageSamples[i] = function.apply( this.minX + ( this.maxX - this.minX ) * (float) i / (this.numberOfDomainSamples-1) );
    	}
    	
    	return imageSamples;
    }
	
	/**
	 * @post Dada los valores estimados de la imagen devuelve el error
	 * 	     cuadrático
	 */
	private float quadraticError(float[] estimatedImageValues) {
		float result = 0.0f;
	
		if ( estimatedImageValues == null ) {
			throw new NullPointerException();
		}
		
		if ( estimatedImageValues.length != this.originalImageValues.length ) {
			throw new IllegalArgumentException("Mismatch with original samples");
		}
		
		for ( int i = 0; i<estimatedImageValues.length; i++ ) {
			float difference = estimatedImageValues[i] - this.originalImageValues[i];
			result += difference * difference;
		}
		
		return result;
	}
	
	/**
	 * @post Dado un individuo devuelve el individuo con su error cuadrático
	 * 		 medio
	 */
	private Pair<Individual, Float> individualWithQuadraticError(Individual individual) {
		return new Pair<Individual, Float>(individual, this.quadraticError(this.imageSamples(individual.getEstimatedFunction())));
	}
	
	/**
	 * @post Entre las miembros de la población especificada selecciona los más aptos,
	 * 		 con el número máximo de miembros a conservar.
	 * 		 Devuelve las más aptos.
	 */
	private Pair<Individual, Float>[] selection(Pair<Individual, Float>[] poblation, int maxNumberOfMembersToConservate) {
		Pair<Individual, Float>[] orderedIndividualsByAscendingOrder = poblation.clone();
		
		Arrays.sort(orderedIndividualsByAscendingOrder, (individual1, individual2) -> Float.compare(individual1.getValue(), individual2.getValue() ) );
		
		if ( orderedIndividualsByAscendingOrder.length > maxNumberOfMembersToConservate ) {
			return Arrays.copyOf(orderedIndividualsByAscendingOrder, maxNumberOfMembersToConservate);
		}
		else {
			return orderedIndividualsByAscendingOrder;
		}
	}
}
