package predictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * 分析summary文件
 * */
public class SummaryAnalysis {
	public static int round = 0;// summary文件中query数
	private static int countChar = 0; // only used by method nextString()
	private static int termSize = 0;// 一行的词项数, 供normalizeAveragePrecision()使用
	
	public static int getTermSize() {
		return termSize;
	}
	public static void setTermSize(int termSize) {
		SummaryAnalysis.termSize = termSize;
	}

	
	public static void extractAveragePrecision(String input, String output) {
		FileReader fileReader = null;
		LineNumberReader lineNumberReader = null;
		FileWriter fileWriter = null;
		String tempLine = null;
		try {
			fileReader = new FileReader(input);
			lineNumberReader = new LineNumberReader(fileReader);
			fileWriter = new FileWriter(output, false);
			for (int i = 0; i < round; i++) {
				lineNumberReader.readLine();
				tempLine = lineNumberReader.readLine();
				fileWriter.write(tempLine + '\t');
				for (int j = 1; j <= 18; j++) {
					tempLine = lineNumberReader.readLine();
				}
				fileWriter.write(tempLine + "\n");
				for (int j = 1; j <= 12; j++) {
					lineNumberReader.readLine();
				}
			}
			System.out.println("average Precision data have been extracted..");
		} catch (IOException e) {
			System.err.println("读取数据出错!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO文件流出错!");
				e.printStackTrace();
			}
		}
	}

	//by Zoey
	//处理TB级Summary
	public static void extractAveragePrecisionForTB(String input, String output) {
		FileReader fileReader = null;
		LineNumberReader lineNumberReader = null;
		FileWriter fileWriter = null;
		String tempLine = null;
		int times=0;//记录循环的次数
		try {
			fileReader = new FileReader(input);
			lineNumberReader = new LineNumberReader(fileReader);
			fileWriter = new FileWriter(output, false);
			
			while((tempLine=lineNumberReader.readLine())!=null){
				if(times<round&&tempLine.startsWith("map")){
					fileWriter.write("Queryid uogTBQEL "+tempLine+"\n");
					times++;
				}
			}
			System.out.println("average Precision data have been extracted..");
		} catch (IOException e) {
			System.err.println("读取数据出错!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO文件流出错!");
				e.printStackTrace();
			}
		}
	}
	
	public static void showResult(String input) throws IOException {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		String info = null;
		fileReader = new FileReader(input);
		bufferedReader = new BufferedReader(fileReader);
		while ((info = bufferedReader.readLine()) != null) {
			System.out.println(info);
		}
		bufferedReader.close();
	}

	public static String nextString(StringBuffer s, int i) {// the same as
		int start = 0, end = 0;
		if (i == 1)
			countChar = 0;
		while ((countChar < s.length())
				&& (s.charAt(countChar) == '\b' || s.charAt(countChar) == '\t'
				|| s.charAt(countChar) == ' '
				|| s.charAt(countChar) == '\n'
				|| s.charAt(countChar) == '\f' || s.charAt(countChar) == '\r'))
			countChar++;
		start = countChar;
		end = start;
		while ((end < s.length()) && (s.charAt(end) != '\b')
				&& (s.charAt(end) != '\t') && (s.charAt(end) != ' ')
				&& (s.charAt(end) != '\n') && (s.charAt(end) != '\f')
				&& (s.charAt(end) != '\r'))
			end++;
		countChar = end;
		return (s.substring(start, end).trim());
	}

	/**
	 * 去除map文件中的空格
	 * */
	public static void normalizeAveragePrecision(String input, String output) {
		String tempLine = null;
		FileReader fileReader = null;
		LineNumberReader lineNumberReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		StringBuffer line = new StringBuffer();
		try {
			fileReader = new FileReader(input);
			lineNumberReader = new LineNumberReader(fileReader);
			fileWriter = new FileWriter(output, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			tempLine = lineNumberReader.readLine();
			while (tempLine != null) {
				line.delete(0, line.length());
				line.append(tempLine);
				for (int i = 1; i <= termSize; i++) {
					bufferedWriter.write(nextString(line, i) + "\t");
				}
				bufferedWriter.write("\n");
				tempLine = lineNumberReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				lineNumberReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("average Precision data have been normalized..");
	}

	public static void main(String[] args) {
		/*SummaryAnalysis.round = 249;
		extractAveragePrecision("./robustTrack2004/summary.apl04rsTDNw5", "./robustTrack2004/map.apl04rsTDNw5");
		termSize = 5;
		normalizeAveragePrecision("./robustTrack2004/map.apl04rsTDNw5", "./robustTrack2004/map.normalized.apl04rsTDNw5");
		*/
		termSize=6;
		normalizeAveragePrecision("./resultsOfTRECs/input.ETHme1","./resultsOfTRECs/input.new.ETHme1");
	}

}
