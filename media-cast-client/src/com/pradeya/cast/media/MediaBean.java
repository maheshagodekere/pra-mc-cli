package com.pradeya.cast.media;

public class MediaBean {
	private String id;
	private String mediaType;
	private String mediaLocalPath;
	private String mediaFileName;
	private String mediaFileNameWithoutExt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getMediaLocalPath() {
		return mediaLocalPath;
	}
	public void setMediaLocalPath(String mediaLocalPath) {
		this.mediaLocalPath = mediaLocalPath;
	}
	public String getMediaFileName() {
		return mediaFileName;
	}
	public void setMediaFileName(String mediaFileName) {
		this.mediaFileName = mediaFileName;
	}
	public String getMediaFileNameWithoutExt() {
		return mediaFileNameWithoutExt;
	}
	public void setMediaFileNameWithoutExt(String mediaFileNameWithoutExt) {
		this.mediaFileNameWithoutExt = mediaFileNameWithoutExt;
	}

}
