package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;

public class HGA extends GA{
	public void localSearch(){
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