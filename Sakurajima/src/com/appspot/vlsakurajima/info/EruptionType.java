package com.appspot.vlsakurajima.info;

/**
 * 噴火現象の種類
 * @author ashphy
 * @license The MIT License
 */
public enum EruptionType {
	
	/** 爆発的噴火 */
	EXPLOSION ("爆発"),
	
	/** 噴火 */
	ERUPTION ("噴火");
	
	/** 日本語現象名 */
	public final String name;
	
	private EruptionType(String name) {
		this.name = name;
	}

	/**
	 * 日本語の現象名に対応するTypeを取得する
	 * @param name 現象名
	 * @return Type
	 */
	public static EruptionType getTypeFromName(String name) {
		for (EruptionType type : values()) {
			if(type.getName().equals(name)) {
				return type;
			}
		}
		
		return null;
	}

	public String getName() {
		return name;
	}
}