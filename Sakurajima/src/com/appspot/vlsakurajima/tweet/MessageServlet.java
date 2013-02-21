package com.appspot.vlsakurajima.tweet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");
		if(action != null && action.equals("del")) {
			del(req, resp);
		}
	}
	
	protected void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(Long.parseLong(req.getParameter("id")));
		MessageBuilder.deleteMessage(Long.parseLong(req.getParameter("id")));
		
		resp.sendRedirect("list.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String message = req.getParameter("message");
		if(message != null && !message.isEmpty()) {
			String[] messages = message.split("\n");
			for (String mes : messages) {
				MessageBuilder.saveMessage(mes, null);
			}
		}
		
		resp.sendRedirect("list.jsp");
	}	
}