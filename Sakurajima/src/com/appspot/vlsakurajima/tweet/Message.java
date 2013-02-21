package com.appspot.vlsakurajima.tweet;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import twitter4j.User;

/**
 * ツイート用のメッセージ
 * @author ashphy
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Message implements Comparable<Message> {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	/** メッセージ */
	@Persistent
	private String message;
	
	/** 表示回数 */
	@Persistent
	private int publishedCount = 0;

	/** 作成日 */
	@Persistent
	private Date created;

	@Persistent
	private String authorScreenName;
	
	@Persistent
	private Long authorId = 0L;
	
	public Message(String message, User user, int publishedCount) {
		this.message = message;
		this.publishedCount = publishedCount;
		this.created = new Date();
		if(user != null) {
			this.authorId = user.getId();
			this.authorScreenName = user.getScreenName();
		}
	}
	
	/**
     * IDを取得します
     * @return ID
     */
    public Long getId() {
		return id;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void incrementPublishCount() {
		this.publishedCount++;
	}

	public int getPublishedCount() {
		return publishedCount;
	}

	public void setPublishedCount(int publishedCount) {
		this.publishedCount = publishedCount;
	}
	
	public Date getCreated() {
		return created;
	}

	public String getAuthorScreenName() {
		return authorScreenName;
	}

	public void setAuthorScreenName(String authorScreenName) {
		this.authorScreenName = authorScreenName;
	}

	public long getAuthorId() {
		if(authorId != null) {
			return authorId;
		}
		
		return -1L;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message
				+ ", publishedCount=" + publishedCount + ", created=" + created
				+ ", authorScreenName=" + authorScreenName + ", authorId="
				+ authorId + "]";
	}

	@Override
	public int compareTo(Message o) {
		if(getCreated() == null && o.getCreated() == null) {
			return 0;
		}else if(getCreated() == null) {
			return -1;
		} else if(o.getCreated() == null) {
			return 1;
		}
		
		return getCreated().compareTo(o.getCreated());
	}
}
