package com.appspot.vlsakurajima.tweet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
		} else {
			index(req, resp);
		}
	}

	protected void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=UTF-8");
		
		PrintWriter w = resp.getWriter();
		w.println("<html>");
		w.println("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		w.println("<link rel=\"stylesheet\" href=\"message.css\" type=\"text/css\" />");
		w.println("</head><body>");
		w.println("<h1>Message list</h1>");
		
		w.println("<table>");
		w.println("<tr><th>ID</th><th>Message</th><th>Count</th><th>Created</th><th>Delete</th></tr>");
		
		List<Message> messages = MessageBuilder.getAllMessageOrderByCreated();
		for (Message message : messages) {
			w.println("<tr>");
				w.println("<td>" + message.getId() + "</td>");
				w.println("<td>" + message.getMessage() + "</td>");
				w.println("<td>" + message.getPublishedCount() + "</td>");
				w.println("<td>" + message.getCreated() + "</td>");
				w.println("<td><a href=\"./message?action=del&id=" +  message.getId() + "\">DEL</a></td>");
			w.println("</tr>");
		}
		w.println("</table>");
		
		w.println("<h2>Add new message</h2>");
		w.println("<form method=\"post\" action=\"message\">");
		w.println("<textarea name=\"message\" rows=\"10\" cols=\"80\"></textarea>");
		w.println("<input type=\"submit\" value=\"Add\">");
		w.println("</form>");
	
		w.println("</body></html>");		
	}
	
	protected void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(Long.parseLong(req.getParameter("id")));
		MessageBuilder.deleteMessage(Long.parseLong(req.getParameter("id")));
		
		resp.sendRedirect("message");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String message = req.getParameter("message");
		if(message != null && !message.isEmpty()) {
			String[] messages = message.split("\n");
			for (String mes : messages) {
				MessageBuilder.saveMessage(mes);
			}
		}
		
		resp.sendRedirect("message");
	}	
}