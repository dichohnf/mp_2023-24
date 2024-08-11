package vision.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ComposedStream implements StreamType {
	
	private final class Position {
		int x;
		int y;
		
		Position(int x ,int y) {
			this.x = x;
			this.y = y;
		}		
	}
	
	private final Map<StreamType, Position> children;

	public ComposedStream() {
		this.children = new HashMap<>();
	}

	@Override
	public EncodedStream getEncoded(StreamEncoder encoder) {
		EncodedStream result = EncodedStream.EMPTY;
		for (Entry<StreamType, Position> child : children.entrySet()) {
			result.compose(
					child.getKey().getEncoded(encoder), 
					child.getValue().x,
					child.getValue().y);
		}
		return result;
		
//		children.entrySet().stream()
//			.map(entry -> entry.getKey().getEncoded(encoder))
//			.reduce(null);
			
		
//		children.forEach(
//				(stream,pos)-> encoder.addStream(
//						stream.getEncoded(encoder), 
//						pos.x, 
//						pos.y));
//		return encoder.encodedComposition();
	}
	
	public void add(StreamType child, int x, int y) {
		 children.put(child, new Position(x, y));
	}
	
	public void remove(StreamType child) {
		children.remove(child);
	}
	
}
