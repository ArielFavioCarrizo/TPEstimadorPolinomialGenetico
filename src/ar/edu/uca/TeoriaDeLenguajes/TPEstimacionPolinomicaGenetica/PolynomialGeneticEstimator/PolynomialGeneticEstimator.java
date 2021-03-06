package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PolynomialGeneticEstimator {
	private final float[] domainValues;
	private final float[] originalImageValues;
	
	private final List<Pair<Individual, Float>> poblation;
	
	private final EvolutionConfig evolutionConfig;
	
	private int iterationNumber;
	
	/**
	 * @post Crea un estimador con la funci�n objetivo de reales a reales, la
	 * 		 cantidad de muestras de la funci�n,
	 * 		 el intervalo de estimaci�n, los coeficientes iniciales,
	 * 		 y la configuraci�n de evoluci�n
	 */
	public PolynomialGeneticEstimator(Function<Float, Float> function, int numberOfFunctionSamples, float minX, float maxX, float[] initialCoefficients, EvolutionConfig evolutionConfig) {
		Common.checkNotNull("function", function);
		if ( numberOfFunctionSamples <= 1 ) {
			throw new IllegalArgumentException("Expected numberOfFunctionSamples > 1");
		}
		
		Common.checkNotNull("evolutionConfig", evolutionConfig);
		
		if ( !(maxX > minX) ) {
			throw new IllegalArgumentException("Expected maxX > minX");
		}
		
		Function<Float, Float> interpolatedDomainValue = t -> minX + ( maxX - minX ) * (float) t;
		
		this.domainValues = new float[numberOfFunctionSamples];
		for ( int i = 0; i<numberOfFunctionSamples; i++ ) {
			this.domainValues[i] = interpolatedDomainValue.apply((i + Common.getRng().nextFloat()) / (float) numberOfFunctionSamples);
		}
		
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
	 * 		 de la funci�n especificada, con los valores del dominio
	 */
	private float[] imageSamples(Function<Float, Float> function) {
		float[] imageSamples = new float[this.domainValues.length];
		
		for ( int i = 0; i<this.domainValues.length; i++ ) {
			imageSamples[i] = function.apply( this.domainValues[i] );
		}
		
		return imageSamples;
	}
	
	/**
	 * @post Dada los valores estimados de la imagen devuelve el error
	 * 	     cuadr�tico
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
	 * @post Dado un individuo devuelve el individuo con su error cuadr�tico
	 * 		 medio
	 */
	private Pair<Individual, Float> individualWithQuadraticError(Individual individual) {
		return new Pair<Individual, Float>(individual, this.quadraticError(this.imageSamples(individual.getEstimatedFunction())));
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
	 * 		 cr�as
	 */
	private void doChilds() {
		int originalSize = this.poblation.size();
		
		for ( int i = 0; i<originalSize; i++ ) {
			for ( int j = i+1; j<originalSize; j++ ) {
				if ( Common.getRng().nextFloat() <= this.evolutionConfig.getProbabilityOfCrossOver() ) {
					Pair<Individual, Individual> childs = this.poblation.get(i).getKey().crossover(this.poblation.get(j).getKey());
					Individual individual1 = childs.getKey();
					Individual individual2 = childs.getValue();
					
					this.poblation.add( individualWithQuadraticError( probabilisticMutate(individual1) ) );
					this.poblation.add( individualWithQuadraticError( probabilisticMutate(individual2) ) );
				}
			}
		}
	}
	
	/**
	 * @post Dado un individuo realiza probabil�sticamente una mutaci�n
	 */
	private Individual probabilisticMutate(Individual individual) {
		float deltaMax = this.evolutionConfig.getMutationDeltaByIteration().apply(this.iterationNumber);
		
		if ( Common.getRng().nextFloat() <= this.evolutionConfig.getProbabilityOfMutation() ) {
			return individual.mutate(deltaMax);
		}
		else {
			return individual;
		}
	}
	
	/**
	 * @post Realiza un ciclo
	 */
	public void doOneCycle() {
		this.selection();
		this.doChilds();
		
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
		return new Pair<Individual, Float>( this.poblation.get(0).getKey(), this.poblation.get(0).getValue() / (float) this.domainValues.length );
	}
}
