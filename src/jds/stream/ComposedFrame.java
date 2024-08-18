package jds.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class ComposedFrame implements VideoFrame {

	final Collection<VideoFrame> children;
	private final FrameCompositor compositor;
	private Position bottomLeft;
	private Position topRight;
	
	public ComposedFrame(FrameCompositor compositor) {
		this.compositor = Objects.requireNonNull(
				compositor, 
				"Null compositor argument");
		children = new ArrayList<>();
	}
	
	@Override
	public Position getBottomLeftPosition() {
		return bottomLeft;
	}

	@Override
	public Position getTopRightPosition() {
		return topRight; 
	}

	@Override
	public void moveTo(Position newBottomLeftPosition) {
		children.forEach(frame -> moveTo(newBottomLeftPosition));
		calculationRelevantPositions();
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

	@Override
	public byte[] getData() {
		children.stream()
		.forEach(compositor::put);
	return compositor.compose().getData();
	}
	
	private void calculationRelevantPositions() {
		bottomLeft = children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::bottomLeftIntersection)
				.orElseThrow();
		topRight = children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::topRightIntersection)
				.orElseThrow();
	}

}
