package piroshi.ga;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import piroshi.util.*;


public class SimpleGA extends GA{
	double finishscore = 0.0;
	public boolean finish(){
		return best(nowscore) <= finishscore;
	}
}