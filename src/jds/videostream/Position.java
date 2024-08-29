package jds.videostream;

import java.util.Objects;

public final class Position {
	
	private int x;
	private int y;
	
	public Position(int x ,int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isCongruentWith(Position other) {
		return isNotNull(other) && x == other.x && y == other.y;
	}
	
	public boolean isBelow(Position other) {
		return isNotNull(other) && y <= other.y;
	}
	
	public boolean isAbove(Position other) {
		return isNotNull(other) && y >= other.y;
	}
	
	public boolean isToTheRightOf(Position other) {
		return isNotNull(other) && x >= other.x ;
	}
	
	public boolean isToTheLeftOf(Position other) {
		return isNotNull(other) && x <= other.x;
	}
	
	public boolean isAtTopRightOf(Position other) {
		return isNotNull(other) && x >= other.x && y >= other.y;
	}
	
	public boolean isAtTopLeftOf(Position other) {
		return isNotNull(other) && x <= other.x && y >= other.y;
	}
	
	public boolean isAtBottomLeftOf(Position other) {
		return isNotNull(other) && x <= other.x && y <= other.y;
	}
	
	public boolean isAtBottomRightOf(Position other) {
		return isNotNull(other) && x >= other.x && y <= other.y;
	}
	
	private boolean isNotNull(Position other) {
		return !Objects.isNull(other);
	}
	
	public Position bottomLeftIntersection(Position other) {
		return new Position(
				(this.isToTheLeftOf(other))
					? this.x
					: other.x,
				(this.isBelow(other))
					? this.y
					: other.y);
	}
	
	public Position topRightIntersection(Position other) {
		return new Position(
				(this.isToTheRightOf(other))
					? this.x
					: other.x,
				(this.isAbove(other))
					? this.y
					: other.y);
	}
	
}