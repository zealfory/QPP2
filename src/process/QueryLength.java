package process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
/**
 * 根据./robustTrack2004/testSet.extracted文件,得到queryId,queryLength,后存入HashMap。
 * 
 * */
public class QueryLength {
	
	/**
	 * 根据./robustTrack2004/testSet.extracted文件,得到queryId,queryLength,后存入HashMap。
	 * 
	 * */
	public static HashMap<String,String> getQueryLength() throws IOException{
		HashMap<String,String> queryMap=new HashMap<String,String>();
		FileReader fileReader=null;
		BufferedReader bufferedReader=null;
		String tempLine=null;
		String[] terms=null;
		String queryId=null;//临时存放queryId
		String queryLen=null;//临时存放queryLen
		
		fileReader=new FileReader("./robustTrack2004/testSet.extracted");
		bufferedReader=new BufferedReader(fileReader);
		while((tempLine=bufferedReader.readLine())!=null){
			if(tempLine.startsWith("<num> Number:")){
				terms=tempLine.split(" |\t");
				queryId=terms[2];
				//获取queryLen
				tempLine=bufferedReader.readLine();
				tempLine=tempLine.substring(7).trim();
				terms=tempLine.split("  | \t| |\t");
				queryLen=String.valueOf(terms.length);
				//把queryId,queryLen存入queryMap
				queryMap.put(queryId, queryLen);
				//读一分隔行
				bufferedReader.readLine();
			}
		}
		bufferedReader.close();
		System.out.println("根据./robustTrack2004/testSet.extracted文件,得到queryId,queryLength,后存入HashMap,已完成..");
		return queryMap;
	}
	/**
	 * 显示queryMap信息
	 * @throws IOException 
	 * 
	 * */
	public static void showQueryMap() throws IOException{
		HashMap<String,String> queryMap=getQueryLength();
		Set<Entry<String,String>> set=queryMap.entrySet();
		Iterator<Entry<String,String>> it=set.iterator();
		int i=0;
		Entry<String,String> queryEntry=null;
		while(it.hasNext()){
			queryEntry=it.next();
			System.out.println((++i)+" "+queryEntry.getKey()+" "+queryEntry.getValue());
		}
	}
	public static void main(String[] args) throws IOException{
		showQueryMap();
	}

}
