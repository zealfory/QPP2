package time_20170529;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * 使用线性回归方法计算baseline方法的性能。
 * @author 1
 *
 */
public class Process_baseline_2 {
	
	public static ArrayList<Run_linear> ArrayRun=null;
	
	/**
	 * 在packageName文件夹中,将AP信息加入heart_scale文件中,形成heart2文件。
	 * @param runId
	 * @param packageName
	 * @throws IOException 
	 */
	public static void add_ap_to_heart_scale(String runId,String out_name,String packageName) throws IOException{
		BufferedReader buffReader=null;
		BufferedWriter buffWriter=null;
		StringBuffer info=null;
		String tempLine=null;
		String[] terms=null;
		
		//将map.normalized.runId文件中的AP信息存入array_info中
		ArrayList<StringBuffer> array_info=new ArrayList<StringBuffer>();
		buffReader=new BufferedReader(new FileReader("./"+packageName+"/map.normalized."+runId));
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			info=new StringBuffer(terms[4]);
			array_info.add(info);
		}
		buffReader.close();
		
		//将heart_scale文件中的信息存入array_info中
		buffReader=new BufferedReader(new FileReader("./"+packageName+"/heart_scale"));
		int pointer=0;//array_info数组的指针
		while((tempLine=buffReader.readLine())!=null){
			info=array_info.get(pointer++);
			info.append(" "+tempLine);
		}
		buffReader.close();
		
		//将array_info中的信息存入heart2文件中
		buffWriter=new BufferedWriter(new FileWriter("./"+packageName+"/"+out_name));
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			buffWriter.write(info.toString()+"\n");
		}
		buffWriter.close();
		System.out.println("将AP信息加入heart_scale文件中并形成heart2文件,已完成..");
	}
	
	/**
	 * 在packageName文件夹中,去除了heart2文件中的<index:>信息
	 * @param packageName
	 * @throws IOException 
	 */
	public static void remove_index(String packageName) throws IOException{
		BufferedReader buffReader=null;
		BufferedWriter buffWriter=null;
		String tempLine=null;
		ArrayList<String> array_info=new ArrayList<String>();
		
		buffReader=new BufferedReader(new FileReader("./"+packageName+"/heart2"));
		while((tempLine=buffReader.readLine())!=null){
			tempLine=tempLine.replaceAll("[\\d]+:", "");
			array_info.add(tempLine);
		}
		buffReader.close();
		
		//将array_info中的信息存入heart2文件中
		buffWriter=new BufferedWriter(new FileWriter("./"+packageName+"/heart2"));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			buffWriter.write(tempLine+"\n");
		}
		buffWriter.close();
		System.out.println("去除了heart2文件中的<index:>信息,已完成..");
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		//根据线性回归文件,和heart_scale_test_x文件,产生对应的预测类别文件。
		task_control();
		
		
	}
	/**
	 * 将线性回归的结果存入内存
	 * @throws IOException 
	 * 
	 */
	public static ArrayList<Run_linear> load_linear(String input) throws IOException{
		BufferedReader buffReader=null;
		String tempLine=null;
		
		//将input文件中的结果,除空字符串外,存入array_info中
		buffReader=new BufferedReader(new FileReader(input));
		ArrayList<String> array_info=new ArrayList<String>();
		while((tempLine=buffReader.readLine())!=null){
			//若tempLine为空字符串,不处理
			if(tempLine.equalsIgnoreCase("")) continue;
			array_info.add(tempLine);
		}
		buffReader.close();
		
		//根据array_info中的信息,给array_run赋值
		ArrayList<Run_linear> array_run=new ArrayList<Run_linear>();
		Run_linear run_linear=null;
		ListIterator<String> iter=array_info.listIterator();
		while(iter.hasNext()){
			run_linear=new Run_linear(iter);
			array_run.add(run_linear);
		}
		System.out.println("将线性回归的结果存入内存,已完成..");
		return array_run;
	}
	
	/**
	 * 针对一个heart_scale_test_x文件,使用线性回归方程(y=a+bx)分析并产生预测类别信息,<br>
	 * 之后将类别信息存入文件。
	 * @throws IOException 
	 */
	public static void generate_prediction(int i_heart_scale_test,int i_packageName,String packageName) throws IOException{
		String input=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		
		//将heart_scale_test_x文件中的信息存入data数组中
		String[] terms=null;
		double[][] data=null;
		double[] terms2=null;
		ArrayList<double[]> array_data=new ArrayList<double[]>();
		//将heart_scale_test_x数据存入array_data
		input="./"+packageName+"/heart_scale_test_"+i_heart_scale_test;
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			//将terms中的字符串存入terms2数组中
			terms2=new double[terms.length];
			for(int i=0;i<terms.length;i++){
				terms2[i]=Double.parseDouble(terms[i]);
			}
			array_data.add(terms2);
		}
		buffReader.close();
		//使用array_data给data数组赋值
		data=new double[array_data.size()][];
		for(int i=0;i<array_data.size();i++){
			data[i]=array_data.get(i);
		}
		
		/****************/
		//将每个预测算法的预测类别都存于classLabel数组中
		int[][] classLabel=null;//classLabel数组的各列分别为<standard,sD2,wIG,sMV,nQC,c,c2,c4>
		double y=0;
		double a=0;
		double b=0;
		//将每个预测算法回归对应的ap值都存于predictedAP数组中（by Zoey）
		double[][] predictedAP=null;
		
		//将标准类别存入classLabel第一列中
		classLabel=new int[data.length][8];//classLabel共有8列值
		for(int i=0;i<data.length;i++){
			classLabel[i][0]=(int)data[i][1];
		}
		
		//将真实AP值存入predictedAP第一列中（by Zoey)
		predictedAP=new double[data.length][8];
		for(int i=0;i<data.length;i++){
			predictedAP[i][0]=data[i][0];
		}
		
		//计算sD2列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].sD2[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].sD2[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][2];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][1]=y;
			if(y<=0.2) classLabel[i][1]=1;
			if(y>0.2&&y<=0.4) classLabel[i][1]=2;
			if(y>0.4) classLabel[i][1]=3;
		}
		//计算wIG列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].wIG[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].wIG[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][3];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][2]=y;
			if(y<=0.2) classLabel[i][2]=1;
			if(y>0.2&&y<=0.4) classLabel[i][2]=2;
			if(y>0.4) classLabel[i][2]=3;
		}
		//计算sMV列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].sMV[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].sMV[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][4];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][3]=y;
			if(y<=0.2) classLabel[i][3]=1;
			if(y>0.2&&y<=0.4) classLabel[i][3]=2;
			if(y>0.4) classLabel[i][3]=3;
		}
		//计算nQC列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].nQC[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].nQC[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][5];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][4]=y;
			if(y<=0.2) classLabel[i][4]=1;
			if(y>0.2&&y<=0.4) classLabel[i][4]=2;
			if(y>0.4) classLabel[i][4]=3;
		}
		//计算c列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][6];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][5]=y;
			if(y<=0.2) classLabel[i][5]=1;
			if(y>0.2&&y<=0.4) classLabel[i][5]=2;
			if(y>0.4) classLabel[i][5]=3;
		}
		//计算c2列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c2[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c2[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][7];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][6]=y;
			if(y<=0.2) classLabel[i][6]=1;
			if(y>0.2&&y<=0.4) classLabel[i][6]=2;
			if(y>0.4) classLabel[i][6]=3;
		}
		//计算c4列的y值,得出预测类别并存入classLabel
		a=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c4[0];
		b=ArrayRun.get(i_packageName).heart_train[i_heart_scale_test].c4[1];
		for(int i=0;i<data.length;i++){
			y=a+b*data[i][8];
			//将y值存入predictedAP（by Zoey）
			predictedAP[i][7]=y;
			if(y<=0.2) classLabel[i][7]=1;
			if(y>0.2&&y<=0.4) classLabel[i][7]=2;
			if(y>0.4) classLabel[i][7]=3;
		}
		
		/********************/
		//将classLabel数组中信息存入文件
		BufferedWriter buffWriter=null;
		tempLine="";//重用tempLine,并将其置为空字符串
		input="./"+packageName+"/heart_scale_test_"+i_heart_scale_test+"_regression";
		buffWriter=new BufferedWriter(new FileWriter(input));
		for(int i=0;i<classLabel.length;i++){
			tempLine="";//将tempLine置为空字符串
			for(int j=0;j<classLabel[i].length;j++){
				tempLine=tempLine+classLabel[i][j]+"\t";
			}
			//去除tempLine末尾的\t
			tempLine=tempLine.trim();
			buffWriter.write(tempLine+"\n");
		}
		buffWriter.close();
		System.out.println("针对一个heart_scale_test_x文件,产生预测类别信息并存入文件,已完成..");
	
		/*********************/
		//将predictedAP数组中信息存入文件（by Zoey）
		BufferedWriter bfWriter=null;
		tempLine="";
		input="./"+packageName+"/heart_scale_test_"+i_heart_scale_test+"_regression_AP_value";
		bfWriter=new BufferedWriter(new FileWriter(input));
		for(int i=0;i<predictedAP.length;i++){
			tempLine="";
			for(int j=0;j<predictedAP[i].length;j++){
				tempLine=tempLine+predictedAP[i][j]+"\t";
			}
			
			tempLine=tempLine.trim();
			bfWriter.write(tempLine+"\n");
		}
		bfWriter.close();
		System.out.println("针对一个heart_scale_test_x文件，产生回归预测到的AP值并存入文件，已完成..");
	}
	
	/**
	 * 根据得到的线性回归方程,批量产生每个heart_scale_test_x文件对应的预测类别,并存入文件
	 * @throws IOException 
	 */
	public static void generate_prediction_batch() throws IOException{
		//为每个packageName下的heart_scale_test_x产生预测类别
		BufferedReader buffReader=null;
		String tempLine=null;
		String input=null;
		String runId=null;
		String packageName=null;//作为generate_prediction()的输入参数
		int i_packageName=-1;//packageName的编号,初始时为-1
		
		input="./robustTrack2004/runId.txt";
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			runId=tempLine.split("\\.")[1];
			packageName="robustTrack2004/"+runId;
			i_packageName++;
			//每个packageName下有5个heart_scale_test_x文件
			for(int i=0;i<5;i++){
				generate_prediction(i,i_packageName,packageName);
			}
		}
		buffReader.close();
		System.out.println("批量产生每个heart_scale_test_x文件对应的预测类别,并存入文件,已完成..");
	}
	
	/**
	 * 针对一个heart_scale_test_x_regression文件,计算准确率信息
	 * @throws IOException 
	 */
	public static double[][] compute_accuracy(String fileName,String packageName) throws IOException{
		//将input文件存入classLabel数组
		BufferedReader buffReader=null;
		String input=null;
		String tempLine=null;
		String[] terms=null;
		int[] terms2=null;
		/**
		 * classLabel数组的格式为:
		 * standard sD2 wIG sMV nQC c c2 c4
		 *     x     x   x   x   x  x  x  x
		 *     x     x   x   ...
		 * 
		 */
		int[][] classLabel=null;
		ArrayList<int[]> array_classLabel=new ArrayList<int[]>();
		
		input="./"+packageName+"/"+fileName;
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			terms2=new int[terms.length];
			for(int i=0;i<terms.length;i++){
				terms2[i]=Integer.parseInt(terms[i]);
			}
			array_classLabel.add(terms2);
		}
		buffReader.close();
		//将array_classLabel中的数据存入classLabel数组中
		classLabel=new int[array_classLabel.size()][];
		for(int i=0;i<array_classLabel.size();i++){
			classLabel[i]=array_classLabel.get(i);
		}
		
		/**************************/
		//计算每个预测方法的类别预测准确率,并存入accuracy数组中,预测方法共有7个
		int labe_1=0;
		int labe_1_matched=0;
		int labe_2=0;
		int labe_2_matched=0;
		int labe_3=0;
		int labe_3_matched=0;
		/**
		 * accuracy数组的格式为:<br>
		 *         sD2 wIG sMV nQC c c2 C4
		 * overall  x   x   x   x  x x  x
		 * hard     x   x   x   ...
		 * medium
		 * easy
		 * (hard+easy)
		 * 
		 */
		double[][] accuracy=new double[5][7];
		
		//预测算法共有7个,k_method为预测算法的编号(从0开始编号)
		for(int k_method=0;k_method<7;k_method++){
			for(int i=0;i<classLabel.length;i++){
				if(classLabel[i][0]==1){
					labe_1++;
					if(classLabel[i][0]==classLabel[i][1+k_method]) labe_1_matched++;
				}
				if(classLabel[i][0]==2){
					labe_2++;
					if(classLabel[i][0]==classLabel[i][1+k_method]) labe_2_matched++;
				}
				if(classLabel[i][0]==3){
					labe_3++;
					if(classLabel[i][0]==classLabel[i][1+k_method]) labe_3_matched++;
				}
			}
			//计算准确率
			accuracy[0][k_method]=(double)(labe_1_matched+labe_2_matched+labe_3_matched)/(labe_1+labe_2+labe_3);
			accuracy[1][k_method]=(double)labe_1_matched/labe_1;
			accuracy[2][k_method]=(double)labe_2_matched/labe_2;
			accuracy[3][k_method]=(double)labe_3_matched/labe_3;
			accuracy[4][k_method]=(double)(labe_1_matched+labe_3_matched)/(labe_1+labe_3);
			//重置labe_x,labe_x_matched变量
			labe_1=labe_1_matched=0;
			labe_2=labe_2_matched=0;
			labe_3=labe_3_matched=0;
		}
		
		/**************************/
		//将accuracy数组中的数据存入文件中
		BufferedWriter buffWriter=null;
		
		input="./"+packageName+"/"+fileName+"_accuracy";
		buffWriter=new BufferedWriter(new FileWriter(input));
		for(int i=0;i<accuracy.length;i++){
			tempLine="";//重置tempLine
			for(int j=0;j<accuracy[i].length;j++){
				tempLine=tempLine+accuracy[i][j]+"\t";
			}
			//去除tempLine末尾的\t
			tempLine=tempLine.trim();
			buffWriter.write(tempLine+"\n");
		}
		buffWriter.close();
		System.out.println("针对一个heart_scale_test_x_regression文件,计算准确率信息并存入文件,已完成..");
		
		//返回accuracy数组
		return accuracy;
	}
	/**
	 * 批量产生heart_scale_test_x_regression文件对应的准确率信息<br>
	 * @throws IOException 
	 * 
	 */
	public static void compute_accuracy_batch() throws IOException{
		//批量产生准确率文件,并计算预测方法在该运行结果上的平均准确率信息
		BufferedReader buffReader=null;
		String tempLine=null;
		String input=null;
		String runId=null;
		String packageName=null;
		String fileName=null;
		double[][][] accu=null;//存放一个运行结果对应的准确率
		ArrayList<double[][]> array_accu=null;
		double[][] accuracy=null;
		
		//遍历每个运行结果,针对每个运行结果,计算平均准确率信息并存入文件。
		input="./robustTrack2004/runId.txt";
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			runId=tempLine.split("\\.")[1];
			packageName="robustTrack2004/"+runId;
			array_accu=new ArrayList<double[][]>();//创建array_accu对象
			//每个runId下有5个heart_scale_test_x_regression文件,相应地产生5组准确率信息
			for(int i=0;i<5;i++){
				fileName="heart_scale_test_"+i+"_regression";
				//针对一个heart_scale_test_x_regression文件,计算准确率信息
				accuracy=compute_accuracy(fileName,packageName);
				array_accu.add(accuracy);
			}
			//将array_accu中的数据存入accu数组中,其中accu[array_accu.size()]存放平均值
			accu=new double[array_accu.size()+1][][];
			for(int i=0;i<array_accu.size();i++){
				accu[i]=array_accu.get(i);
			}
			
			/************************************/
			//创建accu[accu.length-1]并给其赋值
			accu[accu.length-1]=new double[accu[0].length][accu[0][0].length];
			for(int i=0;i<accu.length-1;i++){
				for(int j=0;j<accu[i].length;j++){
					for(int k=0;k<accu[i][j].length;k++){
						//accu[accu.length-1][j][k]中存入平均值,这里提前乘以1.0/(accu.length-1)
						accu[accu.length-1][j][k]+=accu[i][j][k]/(accu.length-1);
					}
				}
			}
			//将accu[accu.length-1]存入文件中
			BufferedWriter buffWriter=null;
			input="./"+packageName+"/regression_accuracy";
			buffWriter=new BufferedWriter(new FileWriter(input));
			for(int i=0;i<accu[accu.length-1].length;i++){
				tempLine="";//置tempLine为空字符串
				for(int j=0;j<accu[accu.length-1][i].length;j++){
					//tempLine=tempLine+accu[accu.length-1][i][j]+"\t";
					//对于平均准确率,保留小数点后3位
					tempLine=tempLine+String.format("%.3f",accu[accu.length-1][i][j])+"\t";
				}
				//去除tempLine末尾的\t
				tempLine=tempLine.trim();
				buffWriter.write(tempLine+"\n");
			}
			buffWriter.close();
		}
		buffReader.close();
		System.out.println("批量产生heart_scale_test_x_regression文件对应的准确率信息,已完成..");
	}
	
	/**
	 * 针对一个heart_scale_test_x_regression_AP_value文件,计算相对误差信息(by Zoey)
	 * @throws IOException 
	 */
	public static double[][] compute_absolute_error(String fileName,String packageName) throws IOException{
		//将input文件存入predictedAP数组
		BufferedReader buffReader=null;
		String input=null;
		String tempLine=null;
		String[] terms=null;
		double[] terms2=null;
		/**
		 * predictedAP数组的格式为:
		 * standard sD2 wIG sMV nQC c c2 c4
		 *     x     x   x   x   x  x  x  x
		 *     x     x   x   ...
		 * 
		 */
		double[][] predictedAP=null;
		ArrayList<double[]> array_predictedAP=new ArrayList<double[]>();
		
		input="./"+packageName+"/"+fileName;
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			terms2=new double[terms.length];
			for(int i=0;i<terms.length;i++){
				terms2[i]=Double.parseDouble(terms[i]);
			}
			array_predictedAP.add(terms2);
		}
		buffReader.close();
		//将array_predictedAP中的数据存入predictedAP数组中
		predictedAP=new double[array_predictedAP.size()][];
		for(int i=0;i<array_predictedAP.size();i++){
			predictedAP[i]=array_predictedAP.get(i);
		}
		
		/**************************/
		//计算每个预测方法的AP预测相对误差,并存入relativeError数组中,预测方法共有7个
		int labe_1=0;
		double labe_1_absolute_error=0.0;
		int labe_2=0;
		double labe_2_absolute_error=0.0;
		int labe_3=0;
		double labe_3_absolute_error=0.0;
		/**
		 * relativeError数组的格式为:<br>
		 *         sD2 wIG sMV nQC c c2 C4
		 * overall  x   x   x   x  x x  x
		 * hard     x   x   x   ...
		 * medium
		 * easy
		 * (hard+easy)
		 * 
		 */
		double[][] absoluteError=new double[5][7];
		
		//预测算法共有7个,k_method为预测算法的编号(从0开始编号)
		for(int k_method=0;k_method<7;k_method++){
			for(int i=0;i<predictedAP.length;i++){
				if(predictedAP[i][0]>0.4){
					labe_3++;
					labe_3_absolute_error+=Math.abs(predictedAP[i][1+k_method]-predictedAP[i][0]);
				}
				else if(predictedAP[i][0]>0.2&&predictedAP[i][0]<=0.4){
					labe_2++;
					labe_2_absolute_error+=Math.abs(predictedAP[i][1+k_method]-predictedAP[i][0]);
				}
				else{
					labe_1++;
					labe_1_absolute_error+=Math.abs(predictedAP[i][1+k_method]-predictedAP[i][0]);
				}
			}
			//计算相对误差
			absoluteError[0][k_method]=(labe_1_absolute_error+labe_2_absolute_error+labe_3_absolute_error)/(labe_1+labe_2+labe_3);
			absoluteError[1][k_method]=labe_1_absolute_error/labe_1;
			absoluteError[2][k_method]=labe_2_absolute_error/labe_2;
			absoluteError[3][k_method]=labe_3_absolute_error/labe_3;
			absoluteError[4][k_method]=(labe_1_absolute_error+labe_3_absolute_error)/(labe_1+labe_3);
			//重置labe_x变量
			labe_1=0;
			labe_2=0;
			labe_3=0;
			//重置labe_x_relative_error变量
			labe_1_absolute_error=0.0;
			labe_2_absolute_error=0.0;
			labe_3_absolute_error=0.0;
			
		}
		
		/**************************/
		//将relativeError数组中的数据存入文件中
		BufferedWriter buffWriter=null;
		
		input="./"+packageName+"/"+fileName+"_absolute_error";
		buffWriter=new BufferedWriter(new FileWriter(input));
		for(int i=0;i<absoluteError.length;i++){
			tempLine="";//重置tempLine
			for(int j=0;j<absoluteError[i].length;j++){
				tempLine=tempLine+absoluteError[i][j]+"\t";
			}
			//去除tempLine末尾的\t
			tempLine=tempLine.trim();
			buffWriter.write(tempLine+"\n");
		}
		buffWriter.close();
		System.out.println("针对一个heart_scale_test_x_regression_AP_value文件,计算绝对误差信息并存入文件,已完成..");
		
		//返回relativeError数组
		return absoluteError;
	}
	
	/**
	 * 批量产生heart_scale_test_x_regression_AP_value文件对应的绝对误差信息（by Zoey）<br>
	 * @throws IOException 
	 * 
	 */
	public static void compute_absoluteError_batch() throws IOException{
		//批量产生准确率文件,并计算预测方法在该运行结果上的平均相对误差信息
		BufferedReader buffReader=null;
		String tempLine=null;
		String input=null;
		String runId=null;
		String packageName=null;
		String fileName=null;
		double[][][] absolute_error=null;//存放一个运行结果对应的相对误差
		ArrayList<double[][]> array_absolute_error=null;
		double[][] absoluteError=null;
		
		//遍历每个运行结果,针对每个运行结果,计算平均相对误差信息并存入文件。
		input="./robustTrack2004/runId.txt";
		buffReader=new BufferedReader(new FileReader(input));
		while((tempLine=buffReader.readLine())!=null){
			runId=tempLine.split("\\.")[1];
			packageName="robustTrack2004/"+runId;
			array_absolute_error=new ArrayList<double[][]>();//创建array_relative_error对象
			//每个runId下有5个heart_scale_test_x_regression_AP_value文件,相应地产生5组相对误差信息
			for(int i=0;i<5;i++){
				fileName="heart_scale_test_"+i+"_regression_AP_value";
				//针对一个heart_scale_test_x_regression_AP_value文件,计算相对误差信息
				absoluteError=compute_absolute_error(fileName,packageName);
				array_absolute_error.add(absoluteError);
			}
			//将array_relative_error中的数据存入relative_error数组中,其中relative_error[array_relative_error.size()]存放平均值
			absolute_error=new double[array_absolute_error.size()+1][][];
			for(int i=0;i<array_absolute_error.size();i++){
				absolute_error[i]=array_absolute_error.get(i);
			}
			
			/************************************/
			//创建relative_error[relative_error.length-1]并给其赋值
			absolute_error[absolute_error.length-1]=new double[absolute_error[0].length][absolute_error[0][0].length];
			for(int i=0;i<absolute_error.length-1;i++){
				for(int j=0;j<absolute_error[i].length;j++){
					for(int k=0;k<absolute_error[i][j].length;k++){
						//relative_error[relative.length-1][j][k]中存入平均值,这里提前乘以1.0/(relative_error.length-1)
						absolute_error[absolute_error.length-1][j][k]+=absolute_error[i][j][k]/(absolute_error.length-1);
					}
				}
			}
			//将relative_error[relative_error.length-1]存入文件中
			BufferedWriter buffWriter=null;
			input="./"+packageName+"/regression_absoluteError";
			buffWriter=new BufferedWriter(new FileWriter(input));
			for(int i=0;i<absolute_error[absolute_error.length-1].length;i++){
				tempLine="";//置tempLine为空字符串
				for(int j=0;j<absolute_error[absolute_error.length-1][i].length;j++){
					//tempLine=tempLine+relative_error[accu.length-1][i][j]+"\t";
					//对于平均误差,保留小数点后3位
					tempLine=tempLine+String.format("%.3f",absolute_error[absolute_error.length-1][i][j])+"\t";
				}
				//去除tempLine末尾的\t
				tempLine=tempLine.trim();
				buffWriter.write(tempLine+"\n");
			}
			buffWriter.close();
		}
		buffReader.close();
		System.out.println("批量产生heart_scale_test_x_regression_AP_value文件对应的绝对误差信息,已完成..");
	}
	
	/**
	 * 根据线性回归文件,和heart_scale_test_x文件,产生对应的预测类别文件。<br>
	 * @throws IOException 
	 * 
	 */
	public static void task_control() throws IOException{
		//将线性回归的结果存入内存
		String input=null;
		input="./robustTrack2004/新建文本文档 (3).txt";
		ArrayRun=load_linear(input);
		
		//批量产生每个heart_scale_test_x文件对应的预测类别,并存入文件
		generate_prediction_batch();
		
		//批量产生heart_scale_test_x_regression文件对应的准确率信息
		compute_accuracy_batch();
		
		//批量产生heart_scale_test_x_regression_AP_value文件对应的绝对误差信息
		compute_absoluteError_batch();		
		
		
	}
	
	
	

}

class Run_linear{
	String runId;
	Heart_train[] heart_train;
	
	public Run_linear(ListIterator<String> iter){
		String tempLine=null;
		tempLine=iter.next();
		runId=tempLine.replaceFirst("[\\d]+、packageName=robustTrack2004/", "").trim();
		heart_train=new Heart_train[5];
		
		//为heart_train数组赋值
		for(int i=0;i<5;i++){
			iter.next();
			heart_train[i]=new Heart_train(iter);
		}
	}

}

class Heart_train{
	double[] sD2;
	double[] wIG;
	double[] sMV;
	double[] nQC;
	double[] c;
	double[] c2;
	double[] c4;
	
	public Heart_train(ListIterator<String> iter){
		sD2=new double[2];
		wIG=new double[2];
		sMV=new double[2];
		nQC=new double[2];
		c=new double[2];
		c2=new double[2];
		c4=new double[2];
		//使用iter中的信息给数组赋值
		String tempLine=null;
		String[] terms=null;
		
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		sD2[0]=Double.parseDouble(terms[1].split("=")[1]);
		sD2[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		wIG[0]=Double.parseDouble(terms[1].split("=")[1]);
		wIG[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		sMV[0]=Double.parseDouble(terms[1].split("=")[1]);
		sMV[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		nQC[0]=Double.parseDouble(terms[1].split("=")[1]);
		nQC[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		c[0]=Double.parseDouble(terms[1].split("=")[1]);
		c[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		c2[0]=Double.parseDouble(terms[1].split("=")[1]);
		c2[1]=Double.parseDouble(terms[2].split("=")[1]);
		tempLine=iter.next();
		terms=tempLine.split(" |\t");
		c4[0]=Double.parseDouble(terms[1].split("=")[1]);
		c4[1]=Double.parseDouble(terms[2].split("=")[1]);
	}

}
