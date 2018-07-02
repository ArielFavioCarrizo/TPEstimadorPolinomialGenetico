package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PolynomialGeneticEstimator {
	private final Map<Float,Float> originalImageValues;
	
	private final List<Pair<Individual, Float>> poblation;
	
	private final float minX;
	private final float maxX;
	
	private final EvolutionConfig evolutionConfig;
	
	private final int numberOfDomainNumbers;
	
	private int iterationNumber;
	
	/**
	 * @post Crea un estimador con la funci�n objetivo de reales a reales,
	 * 		 el intervalo de estimaci�n, los coeficientes iniciales, y la configuraci�n de evoluci�n
	 */
	public PolynomialGeneticEstimator(Function<Float, Float> function, float minX, float maxX, float[] initialCoefficients, EvolutionConfig evolutionConfig) {
		Common.checkNotNull("function", function);
		Common.checkNotNull("evolutionConfig", evolutionConfig);
		
		if ( !(maxX > minX) ) {
			throw new IllegalArgumentException("Expected maxX > minX");
		}
		
		this.minX = minX;
		this.maxX = maxX;
		
		this.numberOfDomainNumbers = initialCoefficients.length;
		
		this.evolutionConfig = evolutionConfig;
		
		this.originalImageValues = this.imageSamples(function);
		
		Individual referenceIndividual = new Individual(initialCoefficients);
		this.poblation = new ArrayList<Pair<Individual, Float>>();

		Float deltaMax = this.evolutionConfig.getMutationDeltaByIteration().apply(0);
		
		for ( int i=0; i<evolutionConfig.getPoblationSize(); i++ ) {
			this.poblation.add( individualWithQuadraticError( referenceIndividual.mutate(deltaMax) ) );
		}
		
		this.sortPoblation();
		
		this.iterationNumber = 1;
	}
	
	/**
	 * @post Devuelve el n�mero de muestras de la imagen
	 * 		 de la funci�n especificada
	 */
	private Map<Float, Float> imageSamples(Function<Float, Float> function) {
		Map<Float, Float> imageSamples = new LinkedHashMap<>();
		
		for ( int i = 0; i<this.numberOfDomainNumbers; i++ ) {
			float key = this.minX + ( this.maxX - this.minX ) * (float) i / (this.numberOfDomainNumbers-1);
			imageSamples.put(key, function.apply( key ));
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
	 * @post Ordena la poblaci�n por error, ascendente
	 */
	private void sortPoblation() {
		Collections.sort(this.poblation, (individual1, individual2) -> Float.compare(individual1.getValue(), individual2.getValue() ) );
	}
	
	/**
	 * @post Entre las miembros de la poblaci�n selecciona los m�s aptos,
	 * 		 con el n�mero m�ximo de miembros a conservar.
	 */
	private void selection() {
		this.sortPoblation();
		
		while ( poblation.size() > this.evolutionConfig.getPoblationSize() ) {
			poblation.remove(poblation.size()-1);
		}
	}
	
	/**
	 * @post Entre los miembros de la poblaci�n especificada realiza aleatoriamente
	 * 		 un entrecruzamiento
	 */
	private void crossOver() {
		int originalSize = this.poblation.size();
		
		for ( int i = 0; i<originalSize; i++ ) {
			for ( int j = i+1; j<originalSize; j++ ) {
				if ( Common.getRng().nextFloat() <= this.evolutionConfig.getProbabilityOfCrossOver() ) {
					this.poblation.add(
						individualWithQuadraticError(
							this.poblation.get(i).getKey().crossover(this.poblation.get(j).getKey())
						)
					);
				}
			}
		}
	}
	
	/**
	 * @post Entre los miembros de la poblaci�n especificada realiza una mutaci�n
	 */
	private void mutation() {
		float deltaMax = this.evolutionConfig.getMutationDeltaByIteration().apply(this.iterationNumber);
		
		for ( int i = 0; i<poblation.size(); i++ ) {
			if ( Common.getRng().nextFloat() <= this.evolutionConfig.getProbabilityOfMutation() ) {
				this.poblation.set(i, this.individualWithQuadraticError(this.poblation.get(i).getKey().mutate(deltaMax)));
			}
		}
	}
	
	/**
	 * @post Realiza un ciclo
	 */
	public void doOneCycle() {
		this.selection();
		this.crossOver();
		this.mutation();
		this.iterationNumber++;
	}
	
	/**
	 * @post Devuelve el n�mero de iteraci�n
	 */
	public int getIterationNumber() {
		return this.iterationNumber;
	}
	
	/**
	 * @post Devuelve la soluci�n m�s �ptima por ahora,
	 * 		 con el error cuadr�tico medio
	 */
	public Pair<Individual, Float> getBestIndividual() {
		return new Pair<Individual, Float>( this.poblation.get(0).getKey(), this.poblation.get(0).getValue() / (float) this.poblation.size() );
	}
}
