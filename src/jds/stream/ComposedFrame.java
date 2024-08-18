package jds.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class ComposedFrame extends VideoFrame {

	final Collection<VideoFrame> children;
	private final FrameCompositor compositor;
	
	public ComposedFrame(FrameCompositor compositor) {
		super(new Position(
				Integer.MIN_VALUE, 
				Integer.MIN_VALUE), 
			new Position(
					Integer.MAX_VALUE, 
					Integer.MAX_VALUE));
		this.compositor = Objects.requireNonNull(
				compositor, 
				"Null compositor argument");
		children = new ArrayList<>();
		/*
		 * TODO: modificare costruttore con posizioni fittizie o creare una posizione
		 * finta, oppure utilizzare un optional o far gestire i campi alle sottoclassi??
		 */
	}
	
	@Override
	public byte[] getData() {
		return compositor.compose(children).getData();
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
	
	private void calculationRelevantPositions() {
		setBottomLeftPosition(children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::bottomLeftIntersection)
				.orElseThrow());
		setTopRightPosition(children.stream()
				.map(VideoFrame::getBottomLeftPosition)
				.reduce(Position::topRightIntersection)
				.orElseThrow());
	}

}
