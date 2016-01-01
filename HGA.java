package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;

public class HGA extends GA{
	public void localSearch(){		
        int ii = 0;
        int x = 0;
        boolean[] flagj = new boolean[obj.length]; 
        
        for(int i=0; i<popsize; i++){
            while(ii < 50){
                ii++;
                x = 0;
                double[][] tmp2 = new double[popsize][obj.length];
                copy(next,tmp2);
                for(int j=0; j<obj.length; j++){
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
                        	flagj[j] = true;
                            tmp2[i][j] = tmp0[i][j];
                        }else{
                        	flagj[j] = false;
                            x++;
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
                        	flagj[j] = true;
                            tmp2[i][j] = tmp0[i][j];
                        }else{
                        	flagj[j] = false;
                            x++;
                        }
                    }else{
                    	flagj[j] = false;
                        x++;
                    }
                }
                for(int jj=0; jj<obj.length; jj++){
                    if(flagj[jj]){
                        next[i][jj] = tmp2[i][jj];
                    }
                }
            if(x == obj.length)break;
            }
        }
	}
}