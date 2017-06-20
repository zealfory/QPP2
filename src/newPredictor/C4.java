package newPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 此预测方法的公式为:Math.pow(Math.E,sum(score(d)*Math.log(score(d)/u)))
 * @author 1
 *
 */
public class C4 {
	
	private int k=20;//k为截断参数
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public double uMean(double[] score){
		double uMean=0;
		double sum=0;
		for(int i=0;i<k;i++){
			sum=sum+score[i];
		}
		uMean=sum/k;
		return uMean;
	}
	/**
	 * 根据score数组和截断参数k计算C4
	 * @param score
	 * @return
	 */
	public double computeC4(double[] score){
		double sum=0;
		double u=uMean(score);
		
		for(int i=0;i<k;i++){
			sum=sum+score[i]*Math.log(score[i]/u);
		}
		return Math.pow(Math.E, sum);
	}
	
	/**
	 * 根据input.runId计算每个query的C4值,并将c4Score存入文件
	 * 
	 * */
	public void getC4Scores(String input, String output) {
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		FileWriter fileWriter = null;
		BufferedWriter buffWriter=null;
		String tempLine = null;
		String[] terms = null;// 分析tempLine
		String preQueryId = null;
		ArrayList<Double> arrayList = new ArrayList<Double>();
		double score = 0;// 临时存放terms[4]的score
		double[] scores = null;// 临时存放一个query对应的score数组
		int scoreCount = 0;// 临时存放score数组的长度
		double c4Score = 0;// 临时存放一个query的c4值
		int k_original=k;//存储起初的k值

		try {
			fileReader = new FileReader(input);
			buffReader = new BufferedReader(fileReader);
			fileWriter = new FileWriter(output, false);
			buffWriter=new BufferedWriter(fileWriter);
			while ((tempLine = buffReader.readLine()) != null) {
				terms = tempLine.split("\t| ");
				// 起初preQueryId为null
				if (preQueryId == null)
					preQueryId = terms[0];
				// queryId相同,存入score
				if (preQueryId.equalsIgnoreCase(terms[0])) {
					score = Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
				// queryId不同,计算preQueryId的c4Score,写入文件,清空arrayList信息,处理terms信息
				if (!preQueryId.equalsIgnoreCase(terms[0])) {
					// 把arrayList转化为double数组
					scoreCount = arrayList.size();
					scores = new double[scoreCount];
					for (int i = 0; i < scoreCount; i++)
						scores[i] = arrayList.get(i);
					//若此查询下的文档数scoreCount小于k,把k设为scoreCount
					if(scoreCount<k) k=scoreCount;
					// 调用computC4()计算此query的c4值
					c4Score = computeC4(scores);
					// 把queryId和c4Score写入文件
					buffWriter.write("queryId:\t" + preQueryId + "\tC4:\t" + c4Score + "\n");
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
			// 最后queryId对应的scores未处理,计算其c4Score,并写入文件
			// 把arrayList转化为double数组
			scoreCount = arrayList.size();
			scores = new double[scoreCount];
			for (int i = 0; i < scoreCount; i++)
				scores[i] = arrayList.get(i);
			//若此查询下的文档数scoreCount小于k,把k设为scoreCount
			if(scoreCount<k) k=scoreCount;
			// 调用computC4()计算此query的c4值
			c4Score = computeC4(scores);
			// 把queryId和c4Score写入文件
			buffWriter.write("queryId:\t" + preQueryId + "\tC4:\t" + c4Score + "\n");
		} catch (IOException e) {
			System.err.println("处理数据出错!");
			e.printStackTrace();
		} finally {
			try {
				buffWriter.close();
				buffReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO连接错误!");
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args){
		
	}

}
