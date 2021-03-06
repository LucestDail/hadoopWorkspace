package dataexpo.main;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import dataexpo.Airline;
import dataexpo.mapreduce.DelayCounters;

public class DelayCountMapperWithCounter extends Mapper<LongWritable, Text, Text, IntWritable>{
	private String workType;
	private final static IntWritable one = new IntWritable(1);
	private Text outputKey = new Text();
	
	@Override
	public void setup(Context context) throws IOException, InterruptedException{
		workType = context.getConfiguration().get("workType");
	}
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		Airline parser = new Airline(value);
		if(workType.equals("departure")) {
			if(parser.isDepartureDelayAvailable()) {
				if(parser.getDepartureDelayTime() > 0) {//출발 지연
					outputKey.set(parser.getYear() + "," + parser.getMonth());
					context.write(outputKey, one);
				}else if(parser.getDepartureDelayTime() == 0) {//정시 출발
					context.getCounter(DelayCounters.scheduled_departure).increment(1);
				}else if(parser.getDepartureDelayTime() < 0) {//일찍 출발
					context.getCounter(DelayCounters.early_departure).increment(1);
				}
			}else {//유효하지 않은 출발 정보
				context.getCounter(DelayCounters.not_available_departure).increment(1);
			}
		}else if(workType.equals("arrival")) {
			if(parser.isArriveDelayAvailable()) {
				if(parser.getArriveDelayTime() > 0) {//도착 지연
					outputKey.set(parser.getYear() + "," + parser.getMonth());
					context.write(outputKey, one);
				}else if(parser.getArriveDelayTime() == 0) {//정시 도착
					context.getCounter(DelayCounters.scheduled_arrival).increment(1);
				}else if(parser.getArriveDelayTime() < 0) {//일찍 도착
					context.getCounter(DelayCounters.early_arrival).increment(1);
				}
			}else {// 유효하지 않은 도착 정보
				context.getCounter(DelayCounters.not_available_arrival).increment(1);
			}
		}
	}
}
