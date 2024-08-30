package jds.videostream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ComposedFrameTest {
	
	Position bottomLeft1;
	Position bottomLeft2;
	Position bottomLeft3;
	Position topRight1;
	Position topRight2;
	Position topRight3;
	byte[] data1;
	byte[] data2;
	byte[] data3;
	VideoFrame simple1;
	VideoFrame simple2;
	VideoFrame simple3;
	ComposedFrame composed1;
	ComposedFrame composed2;
	Collection<VideoFrame> children1;
	Collection<VideoFrame> children2;
	
	@Before
	public void setUp() {
		bottomLeft1	= new Position(0,0);
		bottomLeft2	= new Position(1,2);
		bottomLeft3	= new Position(4,5);
		topRight1	= new Position(1366, 768);
		topRight2	= new Position(1567, 1314);
		topRight3	= new Position(2133, 4212);
		data1 		= "Bytes string.".getBytes();
		data2 		= "Another string!".getBytes();
		data3 		= "Last string?".getBytes();
		simple1 	= new SimpleFrame(bottomLeft1, topRight1, data1);
		simple2 	= new SimpleFrame(bottomLeft2, topRight2, data2);
		simple3 	= new SimpleFrame(bottomLeft3, topRight3, data3);
		composed1 	= new ComposedFrame(new MockFrameCompositor());
		composed2	= new ComposedFrame(new MockFrameCompositor());
		children1 	= composed1.getChildren();
		children2 	= composed2.getChildren();
		children1.add(simple1);
		composed1.calculationRelevantPositions();
		children2.addAll(List.of(simple2, simple3, composed1));
		composed2.calculationRelevantPositions();
 	}
	
	@Test
	public void testComposedFrame() {
		assertThatThrownBy(
				() -> new ComposedFrame(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null compositor argument");
		assertThat(composed1)
			.hasNoNullFieldsOrProperties();
		assertThat(composed2)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testGetData() {
		assertThat(composed1.getData())
			.contains(data1);
		assertThat(composed2.getData())
			.contains(data1)
			.contains(data2)
			.contains(data3);
	}

	@Test
	public void testAdd() {
		addAndSizeCheck(composed1, simple1,	 2);
		addAndSizeCheck(composed1, simple1,	 3);
		addAndSizeCheck(composed1, simple2,	 4);
		addAndSizeCheck(composed1, composed2, 5);
		assertThatThrownBy(
				() -> composed1.add(composed1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Child to insert is this frame object");
	}
	
	private void addAndSizeCheck(ComposedFrame parent, VideoFrame child, int size) {
		assertThat(parent.add(child))
			.isTrue();
		assertThat(parent.getChildren())
			.isNotNull()
			.hasSize(size);
	}

	@Test
	public void testRemove() {
		sizeCheckAndRemove(composed2, 3, simple2);
		assertThat(composed2.getChildren())
			.hasSize(2);
		assertThat(composed2.remove(simple2))
			.isFalse();
		sizeCheckAndRemove(composed2, 2, simple3);
		sizeCheckAndRemove(composed2, 1, composed1);
		assertThat(composed2.getChildren())
			.isNotNull()
			.isEmpty();
	}
	
	private void sizeCheckAndRemove(ComposedFrame parent, int size, VideoFrame child) {
		assertThat(parent.getChildren())
			.hasSize(size);
		assertThat(parent.remove(child))
			.isTrue();
	}

	@Test
	public void testSetBottomLeftPosition() {
		Position tmpPosition = new Position(2849, 1023);
		composed2.setBottomLeftPosition(tmpPosition);
		assertThat(composed2.getBottomLeftPosition())
			.isSameAs(tmpPosition);
	}

	@Test
	public void testSetTopRightPosition() {
		Position tmpPosition = new Position(2849, 1023);
		composed2.setTopRightPosition(tmpPosition);
		assertThat(composed2.getTopRightPosition())
			.isSameAs(tmpPosition);
	}

}
