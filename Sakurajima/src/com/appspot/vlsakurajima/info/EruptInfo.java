package com.appspot.vlsakurajima.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 火山噴火情報
 * @author ashphy
 * @license The MIT License
 */
public class EruptInfo implements Serializable, Comparable<EruptInfo> {
	
	private static final long serialVersionUID = 975703356517696628L;

	public static final String BASE_URL = "http://www.data.jma.go.jp/svd/vois/data/tokyo/STOCK/volinfo/";
	
	/** 火山名 */
	private final String mountName;
	
	/** 噴火日時 */
	private final Date date;
	
	/** 噴火現象 */
	private final EruptionType type;
	
	/** 噴煙の流向 */
	private final Direction direction;
	
	/** 今年の爆発回数 */
	private final int numOfExplosion;
	
	/** 詳細ページへのURL */
	private final String url;
	
	/**
	 * 指定の引数を用いて噴火情報を構築する
	 * @param mountName 火山名
	 * @param date 噴火日時
	 * @param type 噴火現象
	 * @param direction 噴煙の流向
	 * @param numOfExplosion 今年の爆発回数
	 */
	public EruptInfo(String mountName, Date date, EruptionType type,
			Direction direction, int numOfExplosion, String url) {
		super();
		this.mountName = mountName;
		this.date = date;
		this.type = type;
		this.direction = direction;
		this.numOfExplosion = numOfExplosion;
		this.url = url;
	}
	
	/**
	 * 指定された噴火情報詳細ページのURLらら
	 * @param url
	 */
	public static EruptInfo getInsranceFromURL(String url) {
		String[] detailedInfo = fetch(url);
		return parse(detailedInfo, url);
	}

	/**
	 * 詳細ページのHTMLを解析しEruptInfoを構築する
	 * @param detaildInfo
	 * @param url
	 * @return
	 */
	private static EruptInfo parse(String[] detailedInfo, String url) {
		String mountName = null;
		Date date = null;
		EruptionType type = null;
		Direction direction = null;
		int numOfExplosion = 0;
		
		for (int i = 0; i < detailedInfo.length; i++) {
			String[] cells = detailedInfo[i].split("：");
			if(cells.length == 2 && cells[0].equals("火　　山")) {
				mountName = cells[1];
			} else if(isSecondeReport(detailedInfo[i])) {
				//第２報の噴火情報は作成しない
				return null;
			} else if(cells.length == 2 && cells[0].equals("日　　時")) {
				//ex. 2010年12月12日14時55分（120555UTC）
				DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH時mm分", Locale.JAPAN);
				try {
					date = df.parse(cells[1]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else if(cells.length == 2 && cells[0].equals("現　　象")) {
				type = EruptionType.getTypeFromName(cells[1]);
			}
			else if(cells.length == 2 && cells[0].equals("流　　向")) {
				direction = Direction.getDirectionFromName(cells[1]);
			}
			else {
				final Pattern p = Pattern.compile("今年(\\d+)回目");
				Matcher m = p.matcher(detailedInfo[i]);
				if(m.find() && 0 < m.groupCount()) {
					numOfExplosion = Integer.parseInt(m.group(1));
				}
			}
		}
		
		return new EruptInfo(mountName, date, type, direction, numOfExplosion, url);
	}
	
	/**
	 * 噴火情報の詳細ページを取得する
	 * @param url
	 * @return
	 */
	private static String[] fetch(String url) {
		try {
			URL detailedPageUrl = new URL(BASE_URL + url);
			HttpURLConnection connection = (HttpURLConnection) detailedPageUrl.openConnection();
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF8")));
	            String line;
	            List<String> results = new ArrayList<String>();
	            
	            while ((line = in.readLine()) != null) {
	            	results.add(line);
	            }
	            
	            return results.toArray(new String[0]);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static boolean isSecondeReport(String line) {
		return line.contains("第2報");
	}
	
	public String getMountName() {
		return mountName;
	}

	public Date getDate() {
		return date;
	}

	public EruptionType getType() {
		return type;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getNumOfExplosion() {
		return numOfExplosion;
	}
	
	/**
	 * 噴火情報詳細ページへのURL
	 * @return
	 */
	public String getFullURL() {
		return BASE_URL + url;
	}
	
	@Override
	public String toString() {
		return "EruptInfo [mountName=" + mountName + ", date=" + date
				+ ", type=" + type + ", direction=" + direction
				+ ", numOfExplosion=" + numOfExplosion + ", url=" + url + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((mountName == null) ? 0 : mountName.hashCode());
		result = prime * result + numOfExplosion;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EruptInfo other = (EruptInfo) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (direction != other.direction)
			return false;
		if (mountName == null) {
			if (other.mountName != null)
				return false;
		} else if (!mountName.equals(other.mountName))
			return false;
		if (numOfExplosion != other.numOfExplosion)
			return false;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public int compareTo(EruptInfo o) {
		return this.getDate().compareTo(o.getDate());
	}
}