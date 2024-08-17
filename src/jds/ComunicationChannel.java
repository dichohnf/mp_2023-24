package jds;

import jds.stream.VideoStream;

public interface ComunicationChannel {
	
	void sendRequest(String message);
	
	void sendStream(VideoStream stream);
	
	VideoStream streamRecive();
}
