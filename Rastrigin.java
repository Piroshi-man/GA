package piroshi.ga;

public class Rastrigin extends Objective{
	public Rastrigin(int len,double step){
		length = len;
		increment = step;
		domain = new double[length][3];
		for(int i=0; i<length; i++){
			domain[i][0] = -5.12;		// min
			domain[i][1] = 5.12;		// max
			domain[i][2] = increment;		// increment
		}
	}
	
	@Override
	public double objective(double[] x){
		double score = 10.0 * length;
		for(int i=0; i<length; i++){
			double t = x[i];
			score += t * t - 10 * Math.cos(360.0 * t * Math.PI / 180.0);
		}
		return score;
	}
}

