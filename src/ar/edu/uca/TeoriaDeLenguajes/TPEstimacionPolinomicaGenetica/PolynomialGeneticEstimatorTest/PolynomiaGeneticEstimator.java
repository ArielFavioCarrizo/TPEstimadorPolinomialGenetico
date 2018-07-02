package ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimatorTest;

import java.util.function.Function;

import ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator.EvolutionConfig;
import ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator.Individual;
import ar.edu.uca.TeoriaDeLenguajes.TPEstimacionPolinomicaGenetica.PolynomialGeneticEstimator.PolynomialGeneticEstimator;
import javafx.util.Pair;

public final class PolynomiaGeneticEstimator {
	public static void main(String[] args) {
		EvolutionConfig evolutionConfig = new EvolutionConfig(
			50, 0.01f, 0.01f,
			numberOfIteration -> 0.01f / ( 1.0f + (float) Math.exp(-(float) numberOfIteration * 0.01f) )
		);
		
		float[] initialCoefficients = new float[] { 0.1f, 0.05f, 0.025f, 0.00125f };
		
		Function<Float, Float> functionToEstimate = x -> x * x * x * 5.0f + x * x * 10.0f + x * 2.0f + 3.0f;
		
		PolynomialGeneticEstimator estimator = new PolynomialGeneticEstimator(
				functionToEstimate,
				20,
				-5.0f,
				5.0f,
				initialCoefficients,
				evolutionConfig
		);
		
		float expectedMaxAverageQuadraticError = 0.00001f;
		
		float averageQuadraticError = Float.MAX_VALUE;
		
		do {
			System.out.println("Number of iteration: " + estimator.getIterationNumber());
			Pair<Individual, Float> bestIndividual = estimator.getBestIndividual();
			
			averageQuadraticError = bestIndividual.getValue();
			
			System.out.println("Estimated function = " + bestIndividual );
			System.out.println("Average quadratic error = " + averageQuadraticError );
			
			estimator.doOneCycle();
		} while ( averageQuadraticError > expectedMaxAverageQuadraticError );
			
	}
}
