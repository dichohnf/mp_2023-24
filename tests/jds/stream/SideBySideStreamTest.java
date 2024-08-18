package jds.stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.ByteBuffer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SideBySideStreamTest {

	Position origin;
	Position opposite;
	Position farPosition;
	VideoFrame frame1;
	VideoFrame frame2;
	VideoFrame frame3;
	VideoStream simple1;
	VideoStream simple2;
	SideBySideStream composed;
		
	@Before
	public void setUp() {
		origin = new Position(0,0);
		opposite = new Position(1366, 768);
		farPosition = new Position(50321, 24213);
		frame1 = new VideoFrame(
				ByteBuffer.wrap("Fisrt String.".getBytes()), 
				origin, opposite);
		frame2 = new VideoFrame(
				ByteBuffer.wrap("Second String.".getBytes()), 
				origin, opposite);
		frame3 = new VideoFrame(
				ByteBuffer.wrap("Third String.".getBytes()), 
				origin, farPosition);
		simple1 = new SimpleStream(List.of(frame1, frame2, frame3).iterator());
		simple2 = new SimpleStream(List.of(frame1, frame2, frame3).iterator());
		composed = new SideBySideStream(new MockFrameCompositor());
	}

	@Test
	public void testComposedStream() {
		assertThatThrownBy(() -> new SideBySideStream(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null compositor argument");
		assertThat(composed)
			.isInstanceOf(SideBySideStream.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testAdd() {
		assertThat(composed.childrens)
			.isNotNull()
			.isEmpty();
		addAndDimensionCheck(simple1, 1);
		addAndDimensionCheck(simple2, 2);
		addAndDimensionCheck(simple1, 3);
		addAndDimensionCheck(new SideBySideStream(new MockFrameCompositor()), 4);
	}

	private void addAndDimensionCheck(VideoStream stream, int dim) {
		assertThat(composed.add(stream))
			.isTrue();
		assertThat(composed.childrens)
			.isNotNull()
			.hasSize(dim);
	}

	@Test
	public void testRemove() {
		VideoStream emptyComp = new SideBySideStream(new MockFrameCompositor());
		composed.childrens.addAll(
				List.of(simple1, simple2, simple1, emptyComp));
		assertThat(composed.childrens)
			.isNotNull()
			.hasSize(4);
		removeAndDimensionCheck(simple1, 3);
		removeAndDimensionCheck(simple1, 2);
		assertThat(composed.remove(simple1))
			.isFalse();
		removeAndDimensionCheck(emptyComp, 1);
		removeAndDimensionCheck(simple2, 0);
	}

	private void removeAndDimensionCheck(VideoStream stream, int dim) {
		assertThat(composed.remove(stream))
			.isTrue();
		assertThat(composed.childrens)
			.isNotNull()
			.hasSize(dim);
		
	}

	@Test
	public void testNextFrame() {
		composed.childrens.addAll(
				List.of(simple1));
		VideoFrame next = composed.nextFrame();
		assertThat(next.getOrigin())
			.satisfies(pos -> pos.isCongruentWith(origin));
		assertThat(next.opposite)
			.satisfies(pos -> pos.isCongruentWith(farPosition));
	}

}
