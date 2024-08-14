package vision.display;

import vision.stream.VideoStream;

public interface ResolutionCompressor {
	
	public VideoStream compress(VideoStream stream);

}
