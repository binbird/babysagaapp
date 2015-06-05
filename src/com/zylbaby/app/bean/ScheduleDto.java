package com.zylbaby.app.bean;

public class ScheduleDto {
	private int id;
	private String entrytime;
	private String scheduletypename;
	private String scheduletitle;
	private String schedulecontext;
	private String scheduletypeid;
	private int ispublish;
	private String starttime;
	private String endtime;
	private String day;
	public String getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}
	public String getScheduletitle() {
		return scheduletitle;
	}
	public void setScheduletitle(String scheduletitle) {
		this.scheduletitle = scheduletitle;
	}
	public String getScheduletypeid() {
		return scheduletypeid;
	}
	public void setScheduletypeid(String scheduletypeid) {
		this.scheduletypeid = scheduletypeid;
	}
	public int getIspublish() {
		return ispublish;
	}
	public void setIspublish(int ispublish) {
		this.ispublish = ispublish;
	}
	public String getSchedulecontext() {
		return schedulecontext;
	}
	public void setSchedulecontext(String schedulecontext) {
		this.schedulecontext = schedulecontext;
	}
	public String getScheduletypename() {
		return scheduletypename;
	}
	public void setScheduletypename(String scheduletypename) {
		this.scheduletypename = scheduletypename;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

}
