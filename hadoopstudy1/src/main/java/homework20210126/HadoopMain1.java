package homework20210126;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import dataexpo.DateKey;
/**
 * Hadoop Homework 20210126
 * Topic : separating Aircrafe unique code by flight distance
 * @author dhtmd
 *
 */
public class HadoopMain1 {
	public static void main(String[] args) throws Exception {
		String in = "infile/1988.csv";
		String out = "outfile/monly-distance-DateKey-1988";
		Configuration conf = new Configuration();
		// job 이름 설정
		@SuppressWarnings("deprecation")
		Job job = new Job(new Configuration(), "HadoopMain1");

		// 입출력 데이터 경로 설정
		FileInputFormat.addInputPath(job, new Path(in));
		FileOutputFormat.setOutputPath(job, new Path(out));

		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(new Path(out))) {
			hdfs.delete(new Path(out), true);// 출력파일의 경로 존재하는 경우 삭제
			System.out.println("[System log] 기존 출력파일 삭제");
		}
		// Job 클래스 설정
		job.setJarByClass(HadoopMain1.class);

		// Mapper 클래스 설정
		job.setMapperClass(HadoopMain1Mapper.class);

		// Reducer 클래스 설정
		job.setReducerClass(HadoopMain1Reducer.class);

		// 입출력 데이터 포멧 설정
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// 출력키 및 출력값 유형 설정
		job.setOutputKeyClass(DateKey.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.waitForCompletion(true);

	}
}
