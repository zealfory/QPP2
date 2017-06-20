package time_20170529;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 一元线性回归分析程序。
 * 程序来自http://blog.csdn.net/maozefa/article/details/1725535<br>
 * @author 1
 *
 */
public class LinearRegression {

	/**
	 * 存放input文件的整个数据
	 */
	public static double[][] data1=null;
	/**
	 * 存放X Y数据。x1 y1;x2 y2
	 */
	public static double[][] data2=null;

	// 求线性回归方程: Y = a + bx
	// data2[rows*2]数组：X, Y；rows：数据行数；a, b：返回回归系数
	// squarePoor[4]：返回方差分析指标: 回归平方和，剩余平方和，回归平方差，剩余平方差
	// 返回值：0求解成功，-1错误
	public static void linear_reg(){
		//double[] ab,double[] squarePoor
		double[] ab=new double[2];
		double[] squarePoor=new double[4];
		
		double Lxx = 0, Lxy = 0, xa = 0, ya = 0;
		if (data2 == null || data2.length < 1){
			System.out.println("程序错误!");
			return;
		}

		for(int i=0; i<data2.length;i++){
			xa=xa+data2[i][0];
			ya=ya+data2[i][1];
		}
		xa=xa/data2.length;// X平均值
		ya=ya/data2.length;// Y平均值
		
		for(int i=0;i<data2.length;i++){
			Lxx=Lxx+Math.pow(data2[i][0]-xa, 2);// Lxx = Sum((X - Xa)平方)
			Lxy=Lxy+(data2[i][0]-xa)*(data2[i][1]-ya);// Lxy = Sum((X - Xa)(Y - Ya))
		}
		ab[1] = Lxy/Lxx;              // b = Lxy / Lxx
		ab[0] = ya - ab[1] * xa;      // a = Ya - b*Xa
		
		// 方差分析
		squarePoor[0] = squarePoor[1] = 0;
		for(int i=0;i<data2.length;i++){
			Lxy=ab[0]+ab[1]*data2[i][0];
			squarePoor[0]+=Math.pow(Lxy-ya, 2);// U(回归平方和)
			squarePoor[1]+=Math.pow(data2[i][1]-Lxy, 2);// Q(剩余平方和)	
		}
		squarePoor[2]=squarePoor[0];// 回归方差
		squarePoor[3]=squarePoor[1]/(data2.length-2);// 剩余方差
		
		//输出线性回归方程
		String out=null;
		/*
		out=String.format("回归方程式: Y=%.6f",ab[0]);
		if(ab[1]>=0)
			out+=String.format("+%.6fX", ab[1]);
		else
			out+=String.format("%.6fX", ab[1]);
		*/
		out="a="+ab[0]+"\tb="+ab[1];
		System.out.println(out);
	}

	/**
	 * 显示线性回归结果。
	 */
	public static void display(double[] answer, double[] squarePoor, int cols){
		double v;
		int i,j;
		
		//输出线性回归方程
		String out=null;
		double b=0;
		out=String.format("回归方程式: Y=%.6f",answer[0]);
		
		//此处默认cols=2即有两列数据
		for(i=1;i<cols;i++){
			if(answer[i]>=0)
				out+=String.format("+%.6fX", answer[i]);
			else
				out+=String.format("%.6fX", answer[i]);
		}
		
		System.out.println(out);
		
		/*
		System.out.println();
		out="回归显著性检验:\n";
		out+=String.format("回归平方和: %12.4f  回归方差: %12.4f\n", squarePoor[0], squarePoor[2]);
		out+=String.format("剩余平方和: %12.4f  剩余方差: %12.4f\n", squarePoor[1], squarePoor[3]);
		out+=String.format("离差平方和: %12.4f  标准误差: %12.4f\n", squarePoor[0]+squarePoor[1], Math.sqrt(squarePoor[3]));
		out+=String.format("F   检  验: %12.4f  相关系数: %12.4f\n",
				squarePoor[2]/squarePoor[3], Math.sqrt(squarePoor[0]/(squarePoor[0]+squarePoor[1])));
		
		out+=String.format("剩余分析:\n");
		out+=String.format("观察值      估计值      剩余值    剩余平方\n");
		for(i=0;i<data2.length;i++){
			v = answer[0];
			for(j=1;j<cols;j++)
				v+=data2[i][j-1]*answer[j];
			
			out+=String.format("%12.2f\t%12.2f\t%12.2f\t%12.2f\n", data2[i][j-1],v,data2[i][j-1]-v,(data2[i][j-1]-v)*(data2[i][j-1]-v));
		}
		System.out.println(out+"线性回归结果输出完成..");
		*/
	}
	/**
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		//批量计算线性回归方程
		generate_reg_batch();
		
		
		/*
		double[] answer=new double[2];
		double[] squarePoor=new double[4];
		//
		String input=null;
		input="./robustTrack2004/apl04rsTDNfw/heart_scale_train_0";
		init_data(input);
		//
		//linear_reg(answer,squarePoor);
		//display(answer,squarePoor,2);
		linear_reg();
		System.out.println("LinearRegression程序运行完成..");
		*/
		
	}

	/**
	 * 将input文件中的数据存入data1二维数组中。
	 * @throws IOException 
	 */
	public static void init_data(String input) throws IOException{
		BufferedReader buffReader=null;
		String tempLine=null;
		String[] terms=null;

		//将整个数据存入data1数据
		double[] terms2=null;
		ArrayList<double[]> array_data1=new ArrayList<double[]>();
		buffReader=new BufferedReader(new FileReader(input));

		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			//将terms中的字符串存入terms2数组中
			terms2=new double[terms.length];
			for(int i=0;i<terms.length;i++){
				terms2[i]=Double.parseDouble(terms[i]);
			}
			//将terms2存入array_data1中
			array_data1.add(terms2);
		}
		buffReader.close();
		//将array_data1中的数据存入data1数组中
		data1=new double[array_data1.size()][];
		for(int i=0;i<array_data1.size();i++){
			data1[i]=array_data1.get(i);
		}
		//System.out.println("将input文件中的数据存入data1二维数组中,已完成..");
	}
	
	/**
	 * 将input文件中的数据存入data1 data2二维数组中,并计算线性回归方程
	 * 
	 * @throws IOException 
	 * 
	 */
	public static void generate_reg(String fileName,String packageName) throws IOException{
		
		//将input文件中的数据存入data1二维数组中
		String input=null;
		input="./"+packageName+"/"+fileName;
		init_data(input);
		System.out.println(input);
		
		System.out.print("SD2\t");
		//使用data1中的数据更新data2数组
		//向data2数组存入sD2 ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][2];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("WIG\t");
		//向data2数组存入wIG ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][3];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("SMV\t");
		//向data2数组存入sMV ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][4];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("NQC\t");
		//向data2数组存入nQC ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][5];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("C\t");
		//向data2数组存入c ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][6];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("C2\t");
		//向data2数组存入c2 ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][7];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		
		System.out.print("C4\t");
		//向data2数组存入c4 ap数据
		data2=new double[data1.length][2];
		for(int i=0;i<data1.length;i++){
			//存入X
			data2[i][0]=data1[i][8];
			//存入Y
			data2[i][1]=data1[i][0];
		}
		//计算线性回归方程
		linear_reg();
		//System.out.println("将input文件中的数据存入data1二维数组中并计算线性回归方程,已完成..");
	}
	
	/**
	 * 根据packageName文件夹中的heart_scale_train_0,1,2,3,4文件,计算对应的线性回归方程
	 * @param packageName
	 * @throws IOException 
	 */
	public static void generate_reg_batch() throws IOException{
		String packageName=null;
		String fileName=null;
		
		//
		File file=null;
		File[] file_list=null;
		file=new File("./robustTrack2004");
		file_list=file.listFiles();
		for(int i=0;i<file_list.length;i++){
			packageName="robustTrack2004/"+file_list[i].getName();
			//计算该文件夹下每个文件(heart_scale_train_i)的回归方程
			System.out.println("\n\n"+(i+1)+"、packageName="+packageName);
			for(int j=0;j<5;j++){
				fileName="heart_scale_train_"+j;
				generate_reg(fileName,packageName);
			}
		}
		//System.out.println("批量计算线性回归方程,已完成..");
	}

}
