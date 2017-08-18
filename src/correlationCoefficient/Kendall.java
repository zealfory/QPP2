package correlationCoefficient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 根据http://blog.csdn.net/wsywl/article/details/5889419,对kendall类进行了修改。
 * @author 1
 *
 */
public class Kendall {
	private static double[] m;
	private static double[] n;
	private static int k;
	
	public static double cMinusD(){
		int C=0;
		int D=0;
		double ra=0;
		double rb=0;
		double cMinus=0;
		for(int i=0;i<k-1;i++){
			for(int j=i+1;j<k;j++){
				ra=m[i]-m[j];
				rb=n[i]-n[j];
				//若ra和rb一致
				if(ra>0&&rb>0) C++;
				if(ra<0&&rb<0) C++;
				//若ra和rb不一致
				if(ra>0&&rb<0) D++;
				if(ra<0&&rb>0) D++;
			}
		}
		cMinus=C-D;
		return cMinus;
	}
	
	public static double getDenom(){
		double n3=k*(k-1)/2.0;
		//计算数组m中相同元素的配对数
		double n1=0;
		double n3_n1=0;
		n1=getPairs_equal(m);
		n3_n1=n3-n1;
		//计算数组n中相同元素的配对数
		double n2=0;
		double n3_n2=0;
		n2=getPairs_equal(n);
		n3_n2=n3-n2;
		//计算kendall等级系数的denominator
		double denom=0;
		denom=Math.sqrt(n3_n1*n3_n2);
		return denom;
	}
	
	public static double getCoefficientKendall(){
		double cMinus=0;
		double denom=0;
		double coeff=0;
		cMinus=cMinusD();
		denom=getDenom();
		coeff=cMinus/denom;
		return coeff;
	}
	
	public static String computeKendall(double[] score1,double[] score2){
		double kendallCoeff=0;
		String kendallResult=null;
		m=score1;
		n=score2;
		//k取数组长度的较大者
		k=score1.length>score2.length?score1.length:score2.length;
		kendallCoeff=getCoefficientKendall();
		kendallResult="kendallCoefficient= "+kendallCoeff;
		return kendallResult;
	}
	
	/**
	 * 将数组中的相同元素分别组合成小集合,舍去元素个数为1的小集合,
	 * 计算每个小集合中元素的配对数,并求和
	 */
	public static double getPairs_equal(double[] x){
		HashMap<Double,Integer> map=new HashMap<Double,Integer>();
		double figure=0;
		int count=0;
		//把x数组存入map中
		for(int i=0;i<x.length;i++){
			figure=x[i];
			//若map的keys不含figure,存入(figure 1)键值对
			if(!map.containsKey(figure)){
				map.put(figure, 1);
			}else{
				//若map的keys含有figure,更新figure对应的键值对
				count=map.get(figure);
				count++;
				map.put(figure, count);
			}
		}
		//删除map中count为1的键值对
		figure=0;//figure置为空
		count=0;//count置为0
		Set<Entry<Double,Integer>> set=null;
		Iterator<Entry<Double,Integer>> it=null;
		Entry<Double,Integer> entry=null;
		ArrayList<Double> array_figure=new ArrayList<Double>();
		
		set=map.entrySet();
		it=set.iterator();
		while(it.hasNext()){
			entry=it.next();
			figure=entry.getKey();
			count=entry.getValue();
			//把count为1的figure存入array_figure中
			if(count==1) array_figure.add(figure);
		}
		//根据array_figure中的信息删除map中的信息
		for(int i=0;i<array_figure.size();i++){
			figure=array_figure.get(i);
			map.remove(figure);
		}
		//根据map信息,计算每个小集合中元素的配对数,并求和
		count=0;//重置count为0
		double sum_pair=0;
		double pair=0;
		set=map.entrySet();
		it=set.iterator();
		while(it.hasNext()){
			entry=it.next();
			count=entry.getValue();
			pair=1.0/2*count*(count-1);
			sum_pair=sum_pair+pair;
		}
		return sum_pair;
	}
	/**
	 * 需根据input文件的格式对此函数进行修改
	 * 此处加载的文件为: ./robustTrack2004/nQCScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/sDScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/wIGScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 此处加载的文件为: ./robustTrack2004/sMVScore.pircRB04t3  ./robustTrack2004/map.normalized.pircRB04t3
	 * 计算input1和input2的Kendall系数
	 * @throws IOException 
	 * */
	public static void loadScoreAndComputeKendall(String input1, String input2) throws IOException{
		FileReader fileReader=null;
		LineNumberReader lineNumberReader=null;
		double[] score1=null;//存放input1数据
		double[] score2=null;//存放input2数据
		ArrayList<Double> arrayList=new ArrayList<Double>();
		int scoreCount=0;//存放score数量
		double score=0;
		String tempLine=null;
		String[] terms=null;
		String kendallResult=null;//用于显示kendallResult
		
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
		//计算kendall
		kendallResult=computeKendall(score1, score2);
		//根据input1文件,显示kendallResult
		if(input1.contains("nQCScore")) kendallResult="nQC "+kendallResult;
		if(input1.contains("sDScore")) kendallResult="sD "+kendallResult;
		if(input1.contains("wIGScore")) kendallResult="wIG "+kendallResult;
		if(input1.contains("sMVScore")) kendallResult="sMV "+kendallResult;
		
		if(input1.contains("iA_SUMScore")) kendallResult="iA_SUM "+kendallResult;
		
		if(input1.contains("sD2Score")) kendallResult="sD2 "+kendallResult;
		if(input1.contains("cScore")) kendallResult="c "+kendallResult;
		if(input1.contains("c2Score")) kendallResult="c2 "+kendallResult;
		if(input1.contains("c3Score")) kendallResult="c3 "+kendallResult;
		if(input1.contains("c4Score")) kendallResult="c4 "+kendallResult;
		//add by Zoey
		if(input1.contains("sD_WIGScore")) kendallResult="sD_WIG "+kendallResult;
		if(input1.contains("sD_Multi_WIGScore")) kendallResult="sD_Multi_WIG "+kendallResult;
		if(input1.contains("wIG_NQCScore")) kendallResult="wIG_NQC "+kendallResult;
		if(input1.contains("wIG_Multi_NQCScore")) kendallResult="wIG_Multi_NQC "+kendallResult;
		
		System.out.println(kendallResult);
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		/*FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		String[] terms=null;
		String input=null;
		double[] score1=new double[500];
		double[] score2=new double[500];
		int i=0;
		
		input="./diversification/probability_subquery.txt";
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		buffReader.readLine();
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			score1[i]=Double.parseDouble(terms[0]);
			score2[i]=Double.parseDouble(terms[1]);
			i++;
		}
		buffReader.close();
		//计算pearson系数
		String kendallResult=null;
		kendallResult=computeKendall(score1,score2);
		System.out.println(kendallResult);
		*/
	}

}
