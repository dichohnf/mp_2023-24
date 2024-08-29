package jds;

import java.util.ArrayList;
import java.util.List;

import jds.videostream.VideoStream;

public class MockStreamSender implements StreamSender {

	public List<String> requests;
	public List<VideoStream> sentStreams;
	
	public MockStreamSender() {
		requests = new ArrayList<>();
		sentStreams = new ArrayList<>();
	}
	
	@Override
	public void sendRequest(String message) {
		requests.add(message);
	}

	@Override
	public void sendStream(VideoStream stream) {
		sentStreams.add(stream);
	}

}
