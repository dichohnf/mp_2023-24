package vision.stream;

public interface EncodedStream {
		
	EncodedStream compose(EncodedStream other, int xPosition, int yPosition);

}