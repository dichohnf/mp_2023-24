package jds.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PositionTest {

	Position leftBottom;
	Position leftMiddle;
	Position leftTop;
	Position centerBottom;
	Position centerMiddle;
	Position centerTop;
	Position rightBottom;
	Position rightMiddle;
	Position rightTop;
	List<Position> positions;
	static final int LEFT = -2234;
	static final int CENTER = -23;
	static final int RIGHT = 3135;
	static final int BOTTOM = -1651;
	static final int MIDDLE = 16;
	static final int TOP = 6420;
	
	
	@Before
	public void setUp() {
		leftBottom	= new Position(LEFT, BOTTOM);
		leftMiddle	= new Position(LEFT, MIDDLE);
		leftTop		= new Position(LEFT, TOP);
		centerBottom= new Position(CENTER, BOTTOM);
		centerMiddle= new Position(CENTER, MIDDLE);
		centerTop	= new Position(CENTER, TOP);
		rightBottom	= new Position(RIGHT, BOTTOM);
		rightMiddle	= new Position(RIGHT, MIDDLE);
		rightTop	= new Position(RIGHT, TOP);
		positions = List.of(
				leftBottom, leftMiddle, leftTop,
				centerBottom, centerMiddle, centerTop, 
				rightBottom, rightMiddle, rightTop);
	}

	@Test
	public void testIsCongruentWith() {
		assertThat(positions.stream()
				.anyMatch(
						pos1 -> positions.stream()
							.filter(pos2 -> !pos1.equals(pos2))
							.anyMatch(pos1::isCongruentWith)))
			.isFalse();
		assertThat(leftMiddle.isCongruentWith(new Position(LEFT, MIDDLE)))
			.isTrue();
	}

	@Test
	public void testIsBelow() {
		assertThat(positions.stream()
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isBelow(pos2)
								? pos1
								: pos2)))
			.contains(leftBottom);
		assertThat(positions.stream()
				.skip(3)
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isBelow(pos2)
								? pos1
								: pos2)))
			.contains(centerBottom);
		assertThat(positions.stream()
				.skip(6)
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isBelow(pos2)
								? pos1
								: pos2)))
			.contains(rightBottom);
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isBelow(pos2)
								? pos1
								: pos2)))
			.isNotEmpty().get()
			.isIn(leftBottom, centerBottom, rightBottom);
	}

	@Test
	public void testIsAbove() {
		assertThat(positions.stream()
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isAbove(pos2)
								? pos1
								: pos2)))
			.contains(leftTop);
		assertThat(positions.stream()
				.skip(3)
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isAbove(pos2)
								? pos1
								: pos2)))
			.contains(centerTop);
		assertThat(positions.stream()
				.skip(6)
				.limit(3)
				.reduce(
						(pos1, pos2) -> (pos1.isAbove(pos2)
								? pos1
								: pos2)))
			.contains(rightTop);
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isAbove(pos2)
								? pos1
								: pos2)))
			.isNotEmpty().get()
			.isIn(leftTop, centerTop, rightTop);
	}

	@Test
	public void testIsToTheRightOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isToTheRightOf(pos2)
								? pos1
								: pos2)))
			.isNotEmpty().get()
			.isIn(rightBottom, rightMiddle, rightTop);
	}

	@Test
	public void testIsToTheLeftOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isToTheLeftOf(pos2)
								? pos1
								: pos2)))
			.isNotEmpty().get()
			.isIn(leftBottom, leftMiddle, leftTop);
	}

	@Test
	public void testIsAtTopRightOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isAtTopRightOf(pos2)
								? pos1
								: pos2)))
			.contains(rightTop);
	}

	@Test
	public void testIsAtTopLeftOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isAtTopLeftOf(pos2)
								? pos1
								: pos2)))
			.contains(leftTop);
	}

	@Test
	public void testIsAtBottomLeftOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isAtBottomLeftOf(pos2)
								? pos1
								: pos2)))
			.contains(leftBottom);
	}

	@Test
	public void testIsAtBottomRightOf() {
		assertThat(positions.stream()
				.reduce(
						(pos1, pos2) -> (pos1.isAtBottomRightOf(pos2)
								? pos1
								: pos2)))
			.contains(rightBottom);
	}

}
