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
	
	protected final void setBottomLeftPosition(Position bottomLeft) {
		this.bottomLeft = Objects.requireNonNull(
				bottomLeft, 
				"Null bottomLeft argument");
	}

	protected final void setTopRightPosition(Position topRight) {
		this.topRight = Objects.requireNonNull(
				topRight, 
				"Null topRight argument");
	}

	public abstract byte[] getData();
	
}
