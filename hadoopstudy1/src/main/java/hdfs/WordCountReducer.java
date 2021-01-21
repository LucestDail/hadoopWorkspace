package hdfs;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
//read,1
//a,1,1
//book,1,1
//write,1
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{// mapper 에서 받아온 Text, IntWritable, 내가 쓸 Text, IntWritable
	private IntWritable result = new IntWritable();
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
		int sum = 0;
		for(IntWritable v : values) {
			sum += v.get();
		}
		result.set(sum);//1
		context.write(key, result);//read,1
	}
}
