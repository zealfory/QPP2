package predictor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * 计算每个queryId对应的1.0/qlen值。
 * @author 1
 *
 */
public class Reciprocal_qlen {
	
	private HashMap<String,String> queryMap=null;//queryMap中有queryId和queryLen

	public HashMap<String, String> getQueryMap() {
		return queryMap;
	}
	public void setQueryMap(HashMap<String, String> queryMap) {
		this.queryMap = queryMap;
	}
	
	public double computeReciprocal_qlen(String queryId){
		int qlen=0;
		if(queryMap!=null){
			qlen=Integer.parseInt(queryMap.get(queryId));
		}else{
			qlen=1;
		}
		double reciprocal=0;
		reciprocal=1.0/qlen;
		return reciprocal;
	}
	
	/**
	 * 根据input.runId计算每个query的Reciprocal_qlen值, 并将Recip_score存入文件
	 * */
	public void getReciprocal_qlenScores(String input, String output) {
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		FileWriter fileWriter = null;
		String tempLine = null;
		String[] terms = null;// 分析tempLine
		String preQueryId = null;
		double Recip_score = 0;// 临时存放一个query的Reciprocal_qlen值

		try {
			fileReader = new FileReader(input);
			buffReader = new BufferedReader(fileReader);
			fileWriter = new FileWriter(output, false);
			while ((tempLine = buffReader.readLine()) != null) {
				terms = tempLine.split("\t| ");
				// 起初preQueryId为null
				if (preQueryId == null)
					preQueryId = terms[0];
				// queryId相同,存入score
				if (preQueryId.equalsIgnoreCase(terms[0])) {
					
				}
				// queryId不同,计算preQueryId的Recip_score,写入文件,处理terms信息
				if (!preQueryId.equalsIgnoreCase(terms[0])) {
					// 调用computeReciprocal_qlen()计算此query的Reciprocal_qlen值
					Recip_score = computeReciprocal_qlen(preQueryId);
					// 把queryId和Recip_score写入文件
					fileWriter.write("queryId:\t" + preQueryId + "\tReciprocal_qlen:\t"+ Recip_score + "\n");
					//开始处理terms信息
					preQueryId = terms[0];
				}
			}
			// 最后queryId对应的信息未处理,计算其Recip_score,并写入文件
			
			// 调用computeReciprocal_qlen()计算此query的Reciprocal_qlen值
			Recip_score = computeReciprocal_qlen(preQueryId);
			// 把queryId和Recip_score写入文件
			fileWriter.write("queryId:\t" + preQueryId + "\tReciprocal_qlen:\t"+ Recip_score + "\n");
		} catch (IOException e) {
			System.err.println("处理数据出错!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				buffReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO连接错误!");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		
	}

}
