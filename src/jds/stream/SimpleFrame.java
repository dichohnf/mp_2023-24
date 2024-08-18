package jds.stream;

import java.util.Objects;

public final class SimpleFrame implements VideoFrame {
	
	private Position bottomLeft;
	private Position topRight;
	private final byte[] data;
	
	public SimpleFrame(Position bottomLeft, Position topRight, byte[] data) {
		this.bottomLeft = Objects.requireNonNull(
				bottomLeft, 
				"Null origin argument");
		this.topRight =  Objects.requireNonNull(
				topRight,
				"Null opposite to origin position argument");
		this.data = Objects.requireNonNull(
				data, 
				"Null data argument");
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
	public byte[] getData(){
		return data;
	}

	@Override
	public void moveTo(Position newBottomLeftPosition) {
		topRight = new Position(
				newBottomLeftPosition.getX() + topRight.getX() - bottomLeft.getX(), 
				newBottomLeftPosition.getY() + topRight.getY() - bottomLeft.getY());
		bottomLeft = newBottomLeftPosition;
	}

}
