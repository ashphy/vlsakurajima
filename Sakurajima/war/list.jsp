<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.appspot.vlsakurajima.tweet.MessageBuilder" %>
<%@ page import="com.appspot.vlsakurajima.tweet.Message" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>桜島噴火告知Bot メッセージ一覧</title>
		<link rel="stylesheet" href="style.css" />
		<link rel="stylesheet" href="message.css" type="text/css" />
	</head>
	<body>
	<h1>桜島噴火告知Bot メッセージ一覧</h1>

	<%
		List<Message> messages = MessageBuilder.getAllMessageOrderByCreated();
	%>

	<p>現在登録されているメッセージは<%= messages.size() %>個です。</p>
	<table id="messagelist" class="sortable tinytable">
	<thead><tr>
		<th class="nosort"><h3>No</h3></th>
		<th class="nosort"><h3>Message</h3></th>
	</tr></thead>
	<tbody>
	<%
		int index = 0;
		for (Message message : messages) {
	%>
		<tr>
			<td><%= index++ %></td>
			<td><%= message.getMessage() %></td>
		</tr>
	<%
		}
	%>
	</tbody>
	</table>
	<script type="text/javascript" src="packed.js"></script>
	<script type="text/javascript">
		var sorter = new TINY.table.sorter('sorter','messagelist',{
			headclass:'head',
			ascclass:'asc',
			descclass:'desc',
			evenclass:'evenrow',
			oddclass:'oddrow',
			evenselclass:'evenselected',
			oddselclass:'oddselected',
			init:true
		});
	</script>
	</body>
</html>