package org.jaylee.domain;

public class Url {
	private String originURL;
	private String shortURL;
	
	public Url() {}
	
	public Url(String originURL, String shortURL) {
		this.originURL = originURL;
		this.shortURL = shortURL;
	}
	
	public String getOriginURL() {
		return this.originURL;
	}
	
	public String getShortURL() {
		return this.shortURL;
	}
	
	public void setOriginURL(String originURL) {
		this.originURL = originURL;
	}
	
	public void setShortURL(String shortURL) {
		this.shortURL = shortURL;
	}
	
	@Override
	public String toString() {
		return "Url {originURL=" + this.originURL + ", shortURL=" + this.shortURL + "}";
	}
}
