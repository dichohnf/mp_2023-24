package jds.videostream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.exception.AbsentFrameException;

public class VideoStreamTest {
	
	Position bottomLeft1;
	Position bottomLeft2;
	Position topRight1;
	Position topRight2;
	byte[] data1;
	byte[] data2;
	VideoFrame simple1;
	VideoFrame simple2;
	ComposedFrame composed;
	VideoStream stream;

	@Before
	public void setUp() {
		bottomLeft1	= new Position(0,0);
		bottomLeft2	= new Position(1,2);
		topRight1	= new Position(1366, 768);
		topRight2	= new Position(1567, 1314);
		data1 		= "Bytes string.".getBytes();
		data2 		= "Bytes string copy.".getBytes();
		simple1 	= new SimpleFrame(bottomLeft1, topRight1, data1);
		simple2 	= new SimpleFrame(bottomLeft2, topRight2, data2);
		composed 	= new ComposedFrame(new MockFrameCompositor());
		composed.add(simple1);
		composed.add(simple2);
		stream 		= new VideoStream(List.of(simple1, simple2, composed).iterator());
	}

	@Test
	public void testSimpleStream() {
		assertThatThrownBy(
				() -> new VideoStream(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null frames argument");
		assertThat(stream)
			.isInstanceOf(VideoStream.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testNextFrame() {
		assertThat(stream.nextFrame())
			.isSameAs(simple1);
		assertThat(stream.nextFrame())
			.isSameAs(simple2);
		assertThat(stream.nextFrame())
			.isSameAs(composed);
		assertThatThrownBy(() -> stream.nextFrame())
			.isInstanceOf(AbsentFrameException.class)
			.hasMessage("No frame found");
	}

}
