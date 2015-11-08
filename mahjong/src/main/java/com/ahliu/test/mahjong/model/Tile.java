package com.ahliu.test.mahjong.model;

public class Tile {

	public enum Type {
		EAST(101), SOUTH(102), WEST(103), NORTH(104),
		RED_DRAGON(201), GREEN_DRAGON(202), WHITE_DRAGON(203),
		FLOWER1(301), FLOWER2(302), FLOWER3(303), FLOWER4(304),
		FLOWER_EAST(401), FLOWER_SOUTH(402), FLOWER_WEST(403), FLOWER_NORTH(404),
		CHAR1(501), CHAR2(502), CHAR3(503), CHAR4(504), CHAR5(505), CHAR6(506), CHAR7(507), CHAR8(508), CHAR9(509),
		CIRCLE1(601), CIRCLE2(602), CIRCLE3(603), CIRCLE4(604), CIRCLE5(605), CIRCLE6(606), CIRCLE7(607), CIRCLE8(608), CIRCLE9(609),
		BAMBOO1(701), BAMBOO2(702), BAMBOO3(703), BAMBOO4(704), BAMBOO5(705), BAMBOO6(706), BAMBOO7(707), BAMBOO8(708), BAMBOO9(709);

		private final int value;

		private Type(int value) {
			this.value = value;
		}
	}

	private Type type;

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
