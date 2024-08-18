package jds.stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.exception.AbsentFrameException;

public class VideoStreamTest {
	
	Position origin;
	VideoFrame frame1;
	VideoFrame frame2;
	VideoFrame frame3;
	VideoStream stream;

	@Before
	public void setUp() {
		origin = new Position(0,0);
		frame1 = new MockVideoFrame(origin);
		frame2 = new MockVideoFrame(origin);
		frame3 = new MockVideoFrame(origin);
		stream = new VideoStream(List.of(frame1, frame2, frame3).iterator());
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
			.isSameAs(frame1);
		assertThat(stream.nextFrame())
			.isSameAs(frame2);
		assertThat(stream.nextFrame())
			.isSameAs(frame3);
		assertThatThrownBy(() -> stream.nextFrame())
			.isInstanceOf(AbsentFrameException.class)
			.hasMessage("No frame found");
	}

}
