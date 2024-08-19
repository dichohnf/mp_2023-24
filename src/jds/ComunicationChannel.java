package jds;

import jds.videostream.VideoStream;

public interface ComunicationChannel {
	
	void sendRequest(String message);
	
	void sendStream(VideoStream stream);
	
	VideoStream streamRecive();
}
