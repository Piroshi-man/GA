package piroshi.ga;

public class Schaffer extends Objective{
	public Schaffer(int len,double step){
		length = len;
		increment = step;
		domain = new double[length][3];
		for(int i=0; i<length; i++){
			domain[i][0] = -100.0;		// min
			domain[i][1] = 100.0;		// max
			domain[i][2] = increment;		// increment
		}
	}
	
	@Override
	public double objective(double[] x){
        double score = 0;
        for(int i=0; i<length-1; i++){
            double t = x[i];
            double u = x[i+1];
            double tmp1 = (t * t + u * u);
            double tmp2 = Math.sin(50*Math.pow(tmp1, 0.10));
            score += Math.pow(tmp1, 0.25) * (tmp2 * tmp2 + 1.0);
        }
        return score;
	}
}
