package predictor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class SD {
	private int k=100;//k为SD的截断参数
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}

	// standardDev算法
	public double computeSD(double[] m) {//提前确认k<=m.length
		double midd = mean(m, k);
		double s = 0;
		for (int i = 0; i < k; i++) {
			s += (m[i] - midd) * (m[i] - midd);
		}
		double iner = s / (k-1);
		return Math.sqrt(iner);
	}

	public double mean(double[] m, int n) {
		double s = 0;
		for (int i = 0; i < n; i++) {
			s += m[i];
		}
		return s / n;
	}

	/**
	 * 在2016/06/02,作了修改,加入了k_original变量。
	 * 根据input.pircRB04t3计算每个query的SD值, 并将SDScore存入文件
	 * @track为NLPR04OKapi
	 * */
	public void getSDScores(String input, String output) {
		FileReader fileReader = null;
		LineNumberReader lineNumberReader = null;
		FileWriter fileWriter = null;
		String tempLine = null;
		String[] terms = null;// 分析tempLine
		String preQueryId = null;
		ArrayList<Double> arrayList = new ArrayList<Double>();
		double score = 0;// 临时存放terms[4]的score
		double[] scores = null;// 临时存放一个query对应的score数组
		int scoreCount = 0;// 临时存放score数组的长度
		double SDScore = 0;// 临时存放一个query的SD值
		int k_original=k;//存储起初的k值

		try {
			fileReader = new FileReader(input);
			lineNumberReader = new LineNumberReader(fileReader);
			fileWriter = new FileWriter(output, false);
			while ((tempLine = lineNumberReader.readLine()) != null) {
				terms = tempLine.split("\t| ");
				// 起初preQueryId为null
				if (preQueryId == null)
					preQueryId = terms[0];
				// queryId相同,存入score
				if (preQueryId.equalsIgnoreCase(terms[0])) {
					score = Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
				// queryId不同,计算preQueryId的SDScore,写入文件,清空arrayList信息,处理terms信息
				if (!preQueryId.equalsIgnoreCase(terms[0])) {
					// 把arrayList转化为double数组
					scoreCount = arrayList.size();
					scores = new double[scoreCount];
					for (int i = 0; i < scoreCount; i++)
						scores[i] = arrayList.get(i);
					//若此查询下的文档数scoreCount小于k,把k设为scoreCount
					if(scoreCount<k) k=scoreCount;
					// 调用computSD()计算此query的SD值
					SDScore = computeSD(scores);
					// 把queryId和SDScore写入文件
					fileWriter.write("queryId:\t" + preQueryId + "\tSD:\t"
							+ SDScore + "\n");
					//若k不为初始k值,把k设为初始值k_original
					if(k!=k_original) k=k_original;
					// 清空arrayList
					arrayList.clear();
					// 开始处理terms信息
					preQueryId = terms[0];
					score = Double.parseDouble(terms[4]);
					arrayList.add(score);
				}
			}
			// 最后queryId对应的scores未处理,计算其SDScore,并写入文件
			// 把arrayList转化为double数组
			scoreCount = arrayList.size();
			scores = new double[scoreCount];
			for (int i = 0; i < scoreCount; i++)
				scores[i] = arrayList.get(i);
			//若此查询下的文档数scoreCount小于k,把k设为scoreCount
			if(scoreCount<k) k=scoreCount;
			// 调用computSD()计算此query的SD值
			SDScore = computeSD(scores);
			// 把queryId和SDScore写入文件
			fileWriter.write("queryId:\t" + preQueryId + "\tSD:\t" + SDScore
					+ "\n");
		} catch (IOException e) {
			System.err.println("处理数据出错!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				lineNumberReader.close();
			} catch (IOException e) {
				System.err.println("关闭IO连接错误!");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SD sD = new SD();
		sD.k=100;
		sD.getSDScores("./robustTrack2004/input.apl04rsTDNw5.inversed",
				"./robustTrack2004/sDScore.apl04rsTDNw5");
		System.out
		.println("根据input.pircRB04t3计算每个query的SD值,并将SDScore存入文件,已完成..");
	}

}
