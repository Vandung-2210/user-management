package com.codegym.controller;

import com.codegym.dao.UserDAO;
import com.codegym.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        try {
            switch (action) {
                case "create":
                    // Gọi Stored Procedure thêm mới
                    userDAO.insertUserStore(new User(request.getParameter("name"), request.getParameter("email"), request.getParameter("country")));
                    response.sendRedirect("users");
                    break;
                case "edit":
                    userDAO.updateUser(new User(Integer.parseInt(request.getParameter("id")), request.getParameter("name"), request.getParameter("email"), request.getParameter("country")));
                    response.sendRedirect("users");
                    break;
                case "delete":
                    userDAO.deleteUser(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect("users");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        try {
            switch (action) {
                case "create":
                    request.getRequestDispatcher("user/create.jsp").forward(request, response);
                    break;
                case "edit":
                    // Gọi Stored Procedure tìm theo ID
                    request.setAttribute("user", userDAO.getUserById(Integer.parseInt(request.getParameter("id"))));
                    request.getRequestDispatcher("user/edit.jsp").forward(request, response);
                    break;
                case "delete":
                    request.setAttribute("user", userDAO.selectUser(Integer.parseInt(request.getParameter("id"))));
                    request.getRequestDispatcher("user/delete.jsp").forward(request, response);
                    break;
                case "search":
                    List<User> searchUsers = userDAO.searchByCountry(request.getParameter("country"));
                    request.setAttribute("listUser", searchUsers);
                    request.getRequestDispatcher("user/list.jsp").forward(request, response);
                    break;
                case "sort":
                    List<User> sortUsers = userDAO.sortByName();
                    request.setAttribute("listUser", sortUsers);
                    request.getRequestDispatcher("user/list.jsp").forward(request, response);
                    break;
                default:
                    List<User> listUser = userDAO.selectAllUsers();
                    request.setAttribute("listUser", listUser);
                    request.getRequestDispatcher("user/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
