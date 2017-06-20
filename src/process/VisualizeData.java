package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import utils.Run_coeff;

public class VisualizeData {
	
	/**
	 * 保留小数点后3位,使用了四舍五入
	 * @param input
	 * @throws IOException 
	 */
	public static void normalizeCoefficient(String input) throws IOException{
		FileReader fileReader=null;
		BufferedReader bufferedReader=null;
		FileWriter fileWriter=null;
		String tempLine=null;
		String[] terms=null;
		
		fileReader=new FileReader(input);
		bufferedReader=new BufferedReader(fileReader);
		fileWriter=new FileWriter(input+"_withCoefficientNormalized");
		while((tempLine=bufferedReader.readLine())!=null){
			if(tempLine.contains("=")){
				terms=tempLine.split("=");
				tempLine=terms[0]+"="+String.format("%.3f", Double.parseDouble(terms[1].trim()));
			}
			fileWriter.write(tempLine+"\n");
		}
		fileWriter.close();
		bufferedReader.close();
		System.out.println("对accuracy进行规范化,保留小数点后3位,已完成..");
	}
	/**
	 * 处理 新建文本文档 (3).txt,计算1/2*(hard+easy),并添加到easy的下一行
	 * @param input
	 * @throws IOException 
	 */
	public static void accuracy_mean_hard_easy(String input) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		ArrayList<StringBuffer> array_info=new ArrayList<StringBuffer>();
		StringBuffer info=null;
		String tempLine=null;
		
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			info=new StringBuffer(tempLine);
			array_info.add(info);
		}
		buffReader.close();
		//分析array_info中的信息
		double accuracy_1=0;
		double accuracy_3=0;
		double accuracy_4=0;//hard和easy准确率的平均值
		info=null;//info置空
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			tempLine=info.toString();
			if(tempLine.contains("hard: ")){
				accuracy_1=Double.parseDouble(tempLine.split("=")[1].trim());
			}
			if(tempLine.contains("easy: ")){
				accuracy_3=Double.parseDouble(tempLine.split("=")[1].trim());
				accuracy_4=(accuracy_1+accuracy_3)/2;
				info.append("\n1/2(hard+easy): accuracy_4="+accuracy_4);
			}
		}
		//使用array_info中的信息更新input文件
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		fileWriter=new FileWriter(input);
		buffWriter=new BufferedWriter(fileWriter);
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			buffWriter.write(info.toString()+"\n");
		}
		buffWriter.close();
		System.out.println("在input文件中,添加1/2(hard+easy),已完成..");
	}
	/**
	 * 把 新建文本文档 (3).txt_withCoefficientNormalized文件中的信息以表格的形式存入文件中。<br>
	 * @param input
	 * @throws IOException 
	 */
	public static void load_input(String input) throws IOException{
		ArrayList<Organization> array_org=new ArrayList<Organization>();
		Organization org=null;
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		//input文件中,除了空行外,其他信息都存入array_info数组中。
		ArrayList<String> array_info=new ArrayList<String>();
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			if(!tempLine.trim().equalsIgnoreCase("")){
				array_info.add(tempLine);
			}
		}
		buffReader.close();
		//分析array_info中的信息,把信息存入array_org中
		ListIterator<String> iter=array_info.listIterator();
		while(iter.hasNext()){
			tempLine=iter.next();
			if(tempLine.contains("Organization: ")){
				org=new Organization();
				org.name=tempLine.replaceFirst("[\\d]+、Organization: ", "").trim();
				tempLine=iter.next();
				if(tempLine.contains("支持向量机的准确率:")){
					org.support_svm=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("SD2的准确率:")){
					org.sD2=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("WIG的准确率:")){
					org.wIG=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("SMV的准确率:")){
					org.sMV=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("NQC的准确率:")){
					org.nQC=new Accuracy(iter);
				}
				//
				tempLine=iter.next();
				if(tempLine.contains("C的准确率:")){
					org.c=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("C2的准确率:")){
					org.c2=new Accuracy(iter);
				}
				tempLine=iter.next();
				if(tempLine.contains("C4的准确率:")){
					org.c4=new Accuracy(iter);
				}
				//把org存入array_org中
				array_org.add(org);
			}
		}
		//以表格的形式输出array_org中的信息
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		fileWriter=new FileWriter(input+"_visualize");
		buffWriter=new BufferedWriter(fileWriter);
		//输出overall准确率
		tempLine="overall准确率:\nOrganization\tsupport_svm\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC4\n";
		buffWriter.write(tempLine);
		for(int i=0;i<array_org.size();i++){
			org=array_org.get(i);
			tempLine=org.name+"\t"+org.support_svm.overall+"\t"+org.sD2.overall+"\t"+
			         org.wIG.overall+"\t"+org.sMV.overall+"\t"+org.nQC.overall+"\t"+
					 org.c.overall+"\t"+org.c2.overall+"\t"+org.c4.overall+"\n";
			buffWriter.write(tempLine);
		}
		//输出hard准确率
		tempLine="\nhard准确率:\nOrganization\tsupport_svm\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC4\n";
		buffWriter.write(tempLine);
		for(int i=0;i<array_org.size();i++){
			org=array_org.get(i);
			tempLine=org.name+"\t"+org.support_svm.hard+"\t"+org.sD2.hard+"\t"+
					org.wIG.hard+"\t"+org.sMV.hard+"\t"+org.nQC.hard+"\t"+
					org.c.hard+"\t"+org.c2.hard+"\t"+org.c4.hard+"\n";
			buffWriter.write(tempLine);
		}
		//输出medium准确率
		tempLine="\nmedium准确率:\nOrganization\tsupport_svm\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC4\n";
		buffWriter.write(tempLine);
		for(int i=0;i<array_org.size();i++){
			org=array_org.get(i);
			tempLine=org.name+"\t"+org.support_svm.medium+"\t"+org.sD2.medium+"\t"+
					org.wIG.medium+"\t"+org.sMV.medium+"\t"+org.nQC.medium+"\t"+
					org.c.medium+"\t"+org.c2.medium+"\t"+org.c4.medium+"\n";
			buffWriter.write(tempLine);
		}
		//输出easy准确率
		tempLine="\neasy准确率:\nOrganization\tsupport_svm\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC4\n";
		buffWriter.write(tempLine);
		for(int i=0;i<array_org.size();i++){
			org=array_org.get(i);
			tempLine=org.name+"\t"+org.support_svm.easy+"\t"+org.sD2.easy+"\t"+
					org.wIG.easy+"\t"+org.sMV.easy+"\t"+org.nQC.easy+"\t"+
					org.c.easy+"\t"+org.c2.easy+"\t"+org.c4.easy+"\n";
			buffWriter.write(tempLine);
		}
		//输出(hard+easy)准确率
		tempLine="\n(hard+easy)准确率:\nOrganization\tsupport_svm\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC4\n";
		buffWriter.write(tempLine);
		for(int i=0;i<array_org.size();i++){
			org=array_org.get(i);
			tempLine=org.name+"\t"+org.support_svm.mean+"\t"+org.sD2.mean+"\t"+
					org.wIG.mean+"\t"+org.sMV.mean+"\t"+org.nQC.mean+"\t"+
					org.c.mean+"\t"+org.c2.mean+"\t"+org.c4.mean+"\n";
			buffWriter.write(tempLine);
		}
		buffWriter.close();
		System.out.println("把input文件中的信息以表格的形式存入文件中,已完成..");
	}
	/**
	 * 可视化系统的pearson/kendall/spearman系数
	 * @param input
	 * @throws IOException 
	 */
	public static void visualize_run_coeff(String input) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		ArrayList<Run_coeff> array_run=new ArrayList<Run_coeff>();
		Run_coeff run=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		
		//读取pearson,kendall,spearman系数文件,存入array_run中
		while((tempLine=buffReader.readLine())!=null){
			run=new Run_coeff();
			//系数文件中,一个系统占了24行,于是程序中以24行作为循环体。
			run.runId=tempLine.replaceFirst("[\\d]+、track为", "");
			buffReader.readLine();
			//读取pearson系数
			tempLine=buffReader.readLine();
			run.p_sD2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_wIG=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_sMV=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_nQC=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_c=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_c2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_c3=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.p_c4=tempLine.split("=")[1];
			//读取kendall系数
			tempLine=buffReader.readLine();
			run.k_sD2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_wIG=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_sMV=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_nQC=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_c=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_c2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_c3=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.k_c4=tempLine.split("=")[1];
			//读取spearman系数
			tempLine=buffReader.readLine();
			run.s_sD2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_wIG=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_sMV=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_nQC=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_c=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_c2=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_c3=tempLine.split("=")[1];
			tempLine=buffReader.readLine();
			run.s_c4=tempLine.split("=")[1];
			//读两行空行
			buffReader.readLine();
			buffReader.readLine();
			
			//把run存入array_run中
			array_run.add(run);
		}
		//关闭bufferedReader
		buffReader.close();
		//把array_run中的run存入写入文件中
		fileWriter=new FileWriter(input+"_table");
		buffWriter=new BufferedWriter(fileWriter);
		//把预测值与AP的pearson系数写入文件
		tempLine="预测值与AP的pearson系数:\n";
		//tempLine=tempLine+"runId\tWIG\tSD\tSMV\tNQC\tIA_SUM\tSDMulti\tSDMultiWIG_IASUM\tCF\tCF_IASUM\n";
		tempLine=tempLine+"runId\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC3\tC4\n";
		
		buffWriter.write(tempLine);
		for(int i=0;i<array_run.size();i++){
			run=array_run.get(i);
			//tempLine=run.runId+"\t"+run.p_wIG+"\t"+run.p_sD+"\t"+run.p_sMV+"\t"+run.p_nQC+"\t"+run.p_iA_SUM+"\t"+run.p_sDMultiWIG+"\t"+run.p_sDMultiWIG_IASUM+"\t"+run.p_cF+"\t"+run.p_cF_IASUM+"\n";
			tempLine=run.runId+"\t"+run.p_sD2+"\t"+run.p_wIG+"\t"+run.p_sMV+"\t"+run.p_nQC+"\t"+run.p_c+"\t"+run.p_c2+"\t"+run.p_c3+"\t"+run.p_c4+"\n";
			buffWriter.write(tempLine);
		}
		//把预测值与AP的kendall系数写入文件
		tempLine="\n\n预测值与AP的kendall系数:\n";
		//tempLine=tempLine+"runId\tWIG\tSD\tSMV\tNQC\tIA_SUM\tSDMulti\tSDMultiWIG_IASUM\tCF\tCF_IASUM\n";
		tempLine=tempLine+"runId\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC3\tC4\n";
		
		buffWriter.write(tempLine);
		for(int i=0;i<array_run.size();i++){
			run=array_run.get(i);
			//tempLine=run.runId+"\t"+run.k_wIG+"\t"+run.k_sD+"\t"+run.k_sMV+"\t"+run.k_nQC+"\t"+run.k_iA_SUM+"\t"+run.k_sDMultiWIG+"\t"+run.k_sDMultiWIG_IASUM+"\t"+run.k_cF+"\t"+run.k_cF_IASUM+"\n";
			tempLine=run.runId+"\t"+run.k_sD2+"\t"+run.k_wIG+"\t"+run.k_sMV+"\t"+run.k_nQC+"\t"+run.k_c+"\t"+run.k_c2+"\t"+run.k_c3+"\t"+run.k_c4+"\n";
			buffWriter.write(tempLine);
		}
		//把预测值与AP的spearman系数写入文件
		tempLine="\n\n预测值与AP的spearman系数:\n";
		//tempLine=tempLine+"runId\tWIG\tSD\tSMV\tNQC\tIA_SUM\tSDMulti\tSDMultiWIG_IASUM\tCF\tCF_IASUM\n";
		tempLine=tempLine+"runId\tSD2\tWIG\tSMV\tNQC\tC\tC2\tC3\tC4\n";
		
		buffWriter.write(tempLine);
		for(int i=0;i<array_run.size();i++){
			run=array_run.get(i);
			//tempLine=run.runId+"\t"+run.k_wIG+"\t"+run.k_sD+"\t"+run.k_sMV+"\t"+run.k_nQC+"\t"+run.k_iA_SUM+"\t"+run.k_sDMultiWIG+"\t"+run.k_sDMultiWIG_IASUM+"\t"+run.k_cF+"\t"+run.k_cF_IASUM+"\n";
			tempLine=run.runId+"\t"+run.s_sD2+"\t"+run.s_wIG+"\t"+run.s_sMV+"\t"+run.s_nQC+"\t"+run.s_c+"\t"+run.s_c2+"\t"+run.s_c3+"\t"+run.s_c4+"\n";
			buffWriter.write(tempLine);
		}
		buffWriter.close();
		System.out.println("读取pearson,kendall,spearman系数文件,以表格的形式把系数存入文件,已完成..");
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		//
		String input=null;
		String packageName=null;
		packageName="robustTrack2004";
		input="./"+packageName+"/新建文本文档 (3).txt";
		
		/*
		//在input文件中,添加1/2(hard+easy)
		accuracy_mean_hard_easy(input);
		*/
		
		//保留小数点后3位,使用了四舍五入
		normalizeCoefficient(input);
		//把input文件中的信息以表格的形式存入文件中
		input="./"+packageName+"/新建文本文档 (3).txt_withCoefficientNormalized";
		load_input(input);
		
		/*
		//保留小数点后3位,使用了四舍五入
		normalizeCoefficient(input);
		//把input文件中的信息以表格的形式存入文件中
		input="./"+packageName+"/新建文本文档 (3).txt_withCoefficientNormalized";
		visualize_run_coeff(input);
		*/
	}

}

class Accuracy{
	String overall;
	String hard;
	String medium;
	String easy;
	String mean;//hard+easy的准确率
	public Accuracy(){
		
	}
	public Accuracy(ListIterator<String> iter){
		overall=iter.next().split("=")[1].trim();
		hard=iter.next().split("=")[1].trim();
		medium=iter.next().split("=")[1].trim();
		easy=iter.next().split("=")[1].trim();
		mean=iter.next().split("=")[1].trim();
	}
}
class Organization{
	String name;
	Accuracy support_svm;
	Accuracy sD2;
	Accuracy wIG;
	Accuracy sMV;
	Accuracy nQC;
	Accuracy c;
	Accuracy c2;
	Accuracy c4;
	
}

