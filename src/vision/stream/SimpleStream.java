package vision.stream;

import java.util.Objects;

public final class SimpleStream implements StreamType {
	
	private final Double ratio;
	
	public SimpleStream(Double ratio) {
		this.ratio = Objects.requireNonNull(ratio, "Null ratio argument");
	}
	
	public Double getRatio() {
		return ratio;
	}
	
	public EncodedStream getEncoded(StreamEncoder encoder) {
		return encoder.encode(this);
	}

}
