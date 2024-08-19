package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.ComunicationChannel;
import jds.MockComunicationChannel;
import jds.Sensor;
import jds.display.interfaces.VideoInterface;
import jds.exception.AbsentVideoInterfaceException;
import jds.exception.PoorlyDefinedMeasureException;

public class DisplayWithBrightnessSensorDecoratorTest {

	int maxNits;
	List<String> supportedResolutions;
	ComunicationChannel componentChannel;
	List<VideoInterface> supportedInterfaces;
	ComunicationChannel interfaceChannel;
	VideoInterface videoInterface;
	Display component;
	Sensor<Double> sensor;
	double saturation;
	Display decorator;

	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions= List.of("1366x768", "720p");
		componentChannel = new MockComunicationChannel();
		interfaceChannel = new MockComunicationChannel();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		sensor = (Sensor<Double>) () -> Double.valueOf(6000);
		component = new StandardDisplay(maxNits, supportedResolutions, componentChannel, supportedInterfaces);
		saturation = 10000.0;
		decorator = new DisplayWithBrightnessSensorDecorator(sensor, component, saturation);
	}

	@Test
	public void testDisplayWithBrightnessSensorDecorator() {
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(null, component, saturation))
      		.isInstanceOf(NullPointerException.class)
      		.hasMessage("Null sensor argument");
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(sensor, null, saturation))
           .isInstanceOf(NullPointerException.class)
           .hasMessage("Null component argument");
		assertThatThrownBy(() -> new DisplayWithBrightnessSensorDecorator(sensor, component, Double.valueOf(-13)))
			.isInstanceOfAny(IllegalArgumentException.class)
			.hasMessage("Negative saturation argument");
		assertThat(new DisplayWithBrightnessSensorDecorator(sensor, component, null).saturationLuxAmount)
			.isEqualTo(DisplayWithBrightnessSensorDecorator.DEFAULT_SATURATION_LUX);
		assertThat(decorator)
			.isInstanceOf(DisplayWithBrightnessSensorDecorator.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetValue() throws PoorlyDefinedMeasureException {
		assertThat(((StandardDisplay)component).getBrightness())
			.isEqualTo(0.5);
		((DisplayWithBrightnessSensorDecorator)decorator)
			.setValue(Double.valueOf(4500));
		assertThat(((StandardDisplay)component).getBrightness())
			.isEqualTo(0.45);
		((DisplayWithBrightnessSensorDecorator)decorator)
			.setValue(Double.valueOf(500000));
		assertThat(((StandardDisplay)component).getBrightness())
			.isEqualTo(1);
	}
		
	@Test
	public void testDisplayStream() throws AbsentVideoInterfaceException {
		decorator.connectInterface(videoInterface);
		decorator.selectInputInterface(videoInterface);
		assertThat(decorator.getBrightness())
			.isEqualTo(0.5);
		decorator.displayStream();
		assertThat(decorator.getBrightness())
			.isEqualTo(0.6);
	}

	@Test
	public void testDisplayMenu() throws AbsentVideoInterfaceException {
		decorator.connectInterface(videoInterface);
		decorator.selectInputInterface(videoInterface);
		assertThat(decorator.getBrightness())
			.isEqualTo(0.5);
		decorator.displayMenu();
		assertThat(decorator.getBrightness())
			.isEqualTo(0.6);
	}

	@Test
	public void testDisplayError() throws AbsentVideoInterfaceException {
		decorator.connectInterface(videoInterface);
		decorator.selectInputInterface(videoInterface);
		assertThat(decorator.getBrightness())
			.isEqualTo(0.5);
		decorator.displayError("Error string");
		assertThat(decorator.getBrightness())
			.isEqualTo(0.6);
	}

}
