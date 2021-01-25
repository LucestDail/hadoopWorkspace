package dataexpo.main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class DepartureDelayCount {
	public static void main(String[] args) throws Exception {
		String in = "infile/1987.csv";
		String out = "outfile/1987dout";
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf,"DepartureDelayCount");
		FileInputFormat.addInputPath(job, new Path(in));
		FileOutputFormat.setOutputPath(job, new Path(out));
		job.setJarByClass(DepartureDelayCount.class);
		job.setMapperClass(DepartureDelayCountMapper.class);
		job.setReducerClass(DelayCountReducer.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(new Path(out))) {
			hdfs.delete(new Path(out), true);//출력파일의 경로 존재하는 경우 삭제
			System.out.println("[System log] 기존 출력파일 삭제");
		}
		
		job.waitForCompletion(true);
	}

}
