package osh.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import osh.domain.OshCctv;

public class OshMapper  extends Mapper<LongWritable, Text, Text, IntWritable>{
	private final static IntWritable outputValue = new IntWritable();
	private Text outputKey = new Text();
	private String workType;
	
	public void setup(Context context) {
		workType = context.getConfiguration().get("workType");
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		if(key.get() == 0) {
			return;
		}
		
		OshCctv cctv = new OshCctv(value);
		switch(workType) {
		case "mgrgov":
			outputKey.set("mgrgov==" + cctv.getOfficeName());
			outputValue.set(cctv.getNum());
			context.write(outputKey, outputValue);
			break;
			
		case "reason":
			outputKey.set("reason=="+ cctv.getReason());
			outputValue.set(cctv.getNum());
			context.write(outputKey, outputValue);
			break;
			
		case "year":
			outputKey.set("year==" + cctv.getYear());
			outputValue.set(cctv.getNum());
			context.write(outputKey, outputValue);
			break;
		}
	}
}
