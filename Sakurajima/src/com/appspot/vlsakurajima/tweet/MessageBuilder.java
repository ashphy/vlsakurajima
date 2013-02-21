package com.appspot.vlsakurajima.tweet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;

import com.appspot.vlsakurajima.PMF;
import com.appspot.vlsakurajima.info.EruptInfo;
import com.appspot.vlsakurajima.info.EruptionType;
import com.appspot.vlsakurajima.utils.PropertiesUtil;
import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Bitly.Provider;
import com.rosaloves.bitlyj.Url;

/**
 * Tweet用のメッセージを生成するクラス
 * @author ashphy
 */
public class MessageBuilder {

	/**
	 * メッセージの検証結果
	 */
	private static String validateMessage = "";
	
	public static String getMessageFromEruptInfo(EruptInfo info) {
		StringBuilder message = new StringBuilder();
		
		//メッセージ
		message.append(MessageBuilder.getMessageWithRank().getMessage());
		
		//流向
		message.append(" 流向:");
		message.append(info.getDirection().getDirectionName());
		if(!info.getDirection().getCity().isEmpty()) {
			message
			.append("(")
			.append(info.getDirection().getCity())
			.append("方面)");
		}
		message.append(" ");
		
		//噴火回数
		if(info.getType() == EruptionType.EXPLOSION) {
			message
				.append(info.getType().getName())
				.append(":")
				.append(info.getNumOfExplosion())
				.append("回目");
		} else if(info.getType() == EruptionType.ERUPTION) {
			message.append(info.getType().getName());
		}
		
		//日時
		DateFormat df = new SimpleDateFormat(" yyyy年MM月dd日HH時mm分");
		message.append(df.format(info.getDate()));
		message.append(" ");
		
		//リンク
		message.append(getShotURL(info.getFullURL()));
		
		return message.toString();
	}
	
	public static String getShotURL(String url) {
		Properties prop = PropertiesUtil.getProperiesFromResource("/com/appspot/vlsakurajima/tweet/bitly.properties");
		Provider provider = Bitly.as(prop.getProperty("bitly_username"), prop.getProperty("bitly_apikey"));
		Url shortenURL = provider.call(Bitly.shorten(url));
		return shortenURL.getShortUrl();
	}
	
	/**
	 * 新しいメッセージを保存する
	 * @param message 新規メッセージ
	 */
	public static boolean saveMessage(String message, User user) {
		if(validateMessage(message)) {
			Message m = new Message(message, user, 0);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistent(m);
				return true;
			} finally {
				pm.close();
			}
		}
		
		return false;
	}
	
	/**
	 * メッセージを削除します
	 * @param id
	 */
	public static void deleteMessage(Long id) {
		if(id != null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Message message = pm.getObjectById(Message.class, id);
				pm.deletePersistent(message);
			} finally {
				pm.close();
			}
		}
	}
	
	/**
	 * Mentionから新規メッセージを登録する
	 * @param mentions
	 */
	public static void buildMessageFromMentions(ResponseList<Status> mentions) {
		if(mentions == null)
			return;
		
		for (Status status : mentions) {
			Pattern p = Pattern.compile("^@(\\w{1,15}(?!\\w))[ 　]add[ 　](.+)");
			Matcher m = p.matcher(status.getText());
			if(m.matches()) {
				//メッセージの保存
				if(saveMessage(m.group(2), status.getUser())) {
					//確認DMの送信
					String message = "メッセージ追加ありがとう！ 「" + m.group(2) + "」を追加したよ。";
					TwitterHelper.sendDM(status.getUser().getScreenName(), message);
				} else {
					//不正な形式のメッセージ
					System.out.println(status.getUser().getScreenName());
					System.out.println(MessageBuilder.getValidateMessage());
					
					TwitterHelper.sendDM(status.getUser().getScreenName(), MessageBuilder.getValidateMessage());
				}
			}
		}
	}
	
	/**
	 * メッセージを検証する
	 * @param message メッセージ
	 * @return 正しいメッセージならtrue
	 */
	private static boolean validateMessage(String message) {
		
		//長さ制限
		if(80 < message.length()) {
			validateMessage = "メッセージが長すぎます";
			return false;
		}
		
		//爆発を含むメッセージ
		if(message.contains("爆発")) {
			validateMessage = "「爆発」が含まれている発言は登録できません";
			return false;
		}
		
		//重複登録の確認
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Message.class);
			query.setFilter("message == '" + message + "'");
			@SuppressWarnings("unchecked")
			List<Message> list = (List<Message>) query.execute();
			if(!list.isEmpty()) {
				validateMessage = "同じメッセージが既に登録されています";
				return false;
			}
		} finally {
			pm.close();
		}
		
		return true;
	}
	
	/**
	 * メッセージの一覧を取得する
	 * @return メッセージ全件
	 */
	public static List<Message> getAllMessage() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Message.class);
			query.setOrdering("publishedCount acs");
			@SuppressWarnings("unchecked")
			List<Message> messages = (List<Message>) query.execute();
			pm.detachCopyAll(messages);
			return messages;
		} finally {
			pm.close();
		}
	}
	
	/**
	 * メッセージの一覧を取得する
	 * @return メッセージ全件
	 */
	public static List<Message> getAllMessageOrderByCreated() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<Message> extent = pm.getExtent(Message.class);
			List<Message> list = new ArrayList<Message>();
			for (Message message : extent) {
				list.add(message);
			}
			extent.closeAll();
			Collections.sort(list);
			return list;
		} finally {
			pm.close();
		}
	}
	
	/**
	 * 指定された表示回数のメッセージの中からランダムでメッセージを取得する
	 * @param count 表示回数
	 * @return
	 */
	public static Message getMessageWithPublishedCount(int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Message.class);
			query.setFilter("publishedCount == " + count);
			@SuppressWarnings("unchecked")
			List<Message> messages = (List<Message>) query.execute();
			//Make random index
			Random rnd = new Random();
			int index = rnd.nextInt(messages.size());
			Message m = messages.get(index);
			pm.detachCopy(m);
			return m;
		} finally {
			pm.close();
		}
	}
	
	/**
	 * メッセージの表示回数を１つ増やす
	 * @param message
	 */
	public static void incrementPublishCount(Message message) {
		message.incrementPublishCount();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(message);
		} finally {
			pm.close();
		}
	}
	
	/**
	 * ランキング法を用いてメッセージを選択する
	 * @return メッセージ
	 */
	public static Message getMessageWithRank() {
		List<Message> messages = getAllMessage();
		int minCount = messages.get(0).getPublishedCount();
		Message message = getMessageWithPublishedCount(minCount);
		
		incrementPublishCount(message);
		return message;
	}
	
	public static String getValidateMessage() {
		return validateMessage;
	}
}
