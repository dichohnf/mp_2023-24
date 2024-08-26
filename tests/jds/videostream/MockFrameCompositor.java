package jds.videostream;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.stream.IntStream;

public final class MockFrameCompositor implements FrameCompositor {

	@Override
	public byte[] composeData(Collection<VideoFrame> frames) {
	    return frames.stream()
                .map(VideoFrame::getData)
                .flatMapToInt(arr -> IntStream.range(0, arr.length).map(i -> arr[i]))
                .collect(ByteArrayOutputStream::new,
                         ByteArrayOutputStream::write,
                         (arr1, arr2) -> arr1.write(arr2.toByteArray(), 0, arr2.size()))
                .toByteArray();
	}

}
