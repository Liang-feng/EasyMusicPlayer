package com.example.easymusicplayer1.model;

/**
 * 通过此类来把数据存储到数据库中，或者从中提取出数据赖!!!
 * @author feng
 *
 */

public class MusicTop { 

	private int albumId;

	private String downUrl;

	private int seconds;

	private int singerId;

	private String singerName;

	private int songId;

	private String songName;

	private String url;

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getDownUrl() {
		return this.downUrl;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getSeconds() {
		return this.seconds;
	}

	public void setSingerId(int singerId) {
		this.singerId = singerId;
	}

	public int getSingerId() {
		return this.singerId;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getSingerName() {
		return this.singerName;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public int getSongId() {
		return this.songId;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongName() {
		return this.songName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

}
