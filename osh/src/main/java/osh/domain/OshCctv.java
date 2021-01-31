package osh.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;

public class OshCctv {
	private String officeName; // 관리지역 0
	private String reason; //설치 목적 3
	private int num; //카메라 갯수4
	private String year; //설치 년도 8
	public OshCctv(Text text) {
		try {
			String[] columns = text.toString().split(",");
			officeName = columns[0].split("\t")[0].trim();
			reason = columns[3];
			if(columns[4] == null || !StringUtils.isNumeric(columns[4])){
				num = 0;
			}else {
				num = Integer.parseInt(columns[4]);
			}
			year = columns[8];
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String getOfficeName() {
		return officeName;
	}
	public String getReason() {
		return reason;
	}
	public int getNum() {
		return num;
	}
	public String getYear() {
		return year;
	}
	@Override
	public String toString() {
		return "OshCctv [officeName=" + officeName + ", reason=" + reason + ", num=" + num + ", year=" + year + "]";
	}
	
}
