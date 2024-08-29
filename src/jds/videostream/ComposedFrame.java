package jds.videostream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class ComposedFrame extends VideoFrame {

	private final Collection<VideoFrame> children;
	private final FrameCompositor compositor;
	private static final Position MIN_POSITION = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
	private static final Position MAX_POSITION = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public ComposedFrame(FrameCompositor compositor) {
		super(MIN_POSITION, MAX_POSITION);
		this.compositor = Objects.requireNonNull(
				compositor, 
				"Null compositor argument");
		children = new ArrayList<>();
	}
	
	@Override
	public byte[] getData() {
		return compositor.composeData(children);
	}
	
	public boolean add(VideoFrame child) {
		if(this == child)
			throw new IllegalArgumentException("Child to insert is this frame object");
		boolean tmp = children.add(child);
		calculationRelevantPositions();
		return tmp;
	}
	
	public boolean remove(VideoFrame child) {
		boolean tmp = children.remove(child);
		calculationRelevantPositions();
		return tmp;
	}
	
	// package-private to test add() and remove() methods
	void calculationRelevantPositions() {
		setBottomLeftPosition(children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::bottomLeftIntersection)
				.orElse(MIN_POSITION));
		setTopRightPosition(children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::topRightIntersection)
				.orElse(MAX_POSITION));
	}
	
	// Only for test
	Collection<VideoFrame> getChildren() {
		return children;
	}

}
