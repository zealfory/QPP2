package correlationCoefficient;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class PearsonAnalysis {
	/**
	 * 需根据input文件的格式对此函数进行修改
	 * 此处加载的文件为: ./robustTrack2004/nQCScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/sDScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/wIGScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/sMVScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 计算input1和input2的Pearson系数
	 * @throws IOException 
	 * */
	public static void loadScoreAndComputePearson(String input1, String input2) throws IOException{
		FileReader fileReader=null;
		LineNumberReader lineNumberReader=null;
		double[] score1=null;//存放input1数据
		double[] score2=null;//存放input2数据
		ArrayList<Double> arrayList=new ArrayList<Double>();
		int scoreCount=0;//存放score数量
		double score=0;
		String tempLine=null;
		String[] terms=null;
		String pearsonResult=null;//用于显示pearsonResult
		
		//读取input1中的score
		fileReader=new FileReader(input1);
		lineNumberReader=new LineNumberReader(fileReader);
		while((tempLine=lineNumberReader.readLine())!=null){
			terms=tempLine.split("\t");
			score=Double.parseDouble(terms[3]);
			arrayList.add(score);
		}
		//把arrayList转化为double数组
		scoreCount=arrayList.size();
		score1=new double[scoreCount];
		for(int i=0;i<scoreCount;i++)
			score1[i]=arrayList.get(i);
		//关闭IO文件
		lineNumberReader.close();
		
		//读取input2中的score,清空arrayList,修改terms[x]的x
		fileReader=new FileReader(input2);
		lineNumberReader=new LineNumberReader(fileReader);
		arrayList.clear();//清空arrayList
		while((tempLine=lineNumberReader.readLine())!=null){
			terms=tempLine.split("\t");
			score=Double.parseDouble(terms[4]);
			arrayList.add(score);
		}
		//把arrayList转化为double数组
		scoreCount=arrayList.size();
		score2=new double[scoreCount];
		for(int i=0;i<scoreCount;i++)
			score2[i]=arrayList.get(i);
		//关闭IO文件
		lineNumberReader.close();
		//计算pearson
		pearsonResult=PearsonWithDataFromFile.computePearson(score1, score2);
		//根据input1文件,显示pearsonResult
		if(input1.contains("nQCScore")) pearsonResult="nQC "+pearsonResult;
		if(input1.contains("sDScore")) pearsonResult="sD "+pearsonResult;
		if(input1.contains("wIGScore")) pearsonResult="wIG "+pearsonResult;
		if(input1.contains("sMVScore")) pearsonResult="sMV "+pearsonResult;
		
		if(input1.contains("iA_SUMScore")) pearsonResult="iA_SUM "+pearsonResult;
		
		if(input1.contains("sD2Score")) pearsonResult="sD2 "+pearsonResult;
		if(input1.contains("cScore")) pearsonResult="c "+pearsonResult;
		if(input1.contains("c2Score")) pearsonResult="c2 "+pearsonResult;
		if(input1.contains("c3Score")) pearsonResult="c3 "+pearsonResult;
		if(input1.contains("c4Score")) pearsonResult="c4 "+pearsonResult;
		
		System.out.println(pearsonResult);
	}
	public static void main(String[] args){
		try {
			//nQC对应的pearson
			loadScoreAndComputePearson("./robustTrack2004/nQCScore.apl04rsTDNw5","./robustTrack2004/map.normalized.apl04rsTDNw5");
			//sD对应的pearson
			loadScoreAndComputePearson("./robustTrack2004/sDScore.apl04rsTDNw5","./robustTrack2004/map.normalized.apl04rsTDNw5");
			//wIG对应的pearson
			loadScoreAndComputePearson("./robustTrack2004/wIGScore.apl04rsTDNw5","./robustTrack2004/map.normalized.apl04rsTDNw5");
			//sMV对应的pearson
			loadScoreAndComputePearson("./robustTrack2004/sMVScore.apl04rsTDNw5","./robustTrack2004/map.normalized.apl04rsTDNw5");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
// ./robustTrack2004/nQCScore.pircRB04t3 对应的pearsonCoefficient=0.5971430864664645
	// sDScore pearsonCoefficient=0.6458754968527928
	//wIGScore pearsonCoefficient=0.5752996313858094
	//sMVScore pearsonCoefficient=0.5910095602130441
	
}
