package dataexpo.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import dataexpo.Airline;

public class CarrMultiTypeMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
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
			outkey.set("dstnc-"+air.getYear()+"-"+air.getUniqueCarrier());
			DISTANCE.set(air.getDistance());
			context.write(outkey, DISTANCE);
		}
		
		switch(workType) {
		case "d":
			if(air.isDepartureDelayAvailable()) {
				if(air.getDepartureDelayTime() > 0) {
					outkey.set("delay-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}else if(air.getDepartureDelayTime() == 0) {
					outkey.set("intme-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}else {
					outkey.set("early-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}
			}
			break;
		case "a":
			if(air.isArriveDelayAvailable()) {
				if(air.getArriveDelayTime() > 0) {
					outkey.set("delay-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}else if(air.getArriveDelayTime() == 0) {
					outkey.set("intme-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}else {
					outkey.set("early-"+ air.getYear() + "-" + air.getUniqueCarrier());
					context.write(outkey, ONE);
				}
			}
			break;
		}
	}
}
