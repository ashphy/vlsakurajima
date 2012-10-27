package com.appspot.vlsakurajima;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.appspot.vlsakurajima.info.EruptInfo;
import com.appspot.vlsakurajima.info.SakurajimaEruptInfo;
import com.appspot.vlsakurajima.tweet.MessageBuilder;
import com.appspot.vlsakurajima.tweet.TwitterHelper;

@SuppressWarnings("serial")
public class SakurajimaServlet extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
		logger.info("SakurajimaServlet.doGet()");
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Sakurajima eruption crawler");
		resp.getWriter().print(new Date().toString());
		
		SakurajimaEruptInfo info = new SakurajimaEruptInfo();
		List<EruptInfo> eruptInfos = info.getNewInfo();
		for (EruptInfo eruptInfo : eruptInfos) {
			String mes = MessageBuilder.getMessageFromEruptInfo(eruptInfo);
			TwitterHelper.send(mes);
			resp.getWriter().println(mes);
		}
		
		logger.info("MessageBuilder.buildMessageFromMentions");
		MessageBuilder.buildMessageFromMentions(TwitterHelper.getNewMentions());
	}
}
