package vision.display;

import java.util.Collection;

import vision.display.video_interface.VideoInterface;

public final class StandardDisplay extends Display {
	
	public StandardDisplay(int xPixels, int yPixels, int colorBits,
			ComunicationChannel channel,
			Collection<VideoInterface> availableInterfaces, 
			Collection<VideoInterface> connectedInterfaces) {
		super(xPixels, yPixels, colorBits, channel, availableInterfaces, connectedInterfaces);
	}

	@Override
	public void displayMenu() {
		getChannel().displayMenu();
	}

	@Override
	public void displayTestScreen() {
		getChannel().displayTestScreen();
	}

}
