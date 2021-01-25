package homework20210125;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
/**
 * Hadoop Homework 20210125
 * Topic : separating train time by on&off boarding
 * @author dhtmd
 *
 */


public class HadoopMain2 {
	public static void main(String[] args) throws Exception {
		/**
		 * Data Example
		 * "사용월","호선명","지하철역","04시-05시 승차인원","04시-05시 하차인원","05시-06시 승차인원","05시-06시 하차인원","06시-07시 승차인원","06시-07시 하차인원","07시-08시 승차인원","07시-08시 하차인원","08시-09시 승차인원","08시-09시 하차인원","09시-10시 승차인원","09시-10시 하차인원","10시-11시 승차인원","10시-11시 하차인원","11시-12시 승차인원","11시-12시 하차인원","12시-13시 승차인원","12시-13시 하차인원","13시-14시 승차인원","13시-14시 하차인원","14시-15시 승차인원","14시-15시 하차인원","15시-16시 승차인원","15시-16시 하차인원","16시-17시 승차인원","16시-17시 하차인원","17시-18시 승차인원","17시-18시 하차인원","18시-19시 승차인원","18시-19시 하차인원","19시-20시 승차인원","19시-20시 하차인원","20시-21시 승차인원","20시-21시 하차인원","21시-22시 승차인원","21시-22시 하차인원","22시-23시 승차인원","22시-23시 하차인원","23시-24시 승차인원","23시-24시 하차인원","00시-01시 승차인원","00시-01시 하차인원","01시-02시 승차인원","01시-02시 하차인원","02시-03시 승차인원","02시-03시 하차인원","03시-04시 승차인원","03시-04시 하차인원","작업일자"
			"202012","1호선","동대문","692","9","12587","1683","7606","5503","11463","8489","15440","18762","14573","18262","12377","15779","13731","16674","16286","17130","17284","17696","19285","16794","19737","16191","21224","14698","20498","14570","18547","15075","12425","15599","10478","10407","8099","9657","4188","6275","1413","6199","7","960","1","0","0","0","0","0","20210103"

		 * @author dhtmd
		 *
		 */
		String fileName = "train20210125.csv";
		String in = "infile/"+fileName;
		String out = "outfile/"+fileName + "another1";
		Configuration conf = new Configuration();
		// job 이름 설정
		@SuppressWarnings("deprecation")
		Job job = new Job(new Configuration(), "HadoopMain2");

		// 입출력 데이터 경로 설정
		FileInputFormat.addInputPath(job, new Path(in));
		FileOutputFormat.setOutputPath(job, new Path(out));

		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(new Path(out))) {
			hdfs.delete(new Path(out), true);// 출력파일의 경로 존재하는 경우 삭제
			System.out.println("[System log] 기존 출력파일 삭제");
		}
		// Job 클래스 설정
		job.setJarByClass(HadoopMain2.class);

		// Mapper 클래스 설정
		job.setMapperClass(HadoopMain2Mapper.class);

		// Reducer 클래스 설정
		job.setReducerClass(HadoopMain2Reducer.class);

		// 입출력 데이터 포멧 설정
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// 출력키 및 출력값 유형 설정
		job.setOutputKeyClass(Text.class);// 문자열 기반이라서, 결과값일때 1988.2 보다 1988.10이 더 앞섦
		job.setOutputValueClass(IntWritable.class);

		// multipleOutputs 설정
		MultipleOutputs.addNamedOutput(job, "ON", TextOutputFormat.class, Text.class, IntWritable.class);
		MultipleOutputs.addNamedOutput(job, "OUT", TextOutputFormat.class, Text.class, IntWritable.class);
		job.waitForCompletion(true);

	}
}
