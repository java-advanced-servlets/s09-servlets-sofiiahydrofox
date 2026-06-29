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


// Specifies the URL path for this servlet
@WebServlet("/delete-task")
public class DeleteTaskServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/WEB-INF/pages/delete-task.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        Task taskById = taskRepository.read(id);
        if (taskById == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "Task with ID '" + id + "' not found!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }

        boolean deleted = taskRepository.delete(id);

        if (deleted) {
            response.sendRedirect("/tasks-list");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("message", "Task with ID '" + id + "' was not deleted because of unknown reason");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }
}
