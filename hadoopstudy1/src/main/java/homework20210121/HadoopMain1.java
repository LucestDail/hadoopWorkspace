package homework20210121;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import wordcount.WordCountTest2;

public class HadoopMain1 {
	public static void main(String[] args) throws IOException {
		JobConf conf = new JobConf(WordCountTest2.class); // 작업 + 환경 설정
		conf.setJobName("wordcount2");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class); // 맵퍼 설정
		conf.setCombinerClass(Reduce.class); // 컴바이너 설정 : 맵퍼와 리듀서 사이 역할, 각각의 맵퍼 결과를 리듀스하여 성능 향상을 도모, 사용해도 되고 안해도 됨
		conf.setReducerClass(Reduce.class); // 리듀서 설정
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		//여러개의 입력파일 설정
		FileInputFormat.setInputPaths(conf, new Path("infile/scorein.txt"));
		//출력 파일 설정
		FileOutputFormat.setOutputPath(conf, new Path("outfile/scoreout"));
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(new Path("outfile/scoreout"))) {
			hdfs.delete(new Path("outfile/scoreout"), true);//출력파일의 경로 존재하는 경우 삭제
			System.out.println("기존 출력파일 삭제");
		}
		JobClient.runJob(conf);
	}

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			StringTokenizer itr = new StringTokenizer(line);
			while (itr.hasMoreElements()) {
				
				word.set(itr.nextToken());
				
				String nameFromWord = word.toString().split(",")[0];
				int scoreFromWord = Integer.parseInt(word.toString().split(",")[1]);
				
				Text nameValue = new Text();
				IntWritable scoreValue = new IntWritable(scoreFromWord);
				
				nameValue.set(nameFromWord);
				output.collect(nameValue, scoreValue);
			}
		}
	} // map 내부 클래스 종료

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output,
				Reporter reporter) throws IOException {
			// TODO Auto-generated method stub
			int sum = 0;
			while(values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		} //Reduce 내부 클래스 종료
	}
}
