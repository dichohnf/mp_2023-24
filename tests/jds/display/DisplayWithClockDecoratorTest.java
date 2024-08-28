package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import jds.StreamChannel;
import jds.MockStreamChannel;
import jds.Sensor;
import jds.display.interfaces.VideoInterface;
import jds.display.mulfunction.MulfunctionChecker;
import jds.display.mulfunction.UnexpectedlyChangedResolutionChecker;
import jds.exception.AbsentVideoInterfaceException;

public class DisplayWithClockDecoratorTest {

	int maxNits;
	List<String> supportedResolutions;
	StreamChannel componentChannel;
	List<VideoInterface> supportedInterfaces;
	StreamChannel interfaceChannel;
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
		componentChannel = new MockStreamChannel();
		interfaceChannel = new MockStreamChannel();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		brightnessSensor = (Sensor<Double>) () -> Double.valueOf(6000);
		clock = (Sensor<LocalTime>) () -> LocalTime.parse("02:00");
		component = new StandardDisplay(maxNits, supportedResolutions, componentChannel, supportedInterfaces);
		saturation = 10000.0;
		innerDecorator = new DisplayWithBrightnessSensorDecorator(brightnessSensor, component, saturation);
		outerDecorator = new DisplayWithClockDecorator(clock, innerDecorator);
	}

	@Test
	public void testDisplayWithClockDecorator() {
		assertThatThrownBy(() -> new DisplayWithClockDecorator(null, component))
      		.isInstanceOf(NullPointerException.class)
      		.hasMessage("Null sensor argument");
		assertThatThrownBy(() -> new DisplayWithClockDecorator(clock, null))
           .isInstanceOf(NullPointerException.class)
           .hasMessage("Null component argument");
		assertThat(outerDecorator)
			.isInstanceOf(DisplayWithClockDecorator.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetValue() {
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(5);
		((DisplayWithClockDecorator)outerDecorator)
			.setValue(LocalTime.parse("06:59"));
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(10);
		((DisplayWithClockDecorator)outerDecorator)
			.setValue(LocalTime.parse("17:23"));
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(5);
	}
		
	@Test
	public void testDisplayStream() throws AbsentVideoInterfaceException {
		outerDecorator.connectInterface(videoInterface);
		outerDecorator.selectInputInterface(videoInterface);
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(5);
		outerDecorator.displayStream();
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(10);
	}

	@Test
	public void testDisplayMenu() {
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(5);
		outerDecorator.displayMenu();
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(10);
	}

	@Test
	public void testDisplayError() {
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(5);
		outerDecorator.displayError("Error string");
		assertThat(outerDecorator.getColorTemperature())
			.isEqualTo(10);
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
								new MockStreamChannel())))
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
		MulfunctionChecker checker = new UnexpectedlyChangedResolutionChecker(null);
		assertThat(outerDecorator.mulfunctionTest(checker))
			.isEqualTo(component.mulfunctionTest(checker));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSetBestConfiguration() {
		assertThat(component.getBrightness())
			.isEqualTo(0.5);
		assertThat(component.getColorTemperature())
			.isEqualTo(5);
		((DisplayWithSensorDecorator<LocalTime>)outerDecorator)
			.setBestConfiguration();
		assertThat(component.getBrightness())
			.isEqualTo(0.5);
		assertThat(component.getColorTemperature())
			.isEqualTo(10);
	}

}
