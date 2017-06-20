package newPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 此预测方法的公式为:(1-alpha)*SD2+alpha*WIG
 * @author 1
 *
 */
public class C {
	private double alpha=0.5;//SD_2和WIG的平衡参数
	
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double computeC(double sD2Score,double wIGScore){
		double cScore=0;
		cScore=(1-alpha)*sD2Score+alpha*wIGScore;
		return cScore;
	}
	/**
	 * 根据已经计算好的sD2Score.runId,wIGScore.runId文件计算每个query的C值,并将cScore存入文件
	 * @throws IOException 
	 *  
	 * */
	public void getCScores(String input,String output) throws IOException{
		String input_sD2=null;
		String input_wIG=null;
		FileReader fileReader_sD2=null;
		BufferedReader buffReader_sD2=null;
		FileReader fileReader_wIG=null;
		BufferedReader buffReader_wIG=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		
		String tempLine_sD2=null;
		String tempLine_wIG=null;
		String[] terms_sD2=null;
		String[] terms_wIG=null;
		double sD2Score=0;//临时存放sD2Score.runId文件中某queryId对应的sD2Score值
		double wIGScore=0;//临时存放wIGScore.runId文件中某queryId对应的wIGScore值
		double cScore=0;//临时存放一个query的cScore值
		
		input_sD2=input.replaceFirst("input\\.", "sD2Score.");
		input_wIG=input.replaceFirst("input\\.", "wIGScore.");
		fileReader_sD2=new FileReader(input_sD2);
		buffReader_sD2=new BufferedReader(fileReader_sD2);
		fileReader_wIG=new FileReader(input_wIG);
		buffReader_wIG=new BufferedReader(fileReader_wIG);
		fileWriter=new FileWriter(output);
		buffWriter=new BufferedWriter(fileWriter);
		//读取input_sD2,input_wIG文件中某queryId对应的sD2Score和wIGScore,计算C值,并存入output文件
		while((tempLine_sD2=buffReader_sD2.readLine())!=null&&(tempLine_wIG=buffReader_wIG.readLine())!=null){
			terms_sD2=tempLine_sD2.split(" |\t");
			terms_wIG=tempLine_wIG.split(" |\t");
			sD2Score=Double.parseDouble(terms_sD2[3]);
			wIGScore=Double.parseDouble(terms_wIG[3]);
			cScore=computeC(sD2Score,wIGScore);
			//把cScore写入output文件
			buffWriter.write("queryId:\t"+terms_sD2[1]+"\tC:\t"+cScore+"\n");
		}
		buffWriter.close();
		buffReader_wIG.close();
		buffReader_sD2.close();
	}

}
