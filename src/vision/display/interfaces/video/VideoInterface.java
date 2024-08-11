package vision.display.interfaces.video;

import java.util.Objects;

import vision.ComunicationChannel;
import vision.StreamType;

public final class VideoInterface {
	
	private final String name;
	private final String version;
	private final ComunicationChannel channel;
	
	public VideoInterface(String name, String version, ComunicationChannel channel) {
		this.name = Objects.requireNonNull(name, "Null name argument");
		this.version = Objects.requireNonNull(version, "Null version argument");
		this.channel = Objects.requireNonNull(channel, "Null channel argument");
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof VideoInterface)) return false;
		VideoInterface video = (VideoInterface) obj;
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
	
	public StreamType getStream(){
		return channel.streamRecive();
	}
	
}
