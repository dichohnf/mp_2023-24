package jds.stream;

public interface FrameCompositor {

	void put(VideoFrame frame);
	
	VideoFrame compose();
	
}
