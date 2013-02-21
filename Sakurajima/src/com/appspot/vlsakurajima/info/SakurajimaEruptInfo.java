package com.appspot.vlsakurajima.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 桜島噴火情報取得クラス
 * @author ashphy
 * @license The MIT License
 */
public class SakurajimaEruptInfo {
	
	/** 噴火に関する火山観測報のページ */
	private static final String JMA_URL = "http://www.seisvol.kishou.go.jp/tokyo/STOCK/volinfo/gensho.html";
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public SakurajimaEruptInfo() {
	}
	
	/**
	 * 新しい噴火情報を取得する
	 * @return 噴火情報
	 */
	public List<EruptInfo> getNewInfo() {
		log.info("getNewInfo()");
		List<EruptInfo> list = new LinkedList<EruptInfo>();
		
		try {
			String[] htmls = fetch();
			List<String> newInfos = excludeOldInfo(parseEruptInfoURLList(htmls));
			for (String url : newInfos) {
				log.info("New Info URL: " + url);
				EruptInfo info = EruptInfo.getInsranceFromURL(url);
				if(info != null) {
					list.add(info);
				}
			}
			Collections.sort(list);
		} catch (IOException e) {
			log.severe("getNewInfo - " + e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 以前に取得した古い噴火情報を取り除く
	 * @param list 噴火情報のURL一覧
	 * @return 新規に取得した噴火情報のURL一覧
	 */
	private List<String> excludeOldInfo(List<String> list) {
		String lastEruptInfoURL = getLastEruptInfoURL();
		log.info("lastEruptInfoURL is " + lastEruptInfoURL);
		
		for (int i = 0; i < list.size(); i++) {
			if(lastEruptInfoURL.compareTo(list.get(i)) >= 0) {
				log.info("Remove old erupt info at" + i);
				list.remove(i);
				i--;
			}
		}

		if(0 < list.size()) {
			saveLastEruptInfoURL(list.get(0));
		}
		
		return list;
	}
	
	/**
	 * 気象庁の噴火情報ページからHTMLを解析し、各噴火情報へのURL一覧を取得する
	 * @param html 噴火情報ページの生HTML
	 * @return 各噴火情報へのURL一覧
	 */
	private List<String> parseEruptInfoURLList(String[] html) {
		final Pattern p = Pattern.compile("href=\"\\./(.*)\".*桜島");
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < html.length; i++) {
			Matcher m = p.matcher(html[i]);
			if(m.find()) {
				list.add(m.group(1));
			} 
		}
		
		return list;
	}
	
	/**
	 * 気象庁から噴火情報を取得する
	 * @return 噴火情報一覧の生HTML
	 * @throws IOException 通信中の例外 
	 */
	private String[] fetch() throws IOException {
		URL url = new URL(JMA_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		//前回の最終取得日時を取得して、
		Date lastModified = getLastModified();
		log.info("Last Modified is " + lastModified.toString()
				);
		// Sun, 12 Dec 2010 15:04:55 GMT
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		if(lastModified != null) {
			log.info("Set If-Modified-Since");
			connection.setRequestProperty("If-Modified-Since", df.format(lastModified));
		}

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //最終取得日時を更新
            saveLastModified(new Date(connection.getLastModified()));
			
            String encoding = connection.getContentEncoding();
            if(encoding == null) {
            	//encoding = "EUC_JP";
            	encoding = "sjis";
            }
            log.info("Detected encode is " + encoding);
            
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(encoding)));
            String line;
            List<String> results = new ArrayList<String>();
            
            while ((line = in.readLine()) != null) {
            	results.add(line);
            }
            
            log.info("Fetch pages's LoC is " + results.size());
            return results.toArray(new String[0]);
            
		} else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
			// no op
			log.info("Not modified");
		} else {
			log.info(String.valueOf(connection.getResponseCode()));
			log.info(connection.getResponseMessage());
			//通信エラー
			throw new IOException(connection.getResponseMessage());
		}
		
		return new String[0];
	}
	
	/**
	 * 前回の噴火情報の最終取得日時を取得する
	 * @return 前回の噴火情報の最終取得日時
	 */
	private Date getLastModified() {
		return Setting.readSetting().getLast_modified();
	}
	
	/**
	 * 前回の噴火情報の最終取得日時を設定する
	 * @param 前回の噴火情報の最終取得日時
	 */
	private void saveLastModified(Date lastModified) {
		Setting setting = Setting.readSetting();
		setting.setLast_modified(lastModified);
		Setting.saveSetting(setting);
	}
	
	/**
	 * 最後に取得した噴火情報詳細ページのURLを取得する
	 * @return URL
	 */
	private String getLastEruptInfoURL() {
		return Setting.readSetting().getLastEruptInfoURL();
	}
	
	/**
	 * 最後に取得した噴火情報詳細ページのURLを設定する
	 * @param lastEruptInfoURL URL
	 */
	private void saveLastEruptInfoURL(String lastEruptInfoURL) {
		Setting setting = Setting.readSetting();
		setting.setLastEruptInfoURL(lastEruptInfoURL);
		Setting.saveSetting(setting);
	}
}
