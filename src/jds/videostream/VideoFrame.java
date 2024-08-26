package jds.videostream;

import java.util.Objects;

public abstract class VideoFrame {

	private Position bottomLeft;
	private Position topRight;
	
	protected VideoFrame(Position bottomLeft, Position topRight) {
		setBottomLeftPosition(bottomLeft);
		setTopRightPosition(topRight);
	}

	public final Position getBottomLeftPosition() {
		return bottomLeft;
	}
	
	public final Position getTopRightPosition() {
		return topRight;
	}
	
	protected final void setBottomLeftPosition(Position position) {
		this.bottomLeft = Objects.requireNonNull(
				position, 
				"Null bottomLeft argument");
	}

	protected final void setTopRightPosition(Position position) {
		this.topRight = Objects.requireNonNull(
				position, 
				"Null topRight argument");
	}

	public abstract byte[] getData();
	
}
