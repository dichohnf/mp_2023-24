package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	StreamSender componentChannel;
	List<VideoInterface> supportedInterfaces;
	StreamReciver interfaceChannel;
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
		componentChannel = new MockStreamSender();
		interfaceChannel = new MockStreamReciver();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		brightnessSensor = (Sensor<Double>) () -> Double.valueOf(6000);
		clock = (Sensor<LocalTime>) () -> LocalTime.parse("02:00");
		component = new StandardDisplay(maxNits, supportedResolutions, componentChannel, supportedInterfaces);
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
		assertThat(new DisplayWithBrightnessSensorDecorator(brightnessSensor, component, null).saturationLuxAmount)
			.isEqualTo(DisplayWithBrightnessSensorDecorator.DEFAULT_SATURATION_LUX);
		assertThat(outerDecorator)
			.isInstanceOf(DisplayWithBrightnessSensorDecorator.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetValue() throws PoorlyDefinedMeasureException {
		assertThat(((StandardDisplay)component).getBrightness())
			.isEqualTo(0.5);
		((DisplayWithBrightnessSensorDecorator)outerDecorator)
			.setValue(Double.valueOf(4500));
		assertThat(((StandardDisplay)component).getBrightness())
			.isEqualTo(0.45);
		((DisplayWithBrightnessSensorDecorator)outerDecorator)
			.setValue(Double.valueOf(500000));
		assertThat(((StandardDisplay)component).getBrightness())
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
		assertThat(((StandardDisplay)component).currentNits)
			.isEqualTo((int) (0.8 * maxNits));
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
        assertThat(((StandardDisplay)component).colorTemperature)
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
        assertThat(((StandardDisplay)component).resolution)
    		.isEqualTo("1366x768");
        outerDecorator.setResolution("720p");
        assertThat(((StandardDisplay)component).resolution)
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
        assertThat(((StandardDisplay)component).connectedInterfaces)
        	.contains(videoInterface);
	}

	@Test
	public void testDisconnectInterface() {
		((StandardDisplay)component).connectedInterfaces
			.add(videoInterface);
        assertThat(outerDecorator.disconnectInterface(videoInterface))
    		.isTrue();
        assertThat(((StandardDisplay)component).connectedInterfaces)
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
		((StandardDisplay)component).connectedInterfaces
			.add(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);	
		assertThat(((StandardDisplay)component).selectedInterface)
			.isPresent()
			.contains(videoInterface);
		}

	@Test
	public void testGetSelectedInterface() {
		assertThatThrownBy(
				() -> outerDecorator.getSelectedInterface())
			.isInstanceOf(NoSuchElementException.class);
		((StandardDisplay)component).connectedInterfaces.add(videoInterface);
		((StandardDisplay)component).selectedInterface = Optional.of(videoInterface);
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
