package vision.stream;

import java.util.Collection;
import java.util.Objects;

public final class VideoFrame {
	
	private final Position origin;
	private final Position oppositeToOrigin;
	private final int frameNumber;
	private final Collection<Byte> data;
	
	public VideoFrame(int frameNumber, Collection<Byte> data) {
		if(frameNumber < 0 )
			throw new IllegalArgumentException("Negative frameNumber argument");
		this.frameNumber = frameNumber;
		this.data = Objects.requireNonNull(
				data, 
				"Null data argument");
	}

	public int getFrameNumber() {
		return frameNumber;
	}

	public Collection<Byte> getData() {
		return data;
	}
	
}
