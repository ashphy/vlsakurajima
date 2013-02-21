package com.appspot.vlsakurajima.tweet;

import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Logger;

import com.appspot.vlsakurajima.info.Setting;
import com.appspot.vlsakurajima.utils.PropertiesUtil;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterHelper {
	
	private static Logger log = Logger.getLogger(TwitterHelper.class.getName());
	
	/**
	 * Tweetする
	 * @param tweet
	 */
	public static void send(String tweet) {
		Twitter twitter = getTwitter();
		
		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			log.warning(e.getErrorMessage());
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
			log.warning(e.getErrorMessage());
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
				statuses = twitter.getMentionsTimeline(paging);
			} else {
				statuses = twitter.getMentionsTimeline();
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
			log.warning(e.getErrorMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 認証済みTwitterインスタンスを取得する
	 * @return
	 */
	public static Twitter getTwitter() {
		
		Properties prop = PropertiesUtil.getProperiesFromResource("/com/appspot/vlsakurajima/tweet/twitter_account.properties");	
			
		ConfigurationBuilder confbuilder = new ConfigurationBuilder(); 
		confbuilder
			.setOAuthAccessToken(prop.getProperty("token")) 
			.setOAuthAccessTokenSecret(prop.getProperty("token_secret")) 
			.setOAuthConsumerKey(prop.getProperty("consumer_key")) 
			.setOAuthConsumerSecret(prop.getProperty("consumer_secret"))
			.setUseSSL(true);
		return new TwitterFactory(confbuilder.build()).getInstance();		
	}
}
