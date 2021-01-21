package hdfs;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCountEx1 {
	public static void main(String[] args) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException{
		//jobclient jar 사용해서 내부 로그 확인 가능
		Configuration conf = new Configuration(); //하둡의 환경 설정
		String in = "infile/in.txt";String out = "outfile/wordcnt"; //하둡 서버에 저장되는 위치, 블럭화하여 저장
		Job job = new Job(conf,"WordCountEx1");//작업객체
		job.setJarByClass(WordCountEx1.class);//작업객체의 자료형 설정
		//맵리듀스 : 맵퍼 + 리듀서
		job.setMapperClass(WordCountMapper.class);//맵퍼 클래스 설정
		job.setReducerClass(WordCountReducer.class);//리듀서 클래스 설정
		job.setInputFormatClass(TextInputFormat.class);//입력데이터 자료형 설정
		job.setOutputFormatClass(TextOutputFormat.class);//출력 데이터 자료형 설정
		job.setMapOutputKeyClass(Text.class);//키데이터 자료형 설정
		job.setMapOutputValueClass(IntWritable.class);//value 데이터 자료형 설정
		FileInputFormat.addInputPath(job, new Path(in));//입력데이터의 저장파일 설정
		FileOutputFormat.setOutputPath(job, new Path(out));//출력데이터 저장파일의 위치 설정
		job.waitForCompletion(true);//메인은 작업 설정만 해주고 여기에서 작업을 시작합니다.
	}

}
