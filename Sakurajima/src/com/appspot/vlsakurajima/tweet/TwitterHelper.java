package com.appspot.vlsakurajima.tweet;

import java.util.Collections;
import java.util.Comparator;

import com.appspot.vlsakurajima.info.Setting;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterHelper {
	
	public static final String CONSUMER_KEY = "TZWccaVTgnAKKlq8alwHWQ";
	public static final String CONSUMER_SECRET = "4naGHTEhxoXPHDhFXyrx0wgm4hl0jpqqLVNZifV78Gg";

	//Debug Consumer key
	//public static final String CONSUMER_KEY = "eVOvfjUxQlqOSlg250FEuA";
	//public static final String CONSUMER_SECRET = "Q4IQJjo8A7wV9AnYfmPgUdPEpk9D6DCVSmzQzPVKE";
	
	public static final String TOKEN = "220313791-vIeZW0G3DvuZb0XZSIidl4lVBz5oXZOtDZlzegcN";
	public static final String TOKEN_SECRET = "vxD3ScTxiOvJvz9sBxO7oux1tr5bEc6agjyFnvxqQc";
	
	//Debug Token Sakurajima Dev
	//public static final String TOKEN = "227506603-n1rPEkkSMqZePULjjUF5Ee9q0OHygMzY3T3S3B6R";
	//public static final String TOKEN_SECRET = "1tyJ1oCRhir8awqz1XM6364bVevHlKvYaEBBz5tdvY";
	
	/**
	 * Tweetする
	 * @param tweet
	 */
	public static void send(String tweet) {
		Twitter twitter = getTwitter();
		
		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			//TODO Add retry queue
			e.printStackTrace();
		}
	}
	
	/**
	 * DMを送信する
	 * @param screenName スクリーンネーム
	 * @param text 送信メッセージ
	 */
	public static void sendDM (String screenName, String text) {
		Twitter twitter = getTwitter();
		try {
			twitter.sendDirectMessage(screenName, text);
		} catch (TwitterException e) {
			//TODO Add retry queue
			e.printStackTrace();
		}
	}
	
	/**
	 * 新規のMentionを取得する
	 * @return
	 */
	public static ResponseList<Status> getNewMentions() {
		Twitter twitter = getTwitter();
		
		try {
			Long sinceId = Setting.readSetting().getMemtionSinceId();
			ResponseList<Status> statuses;
			if(sinceId != null) {
				Paging paging = new Paging(sinceId);
				statuses = twitter.getMentions(paging);
			} else {
				statuses = twitter.getMentions();
			}
			
			//どこまでMentionを取得したのかを記録する
			if(!statuses.isEmpty()) {
				Collections.sort(statuses, new Comparator<Status>() {
					@Override
					public int compare(Status o1, Status o2) {
						return o1.getCreatedAt().compareTo(o2.getCreatedAt());
					}
				});
				Status last = statuses.get(statuses.size() - 1);
				Setting setting = Setting.readSetting();
				setting.setMemtionSinceId(last.getId());
				Setting.saveSetting(setting);
			}
			
			return statuses;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 認証済みTwitterインスタンスを取得する
	 * @return
	 */
	public static Twitter getTwitter() {
		ConfigurationBuilder confbuilder = new ConfigurationBuilder(); 
		confbuilder
			.setOAuthAccessToken(TOKEN) 
			.setOAuthAccessTokenSecret(TOKEN_SECRET) 
			.setOAuthConsumerKey(CONSUMER_KEY) 
			.setOAuthConsumerSecret(CONSUMER_SECRET)
			.setUseSSL(true);
		return new TwitterFactory(confbuilder.build()).getInstance();		
	}
}
