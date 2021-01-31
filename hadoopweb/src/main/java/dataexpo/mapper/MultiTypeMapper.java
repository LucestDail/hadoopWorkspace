package dataexpo.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import dataexpo.Airline;

public class MultiTypeMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	private String workType;
	private final static IntWritable ONE = new IntWritable(1);
	private final static IntWritable DISTANCE = new IntWritable();
	private Text outkey = new Text();
	
	public void setup(Context context) {
		workType = context.getConfiguration().get("workType");
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		if(key.get() == 0) {
			return;
		}
		Airline air = new Airline(value);
		if(air.isDistanceAvailable() && air.getDistance() > 0) {
			outkey.set("DI-"+air.getYear()+"-"+air.getMonth());
			DISTANCE.set(air.getDistance());
			context.write(outkey, DISTANCE);
		}
		switch(workType) {
		case "d":
			if(air.isDepartureDelayAvailable() && air.getDepartureDelayTime() > 0) {
				outkey.set("D-"+ air.getYear() + "-" + air.getMonth());
				context.write(outkey, ONE);
			}
			if(air.isArriveDelayAvailable() && air.getArriveDelayTime() > 0) {
				outkey.set("A-" + air.getYear() + "-" + air.getMonth());
				context.write(outkey,ONE);
			}
			break;
		case "s":
			if(air.isDepartureDelayAvailable() && air.getDepartureDelayTime() == 0) {
				outkey.set("D-"+ air.getYear() + "-" + air.getMonth());
				context.write(outkey, ONE);
			}
			if(air.isArriveDelayAvailable() && air.getArriveDelayTime() == 0) {
				outkey.set("A-" + air.getYear() + "-" + air.getMonth());
				context.write(outkey,ONE);
			}
			break;
		case "e":
			if(air.isDepartureDelayAvailable() && air.getDepartureDelayTime() < 0) {
				outkey.set("D-"+ air.getYear() + "-" + air.getMonth());
				context.write(outkey, ONE);
			}
			if(air.isArriveDelayAvailable() && air.getArriveDelayTime() < 0) {
				outkey.set("A-" + air.getYear() + "-" + air.getMonth());
				context.write(outkey,ONE);
			}
			break;
		}
	}
}
