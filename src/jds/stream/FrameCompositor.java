package jds.stream;

import java.util.Collection;

public interface FrameCompositor {
	
	VideoFrame compose(Collection<VideoFrame> frames);
	
}
