package vision.stream;

import vision.exception.AbsentFrameException;

public interface VideoStream {

	VideoFrame nextFrame() throws AbsentFrameException;

}