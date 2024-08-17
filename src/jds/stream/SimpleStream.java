package jds.stream;

import java.util.Iterator;
import java.util.Objects;

import jds.exception.AbsentFrameException;

public final class SimpleStream implements VideoStream {
	
	private final Iterator<VideoFrame> frames;
	
	public SimpleStream(Iterator<VideoFrame> frames) {
		this.frames = Objects.requireNonNull(
				frames,
				"Null frames argument");
	}
	
	@Override
	public VideoFrame nextFrame() {
		if(!frames.hasNext())
			throw new AbsentFrameException("No frame found");
		return frames.next();
	}
	
}
