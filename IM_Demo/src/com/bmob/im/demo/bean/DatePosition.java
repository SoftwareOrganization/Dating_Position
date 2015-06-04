package com.bmob.im.demo.bean;



import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

public class DatePosition extends BmobObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User creator;
	private BmobGeoPoint location;
	private String dateName;
	private String dateDesc;
	private BmobDate dateTime;
	private BmobRelation datemember;
	private  String creatorObjectId;
	private String creatorAvatar;
	private String creatorUsername;
	private String datePosition;
	public DatePosition() {
		super();
	}
	
	
	
	public String getDatePosition() {
		return datePosition;
	}



	public void setDatePosition(String datePosition) {
		this.datePosition = datePosition;
	}



	public String getCreatorUsername() {
		return creatorUsername;
	}



	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}



	public String getCreatorAvatar() {
		return creatorAvatar;
	}



	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}



	public String getCreatorObjectId() {
		return creatorObjectId;
	}

	public void setCreatorObjectId(String creatorObjectId) {
		this.creatorObjectId = creatorObjectId;
	}

	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	public String getDateName() {
		return dateName;
	}
	public void setDateName(String dateName) {
		this.dateName = dateName;
	}
	public String getDateDesc() {
		return dateDesc;
	}
	public void setDateDesc(String dateDesc) {
		this.dateDesc = dateDesc;
	}
	public BmobDate getDateTime() {
		return dateTime;
	}
	public void setDateTime(BmobDate dateTime) {
		this.dateTime = dateTime;
	}
	public BmobRelation getDatemember() {
		return datemember;
	}
	public void setDatemember(BmobRelation datemember) {
		this.datemember = datemember;
	}
	
	

}
