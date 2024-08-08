package vision.display.video_interface;

import java.util.Objects;

public final class VideoInterface {
	
	private final String name;
	private final String version;
	private final double transmissionRate;
	
	public VideoInterface(String name, String version, double transmissionRate) {
		this.name = Objects.requireNonNull(name, "Null name argument");
		this.version = Objects.requireNonNull(version, "Null version argument");
		if(transmissionRate<0)
			throw new IllegalArgumentException("Negative transmissionRate argument");
		this.transmissionRate = transmissionRate;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public double getTransmissionRate() {
		return transmissionRate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof VideoInterface)) return false;
		VideoInterface video = (VideoInterface) obj;
		return Double.compare(transmissionRate, video.transmissionRate) == 0 &&
				Objects.equals(this.name, video.name) &&
				Objects.equals(this.version, video.version);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, version, transmissionRate);
	}
	
}
