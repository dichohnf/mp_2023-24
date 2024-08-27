package jds;

import java.util.ArrayList;
import java.util.List;

import jds.videostream.ComposedFrame;
import jds.videostream.MockFrameCompositor;
import jds.videostream.VideoFrame;
import jds.videostream.VideoStream;

public class MockStreamChannel implements StreamChannel {

	public List<String> requests;
	public List<VideoStream> sentStreams;
	public VideoStream recivedStream;
	
	public MockStreamChannel() {
		requests = new ArrayList<>();
		sentStreams = new ArrayList<>();
		recivedStream = new VideoStream(List.of(
				(VideoFrame) new ComposedFrame(new MockFrameCompositor()),
				new ComposedFrame(new MockFrameCompositor()))
			.iterator());
	}
	
	@Override
	public void sendRequest(String message) {
		requests.add(message);
	}

	@Override
	public void sendStream(VideoStream stream) {
		sentStreams.add(stream);
	}

	@Override
	public VideoStream streamRecive() {
		return recivedStream;
	}

}
