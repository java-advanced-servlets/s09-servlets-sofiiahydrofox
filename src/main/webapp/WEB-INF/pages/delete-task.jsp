<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <title>Read existing Task</title>
    <style>
        <%@include file="../styles/main.css"%>
    </style>
</head>
<body>
    <%@include file="header.html"%>

    <div class="details-container">
        <h2>Task Details</h2>
        <%
            Task task = (Task) request.getAttribute("task");
            if (task != null) {
        %>
        <div class="task-info">
            <p><strong>ID:</strong> <%= task.getId() %></p>
            <p><strong>Title:</strong> <%= task.getTitle() %></p>
            <p><strong>Priority:</strong> <%= task.getPriority() %></p>
        </div>
        <form action="${pageContext.request.contextPath}/delete-task?id=<%= task.getId() %>" method="post">

            <button type="submit" class="btn btn-success">Confirm </button>
            <a href="${pageContext.request.contextPath}/tasks-list" class="btn">Cancel</a>
        </form>
        <%
            } else {
        %>
        <p class="alert alert-danger">Task information is missing.</p>
        <%
            }
        %>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/tasks-list" class="btn">Back to List</a>
        </div>
    </div>
</body>
</html>
