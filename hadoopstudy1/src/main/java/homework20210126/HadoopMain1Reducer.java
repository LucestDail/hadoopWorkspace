package homework20210126;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import dataexpo.DateKey;

public class HadoopMain1Reducer extends Reducer<DateKey, IntWritable, DateKey, IntWritable>{
	private IntWritable result = new IntWritable();// reduce 출력값
	public void reduce(DateKey key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for(IntWritable value : values) {
			sum += value.get();
		}
		result.set(sum);
		context.write(key, result);
	}
}
