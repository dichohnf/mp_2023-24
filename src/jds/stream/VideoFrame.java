package jds.stream;

public interface VideoFrame {
	
	public Position getBottomLeftPosition();
	
	public Position getTopRightPosition();
	
	void moveTo(Position newBottomLeftPosition);

	byte[] getData();
	
}
