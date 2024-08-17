package vision.stream;

public interface FrameCompositor {

	void put(VideoFrame frame, Position origin);
	
	VideoFrame compose();
	
}
