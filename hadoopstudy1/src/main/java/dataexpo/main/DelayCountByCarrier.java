package dataexpo.main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import dataexpo.mapreduce.DelayCounters;

public class DelayCountByCarrier extends Configured implements Tool{
	public static void main(String[] args) throws Exception {
		String arg[] = {"-D","workType=departure","infile/1987.csv","outfile/dc-1987"};
		int res = ToolRunner.run(new Configuration(), new DelayCountByCarrier(),arg);
		System.out.println("MR-Job Result : " + res);
	}
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] otherArgs = new GenericOptionsParser(getConf(), args).getRemainingArgs();
		if(otherArgs.length != 2) {
			System.err.println("Usage : DelayCountByCarrier <in> <out>");
			System.exit(2);
		}
		System.out.println(otherArgs[0] + "," + otherArgs[1]);
		@SuppressWarnings("deprecation")
		Job job = new Job(getConf(), "DelayCountByCarrier");
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		job.setJarByClass(DelayCountByCarrier.class);
		job.setMapperClass(DelayCountMapperByCarrierWithCounter.class);
		job.setReducerClass(DelayCountReducer.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileSystem hdfs = FileSystem.get(getConf());
		if (hdfs.exists(new Path(otherArgs[1]))) {
			hdfs.delete(new Path(otherArgs[1]), true);//출력파일의 경로 존재하는 경우 삭제
			System.out.println("[System log] 기존 출력파일 삭제");
		}
		
		job.waitForCompletion(true);
		for(DelayCounters d : DelayCounters.values()) {
			long tot = job.getCounters().findCounter(d).getValue();
			System.out.println(d + " : " + tot);
		}
		return 0;
	}
}
