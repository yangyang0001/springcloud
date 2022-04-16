package com.deepblue.inaction_01_kafka.producer;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class GuPiao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3515896825765925592L;
	
	private String guPiaoName;
	
	private int guPiaoPrice;
	
	private String guPiaoDesc;
	
	private long currentTime;
	
	private String guPiaoNickName;

	public String getGuPiaoName() {
		return guPiaoName;
	}

	public void setGuPiaoName(String guPiaoName) {
		this.guPiaoName = guPiaoName;
	}

	public int getGuPiaoPrice() {
		return guPiaoPrice;
	}

	public void setGuPiaoPrice(int guPiaoPrice) {
		this.guPiaoPrice = guPiaoPrice;
	}

	public String getGuPiaoDesc() {
		return guPiaoDesc;
	}

	public void setGuPiaoDesc(String guPiaoDesc) {
		this.guPiaoDesc = guPiaoDesc;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public String getGuPiaoNickName() {
		return guPiaoNickName;
	}

	public void setGuPiaoNickName(String guPiaoNickName) {
		this.guPiaoNickName = guPiaoNickName;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	

}
