package dataexpo.main;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import dataexpo.Airline;
import dataexpo.DateKey;
import dataexpo.mapreduce.DelayCounters;

public class DelayCountMapperWithDateKey extends Mapper<LongWritable, Text, DateKey, IntWritable> {
	private final static IntWritable ONE = new IntWritable(1);
	private DateKey outputKey = new DateKey();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (key.get() == 0) {
			return;
		}
		Airline airline = new Airline(value);
		if (airline.isDepartureDelayAvailable()) {// 출발 지연 정보
			if (airline.getDepartureDelayTime() > 0) {// 출발 지연 항공기
				outputKey.setYear("D,"+airline.getYear());
				outputKey.setMonth(airline.getMonth());
				context.write(outputKey, ONE);
			} else if (airline.getDepartureDelayTime() == 0) { // 정시 출발
				context.getCounter(DelayCounters.scheduled_departure).increment(1);
			} else if (airline.getDepartureDelayTime() < 0) { // 조기 출발
				context.getCounter(DelayCounters.early_departure).increment(1);
			}
		} else {// 출발 지연 대상 아님
			context.getCounter(DelayCounters.not_available_departure).increment(1);
		}

		if (airline.isArriveDelayAvailable()) { // 도착 지연 정보
			if (airline.getArriveDelayTime() > 0) { // 도착 지연 항공기
				outputKey.setYear("A,"+airline.getYear());
				outputKey.setMonth(airline.getMonth());
				context.write(outputKey, ONE);
			} else if (airline.getArriveDelayTime() == 0) { // 정시 도착
				context.getCounter(DelayCounters.scheduled_arrival).increment(1);
			} else if (airline.getArriveDelayTime() < 0) { // 조기 도착
				context.getCounter(DelayCounters.early_arrival).increment(1);
			}
		} else {// 도착 지연 대상 아님
			context.getCounter(DelayCounters.not_available_arrival).increment(1);
		}
	}
}
