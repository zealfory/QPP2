package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import utils.Result;
import utils.Result_compare;

public class ProcessTrivial {

	/**
	 * 重建input文件：<br>
	 * 1、对input文件中的文档进行排序: sort by topic, then by score, and
	 * then by docno, which is the traditional sort order for TREC runs
	 * 2、生成rank信息,rank值为1,2,3,...,n。
	 * 3、topic_sum为此input文件中应有的topic总数，若此input文件中的实际topic总数
	 * 与topic_sum不相同，输出提示信息，终止程序。<br><br>
	 * 若不需检查此input文件的topic总数，topic_sum置为-1。
	 * @param input
	 * @throws IOException 
	 */
	public static void rebuildInput(String input,int topic_sum) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		String tempLine=null;
		ArrayList<Result> array_result=new ArrayList<Result>();
		Result result=null;

		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		//把tempLine的信息存入array_result中
		while((tempLine=buffReader.readLine())!=null){
			result=new Result(tempLine);
			array_result.add(result);
		}
		buffReader.close();
		//对array_result信息进行排序
		Collections.sort(array_result, new Result_compare());
		//更新array_result中对象result的rank信息,rank值为1,2,3,...,n
		int rank=1;
		int preTopic=0;
		for(int i=0;i<array_result.size();i++){
			result=array_result.get(i);
			//若preTopic==0,对preTopic赋值
			if(preTopic==0) preTopic=result.getTopic();
			//若preTopic和result.getTopic()相同,更新result的rank信息,然后rank++
			if(preTopic==result.getTopic()){
				result.setRank(rank);
				rank++;
			}
			//若preTopic和result.getTopic()不相同,重置rank,preTopic,更新result的rank信息,rank++
			if(preTopic!=result.getTopic()){
				rank=1;
				preTopic=result.getTopic();
				result.setRank(rank);
				rank++;
			}
		}
		//输出array_result信息
		fileWriter=new FileWriter(input);
		buffWriter=new BufferedWriter(fileWriter);
		for(int i=0;i<array_result.size();i++){
			result=array_result.get(i);
			buffWriter.write(result.getTempLine());
		}
		buffWriter.close();
		System.out.println("对input文件中的文档进行排序并更新rank信息,已完成..");
		//若topic_sum不为-1,检查array_result中的topic总数
		int real_topic_sum=0;
		result=null;//result置空
		preTopic=0;//preTopic置空
		if(topic_sum!=-1){
			for(int i=0;i<array_result.size();i++){
				result=array_result.get(i);
				//若preTopic==0,给preTopic赋初值,real_topic_sum++。
				if(preTopic==0){
					preTopic=result.getTopic();
					real_topic_sum++;
				}
				//若preTopic==result.getTopic(),进行下一次循环
				if(preTopic==result.getTopic()){
					continue;
				}
				//若preTopic!=result.getTopic(),更新preTopic,real_topic_sum++。
				if(preTopic!=result.getTopic()){
					preTopic=result.getTopic();
					real_topic_sum++;
				}
			}
			if(topic_sum!=real_topic_sum){
				System.out.println("\n\ntopic_sum和实际topic总数不相等!将退出程序..");
				System.exit(1);
			}
		}
	}


	/**
	 * 输出文件夹中summary文件名和其map值
	 * @param input
	 * @throws IOException 
	 */
	public static void getFileName() throws IOException{
		File f = null;
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in)); // 接受控制台的输入
		System.out.println("请输入一个目录:"); // 提示输入目录
		String path = read.readLine(); // 获取路径
		f = new File(path); // 新建文件实例
		File[] list = f.listFiles(); /* 此处获取文件夹下的所有文件 */
		System.out.println(list.length);
		// by ChenJiawei
		String fileName = null;
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		String tempLine = null;
		//存储文件夹下所有summary文件的map值
		ArrayList<Double> array_map=new ArrayList<Double>();
		double map=0;
		for (int i = 0; i < list.length; i++) {
			fileName = list[i].getName();
			
			//分析summary文件,获取map值
			if(fileName.contains("summary.")){
				fileReader=new FileReader(list[i]);
				buffReader=new BufferedReader(fileReader);
				while((tempLine=buffReader.readLine())!=null){
					if(tempLine.contains("Queryid (Num):")&&tempLine.contains(" all")){
						for(int j=0;j<18;j++)
							tempLine=buffReader.readLine();
						map=Double.parseDouble(tempLine.trim());
						array_map.add(map);
						System.out.println(fileName+"\tmap="+map);
					}
				}
				buffReader.close();
			}
		}
		//找到第一个array_map中最大的map值
		map=0;
		for(int i=0;i<array_map.size();i++){
			if(map==0) map=array_map.get(i);
			if(map<array_map.get(i)) map=array_map.get(i);
		}
		System.out.println("最大的map值为: "+map);
		System.out.println("\n输出文件夹中summary文件名和其map值,已完成..");
	}
	/**
	 * 循环调用getFileName()方法，来输出不同文件夹中summary文件名和其map值。
	 * @throws IOException 
	 */
	public static void getFileName_batch() throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String code="yes";
		while(code.equalsIgnoreCase("yes")){
			getFileName();
			System.out.println("是否继续(yes or no):");
			code = reader.readLine();
		}
		System.out.println("循环调用getFileName()方法,已结束..");
	}
	/**
	 * 循环调用rebuildInput(String input,int topic_sum)方法,来重建input文件。
	 * @throws IOException 
	 */
	public static void rebuildInput_batch() throws IOException{
		String path=null;
		int topic_sum=0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入文件夹路径:");
		path=reader.readLine();
		System.out.println("请输入topic_sum值(若不需检查此input文件的topic总数,topic_sum置为-1。):");
		topic_sum=Integer.parseInt(reader.readLine().trim());
		//遍历path文件夹下的文件
		File file=null;
		File[] files=null;
		String input=null;
		file=new File(path);
		files=file.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].getName().contains("input.")){
				input=files[i].getAbsolutePath();
				rebuildInput(input,topic_sum);
			}
		}
		System.out.println("循环调用rebuildInput(String input,int topic_sum)方法,已结束..");
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		/*
		//循环调用getFileName()方法，来输出不同文件夹中summary文件名和其map值。
		getFileName_batch();
		*/
		//循环调用rebuildInput(String input,int topic_sum)方法,来重建input文件。
		rebuildInput_batch();
	}

}
