package jds.stream;

public final class MockVideoFrame implements VideoFrame {
	
	Position origin;
	
	public MockVideoFrame(Position origin) {
		this.origin = origin;
	}
	
	@Override
	public Position getBottomLeftPosition() {
		return origin;
	}

	@Override
	public VideoFrame movedTo(Position newBottomLeftPosition) {
		return new MockVideoFrame(newBottomLeftPosition);
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
