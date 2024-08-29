package jds.display.interfaces;

import java.util.Objects;

import jds.StreamReciver;
import jds.videostream.VideoStream;

public final class VideoInterface {
	
	private final String name;
	private final String version;
	private final StreamReciver reciver;
	
	public VideoInterface(String name, String version, StreamReciver reciver) {
		this.name = Objects.requireNonNull(
				name, 
				"Null name argument");
		this.version = Objects.requireNonNull(
				version, 
				"Null version argument");
		this.reciver = Objects.requireNonNull(
				reciver, 
				"Null reciver argument");
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof VideoInterface)) return false;
		VideoInterface video = (VideoInterface) other;
		return Objects.equals(this.name, video.name) &&
				Objects.equals(this.version, video.version);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}
	
	@Override
	public String toString() {
		return super.toString() + "-" + name + ":" + version;
	}
	
	public VideoStream getStream(){
		return reciver.streamRecive();
	}
	
}
