package com.log.main;

import java.util.Date;

public class Details {

	private Date time;
	private String name;
	private String status;
	 private int loc;
	 private int numberOfSession;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public int getNumberOfSession() {
		return numberOfSession;
	}
	public void setNumberOfSession(int numberOfSession) {
		this.numberOfSession = numberOfSession;
	}
	@Override
	public String toString() {
		return "Details [time=" + time + ", name=" + name + ", status=" + status + ", loc=" + loc + ", numberOfSession="
				+ numberOfSession + "]";
	}
	
	
	
}
