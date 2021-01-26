package dataexpo.main;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import dataexpo.DateKey;

public class DelayCountReducerWithDateKey extends Reducer<DateKey , IntWritable, DateKey, IntWritable> {
	private MultipleOutputs<DateKey, IntWritable> mos;
	private DateKey outputKey = new DateKey();// reduce 출력키
	private IntWritable result = new IntWritable();// reduce 출력값

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		mos = new MultipleOutputs<DateKey, IntWritable>(context);//리듀서 실행 전에 mos 객체 설정
	}
	
	//key : D,1988,1
	@Override
	public void reduce(DateKey key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		String[] columns = key.getYear().split(",");// 콤마 구분자 분리
		outputKey.setYear(key.getYear().substring(2));
		outputKey.setMonth(key.getMonth());
		int sum = 0;
		for(IntWritable value : values) {
			sum += value.get();
		}
		result.set(sum);
		
		if (columns[0].equals("D")) {// 출발 지연
			mos.write("departure", outputKey, result);// 출력 데이터 생성
		} else {// 도착 지연
			mos.write("arrival", outputKey, result);
		}
	}

	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}
}
