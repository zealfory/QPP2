package newPredictor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WIG_Multi_NQC {

	public double computeWIG_Multi_NQC(double wIGScore,double nQCScore){
		double score=0;
		//临时改成了WIG_Add_NQC,把*改成了+
		score=wIGScore*nQCScore;
		return score;
	}

	/**
	 * 根据已经计算好的wIGScore.runId,nQCScore.runId文件计算每个query的WIG_Multi_NQC值,并将WIG_Multi_NQC值存入文件
	 * @throws IOException 
	 *  
	 * */
	public void getWIG_Multi_NQCScores(String input,String output) throws IOException{
		String input_wIG=null;
		String input_nQC=null;
		FileReader fileReader_wIG=null;
		BufferedReader bufferedReader_wIG=null;
		FileReader fileReader_nQC=null;
		BufferedReader bufferedReader_nQC=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;

		String tempLine_wIG=null;
		String tempLine_nQC=null;
		String[] terms_wIG=null;
		String[] terms_nQC=null;
		double wIGScore=0;//临时存放sDScore.runId文件中某queryId对应的sDScore值
		double nQCScore=0;//临时存放wIGScore.runId文件中某queryId对应的wIGScore值
		double wIG_Multi_NQCScore=0;//临时存放一个query的sD_Multi_WIGScore值

		input_wIG=input.replaceFirst("input\\.", "wIGScore.");
		input_nQC=input.replaceFirst("input\\.", "nQCScore.");
		fileReader_wIG=new FileReader(input_wIG);
		bufferedReader_wIG=new BufferedReader(fileReader_wIG);
		fileReader_nQC=new FileReader(input_nQC);
		bufferedReader_nQC=new BufferedReader(fileReader_nQC);
		fileWriter=new FileWriter(output);
		buffWriter=new BufferedWriter(fileWriter);
		//读取input_sD,input_wIG文件中某queryId对应的sDScore和wIGScore,计算sD_Multi_WIG值,并存入output文件
		while((tempLine_wIG=bufferedReader_wIG.readLine())!=null&&(tempLine_nQC=bufferedReader_nQC.readLine())!=null){
			terms_wIG=tempLine_wIG.split(" |\t");
			terms_nQC=tempLine_nQC.split(" |\t");
			wIGScore=Double.parseDouble(terms_wIG[3]);
			nQCScore=Double.parseDouble(terms_nQC[3]);
			wIG_Multi_NQCScore=computeWIG_Multi_NQC(wIGScore,nQCScore);
			//把wIG_Multi_NQCScore写入output文件
			fileWriter.write("queryId:\t"+terms_wIG[1]+"\tWIG_Multi_NQC:\t"+wIG_Multi_NQCScore+"\n");
		}
		buffWriter.close();
		bufferedReader_nQC.close();
		bufferedReader_wIG.close();
	}

	public static void main(String[] args){

	}

}
