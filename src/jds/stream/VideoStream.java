package jds.stream;

import java.util.Iterator;
import java.util.Objects;

import jds.exception.AbsentFrameException;

public final class VideoStream {
	
	private final Iterator<VideoFrame> frames;
	
	public VideoStream(Iterator<VideoFrame> frames) {
		this.frames = Objects.requireNonNull(
				frames,
				"Null frames argument");
	}
	
	public VideoFrame nextFrame() {
		if(!frames.hasNext())
			throw new AbsentFrameException("No frame found");
		return frames.next();
	}
	
}
