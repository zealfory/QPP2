package predictor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class NQC {
	private int k = 100; // k为NQC的截断指标
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}

	public double mean(double[] m, int n) {
		double s = 0;
		for (int i = 0; i < n; i++) {
			s += m[i];
		}
		return s / n;
	}

	public double Umean(double[] m) {
		double s = 0;
		for (int i = 0; i < k; i++) {
			s += m[i];
		}
		return s / k;
	}

	// NQC算法
	public double computeNQC(double[] score, int n) {
		double u = Umean(score);
		double s = 0;
		double numerator = 0;
		double denom = 0;
		for (int i = 0; i < k; i++) {
			s += (score[i] - u) * (score[i] - u);
		}
		numerator = Math.sqrt(s / k);
		denom = Math.abs(mean(score, n));
		return numerator / denom;
	}
	/**
	 * 在2016/06/02,作了修改,加入了k_original变量。
	 * 根据input.pircRB04t3计算每个query的NQC值, 并将NQCScore存入文件 
	 * @ track为NLPR04OKapi
	 *  
	 * */
	public void getNQCScores(String input,String output){
		FileReader fileReader=null;
		LineNumberReader lineNumberReader=null;
		FileWriter fileWriter=null;
		String tempLine=null;
		String[] terms=null;//分析tempLine
		String preQueryId=null;
		ArrayList<Double> arrayList=new ArrayList<Double>();
		double score=0;//临时存放terms[4]的score
		double[] scores=null;//临时存放一个query对应的score数组
		int scoreCount=0;//临时存放score数组的长度
		double NQCScore=0;//临时存放一个query的NQC值
		int k_original=k;//存储起初的k值
		
		try{
			fileReader=new FileReader(input);
			lineNumberReader=new LineNumberReader(fileReader);
			fileWriter=new FileWriter(output,false);	
			while((tempLine=lineNumberReader.readLine())!=null){
				terms=tempLine.split("\t| ");
				//起初preQueryId为null
				if(preQueryId==null) preQueryId=terms[0];
				//queryId相同,存入score
				if(preQueryId.equalsIgnoreCase(terms[0])){
					score=Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
				//queryId不同,计算preQueryId的NQCScore,写入文件,清空arrayList信息,处理terms信息
				if(!preQueryId.equalsIgnoreCase(terms[0])){
					//把arrayList转化为double数组
					scoreCount=arrayList.size();
					scores=new double[scoreCount];
					for(int i=0;i<scoreCount;i++)
						scores[i]=arrayList.get(i);
					//若此查询下的文档数scoreCount小于k,把k设为scoreCount
					if(scoreCount<k) k=scoreCount;
					//调用computNQC()计算此query的NQC值
					NQCScore=computeNQC(scores,scoreCount);
					//把queryId和NQCScore写入文件
					fileWriter.write("queryId:\t"+preQueryId+"\tNQC:\t"+NQCScore+"\n");
					//若k不为初始k值,把k设为初始值k_original
					if(k!=k_original) k=k_original;
					//清空arrayList
					arrayList.clear();
					//开始处理terms信息
					preQueryId=terms[0];
					score=Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
			}
			//最后queryId对应的scores未处理,计算其NQCScore,并写入文件
			//把arrayList转化为double数组
			scoreCount=arrayList.size();
			scores=new double[scoreCount];
			for(int i=0;i<scoreCount;i++)
				scores[i]=arrayList.get(i);
			//若此查询下的文档数scoreCount小于k,把k设为scoreCount
			if(scoreCount<k) k=scoreCount;
			//调用computNQC()计算此query的NQC值
			NQCScore=computeNQC(scores,scoreCount);
			//把queryId和NQCScore写入文件
			fileWriter.write("queryId:\t"+preQueryId+"\tNQC:\t"+NQCScore+"\n");
		}catch(IOException e){
			System.err.println("处理数据出错!");
			e.printStackTrace();
		}finally{
			try {
				fileWriter.close();
				lineNumberReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO连接错误!");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		NQC nQC=new NQC();
		nQC.k=100;
		nQC.getNQCScores("./robustTrack2004/input.apl04rsTDNw5.inversed", "./robustTrack2004/nQCScore.apl04rsTDNw5");
		System.out.println("根据input.pircRB04t3计算每个query的NQC值,并将NQCScore存入文件,已完成..");
	}
	/*
	//计算了queryId=327的NQCScore
	public static void main(String[] args){
		String fileName=null;
		FileReader fileReader=null;
		LineNumberReader lineNumberReader=null;
		String tempLine=null;
		
		double[] scores=null;//供NQC使用
		int scoreCount=0;//score的数量
		double NQCScore=0;//NQC值
		
		ArrayList<Double> arrayList=null;
		String[] terms=null;
		double score=0;
		arrayList=new ArrayList<Double>();
		
		try{
			fileName="./robustTrack2004/input.pircRB04t3";
			fileReader=new FileReader(fileName);
			lineNumberReader=new LineNumberReader(fileReader);
			
			while((tempLine=lineNumberReader.readLine())!=null){
				terms=tempLine.split("\t| ");
				if(terms[0].equalsIgnoreCase("327")){
					score=Double.parseDouble(terms[4]);
					System.out.println("score="+score);
					arrayList.add(score);
				}
			}
			
		}catch(IOException e){
			System.err.println("出现错误!");
			e.printStackTrace();
		}finally{
			try {
				lineNumberReader.close();
			} catch (IOException e) {
				System.err.println("lineNumberReader.close()错误..");
				e.printStackTrace();
			}
		}
		//把arrayList转化为double[]
		scoreCount=arrayList.size();
		//
		scores=new double[scoreCount];
		System.out.println(scoreCount);
		for(int i=0;i<scoreCount;i++){
			System.out.println("i="+i+" "+arrayList.get(i));
			scores[i]=(double)arrayList.get(i);
			System.out.println("i="+i+" "+arrayList.get(i));
		}
			//scores[i]=arrayList.get(i);
		
		System.out.println("scoreCount="+scoreCount);
		
		NQC nQC=new NQC();
		nQC.k=100;
		NQCScore=nQC.computeNQC(scores, scoreCount);
		System.out.println("NQC值为 "+NQCScore);
		//NQC(324,pircRB04t3)=0.2020690488478744
		//NQC(327,pircRB04t3)=0.20966116075331787
		
	}*/

}
