package ru.ifmo.rain.khusainov.refactoring.servlet;

import ru.ifmo.rain.khusainov.refactoring.DBConnection;

import javax.servlet.http.HttpServlet;

public abstract class WithDBServlet extends HttpServlet {
    protected DBConnection dbConnection;

    public WithDBServlet(DBConnection dbConnection) {
        super();
        this.dbConnection = dbConnection;
    }

}
