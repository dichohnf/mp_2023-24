package jds.display;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.StreamChannel;
import jds.MockStreamChannel;
import jds.Sensor;
import jds.display.interfaces.VideoInterface;

public class DisplayBuilderTest {
	
	int	maxNits;
	List<String> supportedResolutions;
	StreamChannel displayChannel;
	StreamChannel interfaceChannel;
	VideoInterface videoInterface;
	List<VideoInterface> supportedInterfaces;
	Sensor<Double> brightnessSensor;
	double saturation;
	Sensor<LocalTime> clockSensor;
	DisplayBuilder builder;

	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions = List.of("720p", "1080p");
		displayChannel = new MockStreamChannel();
		interfaceChannel = new MockStreamChannel();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		brightnessSensor = (Sensor<Double>) () -> Double.valueOf(6000);
		saturation = 10000;
		clockSensor = (Sensor<LocalTime>) () -> LocalTime.parse("02:00");
		builder = new DisplayBuilder();
	}

	@Test
	public void testDisplayBuilder() {
		afterResetStateCheck();
	}

	private void afterResetStateCheck() {
		assertThat(builder.maxNits)
			.isEqualTo(-1);
		assertThat(builder.supportedResolutions)
			.isNull();
		assertThat(builder.channel)
			.isNull();
		assertThat(builder.supportedInterfaces)
			.isNull();
		assertThat(builder.brightnessSensor)
			.isNull();
		assertThat(builder.saturation)
			.isEqualTo(-1);
		assertThat(builder.clockSensor)
			.isNull();
	}

	@Test
	public void testReset() {
		builder.maxNits= maxNits;
		builder.supportedResolutions = List.of("720p", "1080p");
		builder.channel = displayChannel;
		builder.supportedInterfaces = supportedInterfaces;
		builder.brightnessSensor = brightnessSensor;
		builder.saturation = saturation;
		builder.clockSensor = clockSensor;
		builder.reset();
		afterResetStateCheck();
	}

	@Test
	public void testBuildWithNoSensors() {
		builder.maxNits= maxNits;
		builder.supportedResolutions = List.of("720p", "1080p");
		builder.channel = displayChannel;
		builder.supportedInterfaces = supportedInterfaces;
		assertThat(builder.build())
			.isInstanceOf(StandardDisplay.class);
	}
	 
	@Test
	public void testBuildWithBrightnessSensor() {
		builder.maxNits= maxNits;
		builder.supportedResolutions = List.of("720p", "1080p");
		builder.channel = displayChannel;
		builder.supportedInterfaces = supportedInterfaces;
		builder.saturation = saturation;
		builder.brightnessSensor = brightnessSensor;
		assertThat(builder.build())
			.isInstanceOf(DisplayWithBrightnessSensorDecorator.class);
	}
	 
	@Test
	public void testBuildWithClockSensor() {
		builder.maxNits= maxNits;
		builder.supportedResolutions = List.of("720p", "1080p");
		builder.channel = displayChannel;
		builder.supportedInterfaces = supportedInterfaces;
		builder.clockSensor = clockSensor;
		assertThat(builder.build())
			.isInstanceOf(DisplayWithClockDecorator.class);
	}
	 
	@Test
	public void testBuildWithBothSensors() {
		builder.maxNits= maxNits;
		builder.supportedResolutions = List.of("720p", "1080p");
		builder.channel = displayChannel;
		builder.supportedInterfaces = supportedInterfaces;
		builder.saturation = saturation;
		builder.brightnessSensor = brightnessSensor;
		builder.clockSensor = clockSensor;
		assertThat(builder.build())
			.isInstanceOf(DisplayWithClockDecorator.class);
		assertThat(((DisplayWithClockDecorator) builder.build()).component)
			.isInstanceOf(DisplayWithBrightnessSensorDecorator.class);
	}
	
}