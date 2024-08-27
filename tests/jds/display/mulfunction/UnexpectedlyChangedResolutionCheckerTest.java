package jds.display.mulfunction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jds.StreamChannel;
import jds.MockStreamChannel;
import jds.display.Display;
import jds.display.DisplaySetterToTestMulfunctions;
import jds.display.StandardDisplay;
import jds.display.interfaces.VideoInterface;
import jds.exception.AbsentVideoInterfaceException;

public class UnexpectedlyChangedResolutionCheckerTest {
	
	int maxNits;
	List<String> supportedResolutions;
	StreamChannel displayChannel;
	List<VideoInterface> supportedInterfaces;
	StreamChannel interfaceChannel;
	VideoInterface videoInterface;
	Display display;
	MulfunctionChecker checker1;
	MulfunctionChecker checker2;

	@Before
	public void setUp() {
		maxNits 			= 600;
		supportedResolutions= List.of("1366x768", "720p");
		displayChannel 		= new MockStreamChannel();
		interfaceChannel 	= new MockStreamChannel();
		videoInterface 		= new VideoInterface("VGA", "WVGA", interfaceChannel);
		supportedInterfaces = List.of(videoInterface);
		display 			= new StandardDisplay(maxNits, supportedResolutions, displayChannel, supportedInterfaces);
		checker1 			= new UnexpectedlyChangedResolutionChecker(null);
		checker2 			= new UnexpectedlyDisconnectedInterfaceChecker(checker1);
	}

	@Test
	public void testCheckMulfunction() throws AbsentVideoInterfaceException {
		display.connectInterface(videoInterface);
		display.selectInputInterface(videoInterface);
		String noMulfunction = "No mulfunction detected";
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo(noMulfunction);
		assertThat(checker2.checkMulfunction(display))
			.isEqualTo(noMulfunction);
		DisplaySetterToTestMulfunctions.setResolution(display, "4k");
		assertThat(checker1.checkMulfunction(display))
			.isEqualTo("Unexpectedly changed resolution");
		DisplaySetterToTestMulfunctions.setResolution(display, "4k");
		assertThat(checker2.checkMulfunction(display))
			.isEqualTo("Unexpectedly changed resolution");
	}

}
