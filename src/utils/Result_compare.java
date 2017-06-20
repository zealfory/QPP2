package utils;

import java.util.Comparator;

/**
 * 对Result类的对象进行排序: sort by topic, then by score, and
 * then by docno, which is the traditional sort order for TREC runs
 * @author 1
 *
 */
public class Result_compare implements Comparator<Result>{

	@Override
	public int compare(Result arg0, Result arg1) {
		if(arg0.getTopic()<arg1.getTopic())
			return -1;
		if(arg0.getTopic()>arg1.getTopic())
			return 1;
		if(arg0.getScore()<arg1.getScore())
			return 1;
		if(arg0.getScore()>arg1.getScore())
			return -1;
		return arg0.getDocno().compareTo(arg1.getDocno());
	}

}
