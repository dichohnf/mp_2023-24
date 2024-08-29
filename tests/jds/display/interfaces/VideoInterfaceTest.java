package jds.display.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import jds.MockStreamReciver;
import jds.StreamReciver;

public class VideoInterfaceTest {

	String name;
	String version;
	StreamReciver reciver;
	VideoInterface inter;
	
	@Before
	public void setUp() {
		name	= "VGA";
		version	= "HD 720";
		reciver = new MockStreamReciver();
		inter 	= new VideoInterface(name, version, reciver);
	}

	@Test
	public void testVideoInterface() {
		assertThatThrownBy(() -> new VideoInterface(null, version, reciver))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null name argument");
		assertThatThrownBy(() -> new VideoInterface(name, null, reciver))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null version argument");
		assertThatThrownBy(() -> new VideoInterface(name, version, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null reciver argument");
		assertThat(inter)
			.isInstanceOf(VideoInterface.class)
			.hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void testEquals() {
		StreamReciver secondReciver	 = new MockStreamReciver();
		VideoInterface equalInter	 = new VideoInterface(name, version, reciver);
		VideoInterface diffName		 = new VideoInterface("HDMI", version, reciver);
		VideoInterface diffVersion	 = new VideoInterface(name, "SXGA", reciver);
		VideoInterface diffReciver	 = new VideoInterface(name, version, secondReciver);
		VideoInterface completelyDiff=  new VideoInterface("HDMI", "1.3", secondReciver);
		assertThat(inter.equals(equalInter))
			.isEqualTo(equalInter.equals(inter))
			.isEqualTo(inter.equals(diffReciver))
			.isTrue();
		assertThat(inter.equals(completelyDiff))
			.isEqualTo(completelyDiff.equals(inter))
			.isEqualTo(inter.equals(diffName))
			.isEqualTo(inter.equals(diffVersion))
			.isFalse();
	}
	
	@Test
	public void testHashCode() {
		StreamReciver secondReciver	 = new MockStreamReciver();
		VideoInterface equalInter	 = new VideoInterface(name, version, reciver);
		VideoInterface diffName		 = new VideoInterface("HDMI", version, reciver);
		VideoInterface diffVersion	 = new VideoInterface(name, "SXGA", reciver);
		VideoInterface diffReciver	 = new VideoInterface(name, version, secondReciver);
		VideoInterface completelyDiff=  new VideoInterface("HDMI", "1.3", secondReciver);
		assertThat(inter.hashCode())
			.isEqualTo(equalInter.hashCode())
			.isNotEqualTo(completelyDiff.hashCode())	
			.isNotEqualTo(diffName.hashCode())
			.isNotEqualTo(diffVersion.hashCode())
			.isEqualTo(diffReciver.hashCode());
	}

	@Test
	public void testToString() {
		assertThat(inter.toString())
			.isEqualTo(inter.getClass().getCanonicalName() +
					"@" + Integer.toHexString(inter.hashCode()) 
					+ "-VGA:HD 720");
	}

	@Test
	public void testGetStream() {
		assertThat(inter.getStream())
			.isSameAs(reciver.streamRecive());
	}

}
