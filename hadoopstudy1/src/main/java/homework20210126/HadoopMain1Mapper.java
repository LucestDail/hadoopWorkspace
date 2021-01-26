package homework20210126;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import dataexpo.Airline;
import dataexpo.DateKey;

public class HadoopMain1Mapper extends Mapper<LongWritable, Text, DateKey, IntWritable>{
	private DateKey outputKey = new DateKey();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() == 0) {
			return;
		}
		Airline airline = new Airline(value);
//		outputKey.set(airline.getYear() + "," + airline.getMonth());
		outputKey.setYear(airline.getYear()+"");
		outputKey.setMonth(airline.getMonth());
		IntWritable distance = new IntWritable(airline.getDistance());
		context.write(outputKey, distance);
	}
}
