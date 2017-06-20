package utils;

public class Result{
	public int topic;
	public String docno;
	public int rank;
	public double score;
	public String runId;

	public int getTopic() {
		return topic;
	}
	public void setTopic(int topic) {
		this.topic = topic;
	}
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getRunId() {
		return runId;
	}
	public void setRunId(String runId) {
		this.runId = runId;
	}
	public Result(String tempLine){
		String[] terms=null;
		terms=tempLine.split(" |\t");
		topic=Integer.parseInt(terms[0]);
		docno=terms[2];
		rank=Integer.parseInt(terms[3]);
		score=Double.parseDouble(terms[4]);
		runId=terms[5];
	}
	public String getTempLine(){
		String tempLine="";
		tempLine=topic+"\tQ0\t"+docno+"\t"+rank+"\t"+score+"\t"+runId+"\n";
		return tempLine;
	}
}
