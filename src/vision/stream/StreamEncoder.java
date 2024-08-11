package vision.stream;

public interface StreamEncoder {
	
	void addStream(EncodedStream stream, int xPosition, int yPosition);

	EncodedStream encodedComposition();
	
	EncodedStream encode(StreamType stream);

}
