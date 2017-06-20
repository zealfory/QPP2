package correlationCoefficient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PearsonWithDataFromFile {
	public static double[] m;
	public static double[] n;
	private static int k;//double数组的size

	public static double sum(double[] r) {
		double s = 0;
		for (int i = 0; i < k; i++) {
			s += r[i];
		}
		return s;
	}

	public static double sumSqure(double[] r) {
		double s = 0;
		for (int i = 0; i < k; i++) {
			s += r[i] * r[i];
		}
		return s;
	}

	public static double sumMulti(double[] x, double[] y) {
		double s = 0;
		for (int i = 0; i < k; i++) {
			s += x[i] * y[i];
		}
		return s;
	}

	public static double getPearsonCoefficient() {
		double sumXY = sumMulti(m, n);
		double sumX = sum(m);
		double sumY = sum(n);
		double sumX2 = sumSqure(m);
		double sumY2 = sumSqure(n);
		double p = (sumXY - sumX * sumY / k)
				/ Math.sqrt((sumX2 - sumX * sumX / k)
						* (sumY2 - sumY * sumY / k));
		return p;
	}
	/**
	 * 根据数组score1和score2,计算Pearson
	 * */
	public static String computePearson(double[] score1,double[] score2){
		double pearsonCoefficient=0;
		String pearsonResult=null;
		m=score1;
		n=score2;
		k=m.length>n.length?m.length:n.length;
		pearsonCoefficient=getPearsonCoefficient();
		pearsonResult="pearsonCoefficient="+pearsonCoefficient;
		return pearsonResult; 
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
		String pearsonResult=null;
		pearsonResult=computePearson(score1,score2);
		System.out.println(pearsonResult);
		*/
	}

}
