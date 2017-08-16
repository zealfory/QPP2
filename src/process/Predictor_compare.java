package process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Predictor_compare {

	public static void processSummary(String runId,String packageName,int round){
		//处理summary文件
		if(runId.equals("uogTBQEL")){
			predictor.SummaryAnalysis.round = round;
			predictor.SummaryAnalysis.extractAveragePrecisionForTB("./"+packageName+"/summary."+runId, "./"+packageName+"/map."+runId);
			predictor.SummaryAnalysis.setTermSize(5);
			predictor.SummaryAnalysis.normalizeAveragePrecision("./"+packageName+"/map."+runId, "./"+packageName+"/map.normalized."+runId);
		}
		else{
		predictor.SummaryAnalysis.round = round;
		predictor.SummaryAnalysis.extractAveragePrecision("./"+packageName+"/summary."+runId, "./"+packageName+"/map."+runId);
		predictor.SummaryAnalysis.setTermSize(5);
		predictor.SummaryAnalysis.normalizeAveragePrecision("./"+packageName+"/map."+runId, "./"+packageName+"/map.normalized."+runId);
		}
	}

	public static void processPrediction(String runId,String packageName) throws IOException{
		if(runId.equals("uogTBQEL")){
			// 计算SD
			predictor.SD predictorSD = new predictor.SD();
			predictorSD.setK(1000);
			predictorSD.getSDScores("./" + packageName + "/input." + runId, "./"+ packageName + "/sDScore." + runId);
			System.out.println("根据input计算每个query的SD值,并将SDScore存入文件,已完成..");
			//计算SMV
			predictor.SMV predictorSMV = new predictor.SMV();
			predictorSMV.setK(1000);//把predictorSMV的k设为100
			predictorSMV.getSMVScores("./"+packageName+"/input."+runId,"./"+packageName+"/sMVScore."+runId);
			System.out.println("根据input计算每个query的SMV值,并将SMVScore存入文件,已完成..");
			
			//计算NQC
			predictor.NQC predictorNQC=new predictor.NQC();
			predictorNQC.setK(1000);//把predictorNQC的k设为100
			predictorNQC.getNQCScores("./"+packageName+"/input."+runId, "./"+packageName+"/nQCScore."+runId);
			System.out.println("根据input计算每个query的NQC值,并将NQCScore存入文件,已完成..");
			
			//计算C2
			newPredictor.C2 newPredictorC2=new newPredictor.C2();
			newPredictorC2.setK(1000);//截断参数
			newPredictorC2.setQueryMap(null);
			newPredictorC2.getC2Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c2Score."+runId);
		}
		else{
		// 计算SD
		predictor.SD predictorSD = new predictor.SD();
		predictorSD.setK(100);
		predictorSD.getSDScores("./" + packageName + "/input." + runId, "./"+ packageName + "/sDScore." + runId);
		System.out.println("根据input计算每个query的SD值,并将SDScore存入文件,已完成..");
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
		
		//计算C2
		newPredictor.C2 newPredictorC2=new newPredictor.C2();
		newPredictorC2.setK(100);//截断参数
		newPredictorC2.setQueryMap(null);
		newPredictorC2.getC2Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c2Score."+runId);
		}
		
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
		

		
		//计算C
		newPredictor.C newPredictorC=new newPredictor.C();
		newPredictorC.setAlpha(0.5);//SD_2和WIG的平衡参数
		newPredictorC.getCScores("./"+packageName+"/input."+runId,"./"+packageName+"/cScore."+runId);
		
		
		
		//计算C3
		newPredictor.C3 newPredictorC3=new newPredictor.C3();
		newPredictorC3.setK(100);//截断参数
		newPredictorC3.getC3Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c3Score."+runId);
		
		//计算C4
		newPredictor.C4 newPredictorC4=new newPredictor.C4();
		newPredictorC4.setK(100);//截断参数
		newPredictorC4.getC4Scores("./"+packageName+"/input."+runId,"./"+packageName+"/c4Score."+runId);
		
	}
	/**
	 * 可供getGeneratedResult_batch()方法调用
	 * @param args
	 * @throws IOException
	 */
	public static void getGeneratedResult(String runId,String packageName) throws IOException {
		//robustTrack2004的可验证topic数量为249
		int round=0;
		switch(runId){
		case "pircRB04t3":round=249;break;
		case "uogTBQEL":round=49;break;
		case "CnQst2":round=49;break;
		case "thutd5":round=49;break;
		default:round=50;break;
		
		}
		//round=249;
		//by Zoey
		//分析summary文件,获取average Precision信息
		processSummary(runId,packageName,round);
		
		//根据input文件,运行预测算法,得到预测信息
		//by Zoey
		processPrediction(runId,packageName);
		
		
		//预测值与AP的pearson kendall spearman系数:
		System.out.println("预测值与AP的pearson kendall spearman系数:\n");
	
		//运行pearson算法
		try {
			//SD
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/sDScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sD2对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/sD2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//wIG对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/wIGScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sMV对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/sMVScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//nQC对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/nQCScore."+runId,"./"+packageName+"/map.normalized."+runId);
			
			//c对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/cScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//c2对应的pearson
			correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/c2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c3对应的pearson
			//by Zoey
			//correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/c3Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c4对应的pearson
			//correlationCoefficient.PearsonAnalysis.loadScoreAndComputePearson("./"+packageName+"/c4Score."+runId,"./"+packageName+"/map.normalized."+runId);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//运行kendall算法
		try {
			//SD
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/sDScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sD2对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/sD2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//wIG对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/wIGScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sMV对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/sMVScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//nQC对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/nQCScore."+runId,"./"+packageName+"/map.normalized."+runId);
			
			//c对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/cScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//c2对应的kendall
			correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/c2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c3对应的kendall
			//correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/c3Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c4对应的kendall
			//correlationCoefficient.Kendall.loadScoreAndComputeKendall("./"+packageName+"/c4Score."+runId,"./"+packageName+"/map.normalized."+runId);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//运行spearman算法
		try {
			//SD
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/sDScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sD2对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/sD2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//wIG对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/wIGScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//sMV对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/sMVScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//nQC对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/nQCScore."+runId,"./"+packageName+"/map.normalized."+runId);
			
			//c对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/cScore."+runId,"./"+packageName+"/map.normalized."+runId);
			//c2对应的spearman
			new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/c2Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c3对应的spearman
			//new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/c3Score."+runId,"./"+packageName+"/map.normalized."+runId);
			//c4对应的spearman
			//new correlationCoefficient.Spearman().loadScoreAndComputeSpearman("./"+packageName+"/c4Score."+runId,"./"+packageName+"/map.normalized."+runId);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 批量产生预测结果
	 * @throws InterruptedException 
	 */
	public static void getGeneratedResult_batch()throws IOException, InterruptedException{
		FileReader fileReader=null;
		BufferedReader buffReader=null;
		String runIdFile=null;
		String tempLine=null;
		String packageName=null;
		String runId=null;
		int n=0;
		
		runIdFile="runId.txt";
		//by Zoey
		//packageName="robustTrack2004/13org_best_map_runs";
		packageName="six_runs";
		fileReader=new FileReader("./"+packageName+"/"+runIdFile);
		buffReader=new BufferedReader(fileReader);
		
		while((tempLine=buffReader.readLine())!=null){
			runId=tempLine.split("\\.")[1];
			System.out.println("\n\n"+(++n)+"、track为"+runId);
			//by Zoey
			packageName=packageName+"/"+runId;
			getGeneratedResult(runId,packageName);
			packageName="six_runs";
			//Thread.sleep(15000);
		}
		buffReader.close();
		System.out.println("批量产生结果,已完成!");
	}
	/**
	 * 
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		//批量产生预测结果
		getGeneratedResult_batch();
		
	}

}
