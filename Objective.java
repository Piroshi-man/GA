package piroshi.ga;

import piroshi.util.*;

public class Objective{
	int length;
	double increment;
	double[][] domain;
	
	public double[] objective_all(double[][] xs){
		double[] score = new double[xs.length];
		for(int i=0; i<xs.length; i++)
			score[i] = objective(xs[i]);
		return score;
	}
	
	public double objective(double[] t){
		P.pln("Error: �ړI�֐����ݒ肳��Ă��܂���D");
		return 0.0;
	}
	
}
