package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.Result;

public class Normalization {

	/**
	 * 分析input文件,若符合1、2两个条件,不做修改。
	 * 1、使分数都大于0,2、使分数在[0,1]之间。
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void normalize_input(String input) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		//
		double max=0;
		double min=0;
		double score=0;
		int pointer=0;
		ArrayList<Result> array_result=new ArrayList<Result>();
		Result result=null;
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//把信息存入array_result中
			result=new Result(tempLine);
			array_result.add(result);
			score=result.getScore();
			//给min,max赋初值
			if(pointer==0){
				pointer++;
				min=score;
				max=score;
			}
			//获取文件的min,max
			if(score<min) min=score;
			if(score>max) max=score;
		}
		buffReader.close();
		//若min<0,array_result中所有对象的分数减去min
		if(min<0){
			result=null;//result置空
			for(int i=0;i<array_result.size();i++){
				result=array_result.get(i);
				result.score=result.score-min;
			}
			//更新max值，并使array_result中的分数在区间[0,1]中
			max=max-min;
			if(max>1){
				result=null;
				for(int i=0;i<array_result.size();i++){
					result=array_result.get(i);
					result.score=result.score/max;
				}
			}
		}else{
			//当min>=0时，分析max
			if(max>1){
				result=null;
				for(int i=0;i<array_result.size();i++){
					result=array_result.get(i);
					result.score=result.score/max;
				}
			}
		}
		//把array_result中的信息存入output中。
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		fileWriter=new FileWriter(input+"_normalized");
		buffWriter=new BufferedWriter(fileWriter);
		result=null;
		for(int i=0;i<array_result.size();i++){
			result=array_result.get(i);
			buffWriter.write(result.getTempLine());
		}
		buffWriter.close();
		
		//使用input+"_normalized"文件替换input文件
		File file1=null;
		File file2=null;
		file1=new File(input);
		file1.delete();
		file2=new File(input+"_normalized");
		file2.renameTo(file1);
		System.out.println("规范化input文件,已完成..");
	}
	/**
	 * 删除input文件中topics对应的记录
	 * @param input
	 * @param topics
	 * @throws IOException 
	 */
	public static void delete_topic(String input,String topics) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		String[] terms=null;
		ArrayList<Result> array_result=new ArrayList<Result>();
		Result result=null;
		
		//设topics中的信息格式为"topic1\ttopic2"。
		String[] arrayTopic=topics.split(" |\t");
		boolean need_delete=false;
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		//input文件的信息经删除部分信息后，存入array_result中。
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			//重置nee_deleted为false
			need_delete=false;
			for(int i=0;i<arrayTopic.length;i++){
				if(terms[0].equalsIgnoreCase(arrayTopic[i])){
					need_delete=true;
					break;
				}
			}
			//若need_delete为false,把tempLine存入array_result中。
			if(need_delete==false){
				result=new Result(tempLine);
				array_result.add(result);
			}
		}
		buffReader.close();
		//把array_result中的信息存入output文件中
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		fileWriter=new FileWriter(input+"_part_deleted");
		buffWriter=new BufferedWriter(fileWriter);
		result=null;//result置空
		for(int i=0;i<array_result.size();i++){
			result=array_result.get(i);
			buffWriter.write(result.getTempLine());
		}
		buffWriter.close();
		//
		//使用input+"_part_deleted"文件替换input文件
		File file1=null;
		File file2=null;
		file1=new File(input);
		file1.delete();
		file2=new File(input+"_part_deleted");
		file2.renameTo(file1);
		System.out.println("删除input文件中topics对应的记录,已完成..");
	}


	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		String parent_root="C:/Users/1/Desktop/新建文件夹"+"/";
		File file=null;
		File[] list=null;
		String input=null;
		//需删除的topic为672
		String topics="95\t100";
		file=new File(parent_root);
		list=file.listFiles();
		System.out.println("父目录包含文件的个数为:"+list.length);
		//输出这些文件名称
		for(int i=0;i<list.length;i++){
			System.out.println(list[i].getName());
		}
		System.out.println();
		//
		for(int i=0;i<list.length;i++){
			if(list[i].getName().contains("input.")){
				input=parent_root+list[i].getName();
				//规范化文件夹中的input文件
				normalize_input(input);
				//删除input文件中topics对应的记录
				//delete_topic(input,topics);
			}
		}
		System.out.println("规范化文件夹中的input文件,并删除input文件中topics对应的记录,已完成..");
	}

}
