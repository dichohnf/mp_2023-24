package jds.videostream;

import java.util.Collection;

public interface FrameCompositor {
	
	byte[] composeData(Collection<VideoFrame> frames);
	
}
