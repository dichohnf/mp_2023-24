package jds.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.ComunicationChannel;
import jds.MockComunicationChannel;
import jds.Sensor;
import jds.display.interfaces.VideoInterface;
import jds.exception.AbsentVideoInterfaceException;

public class DisplayWithClockDecoratorTest {

	int maxNits;
	List<String> supportedResolutions;
	ComunicationChannel componentChannel;
	List<VideoInterface> supportedInterfaces;
	ComunicationChannel interfaceChannel;
	VideoInterface videoInterface;
	Display component;
	Sensor<LocalTime> clock;
	Display decorator;

	@Before
	public void setUp() {
		maxNits = 600;
		supportedResolutions= List.of("1366x768", "720p");
		componentChannel = new MockComunicationChannel();
		interfaceChannel = new MockComunicationChannel();
		videoInterface = new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		clock = (Sensor<LocalTime>) () -> LocalTime.parse("02:00");
		component = new StandardDisplay(maxNits, supportedResolutions, componentChannel, supportedInterfaces);
		decorator = new DisplayWithClockDecorator(clock, component);
	}

	@Test
	public void testDisplayWithClockDecorator() {
		assertThatThrownBy(() -> new DisplayWithClockDecorator(null, component))
      		.isInstanceOf(NullPointerException.class)
      		.hasMessage("Null sensor argument");
		assertThatThrownBy(() -> new DisplayWithClockDecorator(clock, null))
           .isInstanceOf(NullPointerException.class)
           .hasMessage("Null component argument");
		assertThat(decorator)
			.isInstanceOf(DisplayWithClockDecorator.class)
			.hasNoNullFieldsOrProperties();
	}

	@Test
	public void testSetValue() {
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(5);
		((DisplayWithClockDecorator)decorator)
			.setValue(LocalTime.parse("06:59"));
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(10);
		((DisplayWithClockDecorator)decorator)
			.setValue(LocalTime.parse("17:23"));
		assertThat(((StandardDisplay)component).colorTemperature)
			.isEqualTo(5);
	}
		
	@Test
	public void testDisplayStream() throws AbsentVideoInterfaceException {
		decorator.connectInterface(videoInterface);
		decorator.selectInputInterface(videoInterface);
		assertThat(decorator.getColorTemperature())
			.isEqualTo(5);
		decorator.displayStream();
		assertThat(decorator.getColorTemperature())
			.isEqualTo(10);
	}

	@Test
	public void testDisplayMenu() {
		assertThat(decorator.getColorTemperature())
			.isEqualTo(5);
		decorator.displayMenu();
		assertThat(decorator.getColorTemperature())
			.isEqualTo(10);
	}

	@Test
	public void testDisplayError() {
		assertThat(decorator.getColorTemperature())
			.isEqualTo(5);
		decorator.displayError("Error string");
		assertThat(decorator.getColorTemperature())
			.isEqualTo(10);
	}

}
