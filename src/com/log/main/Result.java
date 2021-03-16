package com.log.main;

public class Result {
	
	private String name;
	private int numSession;
	private long duration;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumSession() {
		return numSession;
	}
	public void setNumSession(int numSession) {
		this.numSession = numSession;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long difference_In_Seconds) {
		this.duration = difference_In_Seconds;
	}
	@Override
	public String toString() {
		return "Result [name=" + name + ", numSession=" + numSession + ", duration=" + duration + "]";
	}
	

}
