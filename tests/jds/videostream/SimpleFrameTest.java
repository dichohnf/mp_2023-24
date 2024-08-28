package jds.videostream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

public class SimpleFrameTest {

	Position bottomLeft;
	Position topRight;
	byte[] data;
	VideoFrame simple;
	
	@Before
	public void setUp() {
		bottomLeft	= new Position(0,0);
		topRight	= new Position(1366, 768);
		data = "Bytes string.".getBytes();
		simple = new SimpleFrame(bottomLeft, topRight, data);
	}

	@Test
	public void testSimpleFrame() {
		assertThatThrownBy(() -> new SimpleFrame(null, topRight, data))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null bottomLeft argument");
		assertThatThrownBy(() -> new SimpleFrame(bottomLeft, null, data))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null topRight argument");
		assertThatThrownBy(() -> new SimpleFrame(bottomLeft, topRight, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null data argument");
		assertThat(simple)
			.isInstanceOf(SimpleFrame.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetBottomLeftPosition() {
		Position tmpPosition = new Position(2849, 1023);
		simple.setBottomLeftPosition(tmpPosition);
		assertThat(simple.getBottomLeftPosition())
			.isSameAs(tmpPosition);
	}

	@Test
	public void testSetTopRightPosition() {
		Position tmpPosition = new Position(2849, 1023);
		simple.setTopRightPosition(tmpPosition);
		assertThat(simple.getTopRightPosition())
			.isSameAs(tmpPosition);
	}

}
