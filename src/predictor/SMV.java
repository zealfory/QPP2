package predictor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class SMV {
	private int k=100;//k为截断参数
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public double mean(double[] score){
		int length=score.length;
		double sum=0;
		for(int i=0;i<length;i++)
			sum=sum+score[i];
		return sum/length;
	}
	/**
	 * 计算前k项的平均值
	 * */
	public double Umean(double[] score){
		double sum=0;
		for(int i=0;i<k;i++)
			sum=sum+score[i];
		return sum/k;
	}
	/**
	 * 根据score数组和截断参数k计算SMV
	 * */
	public double computeSMV(double[] score){
		double u=Umean(score);
		double sum=0;
		double numerator=0;
		double denom=0;
		
		for(int i=0;i<k;i++){
			sum=sum+score[i]*Math.abs(Math.log(score[i]/u));
		}
		numerator=sum/k;
		denom=mean(score);
		return numerator/denom;
	}
	/**
	 * 在2016/06/02,作了修改,加入了k_original变量。
	 * 根据input.pircRB04t3计算每个query的SMV值, 并将SMVScore存入文件
	 * 
	 * */
	public void getSMVScores(String input, String output) {
		FileReader fileReader = null;
		LineNumberReader lineNumberReader = null;
		FileWriter fileWriter = null;
		String tempLine = null;
		String[] terms = null;// 分析tempLine
		String preQueryId = null;
		ArrayList<Double> arrayList = new ArrayList<Double>();
		double score = 0;// 临时存放terms[4]的score
		double[] scores = null;// 临时存放一个query对应的score数组
		int scoreCount = 0;// 临时存放score数组的长度
		double SMVScore = 0;// 临时存放一个query的SMV值
		int k_original=k;//存储起初的k值

		try {
			fileReader = new FileReader(input);
			lineNumberReader = new LineNumberReader(fileReader);
			fileWriter = new FileWriter(output, false);
			while ((tempLine = lineNumberReader.readLine()) != null) {
				terms = tempLine.split("\t| ");
				// 起初preQueryId为null
				if (preQueryId == null)
					preQueryId = terms[0];
				// queryId相同,存入score
				if (preQueryId.equalsIgnoreCase(terms[0])) {
					score = Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
				// queryId不同,计算preQueryId的SMVScore,写入文件,清空arrayList信息,处理terms信息
				if (!preQueryId.equalsIgnoreCase(terms[0])) {
					// 把arrayList转化为double数组
					scoreCount = arrayList.size();
					scores = new double[scoreCount];
					for (int i = 0; i < scoreCount; i++)
						scores[i] = arrayList.get(i);
					//若此查询下的文档数scoreCount小于k,把k设为scoreCount
					if(scoreCount<k) k=scoreCount;
					// 调用computSMV()计算此query的SMV值
					SMVScore = computeSMV(scores);
					// 把queryId和SMVScore写入文件
					fileWriter.write("queryId:\t" + preQueryId + "\tSMV:\t"
							+ SMVScore + "\n");
					//若k不为初始k值,把k设为初始值k_original
					if(k!=k_original) k=k_original;
					// 清空arrayList
					arrayList.clear();
					// 开始处理terms信息
					preQueryId = terms[0];
					score = Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
			}
			// 最后queryId对应的scores未处理,计算其SMVScore,并写入文件
			// 把arrayList转化为double数组
			scoreCount = arrayList.size();
			scores = new double[scoreCount];
			for (int i = 0; i < scoreCount; i++)
				scores[i] = arrayList.get(i);
			//若此查询下的文档数scoreCount小于k,把k设为scoreCount
			if(scoreCount<k) k=scoreCount;
			// 调用computSMV()计算此query的SMV值
			SMVScore = computeSMV(scores);
			// 把queryId和SMVScore写入文件
			fileWriter.write("queryId:\t" + preQueryId + "\tSMV:\t" + SMVScore
					+ "\n");
		} catch (IOException e) {
			System.err.println("处理数据出错!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				lineNumberReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO连接错误!");
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		SMV sMV = new SMV();
		sMV.k=100;
		sMV.getSMVScores("./robustTrack2004/input.apl04rsTDNw5.inversed",
				"./robustTrack2004/sMVScore.apl04rsTDNw5");
		System.out
		.println("根据input.pircRB04t3计算每个query的SMV值,并将SMVScore存入文件,已完成..");
	}

}
