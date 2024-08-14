package vision.stream;

import java.util.HashMap;
import java.util.Map;

public final class ComposedStream implements VideoStream {
	
	private final Map<VideoStream, Position> children;
	private int nextFrame;

	public ComposedStream() {
		children = new HashMap<>();
		nextFrame = 0;
	}
	
	public void add(VideoStream child, int x, int y) {
		 children.put(child, new Position(x, y));
	}
	
	public void remove(VideoStream child) {
		children.remove(child);
	}

//	@Override
//	public VideoFrame nextFrame() throws AbsentFrameException {
//		int minX = children.values().stream().map(pos -> pos.x).min((x1, x2) -> (x1 < x2)?x1:x2).orElseThrow();
//		int minY = children.values().stream().map(pos -> pos.y).min((y1, y2) -> (y1 < y2)?y1:y2).orElseThrow();
//		int maxX = children.values().stream().map(pos -> pos.x).min((x1, x2) -> (x1 > x2)?x1:x2).orElseThrow();
//		int maxY = children.values().stream().map(pos -> pos.y).min((y1, y2) -> (y1 > y2)?y1:y2).orElseThrow();
//		String resolution = String.format("%dx%d", maxX - minX, maxY, minY);
//		return Video
//		
//	}
	
	
	
	
}
