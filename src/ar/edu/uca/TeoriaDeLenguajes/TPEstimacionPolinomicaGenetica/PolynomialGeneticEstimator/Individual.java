package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.function.Function;

final class Individual {
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
	 * @post Devuelve la función representada por el individuo
	 */
	public Function<Float, Float> getEstimatedFunction() {
		return this.function;
	}
}
