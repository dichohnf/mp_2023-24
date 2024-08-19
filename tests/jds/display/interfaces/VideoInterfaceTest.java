package jds.display.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import jds.ComunicationChannel;
import jds.MockComunicationChannel;

public class VideoInterfaceTest {

	String name;
	String version;
	ComunicationChannel channel;
	VideoInterface inter;
	
	@Before
	public void setUp() {
		name	= "VGA";
		version	= "HD 720";
		channel = new MockComunicationChannel();
		inter = new VideoInterface(name, version, channel);
	}

	@Test
	public void testVideoInterface() {
		assertThatThrownBy(() -> new VideoInterface(null, version, channel))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null name argument");
		assertThatThrownBy(() -> new VideoInterface(name, null, channel))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null version argument");
		assertThatThrownBy(() -> new VideoInterface(name, version, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null channel argument");
		assertThat(inter)
			.isInstanceOf(VideoInterface.class)
			.hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void testEquals() {
		ComunicationChannel secondChannel = new MockComunicationChannel();
		VideoInterface equalInter	 = new VideoInterface(name, version, channel);
		VideoInterface diffName		 = new VideoInterface("HDMI", version, channel);
		VideoInterface diffVersion	 = new VideoInterface(name, "SXGA", channel);
		VideoInterface diffChannel	 = new VideoInterface(name, version, secondChannel);
		VideoInterface completelyDiff=  new VideoInterface("HDMI", "1.3", secondChannel);
		assertThat(inter.equals(equalInter))
			.isEqualTo(equalInter.equals(inter))
			.isEqualTo(inter.equals(diffChannel))
			.isTrue();
		assertThat(inter.equals(completelyDiff))
			.isEqualTo(completelyDiff.equals(inter))
			.isEqualTo(inter.equals(diffName))
			.isEqualTo(inter.equals(diffVersion))
			.isFalse();
	}
	
	@Test
	public void testHashCode() {
		ComunicationChannel secondChannel = new MockComunicationChannel();
		VideoInterface equalInter	 = new VideoInterface(name, version, channel);
		VideoInterface diffName		 = new VideoInterface("HDMI", version, channel);
		VideoInterface diffVersion	 = new VideoInterface(name, "SXGA", channel);
		VideoInterface diffChannel	 = new VideoInterface(name, version, secondChannel);
		VideoInterface completelyDiff=  new VideoInterface("HDMI", "1.3", secondChannel);
		assertThat(inter.hashCode())
			.isEqualTo(equalInter.hashCode())
			.isNotEqualTo(completelyDiff.hashCode())	
			.isNotEqualTo(diffName.hashCode())
			.isNotEqualTo(diffVersion.hashCode())
			.isEqualTo(diffChannel.hashCode());
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
			.isSameAs(channel.streamRecive());
	}

}
