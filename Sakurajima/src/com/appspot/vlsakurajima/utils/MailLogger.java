package com.appspot.vlsakurajima.utils;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;

public class MailLogger {

	private static Logger mailLog = Logger.getLogger(MailLogger.class.getName());
	
	static {
		MailHandler mailHandler = new MailHandler();
		try {
			mailHandler.setFrom(new InternetAddress("ne.vivam.si.abis@gmail.com", "ashphy"));
			mailHandler.setTo(new InternetAddress("ne.vivam.si.abis@gmail.com", "ashphy"));
			mailLog.addHandler(mailHandler);		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		return mailLog;
	}
}
