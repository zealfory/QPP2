package correlationCoefficient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 根据http://blog.csdn.net/wsywl/article/details/5859751的介绍写成。
 * Spearman's rank correlation coefficient。
 * @author 1
 *
 */
public class Spearman {
	
	private int k=0;//score数组的最大长度
	private double[] m;
	private double[] n;
	private double[] mRank;
	private double[] nRank;
	/**
	 * 
	 * @return
	 */
	public double getSpearmanCoeff(){
		mRank=new double[k];
		nRank=new double[k];
		//根据m数组给mRank数组赋值
		int cont1=0;//记录大于特定元素的元素个数
		int cont2=0;//记录与特定元素相同的元素个数
		double sum_cont2=0;
		for(int i=0;i<k;i++){
		    cont1 = 1;
		    cont2 = -1;
			for(int j=0;j<k;j++){
				if(m[i]<m[j]){
					cont1++;
				}else if(m[i]==m[j]){
					cont2++;
				}
			}
			//计算0到sum_cont2的平均值
			sum_cont2=0;//sum_cont2置为0
			for(int t=0;t<=cont2;t++){
				sum_cont2=sum_cont2+t;
			}
			sum_cont2=sum_cont2/(cont2+1);
			mRank[i]=cont1+sum_cont2;
		}
		//根据n数组给nRank数组赋值
		for(int i=0;i<k;i++){
			cont1 = 1;
			cont2 = -1;
			for(int j=0;j<k;j++){
				if(n[i]<n[j]){
					cont1++;
				}else if(n[i]==n[j]){
					cont2++;
				}
			}
			//计算0到sum_cont2的平均值
			sum_cont2=0;//sum_cont2置为0
			for(int t=0;t<=cont2;t++){
				sum_cont2=sum_cont2+t;
			}
			sum_cont2=sum_cont2/(cont2+1);
			nRank[i]=cont1+sum_cont2;
		}
		//计算mRank和nRank间的pearson系数
		return getPearsonCoeff();
	}
	/**
	 * 计算mRank和nRank间的pearson系数
	 * @return
	 */
	public double getPearsonCoeff(){
		String pearsonResult=null;
		double pearsonCoeff=0;
		pearsonResult=PearsonWithDataFromFile.computePearson(mRank, nRank);
		pearsonCoeff=Double.parseDouble(pearsonResult.split("=")[1].trim());
		return pearsonCoeff;
	}
	/**
	 * 计算Spearman系数
	 * @param score1
	 * @param score2
	 */
	public String computeSpearman(double[] score1,double[] score2){
		double spearmanCoeff=0;
		String spearmanResult=null;
		m=score1;
		n=score2;
		//k取数组长度的较大者
		k=score1.length>score2.length?score1.length:score2.length;
		spearmanCoeff=getSpearmanCoeff();
		spearmanResult="spearmanCoefficient="+spearmanCoeff;
		return spearmanResult;
	}
	
	/**
	 * 需根据input文件的格式对此函数进行修改
	 * 此处加载的文件为: ./robustTrack2004/nQCScore.runId  ./robustTrack2004/map.normalized.runId
	 * 计算input1和input2的Spearman系数
	 * @throws IOException 
	 * */
	public void loadScoreAndComputeSpearman(String input1, String input2) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		double[] score1=null;//存放input1数据
		double[] score2=null;//存放input2数据
		ArrayList<Double> arrayList=new ArrayList<Double>();
		int scoreCount=0;//存放score数量
		double score=0;
		String tempLine=null;
		String[] terms=null;
		String result=null;//用于显示spearman的结果
		
		//读取input1中的score
		fileReader=new FileReader(input1);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
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
		buffReader.close();
		
		//读取input2中的score,清空arrayList,修改terms[x]的x
		fileReader=new FileReader(input2);
		buffReader=new BufferedReader(fileReader);
		arrayList.clear();//清空arrayList
		while((tempLine=buffReader.readLine())!=null){
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
		buffReader.close();
		//计算spearman
		result=computeSpearman(score1, score2);
		//根据input1文件,显示result
		if(input1.contains("nQCScore")) result="nQC "+result;
		if(input1.contains("sDScore")) result="sD "+result;
		if(input1.contains("wIGScore")) result="wIG "+result;
		if(input1.contains("sMVScore")) result="sMV "+result;
		
		if(input1.contains("sD2Score")) result="sD2 "+result;
		if(input1.contains("cScore")) result="c "+result;
		if(input1.contains("c2Score")) result="c2 "+result;
		if(input1.contains("c3Score")) result="c3 "+result;
		if(input1.contains("c4Score")) result="c4 "+result;
		
		System.out.println(result);
	}
	
	public static void main(String[] args){
		/*
		double[] a={0.586,0.89,0.357,0.41,0.41};
		double[] b={0.926,0.995,0.322,0.282,0.282};
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]+" ");
		}
		Spearman spearman=new Spearman();
		String result=spearman.computeSpearman(a, b);
		System.out.println("\n"+result);
		*/
	}

}
