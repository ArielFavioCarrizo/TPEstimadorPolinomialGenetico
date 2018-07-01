package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator;

import java.util.Random;

class Common {
	private static ThreadLocal<Random> rng = new ThreadLocal<Random>();

	/**
	 * @post Devuelve el Random Number Generator
	 */
	public static Random getRng() {
		if ( rng.get() == null ) {
			rng.set(new Random());
		}
		
		return rng.get();
	}
	
	public static void checkProbabilityValue(String name, float value) {
		if ( !( ( value >= 0.0f ) && ( value <= 1.0f ) ) ) {
			throw new IllegalArgumentException("'" + name + "' is invalid");
		}
	}
	
	public static void checkNotNull(String name, Object object) {
		if ( object == null ) {
			throw new NullPointerException("'" + name + "' is null");
		}
	}
}
