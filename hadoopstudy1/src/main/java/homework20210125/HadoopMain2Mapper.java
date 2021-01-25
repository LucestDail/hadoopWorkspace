package homework20210125;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import homework20210125.data.Train;

public class HadoopMain2Mapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	private final static IntWritable ONE = new IntWritable(1);
	private Text outputKey = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() == 0) {
			// 첫 줄 컬럼명칭 스킵
			return;
		}
		Train train = new Train(value);
		
		for(int i = 0; i<train.getOnboard().size(); i++) {
			String keySetter1 = "ON," + /* train.getDate() + "," + */ train.getLane() + "," + train.getTimezone()[i];
			outputKey.set(keySetter1);
			IntWritable num = new IntWritable(train.getOnboard().get(i));
			context.write(outputKey, num);
			keySetter1 = "OUT," + /* train.getDate() + "," + */ train.getLane() + "," + train.getTimezone()[i];
			outputKey.set(keySetter1);
			num = new IntWritable(train.getOutboard().get(i));
			context.write(outputKey, num);
		}
	}
}
