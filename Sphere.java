package piroshi.ga;

public class Sphere extends Objective{
	public Sphere(int len,double step){
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
		double score = 0.0;
		for(int i=0; i<length; i++){
			double t = x[i];
			score += t * t;
		}
		return score;
	}
}

