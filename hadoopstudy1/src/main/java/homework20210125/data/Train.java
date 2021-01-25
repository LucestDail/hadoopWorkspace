package homework20210125.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;

public class Train {
	private int date;
	private String lane;
	private String station;
	private String[] timezone =
		{"04-05","05-06","06-07","07-08"
		,"08-09","09-10","10-11","11-12"
		,"12-13","13-14","14-15","15-16"
		,"16-17","17-18","18-19","19-20"
		,"20-21","21-22","22-23","23-24"
		,"00-01","01-02","02-03","03-04"};
	private List<Integer> onboard = new ArrayList<>();
	private List<Integer> outboard = new ArrayList<>();
	
	public Train(Text text) {
		try {
			String targetString = text.toString().replace("\"","");
			String[] columns = targetString.toString().split(",");
			date = Integer.parseInt(columns[0]);
			lane = columns[1];
			station = columns[2];
			for(int i = 3; i < columns.length; i++) {
				if(i == columns.length-1) {
					break;
				}
				if(i%2 == 0) {
					outboard.add(Integer.parseInt(columns[i]));
				}else {
					onboard.add(Integer.parseInt(columns[i]));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getDate() {
		return date;
	}

	public String getLane() {
		return lane;
	}

	public String getStation() {
		return station;
	}

	public String[] getTimezone() {
		return timezone;
	}

	public List<Integer> getOnboard() {
		return onboard;
	}

	public List<Integer> getOutboard() {
		return outboard;
	}
	




}
