package vision;

import vision.stream.VideoStream;

public interface ComunicationChannel {
	
	void sendRequest(String message);
	
	void sendStream(VideoStream stream);
	
	VideoStream streamRecive();
}
