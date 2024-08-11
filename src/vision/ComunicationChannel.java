package vision;

import vision.stream.StreamType;

public interface ComunicationChannel {
	
	void sendRequest(String message);
	
	void sendStream(StreamType stream);
	
	StreamType streamRecive();
}
