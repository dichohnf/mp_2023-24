package jds.stream;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public final class VideoFrame {
	
	private final Position origin;
	private final Position opposite;
	private final int frameNumber;
	private final Collection<Byte> data;
	
	public VideoFrame(int frameNumber, Collection<Byte> data, Position origin, Position oppositeToOrigin) {
		if(frameNumber < 0 )
			throw new IllegalArgumentException("Negative frameNumber argument");
		this.frameNumber = frameNumber;
		this.data = Objects.requireNonNull(
				data, 
				"Null data argument");
		if (origin.getX() >= oppositeToOrigin.getX() || origin.getY() >= oppositeToOrigin.getY())
			throw new IllegalArgumentException("Origin coordinates are greater than opposite's");
		this.origin = Objects.requireNonNull(origin, "Null origin argument");
		opposite = Objects.requireNonNull(oppositeToOrigin, "Null opposite to origin position argument");
	}
	
	public VideoFrame(int frameNumber, Collection<VideoFrame> frames){
		if(frameNumber < 0 )
			throw new IllegalArgumentException("Negative frameNumber argument");
		this.frameNumber = frameNumber;
		origin = new Position(
				frames.stream()
				.mapToInt(
						frame -> frame.origin.getX())
				.min().orElseThrow(),
				frames.stream()
				.mapToInt(
						frame -> frame.origin.getY())
				.min().orElseThrow());
		opposite = new Position(
				frames.stream()
				.mapToInt(
						frame -> frame.opposite.getX())
				.min().orElseThrow(),
				frames.stream()
				.mapToInt(
						frame -> frame.opposite.getY())
				.min().orElseThrow());
		data = frames.stream()
				.map(VideoFrame::getData)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public int getFrameNumber() {
		return frameNumber;
	}
	
	public Position getOrigin() {
		return origin;
	}

	public Collection<Byte> getData() {
		return Collections.unmodifiableCollection(data);
	}
	
	public Double getRatio(){
		return Double.valueOf(
				(double) (opposite.getX() - origin.getX()) / 
				opposite.getY() - origin.getX());
	}

}
