package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// URL path for accessing this servlet
@WebServlet("/edit-task")
public class UpdateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Task task = taskRepository.read(id);
                if (task != null) {
                    request.setAttribute("task", task);
                    request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp").forward(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    request.setAttribute("message", "Task with ID '" + id + "' not found!");
                    request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("message", "Invalid Task ID format!");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Task ID is missing!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String priorityStr = request.getParameter("priority");
        int id = Integer.parseInt(idStr);
        Task taskById = taskRepository.read(id);
        if (taskById == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "Task with ID '" + id + "' not found!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }

        if (title != null && !title.isEmpty() && priorityStr != null && !priorityStr.isEmpty()) {
            Priority priority = Priority.valueOf(priorityStr);
            Task task = new Task(id, title, priority);

            boolean updated = taskRepository.update(task);

            if (updated) {
                response.sendRedirect("/tasks-list");
            } else {
                request.setAttribute("error", "Task with a given name already exists!");
                request.setAttribute("title", title);
                request.setAttribute("priority", priority);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
                requestDispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("error", "Title and priority must not be empty!");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
