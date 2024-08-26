package jds.videostream;

import java.util.Objects;

public final class SimpleFrame extends VideoFrame {
	
	private final byte[] data;
	
	public SimpleFrame(Position bottomLeft, Position topRight, byte[] data) {
		super(bottomLeft, topRight);
		this.data = Objects.requireNonNull(
				data, 
				"Null data argument");
	}

	@Override
	public byte[] getData(){
		return data;
	}

}
