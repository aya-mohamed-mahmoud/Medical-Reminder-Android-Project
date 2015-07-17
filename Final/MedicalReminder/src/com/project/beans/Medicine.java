package com.project.beans;

import java.io.Serializable;

public class Medicine implements Serializable {

	private int id;
	private String name;
	private double dose;
	private String type;
	private int frecuency;
	private String interval;
	private long startTime;
	private long startDate;
	private long endDate;
	private String image;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Medicine() {
		super();
	}

	public Medicine(String name, double dose, String type, int frecuency,
			String interval, long startTime, long startDate, long endDate,
			String image) {
		super();
		this.name = name;
		this.dose = dose;
		this.type = type;
		this.frecuency = frecuency;
		this.interval = interval;
		this.startTime = startTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.image = image;
	}

	public Medicine(int id, String name, double dose, String type, int frecuency,
			String interval, long startTime, long startDate, long endDate,
			String image) {
		super();
		this.id = id;
		this.name = name;
		this.dose = dose;
		this.type = type;
		this.frecuency = frecuency;
		this.interval = interval;
		this.startTime = startTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDose() {
		return dose;
	}

	public void setDose(double dose) {
		this.dose = dose;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFrecuency() {
		return frecuency;
	}

	public void setFrecuency(int frecuency) {
		this.frecuency = frecuency;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
