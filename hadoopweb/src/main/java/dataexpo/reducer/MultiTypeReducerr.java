package dataexpo.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MultiTypeReducerr extends Reducer<Text, IntWritable, Text, IntWritable>{
	private MultipleOutputs<Text, IntWritable> mos;
	private Text outkey = new Text();// reduce 출력키
	private IntWritable result = new IntWritable();
	
	@Override
	public void setup(Context context) {
		mos = new MultipleOutputs<Text, IntWritable>(context);//리듀서 실행 전에 mos 객체 설정
	}
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
		String[] columns = key.toString().split("-");// 콤마 구분자 분리
		outkey.set(columns[1] + "-" + columns[2]);// 출력키 설정
		if (columns[0].trim().equals("D")) {// 출발 지연
			int sum = 0;// 출발 지연 횟수 합산
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);// 출력갑 설정
			mos.write("departure", outkey, result);// 출력 데이터 생성
		} else if(columns[0].equals("DI")){
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set((int)(sum/1000));// 출력갑 설정
			mos.write("distance", outkey, result);// 출력 데이터 생성
			
		}else {// 도착 지연
			int sum = 0;// 도착 지연 횟수 합산
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			mos.write("arrival", outkey, result);
		}
	}
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}
}
