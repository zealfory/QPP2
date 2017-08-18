package newPredictor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WIG_NQC {

	/**
	 * 根据input_wIG,input_nQC文件,计算WIG_NQC值,并产生output文件
	 * @throws IOException 
	 */
	public void computeWIG_NQC(String input_wIG,String input_nQC,String output) throws IOException{
		BufferedReader buffReader=null;
		String tempLine=null;

		//读取input_wIG文件中的信息
		ArrayList<Struct_score> array_info=new ArrayList<Struct_score>();
		Struct_score info=null;
		buffReader=new BufferedReader(new FileReader(input_wIG));
		while((tempLine=buffReader.readLine())!=null){
			info=new Struct_score(tempLine);
			array_info.add(info);
		}
		buffReader.close();
		//根据score给array_info数组中的对象排序
		Collections.sort(array_info, new Compare());
		//给array_info数组中对象的rank赋值
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			info.rank=i+1;
		}
		//根据topic给array_info数组中的对象排序
		Collections.sort(array_info,new Compare2());

		//读取input_nQC文件中的信息
		ArrayList<Struct_score> array_info2=new ArrayList<Struct_score>();
		buffReader=new BufferedReader(new FileReader(input_nQC));
		while((tempLine=buffReader.readLine())!=null){
			info=new Struct_score(tempLine);
			array_info2.add(info);
		}
		buffReader.close();
		//根据score给array_info2数组中的对象排序
		Collections.sort(array_info2, new Compare());
		//给array_info2数组中对象的rank赋值
		for(int i=0;i<array_info2.size();i++){
			info=array_info2.get(i);
			info.rank=i+1;
		}
		//根据topic给array_info2数组中的对象排序
		Collections.sort(array_info2,new Compare2());

		//根据array_info和array_info2生成array_info3
		ArrayList<Struct_score> array_info3=new ArrayList<Struct_score>();
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			info.rank=info.rank+array_info2.get(i).rank;
			array_info3.add(info);
		}

		//输出array_info3中的信息
		BufferedWriter buffWriter=new BufferedWriter(new FileWriter(output));

		for(int i=0;i<array_info3.size();i++){
			info=array_info3.get(i);
			tempLine="queryId:\t"+info.topic+"\tWIG_NQC:\t"+info.rank+"\n";
			buffWriter.write(tempLine);
		}
		buffWriter.close();
		System.out.println("计算WIG_NQC值,并产生output文件,已完成..");
	}

	/**
	 * 根据已经计算好的wIGScore.runId,nQCScore.runId文件计算每个query的WIG_NQC值,并将WIG_NQC值存入文件
	 * @throws IOException 
	 *  
	 * */
	public void getWIG_NQCScores(String input,String output) throws IOException{
		String input_wIG=null;
		String input_nQC=null;
		input_wIG=input.replaceFirst("input\\.", "wIGScore.");
		input_nQC=input.replaceFirst("input\\.", "nQCScore.");
		computeWIG_NQC(input_wIG,input_nQC,output);		
	}

}
