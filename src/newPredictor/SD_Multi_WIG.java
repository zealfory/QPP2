package newPredictor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SD_Multi_WIG {

	public double computeSD_Multi_WIG(double sDScore,double wIGScore){
		double score=0;
		//临时改成SD_Add_WIG,把*改成了+
		score=sDScore*wIGScore;
		return score;
	}

	/**
	 * 根据已经计算好的sDScore.runId,wIGScore.runId文件计算每个query的SD_Multi_WIG值,并将SD_Multi_WIG值存入文件
	 * @throws IOException 
	 *  
	 * */
	public void getSD_Multi_WIGScores(String input,String output) throws IOException{
		String input_sD=null;
		String input_wIG=null;
		FileReader fileReader_sD=null;
		BufferedReader bufferedReader_sD=null;
		FileReader fileReader_wIG=null;
		BufferedReader bufferedReader_wIG=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;

		String tempLine_sD=null;
		String tempLine_wIG=null;
		String[] terms_sD=null;
		String[] terms_wIG=null;
		double sDScore=0;//临时存放sDScore.runId文件中某queryId对应的sDScore值
		double wIGScore=0;//临时存放wIGScore.runId文件中某queryId对应的wIGScore值
		double sD_Multi_WIGScore=0;//临时存放一个query的sD_Multi_WIGScore值

		input_sD=input.replaceFirst("input\\.", "sDScore.");
		input_wIG=input.replaceFirst("input\\.", "wIGScore.");
		fileReader_sD=new FileReader(input_sD);
		bufferedReader_sD=new BufferedReader(fileReader_sD);
		fileReader_wIG=new FileReader(input_wIG);
		bufferedReader_wIG=new BufferedReader(fileReader_wIG);
		fileWriter=new FileWriter(output);
		buffWriter=new BufferedWriter(fileWriter);
		//读取input_sD,input_wIG文件中某queryId对应的sDScore和wIGScore,计算sD_Multi_WIG值,并存入output文件
		while((tempLine_sD=bufferedReader_sD.readLine())!=null&&(tempLine_wIG=bufferedReader_wIG.readLine())!=null){
			terms_sD=tempLine_sD.split(" |\t");
			terms_wIG=tempLine_wIG.split(" |\t");
			sDScore=Double.parseDouble(terms_sD[3]);
			wIGScore=Double.parseDouble(terms_wIG[3]);
			sD_Multi_WIGScore=computeSD_Multi_WIG(sDScore,wIGScore);
			//把sD_Multi_WIGScore写入output文件
			fileWriter.write("queryId:\t"+terms_sD[1]+"\tSD_Multi_WIG:\t"+sD_Multi_WIGScore+"\n");
		}
		buffWriter.close();
		bufferedReader_wIG.close();
		bufferedReader_sD.close();
	}

	public static void main(String[] args){

	}

}
