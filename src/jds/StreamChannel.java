package jds;

import jds.videostream.VideoStream;

public interface StreamChannel {
	
	void sendRequest(String message);
	
	void sendStream(VideoStream stream);
	
	VideoStream streamRecive();
}
