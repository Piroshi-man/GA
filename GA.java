package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;

public class GA{
	public Objective obj;
	public int popsize;
	public int ngen;
	public int scale;
	public int term;
	public double pcross;
	public double pmut;
	public double step;
	public double shift;
	public boolean minmax;
	public double[][] now;
	public double[] nowscore;
	public double[][] next;
	public double[] nextscore;
	public double[][] prev;
	public double[] prevscore;
	public int seed = (int)System.currentTimeMillis();
	public long count = 0;
	public int countchange = 0;
	public Sfmt r;
	
	public void maximize(){
		minmax = false;
	}
	public void minimize(){
		minmax = true;
	}
	public void setObjective(Objective obj){
		this.obj = obj;
	}
	public void populationSize(int popsize){
		this.popsize = popsize;
	}
	public void nGenerations(int ngen){
		if(ngen <= 0)
			System.out.println("世代数 ngen は正整値");
		this.ngen = ngen;
	}
	public void pCrossover(double pcross){
		this.pcross = pcross;
	}
	public void pMutation(double pmut){
		this.pmut = pmut;
	}
	public void nStep(double step){
		this.step = step;
	}
	public void nShift(double shift){
		this.shift = shift;
	}
	public void nScale(int scale){
		this.scale = scale;
	}
	public void nTerm(int term){
		this.term = term;
	}
	
	// 一様交叉
	public double[] crossing(double[] src1, double[] src2){
		double[] dst = new double[src1.length];
		for(int i=0; i<src1.length; i++){
			if(rand() < 0.5){
				dst[i] = src1[i];
			}else{
				dst[i] = src2[i];
			}
		}
		return dst;
	}
	
	public boolean finish(){
		if(generation >= ngen)
			return true;
		return false;
	}
	


	
	public int generation = 0;
	public boolean lsmode = false;
	public boolean end = false;
	public double bestscore = Double.MAX_VALUE;
	
	
	public void evolve(){
		// 初期集団の生成
		r = new Sfmt(seed);
		now = init();
		
		// メモリの確保
		next = new double[popsize][obj.length];
		nextscore = new double[popsize];

		// 初期集団生成
		// loop{
			// 評価
			// 終了？ -> 終了
			// 選択
			// 交叉
			// 突然変異
			// 適応度更新？[評価]
			// loop{
				// 微分計算[評価 -> 現在、更新予定値]
				// 更新
			// }
		// }
		
		// 評価
		nowscore = eval(now);
		count += popsize;
		renewRecord(nowscore);
		
		while(true){
			if(bestscore == 0.0){
//				export_end();
				export_result();
				break;
			}
			// 選択
			// 最小化問題仕様
			// 最大値(worst)からの差(worst-score_i)を適合度として，
			// ルーレット選択を適用
			// pselect_i には index0からindexiまでの選択確率の総和が入っている
			// 0から1までの乱数を用いて選択
			double[] pselect = new double[popsize];
			double total = 0.0;
			double worstscore = worst(nowscore);
			for(int i=0; i<popsize; i++)
				total += worstscore - nowscore[i] + 0.000001;
			for(int i=0; i<popsize; i++){
				for(int j=0; j<=i; j++)
					pselect[i] += (worstscore - nowscore[j])+0.000001;
				pselect[i] /= total;
			}
			
			next[0] = clone(now[bestindex(nowscore)]);
			for(int i=1; i<popsize; i++){
				int index1 = selectindex(pselect);
				next[i] = clone(now[index1]);
				
				// 交叉
				if(pcross > rand()){
					int index2 = selectindex(pselect);
					while(index1 == index2)
						index2 = selectindex(pselect);
					next[i] = crossing(now[index1], now[index2]);
				}
			}
			
			
			// 突然変異
			for(int i=1; i<popsize; i++){
				for(int j=0; j<obj.length; j++){
					if(pmut > rand()){
						next[i][j] = randval(j);
					}
				}	
			}
			
			//整値
			for(int i=1; i<popsize; i++){
				for(int j=0; j<obj.length; j++){
					double fixval_a = next[i][j];
					BigDecimal bd_a = new BigDecimal(fixval_a);
					BigDecimal bd1_a = bd_a.setScale(scale, BigDecimal.ROUND_HALF_UP);
					next[i][j] = bd1_a.doubleValue();
				}
			}
			
			nextscore = eval(next);
			count += popsize;
			renewRecord(nextscore);
			if(bestscore == 0.0){
//				export_end();
				export_result();
                break;
            }
			
			/**********************************************************/
            //                    局所探索
            /**********************************************************/
			
			localSearch();
			// next を次の世代の now にする
			// now を次の世代の next を格納する領域にする
			swap();
		} // while end
	}
	
	public void renewRecord(double[] score){
		double bestindividual = best(score);
		if(bestscore - bestindividual > 0){
			bestscore = bestindividual;
//			export();
		}
	}
	
	public void export(){
		try{
			File f = new File("score.csv");
			BufferedWriter bwf = new BufferedWriter(new FileWriter(f,true));
			bwf.write(bestscore + ",");
			bwf.close();
			File g = new File("count.csv");
			BufferedWriter bwg = new BufferedWriter(new FileWriter(g,true));
			bwg.write(count + ",");
			bwg.close();
		}catch(IOException e){
			System.out.print(e);
		}
	}
	
	public void export_end(){
		try{
			File f = new File("score.csv");
			BufferedWriter bwf = new BufferedWriter(new FileWriter(f,true));
			bwf.newLine();
			bwf.close();
			File g = new File("count.csv");
			BufferedWriter bwg = new BufferedWriter(new FileWriter(g,true));
			bwg.newLine();
			bwg.close();
		}catch(IOException e){
			System.out.print(e);
		}
	}
	
	public void export_result(){
		try{
			File h = new File("result.csv");
			BufferedWriter bwh = new BufferedWriter(new FileWriter(h,true));
			bwh.write(count + "");
			bwh.newLine();
			bwh.close();
		}catch(IOException e){
			System.out.print(e);
		}
	}
	
	
	public void localSearch(){
	}
	public void swap(){
		double[][] tmp = now;
		now = next;
		next = tmp;
		double[] tmpscore = nowscore;
		nowscore = nextscore;
		nextscore = tmpscore;
	}
	
	public void copy(double[][] array1,double[][] array2){
		for(int i=0;i<popsize;i++){
			for(int j=0;j<obj.length;j++){
				array2[i][j] = array1[i][j];
			}
		}
	}
	
	
	public double differentialCalculus(double[] genome, int index){
        double h = step / 10;
        double[] tmp1 = new double[genome.length];
        double[] tmp2 = new double[genome.length];
        
        for(int i=0; i<genome.length; i++){
            tmp1[i] = genome[i];
            tmp2[i] = genome[i];
        }
        tmp1[index] += h/2;
        tmp2[index] -= h/2;
        
        double value = obj.objective(tmp1)-obj.objective(tmp2);
        count += 2;
        
        return  value / h;
    }
    
	
	public double[][] init(){
		double[][] gene = new double[popsize][obj.length];
		for(int i=0; i<popsize; i++)
		for(int j=0; j<obj.length; j++)
			gene[i][j] = randval(j);
		
		return gene;
	}

	public double rand(){
		return r.NextUnif();
	}

	public double randval(int index){
		double[] a = obj.domain[index];
		double tmp;
		tmp = rand() * (a[1] - a[0] + a[2]) + a[0];
		BigDecimal bd = new BigDecimal(tmp);
		BigDecimal bd1 = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
		
		return bd1.doubleValue();
	}

	public double[] eval(double[][] genome){
		return obj.objective_all(genome);
	}

	public double best(double[] score){
		double goodscore = score[0];
		if(minmax){
			// minimize
			for(int i=1; i<score.length; i++)
				if(score[i] < goodscore)
					goodscore = score[i];
		}else{
			// maximize
			for(int i=1; i<score.length; i++)
				if(score[i] > goodscore)
					goodscore = score[i];
		}
		return goodscore;
	}
	public double worst(double[] score){
		minmax = !minmax;
		double worstscore = best(score);
		minmax = !minmax;
		return worstscore;
	}
	
	
	public double[] clone(double[] src){
		double[] dst = new double[src.length];
		for(int i=0; i<src.length; i++)
			dst[i] = src[i];
		return dst;
	}
	
	public int selectindex(double[] pselect, double prob, int index){
		if(pselect.length <= index){
			return index-1;
		}else if(prob < pselect[index]){
			return index;
		}
		return selectindex(pselect, prob, index+1);
	}
	public int selectindex(double[] pselect){
		return selectindex(pselect, rand(), 0);
	}
	
	public int bestindex(double[] score){
		int bestindex = 0;
		if(minmax){
			// minimize
			for(int i=1; i<score.length; i++)
				if(score[i] < score[bestindex])
					bestindex = i;
		}else{
			// maximize
			for(int i=1; i<score.length; i++)
				if(score[i] > score[bestindex])
					bestindex = i;
		}
		return bestindex;
	}
	public int worstindex(double[] score){
		minmax = !minmax;
		int worstindex = bestindex(score);
		minmax = !minmax;
		return worstindex;
	}
	
	public void printscore(){
		System.out.print("" + best(nowscore));
	}
	public void printgene(){
		int bestindex = bestindex(nowscore);
		System.out.print("" + now[bestindex][0]);
		for(int i=1; i<obj.length; i++)
			System.out.print(", " + now[bestindex][i]);
	}
}
