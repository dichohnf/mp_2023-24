package vision;

public interface ComunicationChannel {
	
	void sendRequest(String message);
	
	void sendStream(StreamType stream);
	
	StreamType streamRecive();
}
