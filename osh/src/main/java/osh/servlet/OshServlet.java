package osh.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileAlreadyExistsException;
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

import osh.mapper.OshMapper;
import osh.reducer.OshReducer;
/**
 * mgrgov : 관리지역
 * reason : 설치목적
 * year : 설치년도
 * @author dhtmd
 *
 */
@WebServlet("/OshServlet") //web.xml 의 servlet 태그 대체
public class OshServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public OshServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String kbn = request.getParameter("kbn");
		String input = "C:/Users/dhtmd/myworkspace/hadoopWorkspace/osh/oshfile/cctv.csv";
		String output = "C:/Users/dhtmd/myworkspace/hadoopWorkspace/osh/oshresult/"+kbn;
		Configuration conf = new Configuration();
		conf.set("workType", kbn);
		try {
			System.out.println("into try");
			
			Job job = new Job(conf,"OshServlet");
			
			FileInputFormat.addInputPath(job, new Path(input));
			FileOutputFormat.setOutputPath(job, new Path(output));
			
			job.setJarByClass(this.getClass());
			
			job.setMapperClass(OshMapper.class);
			job.setReducerClass(OshReducer.class);
			
			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			MultipleOutputs.addNamedOutput(job, "mgrgov", TextOutputFormat.class, Text.class, IntWritable.class);
			MultipleOutputs.addNamedOutput(job, "reason", TextOutputFormat.class, Text.class, IntWritable.class);
			MultipleOutputs.addNamedOutput(job, "year", TextOutputFormat.class, Text.class, IntWritable.class);
			
			job.waitForCompletion(true);
			
			System.out.println("end try");
			
		} catch(FileAlreadyExistsException e) {
			
			System.out.println("기존 파일 존재 : " + output);
			
			e.printStackTrace();
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		String afile = "mgrgov-r-00000";
		String bfile = "reason-r-00000";
		String cfile = "year-r-00000";
		String target = kbn.equals("mgrgov")? afile: kbn.equals("reason")?bfile:cfile; 
		Path outFile = new Path(output + "/" + target);
		FileSystem fs = FileSystem.get(conf);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(outFile),"UTF-8"));
		Map<String,Integer> map = new TreeMap<String,Integer>();
		String line = null;
		while((line = br.readLine()) != null) {
			String[] v = line.split("\t");
			int resultValue = Integer.parseInt(v[1].trim());
			if(resultValue < 1000) {
				continue;
			}else {
				map.put(v[0].trim(), Integer.parseInt(v[1].trim()));
			}
		}
		System.out.println(map);
		request.setAttribute("map", map);
		String jsp = "/cctv/osh.jsp";
		RequestDispatcher v = request.getRequestDispatcher(jsp);
		v.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
