package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PolynomialGeneticEstimator {
	private float[] originalImageValues;
	
	private float minX;
	private float maxX;
	
	private int numberOfDomainSamples;
	
	/**
	 * @post Crea un estimador con la funci�n objetivo de reales a reales,
	 * 		 el intervalo de estimaci�n, y la cantidad de muestras deseadas
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
	 * @post Devuelve el n�mero de muestras de la imagen
	 * 		 de la funci�n especificada
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
	 * 	     cuadr�tico
	 */
	private float getQuadraticError(Individual individual) {
		final float[] result = new float[1];
		result[0] = 0.0f;
	
		if ( individual == null ) {
			throw new NullPointerException();
		}
		
		this.originalImageValues.forEach((x,y)->{
			float difference = individual.getEstimatedFunction().apply(x) - y;
			result[0] += difference * difference;
		});
		
		return result[0];
	}
	
	/**
	 * @post Dado un individuo devuelve el individuo con su error cuadr�tico
	 * 		 medio
	 */
	private Pair<Individual, Float> individualWithQuadraticError(Individual individual) {
		return new Pair<Individual, Float>(individual, getQuadraticError(individual));
	}
	
	/**
	 * @post Entre las miembros de la poblaci�n especificada selecciona los m�s aptos,
	 * 		 con el n�mero m�ximo de miembros a conservar.
	 * 		 Devuelve las m�s aptos.
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
