package jds;

import java.util.List;

import jds.videostream.ComposedFrame;
import jds.videostream.MockFrameCompositor;
import jds.videostream.VideoFrame;
import jds.videostream.VideoStream;

public class MockStreamReciver implements StreamReciver {

	public VideoStream recivedStream;
	
	public MockStreamReciver() {
		recivedStream = new VideoStream(
				List.of(
						(VideoFrame) new ComposedFrame(new MockFrameCompositor()),
						new ComposedFrame(new MockFrameCompositor()))
				.iterator());
	}
		
	@Override
	public VideoStream streamRecive() {
		return recivedStream;
	}

}
