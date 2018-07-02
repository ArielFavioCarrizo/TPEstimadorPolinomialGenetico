package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class Individual {
	private float[] coeficients;
	private Function<Float, Float> function;
	
	/**
	 * @post Crea un individuo con los coeficientes
	 * 		 especificados
	 */
	public Individual(float[] coeficients) {
		this.coeficients = coeficients;
		this.function = x -> {
			float result = 0.0f;
			float xPower = 1.0f;
			
			for ( int i = 0; i<this.coeficients.length; i++ ) {
				result += this.coeficients[i] * xPower;
				xPower *= x;
			}
			
			return result;
		};
	}
	
	/**
	 * @post Devuelve los coeficientes estimados de la función
	 */
	public float[] getCoeficients() {
		return this.coeficients.clone();
	}
	
	/**
	 * @pre El otro individuo no puede ser nulo
	 * @post Realiza una cruza con el individuo especificado,
	 * 		 devolviendo otro individuo.
	 */
	public Individual crossover(Individual other) {
		if ( other == null ) {
			throw new NullPointerException();
		}

		if ( other.coeficients.length != this.coeficients.length ) {
			throw new IllegalArgumentException("Number of coeficients mismatch");
		}
		
		// Crossover uniforme
		float[] newCoeficients = new float[this.coeficients.length];
		
		for ( int i = 0; i<this.coeficients.length; i++ ) {
			newCoeficients[i] = Common.getRng().nextBoolean() ? other.coeficients[i] : this.coeficients[i];
		}
		
		return new Individual(newCoeficients);
	}
	
	/**
	 * @post Realiza una mutación del individuo,
	 * 		 devolviendo otro.
	 * 		 Con el delta de variación especificado
	 */
	public Individual mutate(float deltaMax) {
		float[] newCoeficients = new float[this.coeficients.length];
		
		for ( int i = 0; i<this.coeficients.length ; i++ ) {
			float t = Common.getRng().nextFloat();
			
			newCoeficients[i] = this.coeficients[i] + deltaMax * (t * 2.0f - 1.0f);
		}
		
		return new Individual(newCoeficients);
	}
	
	/**
	 * @post Devuelve la función representada por el individuo
	 */
	public Function<Float, Float> getEstimatedFunction() {
		return this.function;
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for ( int i = this.coeficients.length-1;i>=0; i-- ) {
			str += this.coeficients[i] + " * x^" + i;
			
			if ( i != 0 ) {
				str += "+ ";
			}
		}
		
		return str;
	}
}
