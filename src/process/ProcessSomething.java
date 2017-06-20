package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author 1
 *
 */
public class ProcessSomething {
	
	public static void processSummary(String runId,String packageName,int round){
		//处理summary文件
		predictor.SummaryAnalysis.round = round;
		predictor.SummaryAnalysis.extractAveragePrecision("./"+packageName+"/summary."+runId, "./"+packageName+"/map."+runId);
		predictor.SummaryAnalysis.setTermSize(5);
		predictor.SummaryAnalysis.normalizeAveragePrecision("./"+packageName+"/map."+runId, "./"+packageName+"/map.normalized."+runId);
	}
	/**
	 * 给map.normalized.runId的每条queryId加上类别标签,加在末尾
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void addClassLabel(String input,String output) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		FileWriter fileWriter=null;
		String tempLine=null;
		String[] terms=null;
		int classification=0;//分类的类别
		double ap=0;
		
		fileReader=new FileReader(input);
		buffReader=new BufferedReader(fileReader);
		fileWriter=new FileWriter(output);
		
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split("\t| ");
			ap=Double.parseDouble(terms[4]);
			
			if(ap<=0.2) classification=1;
			if(ap>0.2&&ap<=0.4) classification=2;
			if(ap>0.4) classification=3;
			
			tempLine="queryId:\t"+terms[2]+"\tAP:\t"+terms[4]+"\tClassLabel:\t"+classification+"\n";
			fileWriter.write(tempLine);
		}
		fileWriter.close();
		buffReader.close();
		System.out.println("给map.normalized.runId的每条queryId加上类别标签,加在末尾,已完成..");
	}
	/**
	 * 需提前确认5个input文件内部信息的顺序一致
	 * 
	 * @param input_classLabel 存放classLabel信息的文件
	 * @param input1 存放特征值1的文件
	 * @param input2 存放特征值2的文件
	 * @param input3 存放特征值3的文件
	 * @param input4 存放特征值4的文件
	 * @param output 整合后的文件
	 * @throws IOException 
	 */
	public static void combineData(String input_classLabel,String input1,String input2,String input3,String input4,String input5,String input6,String input7,String output) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		String tempLine=null;
		String[] terms=null;
		ArrayList<Heart_scale> array_heart=new ArrayList<Heart_scale>();
		Heart_scale heart=null;
		
		//读取input_classLabel文件的信息,存入array_heart中
		fileReader=new FileReader(input_classLabel);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			heart=new Heart_scale();
			heart.setLabel(terms[5]);
			array_heart.add(heart);
		}
		buffReader.close();
		//读取input1文件的信息,存入array_heart中
		int pointer=0;
		fileReader=new FileReader(input1);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue1(terms[3]);
		}
		buffReader.close();
		//读取input2文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input2);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue2(terms[3]);
		}
		buffReader.close();
		//读取input3文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input3);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue3(terms[3]);
		}
		buffReader.close();
		//读取input4文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input4);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue4(terms[3]);
		}
		buffReader.close();
		//读取input5文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input5);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue5(terms[3]);
		}
		buffReader.close();
		//读取input6文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input6);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue6(terms[3]);
		}
		buffReader.close();
		//读取input7文件的信息,存入array_heart中
		pointer=0;//pointer置为0
		fileReader=new FileReader(input7);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			//获取array_heart数组中对应的对象
			heart=array_heart.get(pointer++);
			terms=tempLine.split(" |\t");
			heart.setValue7(terms[3]);
		}
		buffReader.close();
		
		//把array_heart中的信息存入output文件中
		fileWriter=new FileWriter(output);
		buffWriter=new BufferedWriter(fileWriter);
		for(int i=0;i<array_heart.size();i++){
			heart=array_heart.get(i);
			buffWriter.write(heart.getTempLine());
		}
		buffWriter.close();
		System.out.println("整合8个文件的信息,形成output文件,已完成..");
	}
	/**
	 * 计算hard,medium,easy区间的准确率
	 * @throws IOException 
	 */
	public static double[] computeAccuracy(String input_test,String input_predict) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String[] terms=null;
		String tempLine=null;
		//array_info用于存储input_test和input_predict中的信息
		ArrayList<StringBuffer> array_info=new ArrayList<StringBuffer>();
		StringBuffer info=null;
		int lable_1=0;//input_test文件中classLable为lable_1的行数
		int lable_1_matched=0;//input_predict文件和input_test匹配的信息中lable_1的数量
		int lable_2=0;
		int lable_2_matched=0;
		int lable_3=0;
		int lable_3_matched=0;
		
		//获取input_test的classLable信息
		fileReader=new FileReader(input_test);
		buffReader=new BufferedReader(fileReader);
		
		while((tempLine=buffReader.readLine())!=null){
			terms=tempLine.split(" |\t");
			info=new StringBuffer(terms[0]);
			array_info.add(info);
		}
		buffReader.close();
		//获取input_predict文件中的classLable信息
		fileReader=new FileReader(input_predict);
		buffReader=new BufferedReader(fileReader);
		int pointer=0;
		while((tempLine=buffReader.readLine())!=null){
			info=array_info.get(pointer++);
			info.append("\t"+tempLine.trim());
		}
		buffReader.close();
		//计算准确率
		for(int i=0;i<array_info.size();i++){
			info=array_info.get(i);
			terms=info.toString().split(" |\t");
			if(terms[0].equalsIgnoreCase("1")){
				lable_1++;
				if(terms[1].equalsIgnoreCase(terms[0])) lable_1_matched++;
			}
			if(terms[0].equalsIgnoreCase("2")){
				lable_2++;
				if(terms[1].equalsIgnoreCase(terms[0])) lable_2_matched++;
			}
			if(terms[0].equalsIgnoreCase("3")){
				lable_3++;
				if(terms[1].equalsIgnoreCase(terms[0])) lable_3_matched++;
			}
		}
		//
		double accuracy_1=0;
		double accuracy_2=0;
		double accuracy_3=0;
		accuracy_1=(double)lable_1_matched/lable_1;
		accuracy_2=(double)lable_2_matched/lable_2;
		accuracy_3=(double)lable_3_matched/lable_3;
		//计算整体的准确率
		double accuracy_4=(double)(lable_1_matched+lable_2_matched+lable_3_matched)/(lable_1+lable_2+lable_3);
		//System.out.println("overall: accuracy="+accuracy_4);
		//System.out.println("hard: accuracy_1="+accuracy_1+"\nmedium: accuracy_2="+accuracy_2+"\neasy: accuracy_3="+accuracy_3);
		//计算(hard+easy)这部分的准确率
		double accuracy_5=0;
		accuracy_5=(double)(lable_1_matched+lable_3_matched)/(lable_1+lable_3);
		//System.out.println("(hard+easy): accuracy_5="+accuracy_5);
		
		//把准确率存入accuracy数组中
		double[] accuracy=null;
		accuracy=new double[5];
		accuracy[0]=accuracy_4;
		accuracy[1]=accuracy_1;
		accuracy[2]=accuracy_2;
		accuracy[3]=accuracy_3;
		accuracy[4]=accuracy_5;
		return accuracy;
	}
	/**
	 * 产生heart_scale.runId文件
	 * @param runId
	 * @param packageName
	 * @throws IOException
	 */
	public static void getHeart_scale(String runId,String packageName) throws IOException{
		//robustTrack2004的可验证topic数量为249
		int round=0;
		round=249;
		
		//分析summary文件,获取average Precision信息
		processSummary(runId,packageName,round);
		
		//给map.normalized.runId的每条queryId加上类别标签
		String input=null;
		String output=null;
		input="./"+packageName+"/map.normalized."+runId;
		output=input+"_classLabel";
		addClassLabel(input,output);
		
		// 计算SD2
		predictor.SD2 predictorSD2 = new predictor.SD2();
		predictorSD2.setX(0.5);// 把predictorSD2的x设为0.5
		predictorSD2.setQueryMap(null);
		predictorSD2.getSD2Scores("./" + packageName + "/input." + runId, "./"+ packageName + "/sD2Score." + runId);
		System.out.println("根据input计算每个query的SD2值,并将SD2Score存入文件,已完成..");
		
		//计算WIG
		predictor.WIG predictorWIG=new predictor.WIG();
		predictorWIG.setK(5);//把predictorWIG的k设为5
		//这里的QueryLength.getQueryLength()为packageName包中的,
		predictorWIG.setQueryMap(null);
		predictorWIG.getWIGScores("./"+packageName+"/input."+runId,"./"+packageName+"/wIGScore."+runId);
		System.out.println("根据input计算每个query的WIG值,并将WIGScore存入文件,已完成..");
		
		//计算SMV
		predictor.SMV predictorSMV = new predictor.SMV();
		predictorSMV.setK(100);//把predictorSMV的k设为100
		predictorSMV.getSMVScores("./"+packageName+"/input."+runId,"./"+packageName+"/sMVScore."+runId);
		System.out.println("根据input计算每个query的SMV值,并将SMVScore存入文件,已完成..");
		
		//计算NQC
		predictor.NQC predictorNQC=new predictor.NQC();
		predictorNQC.setK(100);//把predictorNQC的k设为100
		predictorNQC.getNQCScores("./"+packageName+"/input."+runId, "./"+packageName+"/nQCScore."+runId);
		System.out.println("根据input计算每个query的NQC值,并将NQCScore存入文件,已完成..");
		
		//计算C
		newPredictor.C newPredictorC=new newPredictor.C();
		newPredictorC.setAlpha(0.5);//SD_2和WIG的平衡参数
		newPredictorC.getCScores("./"+packageName+"/input."+runId,"./"+packageName+"/cScore."+runId);

		//计算C2
		newPredictor.C2 newPredictorC2=new newPredictor.C2();
		newPredictorC2.setK(100);//截断参数
		newPredictorC2.setQueryMap(null);
		newPredictorC2.getC2Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c2Score."+runId);

		//计算C4
		newPredictor.C4 newPredictorC4=new newPredictor.C4();
		newPredictorC4.setK(100);//截断参数
		newPredictorC4.getC4Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c4Score."+runId);

		//整合5个文件的信息,形成output文件
		String input_classLabel="./"+packageName+"/map.normalized."+runId+"_classLabel";
		String input1="./"+packageName+"/sD2Score."+runId;
		String input2="./"+packageName+"/wIGScore."+runId;
		String input3="./"+packageName+"/sMVScore."+runId;
		String input4="./"+packageName+"/nQCScore."+runId;
		String input5="./"+packageName+"/cScore."+runId;
		String input6="./"+packageName+"/c2Score."+runId;
		String input7="./"+packageName+"/c4Score."+runId;
		
		output="./"+packageName+"/heart_scale";
		combineData(input_classLabel,input1,input2,input3,input4,input5,input6,input7,output);
		
		/*
		String input_test="./"+packageName+"/heart_scale."+runId;
		String input_predict=input_test+".predict";
		computeAccuracy(input_test,input_predict);
		*/
	}
	/**
	 * 产生heart_scale_train_x,heart_scale_test_x文件。<br>
	 * 
	 * @param input1
	 * @param input
	 * @throws IOException 
	 */
	public static void combineData_2(String packageName) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		ArrayList<String> array_info=new ArrayList<String>();
		
		//把heart_scale中的信息存入array_info中
		//
		//临时进行了修改
		//fileReader=new FileReader("./"+packageName+"/heart_scale");
		fileReader=new FileReader("./"+packageName+"/heart2");
		//
		//
		
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			array_info.add(tempLine);
		}
		buffReader.close();
		
		//使用5折交叉验证,把array_info中的信息分给heart_scale_train_x,heart_scale_test_x文件
		int belong=0;
		String train=null;
		String test=null;
		BufferedWriter buffWriter_train=null;
		BufferedWriter buffWriter_test=null;
		
		//产生heart_scale_train_0,heart_scale_test_0
		train="./"+packageName+"/heart_scale_train_0";
		test="./"+packageName+"/heart_scale_test_0";
		buffWriter_train=new BufferedWriter(new FileWriter(train));
		buffWriter_test=new BufferedWriter(new FileWriter(test));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			belong=i%5;
			if(belong!=0) buffWriter_train.write(tempLine+"\n");
			if(belong==0) buffWriter_test.write(tempLine+"\n");
		}
		buffWriter_test.close();
		buffWriter_train.close();
		
		//产生heart_scale_train_1,heart_scale_test_1
		train="./"+packageName+"/heart_scale_train_1";
		test="./"+packageName+"/heart_scale_test_1";
		buffWriter_train=new BufferedWriter(new FileWriter(train));
		buffWriter_test=new BufferedWriter(new FileWriter(test));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			belong=i%5;
			if(belong!=1) buffWriter_train.write(tempLine+"\n");
			if(belong==1) buffWriter_test.write(tempLine+"\n");
		}
		buffWriter_test.close();
		buffWriter_train.close();
		
		//产生heart_scale_train_2,heart_scale_test_2
		train="./"+packageName+"/heart_scale_train_2";
		test="./"+packageName+"/heart_scale_test_2";
		buffWriter_train=new BufferedWriter(new FileWriter(train));
		buffWriter_test=new BufferedWriter(new FileWriter(test));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			belong=i%5;
			if(belong!=2) buffWriter_train.write(tempLine+"\n");
			if(belong==2) buffWriter_test.write(tempLine+"\n");
		}
		buffWriter_test.close();
		buffWriter_train.close();
		
		//产生heart_scale_train_3,heart_scale_test_3
		train="./"+packageName+"/heart_scale_train_3";
		test="./"+packageName+"/heart_scale_test_3";
		buffWriter_train=new BufferedWriter(new FileWriter(train));
		buffWriter_test=new BufferedWriter(new FileWriter(test));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			belong=i%5;
			if(belong!=3) buffWriter_train.write(tempLine+"\n");
			if(belong==3) buffWriter_test.write(tempLine+"\n");
		}
		buffWriter_test.close();
		buffWriter_train.close();
		
		//产生heart_scale_train_4,heart_scale_test_4
		train="./"+packageName+"/heart_scale_train_4";
		test="./"+packageName+"/heart_scale_test_4";
		buffWriter_train=new BufferedWriter(new FileWriter(train));
		buffWriter_test=new BufferedWriter(new FileWriter(test));
		for(int i=0;i<array_info.size();i++){
			tempLine=array_info.get(i);
			belong=i%5;
			if(belong!=4) buffWriter_train.write(tempLine+"\n");
			if(belong==4) buffWriter_test.write(tempLine+"\n");
		}
		buffWriter_test.close();
		buffWriter_train.close();
		
		System.out.println("产生5折heart_scale_train_x,heart_scale_test_x文件,已完成..");
	}
	
	/**
	 * 查看heart_scale_train,heart_scale_test文件,若含有NaN,便删除这一项特征值。
	 * @param packageName
	 * @throws IOException 
	 */
	public static void delete_NaN(String packageName,String fileName) throws IOException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String tempLine=null;
		//分析heart_scale_train文件的信息,若有NaN项，删除这一特征值，并把信息存入array_info中。
		ArrayList<String> array_info=new ArrayList<String>();
		fileReader=new FileReader("./"+packageName+"/"+fileName);
		buffReader=new BufferedReader(fileReader);
		while((tempLine=buffReader.readLine())!=null){
			if(tempLine.contains(":NaN ")){
				System.out.println("含NaN值的记录: "+tempLine);
				//tempLine=tempLine.replaceAll("[\\d]+:NaN ", "");
				//
				//
				//临时修改
				tempLine=tempLine.replaceAll(":NaN", ":0");
			}
			array_info.add(tempLine);
		}
		buffReader.close();
		//
		FileWriter fileWriter=null;
		BufferedWriter buffWriter=null;
		fileWriter=new FileWriter("./"+packageName+"/"+fileName);
		buffWriter=new BufferedWriter(fileWriter);
		for(int i=0;i<array_info.size();i++){
			buffWriter.write(array_info.get(i)+"\n");
		}
		buffWriter.close();
		System.out.println("删除"+fileName+"文件中的NaN特征项,已完成..");
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		String runId;
		String packageName;
		int round=0;
		
		runId="apl04rsTw";
		packageName="robustTrack2004";
		round=249;
		
		//分析summary文件,获取average Precision信息
		processSummary(runId,packageName,round);
		
		
		//给map.normalized.runId的每条queryId加上类别标签
		String input=null;
		String output=null;
		input="./"+packageName+"/map.normalized."+runId;
		output=input+"_classLabel";
		addClassLabel(input,output);
		
		
		// 计算SD2
		predictor.SD2 predictorSD2 = new predictor.SD2();
		predictorSD2.setX(0.5);// 把predictorSD2的x设为0.5
		predictorSD2.setQueryMap(null);
		predictorSD2.getSD2Scores("./" + packageName + "/input." + runId, "./"+ packageName + "/sD2Score." + runId);
		System.out.println("根据input计算每个query的SD2值,并将SD2Score存入文件,已完成..");
		
		//计算WIG
		predictor.WIG predictorWIG=new predictor.WIG();
		predictorWIG.setK(5);//把predictorWIG的k设为5
		//这里的QueryLength.getQueryLength()为packageName包中的,
		predictorWIG.setQueryMap(null);
		predictorWIG.getWIGScores("./"+packageName+"/input."+runId,"./"+packageName+"/wIGScore."+runId);
		System.out.println("根据input计算每个query的WIG值,并将WIGScore存入文件,已完成..");
		
		//计算SMV
		predictor.SMV predictorSMV = new predictor.SMV();
		predictorSMV.setK(100);//把predictorSMV的k设为100
		predictorSMV.getSMVScores("./"+packageName+"/input."+runId,"./"+packageName+"/sMVScore."+runId);
		System.out.println("根据input计算每个query的SMV值,并将SMVScore存入文件,已完成..");
		
		//计算NQC
		predictor.NQC predictorNQC=new predictor.NQC();
		predictorNQC.setK(100);//把predictorNQC的k设为100
		predictorNQC.getNQCScores("./"+packageName+"/input."+runId, "./"+packageName+"/nQCScore."+runId);
		System.out.println("根据input计算每个query的NQC值,并将NQCScore存入文件,已完成..");
		/*
		//整合5个文件的信息,形成output文件
		String input_classLabel="./"+packageName+"/map.normalized."+runId+"_classLabel";
		String input1="./"+packageName+"/sD2Score."+runId;
		String input2="./"+packageName+"/wIGScore."+runId;
		String input3="./"+packageName+"/sMVScore."+runId;
		String input4="./"+packageName+"/nQCScore."+runId;
		output="./"+packageName+"/heart_scale."+runId;
		combineData(input_classLabel,input1,input2,input3,input4,output);
		*/
		/*
		String input_test="./"+packageName+"/heart_scale."+runId;
		String input_predict=input_test+".predict";
		computeAccuracy(input_test,input_predict);
		*/
		
	}

}
class Heart_scale{
	private String label;
	private int index1;
	private String value1;
	private int index2;
	private String value2;
	private int index3;
	private String value3;
	private int index4;
	private String value4;
	private int index5;
	private String value5;
	private int index6;
	private String value6;
	private int index7;
	private String value7;
	
	public String getValue5() {
		return value5;
	}
	public void setValue5(String value5) {
		this.value5 = value5;
	}
	public String getValue6() {
		return value6;
	}
	public void setValue6(String value6) {
		this.value6 = value6;
	}
	public String getValue7() {
		return value7;
	}
	public void setValue7(String value7) {
		this.value7 = value7;
	}
	public String getValue4() {
		return value4;
	}
	public void setValue4(String value4) {
		this.value4 = value4;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getValue3() {
		return value3;
	}
	public void setValue3(String value3) {
		this.value3 = value3;
	}
	
	public Heart_scale(){
		index1=1;
		index2=2;
		index3=3;
		index4=4;
		index5=5;
		index6=6;
		index7=7;
	}
	public String getTempLine(){
		String tempLine="";
		tempLine=label+" "+index1+":"+value1+" "+index2+":"+value2+" "+index3+":"+value3+" "+index4+":"+value4+" "+index5+":"+value5+" "+index6+":"+value6+" "+index7+":"+value7+" \n";
		return tempLine;
	}

}
