package vision.stream;

import java.util.Iterator;
import java.util.Objects;

import vision.exception.AbsentFrameException;

public final class SimpleStream implements VideoStream {
	
	private final Double ratio;
	private final Iterator<VideoFrame> frames;
	
	public SimpleStream(Double ratio, Iterator<VideoFrame> frames) {
		this.ratio = Objects.requireNonNull(ratio, "Null ratio argument");
		this.frames = Objects.requireNonNull(frames, "Null frames argument");
	}
	
	public Double getRatio() {
		return ratio;
	}
	
	@Override
	public VideoFrame nextFrame() throws AbsentFrameException {
		if(!frames.hasNext())
			throw new AbsentFrameException("No frame found");
		return frames.next();
	}
	
}
