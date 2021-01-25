package homework20210125;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class HadoopMain2Reducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	private MultipleOutputs<Text, IntWritable> mos;
	private Text outputKey = new Text();// reduce 출력키
	private IntWritable result = new IntWritable();// reduce 출력값

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		mos = new MultipleOutputs<Text, IntWritable>(context);//리듀서 실행 전에 mos 객체 설정
	}
	
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		String[] columns = key.toString().split(",");
		outputKey.set(columns[1] + "," + columns[2]/* +","+columns[3] */);
		if (columns[0].equals("ON")) {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("ON", outputKey, result);
		} else {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("OUT", outputKey, result);
		}
	}
	
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}
}
