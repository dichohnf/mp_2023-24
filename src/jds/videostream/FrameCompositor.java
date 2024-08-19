package jds.videostream;

import java.util.Collection;

public interface FrameCompositor {
	
	byte[] composedData(Collection<VideoFrame> frames);
	
}
