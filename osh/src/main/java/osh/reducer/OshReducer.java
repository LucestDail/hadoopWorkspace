package osh.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class OshReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	private MultipleOutputs<Text, IntWritable> mos;
	private Text outputKey = new Text();
	private IntWritable result = new IntWritable();
	
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		mos = new MultipleOutputs<Text, IntWritable>(context);
	}
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
		String[] columns = key.toString().split("==");
		outputKey.set(columns[1]);
		if (columns[0].equals("mgrgov")) {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("mgrgov", outputKey, result);
		}
		if (columns[0].equals("reason")) {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("reason", outputKey, result);
		}
		if (columns[0].equals("year")) {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("year", outputKey, result);
		}
	}
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}
}
