package com.appspot.vlsakurajima.info;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * 桜島噴火情報取得クラス用の設定エンティティ
 * @author ashphy
 * @license The MIT License
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Setting {
	
	/** 設定用エンティティID */
	public final static Long SETTING_ID = 1L;
	
	@PrimaryKey
    @Persistent
    private Long id;
    
    /** 噴火情報の最終取得日時 */
    @Persistent
    private Date last_modified;
    
    /** 最後に取得した噴火情報詳細ページのURL */
    @Persistent
    private String lastEruptInfoURL;
	
    /** 最後に取得したMentionのID */
    @Persistent
    private Long memtionSinceId = 1L;
    
	public Setting(Long id) {
    	this.id = id;
    	this.lastEruptInfoURL = "VG20101211184155_506.html";
    }
    
	public Setting(Long id, Date last_modified, String lastEruptInfoURL) {
    	this.id = id;
    	this.last_modified = last_modified;
    	this.lastEruptInfoURL = lastEruptInfoURL;
	}

	/**
     * IDを取得します
     * @return ID
     */
    public Long getId() {
		return id;
	}

	/**
	 * IDを設定します
	 * @param id ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 噴火情報の最終取得日時を取得する
	 * @return 噴火情報の最終取得日時
	 */
	public Date getLast_modified() {
		return last_modified;
	}

	/**
	 * 噴火情報の最終取得日時を設定する
	 * @param last_modified 噴火情報の最終取得日時
	 */
	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}
	
	/**
	 * 最後に取得した噴火情報詳細ページのURLを取得する
	 * @return
	 */
	public String getLastEruptInfoURL() {
		return lastEruptInfoURL;
	}

	/**
	 * 最後に取得した噴火情報詳細ページのURLを設定する
	 * @param lastEruptInfoURL
	 */
	public void setLastEruptInfoURL(String lastEruptInfoURL) {
		this.lastEruptInfoURL = lastEruptInfoURL;
	}
	
    /**
     * 
     * @return
     */
    public Long getMemtionSinceId() {
		return memtionSinceId;
	}

	/**
	 * 
	 * @param memtionSinceId
	 */
	public void setMemtionSinceId(Long memtionSinceId) {
		this.memtionSinceId = memtionSinceId;
	}
	
	/**
	 * 設定を読み込む
	 * @return 設定
	 */
	public static Setting readSetting() {
		PersistenceManager pm = com.appspot.vlsakurajima.PMF.get().getPersistenceManager();
		try {
			Setting setting = pm.getObjectById(Setting.class, SETTING_ID);
			return setting;
		} catch (JDOObjectNotFoundException e) {
			//If setting is not found, make new setting
			return new Setting(SETTING_ID);
		} finally {
			pm.close();
		}
	}
	
	/**
	 * 設定を保存する
	 * @param setting 設定
	 */
	public static void saveSetting(Setting setting) {
		PersistenceManager pm = com.appspot.vlsakurajima.PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(setting);
		}
		finally { 
			pm.close();
		}
	}
}
