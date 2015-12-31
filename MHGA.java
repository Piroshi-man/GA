package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;

public class MHGA extends GA{
	double finishscore = 0.0;
	int stopCount=0;
	boolean lsOn;
	
	public boolean finish(){
		return best(nowscore) <= finishscore;
	}
	void stopJudge(){
		nextscore = eval(next);
		
		if(best(nowscore) == best(nextscore)){
			stopCount++;
		}else{
			stopCount = 0;
		}
		if(stopCount == 1000 && lsOn == false){
			lsOn = true;
			stopCount = 0;
			countchange++;
		}else if(lsOn == true){
			lsOn = false;
			stopCount = 0;
			countchange++;

		}
	}
	
	public void localSearch(){
		lsmode = true;
		stopJudge();
		
		copy(next,now);
		if(lsOn == true){
			boolean[] flagi = new boolean[popsize];
			boolean[][] flagj = new boolean[popsize][obj.length];
			for(int i=0; i<popsize; i++){
				flagi[i] = true;
				for(int j=0;j<obj.length; j++){
					flagj[i][j] = true;
				}
			}
			int ii=0;
			for(int i=0; i<popsize; i++){
				while(flagi[i] == true){
					flagi[i] = false;
					if(ii <= 50){
						ii++;
						double[][] tmp2 = new double[popsize][obj.length];
						copy(next,tmp2);
						for(int j=0; j<obj.length; j++){
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
									// renewRecord(next,tmp0,"ls");
									tmp2[i][j] = tmp0[i][j];
									flagi[i] = true;
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
									// renewRecord(next,tmp0,"ls");
									tmp2[i][j] = tmp0[i][j];
									flagi[i] = true;
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
			}
		}
	}
}