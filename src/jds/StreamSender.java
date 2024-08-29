package jds;

import jds.videostream.VideoStream;

public interface StreamSender {
	
	void sendRequest(String message);
	
	void sendStream(VideoStream stream);

}
