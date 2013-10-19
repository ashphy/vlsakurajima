package com.appspot.vlsakurajima.utils;


import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail Handler for GAE/J
 * <p>
 * Usage:
 * <pre>
 * {@code
 * Logger log = Logger.getLogger(GmailHandlerServlet.class.getName());
 * MailHandler handler = new MailHandler();
 * handler.setFrom(new InternetAddress("ashphy@ashphy.com", "ashphy"));
 * handler.setTo(new InternetAddress("ashphy@ashphy.com", "ashphy"));	
 * log.addHandler(handler);		
 * log.severe("An servere message.");
 * }
 * </pre>
 * 
 * @author ashphy<ashphy@ashphy.com>
 */
public class MailHandler extends Handler {

	private InternetAddress form = null;
	private InternetAddress to = null;

	public MailHandler() {
		// Set default settings
		setLevel(Level.SEVERE);
		setFilter(null);
		setFormatter(new SimpleFormatter());
		try {
			setEncoding("UTF-8");
		} catch (SecurityException | UnsupportedEncodingException e) {
			setFormatter(null);
		}
	}

	@Override
	public void publish(LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}

		String msgBody;
		try {
			msgBody = getFormatter().format(record);
		} catch (Exception ex) {
			reportError(null, ex, ErrorManager.FORMAT_FAILURE);
			return;
		}

		try {
			Session session = Session
					.getDefaultInstance(new Properties(), null);
			Message mineMsg = new MimeMessage(session);
			mineMsg.setFrom(this.form);
			mineMsg.addRecipient(Message.RecipientType.TO, this.to);
			mineMsg.setSubject(getFormatter().getHead(this));
			mineMsg.setText(msgBody);
			Transport.send(mineMsg);
		} catch (Exception ex) {
			reportError(null, ex, ErrorManager.WRITE_FAILURE);
		}
	}

	public void setFrom(InternetAddress address) {
		this.form = address;
	}

	public void setTo(InternetAddress address) {
		this.to = address;
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if (this.form == null || this.to == null) {
			return false;
		}
		return super.isLoggable(record);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}
