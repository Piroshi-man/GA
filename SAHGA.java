package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;

public class SAHGA extends GA{
	double finishscore = 0.0;
	public boolean finish(){
		return best(nowscore) <= finishscore;
	}
	
	double	global_inc = 0;
	double  local_inc = 0; 
	double fitness_sum;
	double fitness_sum_double;
	double fitness_avg;
	double fitness_avg_double;
	double var;
	double dev;
	double cv_before = 0;
	double cv_after = 0;
	double cvr;
	

	
	public void localSearch(){
		lsmode = true;
		nextscore = eval(next);
		global_inc = (best(nowscore) - best(nextscore)) / popsize;
		fitness_sum = 0;
		fitness_sum_double = 0;
		for(int i=0; i<popsize; i++){
			double x = nowscore[i];
			fitness_sum += x;
			fitness_sum_double += x * x; 
		}
		fitness_avg = fitness_sum / popsize;
		fitness_avg_double = fitness_sum_double / popsize;
		var = fitness_avg_double - (fitness_avg * fitness_avg);
		dev = Math.sqrt(var);
		cv_after = dev / popsize;
		cvr = cv_after / cv_before;
		cv_before = cv_after;		

		
		boolean[][] flagj = new boolean[popsize][obj.length];
		for(int i=0; i<popsize; i++){
			for(int j=0;j<obj.length; j++){
				flagj[i][j] = true;
			}
		}
		
		
		if(cvr >= 1.0){
			int times = 0;
			boolean flag = true;
			int lscount = 0;
			while(flag){
				times++;
				for(int i=0; i<popsize; i++){
					if(Math.pow((1-0.001),times) > rand()){
						double[][] tmp2 = new double[popsize][obj.length];
						copy(next,tmp2);
						for(int j=0; j<obj.length; j++){
							lscount += 2;
							flagj[i][j] = true;
							double differentialCoefficient = differentialCalculus(next[i],j);
							if(differentialCoefficient > 0){
								double tmp1 = next[i][j];
								tmp1 -= shift;
								BigDecimal bd = new BigDecimal(tmp1);
								BigDecimal bd1 = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
								double[][] tmp0 = new double[popsize][obj.length];
								copy(next,tmp0);
								tmp0[i][j] = bd1.doubleValue();
								if(obj.objective(tmp0[i]) < obj.objective(next[i])){
									tmp2[i][j] = tmp0[i][j];
								}else{
									flagj[i][j] = false;
								}
							}else if(differentialCoefficient < 0){
								double tmp1 = next[i][j];
								tmp1 += shift;
								BigDecimal bd = new BigDecimal(tmp1);
								BigDecimal bd1 = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
								double[][] tmp0 = new double[popsize][obj.length];
								copy(next,tmp0);
								tmp0[i][j] = bd1.doubleValue();
								if(obj.objective(tmp0[i]) < obj.objective(next[i])){
									tmp2[i][j] = tmp0[i][j];
								}else{
									flagj[i][j] = false;
								}
							}else{
								flagj[i][j] = false;
							}
						}
						for(int jj=0; jj<obj.length; jj++){
							if(flagj[i][jj]){
								next[i][jj] = tmp2[i][jj];
							}
						}						
					}
				}
				nextscore = eval(next);
				local_inc = (best(nowscore) - best(nextscore)) / (lscount * 2);
				copy(next,now);
				nowscore = eval(now);
				if(local_inc > global_inc){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
	}
}