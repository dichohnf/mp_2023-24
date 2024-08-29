package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import jds.MockStreamReciver;
import jds.MockStreamSender;
import jds.Sensor;
import jds.StreamReciver;
import jds.StreamSender;
import jds.display.interfaces.VideoInterface;
import jds.display.malfunction.MalfunctionChecker;
import jds.display.malfunction.UnexpectedlyChangedResolutionChecker;
import jds.exception.AbsentVideoInterfaceException;
import jds.exception.PoorlyDefinedMeasureException;

public class DisplayWithBrightnessSensorDecoratorTest {

	int maxNits;
	List<String> supportedResolutions;
	StreamSender sender;
	List<VideoInterface> supportedInterfaces;
	StreamReciver reciver;
	VideoInterface videoInterface;
	Display component;
	Sensor<Double> brightnessSensor;
	Sensor<LocalTime> clock;
	double saturation;
	Display innerDecorator;
	Display outerDecorator;

	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions= List.of("1366x768", "720p");
		sender = new MockStreamSender();
		reciver = new MockStreamReciver();
		videoInterface = new VideoInterface("VGA", "WVGA", reciver);
		supportedInterfaces = List.of(videoInterface);
		brightnessSensor = (Sensor<Double>) () -> Double.valueOf(6000);
		clock = (Sensor<LocalTime>) () -> LocalTime.parse("02:00");
		component = new StandardDisplay(maxNits, supportedResolutions, sender, supportedInterfaces);
		saturation = 10000.0;
		innerDecorator = new DisplayWithClockDecorator(clock, component);
		outerDecorator = new DisplayWithBrightnessSensorDecorator(brightnessSensor, innerDecorator, saturation);
	}

	@Test
	public void testDisplayWithBrightnessSensorDecorator() {
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(null, component, saturation))
      		.isInstanceOf(NullPointerException.class)
      		.hasMessage("Null sensor argument");
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(brightnessSensor, null, saturation))
           .isInstanceOf(NullPointerException.class)
           .hasMessage("Null component argument");
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(brightnessSensor, component, Double.valueOf(-13)))
			.isInstanceOfAny(IllegalArgumentException.class)
			.hasMessage("Negative saturation argument");
		assertThat(new DisplayWithBrightnessSensorDecorator(brightnessSensor, component, null).getSaturationLuxAmount())
			.isEqualTo(10000);
		assertThat(outerDecorator)
			.isInstanceOf(DisplayWithBrightnessSensorDecorator.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetValue() throws PoorlyDefinedMeasureException {
		assertThat(component.getBrightness())
			.isEqualTo(0.5);
		((DisplayWithBrightnessSensorDecorator)outerDecorator)
			.setValue(Double.valueOf(4500));
		assertThat(component.getBrightness())
			.isEqualTo(0.45);
		((DisplayWithBrightnessSensorDecorator)outerDecorator)
			.setValue(Double.valueOf(500000));
		assertThat(component.getBrightness())
			.isEqualTo(1);
	}
		
	@Test
	public void testDisplayStream() throws AbsentVideoInterfaceException {
		outerDecorator.connectInterface(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.5);
		outerDecorator.displayStream();
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.6);
	}

	@Test
	public void testDisplayMenu() throws AbsentVideoInterfaceException {
		outerDecorator.connectInterface(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.5);
		outerDecorator.displayMenu();
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.6);
	}

	@Test
	public void testDisplayError() throws AbsentVideoInterfaceException {
		outerDecorator.connectInterface(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.5);
		outerDecorator.displayError("Error string");
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(0.6);
	}
	
	
	@Test
	public void testGetSupportedInterfaces() {
		assertThat(outerDecorator.getSupportedInterfaces())
			.isEqualTo(component.getSupportedInterfaces());
	}

	@Test
	public void testSetBrightness() {
		assertThatThrownBy(
				() -> outerDecorator.setBrightness(-0.1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		assertThatThrownBy(
				() -> outerDecorator.setBrightness(5))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Brightness value not acceptable: Defined from 0 to 1");
		outerDecorator.setBrightness(0.8);
		assertThat(component.getBrightness())
			.isEqualTo(0.8);
	}

	@Test
	public void testGetBrightness() {
		assertThat(outerDecorator.getBrightness())
			.isEqualTo(component.getBrightness());
	}

	@Test
	public void testSetColorTemperature() {
        assertThatThrownBy(
        		() -> outerDecorator.setColorTemperature(-1))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        assertThatThrownBy(
        		() -> outerDecorator.setColorTemperature(11))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Not acceptable colorTemperature argument: must be between 0 and 10");
        outerDecorator.setColorTemperature(9);
        assertThat(component.getColorTemperature())
        	.isEqualTo(9);
	}

	@Test
	public void testSetResolution() {
		assertThatThrownBy(
				() -> outerDecorator.setResolution(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("Null resolution argument");
		assertThatThrownBy(
				() -> outerDecorator.setResolution("1440p"))
        	.isInstanceOf(IllegalArgumentException.class)
        	.hasMessage("Selected resolution is not supported");
        assertThat(component.getResolution())
    		.isEqualTo("1366x768");
        outerDecorator.setResolution("720p");
        assertThat(component.getResolution())
        	.isEqualTo("720p");
	}

	@Test
	public void testConnectInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> outerDecorator.connectInterface(
						new VideoInterface("HDMI",
								"1.1", 
								new MockStreamReciver())))
        	.isInstanceOf(AbsentVideoInterfaceException.class)
        	.hasMessage("Impossible connection: Display is not provided with specified video interface");
        assertThat(outerDecorator.connectInterface(videoInterface))
        	.isTrue();
        assertThat(component.getConnectedInterfaces())
        	.contains(videoInterface);
	}

	@Test
	public void testDisconnectInterface() throws AbsentVideoInterfaceException {
		component.connectInterface(videoInterface);
        assertThat(outerDecorator.disconnectInterface(videoInterface))
    		.isTrue();
        assertThat(component.getConnectedInterfaces())
    		.doesNotContain(videoInterface);
        assertThat(outerDecorator.disconnectInterface(videoInterface))
    		.isFalse();
	}

	@Test
	public void testGetConnectedInterfaces() {
		assertThat(outerDecorator.getConnectedInterfaces())
			.isEqualTo(component.getConnectedInterfaces());
	}

	@Test
	public void testSelectInputInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> outerDecorator.selectInputInterface(videoInterface))
			.isInstanceOf(AbsentVideoInterfaceException.class)
			.hasMessage("Selected interface is not conneted");
		component.connectInterface(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);	
		assertThat(component.getSelectedInterface())
			.isSameAs(videoInterface);
		}

	@Test
	public void testGetSelectedInterface() throws AbsentVideoInterfaceException {
		assertThatThrownBy(
				() -> outerDecorator.getSelectedInterface())
			.isInstanceOf(NoSuchElementException.class);
		component.connectInterface(videoInterface);
		((StandardDisplay)component).forceInputInterface(videoInterface);
		assertThat(outerDecorator.getSelectedInterface())
			.isEqualTo(component.getSelectedInterface());
	}

	@Test
	public void testMulfunctionTest() {
		MalfunctionChecker checker = new UnexpectedlyChangedResolutionChecker(null);
		assertThat(outerDecorator.malfunctionTest(checker))
			.isEqualTo(component.malfunctionTest(checker));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSetBestConfiguration() {
		assertThat(component.getBrightness())
			.isEqualTo(0.5);
		assertThat(component.getColorTemperature())
			.isEqualTo(5);
		((DisplayWithSensorDecorator<Double>)outerDecorator)
			.setBestConfiguration();
		assertThat(component.getBrightness())
			.isEqualTo(0.6);
		assertThat(component.getColorTemperature())
			.isEqualTo(5);
	}

}
