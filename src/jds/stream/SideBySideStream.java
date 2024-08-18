package jds.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class SideBySideStream implements VideoStream {
	
	final Collection<VideoStream> childrens;
	private final FrameCompositor compositor;

	public SideBySideStream(FrameCompositor compositor) {
		this.compositor = Objects.requireNonNull(
				compositor, 
				"Null compositor argument");
		childrens = new ArrayList<>();
	}
	
	public boolean add(VideoStream child) {
		if(this == child)
			throw new IllegalArgumentException("Child to insert is this stream object");
		return childrens.add(child);
	}
	
	public boolean remove(VideoStream child) {
		return childrens.remove(child);
	}

	@Override
	public VideoFrame nextFrame() {
		childrens.stream()
			.map(VideoStream::nextFrame)
			.forEach(
					frame -> compositor.put(frame, frame.getBottomLeftPosition()));
		return compositor.compose();
	}
	
}
