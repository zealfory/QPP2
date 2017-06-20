package process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 若预测值为NaN,改为0,并输出修改的提示<br>
 * 计算baseline的准确率
 * @author 1
 *
 */
public class ProcessBaseline {

	/**
	 * 根据SD2的结果,计算准确率
	 * @throws IOException 
	 */
	public static void computeAccuracy(String input_map,String input_sD2) throws IOException{
		//分析input_sD2文件
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		String[] terms=null;
		ArrayList<SD2Result> array_sD2=new ArrayList<SD2Result>();
		SD2Result sD2Result=null;

		fileReader=new FileReader(input_sD2);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			sD2Result=new SD2Result(tempLine);
			array_sD2.add(sD2Result);
		}
		buffReader.close();
		//根据SD2值,给array_sD2中的对象排序
		Collections.sort(array_sD2, new SD2Result_compare());
		//给array_sD2中的对象赋classLabel值
		double size=0;
		size=array_sD2.size();
		int size_1_4=0;
		int size_3_4=0;

		size_1_4=(int)(size/4-1);
		size_3_4=(int)(size/4*3-1);
		sD2Result=null;//sD2Result置空
		for(int i=0;i<array_sD2.size();i++){
			sD2Result=array_sD2.get(i);
			if(i<=size_1_4) sD2Result.setClassLabel("1");
			if(i>size_1_4&&i<=size_3_4) sD2Result.setClassLabel("2");
			if(i>size_3_4) sD2Result.setClassLabel("3");
		}
		//根据topic值,给array_sD2中的对象排序
		Collections.sort(array_sD2, new SD2Result_compare2());
		
		//array_info用于存储input_map和array_sD2中的classLabel信息
		ArrayList<StringBuffer> array_info=new ArrayList<StringBuffer>();
		StringBuffer info=null;
		int label_1=0;//input_map文件中classLabel为label_1的行数
		int label_1_matched=0;//array_sD2和input_map匹配的信息中label_1的数量
		int label_2=0;
		int label_2_matched=0;
		int label_3=0;
		int label_3_matched=0;

		//获取input_map的classLabel信息
		fileReader=new FileReader(input_map);
		buffReader=new BufferedReader(fileReader);

		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			info=new StringBuffer(terms[5]);
			array_info.add(info);
		}
		buffReader.close();
		//获取array_sD2的classLabel信息
		int pointer=0;
		info=null;//info置空
		for(int i=0;i<array_sD2.size();i++){
			info=array_info.get(pointer++);
			info.append("\t"+array_sD2.get(i).getClassLabel());
		}
		
		//计算准确率
		info=null;//info置空
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			terms=info.toString().split(" |\t");
			if(terms[0].equalsIgnoreCase("1")){
				label_1++;
				if(terms[1].equalsIgnoreCase(terms[0])) label_1_matched++;
			}
			if(terms[0].equalsIgnoreCase("2")){
				label_2++;
				if(terms[1].equalsIgnoreCase(terms[0])) label_2_matched++;
			}
			if(terms[0].equalsIgnoreCase("3")){
				label_3++;
				if(terms[1].equalsIgnoreCase(terms[0])) label_3_matched++;
			}
		}
		//
		double accuracy_1=0;
		double accuracy_2=0;
		double accuracy_3=0;
		accuracy_1=(double)label_1_matched/label_1;
		accuracy_2=(double)label_2_matched/label_2;
		accuracy_3=(double)label_3_matched/label_3;
		//计算整体的准确率
		double accuracy_4=0;
		accuracy_4=(double)(label_1_matched+label_2_matched+label_3_matched)/(label_1+label_2+label_3);
		System.out.println("overall: accuracy="+accuracy_4);
		System.out.println("hard: accuracy_1="+accuracy_1+"\nmedium: accuracy_2="+accuracy_2+"\neasy: accuracy_3="+accuracy_3);
		
		//计算(hard+easy)准确率
		double accuracy_5=0;
		accuracy_5=(double)(label_1_matched+label_3_matched)/(label_1+label_3);
		System.out.println("(hard+easy): accuracy_5="+accuracy_5);
	}
	/**
	 * 计算SD2/WIG/SMV/NQC的准确率
	 * @throws IOException 
	 */
	public static void getAccuracy(String runId,String packageName) throws IOException{
		String input_map=null;
		String input_predict=null;
		//计算SD2的准确率
		input_map="./"+packageName+"/map.normalized."+runId+"_classLabel";
		input_predict="./"+packageName+"/sD2Score."+runId;
		System.out.println("\nSD2的准确率:");
		computeAccuracy(input_map,input_predict);
		//计算WIG的准确率
		input_predict="./"+packageName+"/wIGScore."+runId;
		System.out.println("\nWIG的准确率:");
		computeAccuracy(input_map,input_predict);
		//计算SMV的准确率
		input_predict="./"+packageName+"/sMVScore."+runId;
		System.out.println("\nSMV的准确率:");
		computeAccuracy(input_map,input_predict);
		//计算NQC的准确率
		input_predict="./"+packageName+"/nQCScore."+runId;
		System.out.println("\nNQC的准确率:");
		computeAccuracy(input_map,input_predict);
		
		//计算C的准确率
		input_predict="./"+packageName+"/cScore."+runId;
		System.out.println("\nC的准确率:");
		computeAccuracy(input_map,input_predict);
		//计算C2的准确率
		input_predict="./"+packageName+"/c2Score."+runId;
		System.out.println("\nC2的准确率:");
		computeAccuracy(input_map,input_predict);
		//计算C4的准确率
		input_predict="./"+packageName+"/c4Score."+runId;
		System.out.println("\nC4的准确率:");
		computeAccuracy(input_map,input_predict);
		
		System.out.println("计算SD2/WIG/SMV/NQC/C C2 C4的准确率,已完成..");
	}
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		//根据SD2的结果,计算准确率
		String input_map="./robustTrack2004/map.normalized.apl04rsTw_classLabel";
		String input_sD2="./robustTrack2004/sD2Score.apl04rsTw";
		computeAccuracy(input_map,input_sD2);
		
	}

}

class SD2Result{
	private int topic;
	private double sD2;
	private String classLabel;

	public int getTopic() {
		return topic;
	}
	public void setTopic(int topic) {
		this.topic = topic;
	}
	public double getsD2() {
		return sD2;
	}
	public void setsD2(double sD2) {
		this.sD2 = sD2;
	}
	public String getClassLabel() {
		return classLabel;
	}
	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}
	/**
	 * 若sD2为NaN,改为0,并输出修改的提示
	 * @param tempLine
	 */
	public SD2Result(String tempLine){
		String[] terms=null;
		terms=tempLine.split(" |\t");
		topic=Integer.parseInt(terms[1]);
		sD2=Double.parseDouble(terms[3]);
		classLabel=null;
		
		//若sD2为NaN,改为0
		if(new Double(sD2).isNaN()){
			sD2=0;
			System.out.println("\n\n此条记录中存在NaN值,内存中已被改为0。tempLine="+tempLine);
		}
	}
}

/**
 * 对SD2Result类的对象进行排序,根据SD2值从小到大排序
 *
 */
class SD2Result_compare implements Comparator<SD2Result>{

	/**
	 * 根据SD2值从小到大排序
	 */
	@Override
	public int compare(SD2Result arg0, SD2Result arg1) {
		if(arg0.getsD2()<arg1.getsD2())
			return -1;
		if(arg0.getsD2()>arg1.getsD2())
			return 1;
		return 0;
	}
}
/**
 * 对SD2Result类的对象进行排序,根据topic值从小到大排序
 *
 */
class SD2Result_compare2 implements Comparator<SD2Result>{

	/**
	 * 根据topic值从小到大排序
	 */
	@Override
	public int compare(SD2Result arg0, SD2Result arg1) {
		if(arg0.getTopic()<arg1.getTopic())
			return -1;
		if(arg0.getTopic()>arg1.getTopic())
			return 1;
		return 0;
	}
}
