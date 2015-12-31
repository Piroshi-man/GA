package piroshi.ga;

public class Rosenbrock extends Objective{
	public Rosenbrock(int len,double step){
		length = len;
		increment = step;
		domain = new double[length][3];
		for(int i=0; i<length; i++){
			domain[i][0] = -2.048;		// min
			domain[i][1] = 2.048;		// max
			domain[i][2] = increment;		// increment
		}
	}
	
	@Override
	public double objective(double[] x){
		double score = 0.0;
		for(int i=0; i<length - 1; i++){
			double t = x[i];
			double u = x[i+1];
			score += (100 * (u - (t * t)) * (u - (t * t))) + ((1 - t) * (1 -t));
		}
		return score;
	}
}

