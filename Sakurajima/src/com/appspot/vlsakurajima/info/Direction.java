package com.appspot.vlsakurajima.info;

/**
 * 桜島からの方角
 * @author ashphy
 * @license The MIT License
 */
public enum Direction {

	NORTH ("北", "加治木"),
	EAST ("東", "曽於弥五郎IC"),
	SOUTH ("南", "指宿"),
	West ("西", "鹿児島市"),
	NORTH_EAST ("北東", "国分"),
	SOUTH_EAST ("南東", "垂水"),
	SOUTH_WEST ("南西", "谷山"),
	NORTH_WEST ("北西", "竜ケ水"),
	
	UP ("直上", ""),
	UNKNOWN ("不明", "");

	private final String japaneseName;
	
	private final String city;
	
	private Direction(String japaneseName, String city) {
		this.japaneseName = japaneseName;
		this.city = city;
	}
	
	public String getDirectionName() {
		return this.japaneseName;
	}
	
	public String getCity() {
		return this.city;
	}
	
	/**
	 * 方角名からDirectionを取得します。
	 * @param name 北東、南東などの方角名
	 * @return 認識できない方角の場合はUNKNOWNを返す
	 */
	public static Direction getDirectionFromName(String name) {
		for (Direction direction : values()) {
			if(direction.getDirectionName().equals(name)) {
				return direction;
			}
		}
		
		return UNKNOWN;
	}
}
