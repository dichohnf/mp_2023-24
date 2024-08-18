package jds.stream;

import java.util.ArrayList;
import java.util.List;

public final class MockFrameCompositor implements FrameCompositor {
	
	List<VideoFrame> frames;
	
	public MockFrameCompositor() {
		frames = new ArrayList<>();
	}

	@Override
	public void put(VideoFrame frame, Position origin) {
		frames.add(frame.movedTo(origin));
	}

	@Override
	public VideoFrame compose() {
		return new VideoFrame(frames);
	}

}
