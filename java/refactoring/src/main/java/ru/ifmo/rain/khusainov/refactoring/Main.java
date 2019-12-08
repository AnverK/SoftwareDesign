package ru.ifmo.rain.khusainov.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.ifmo.rain.khusainov.refactoring.servlet.AddProductServlet;
import ru.ifmo.rain.khusainov.refactoring.servlet.GetProductsServlet;
import ru.ifmo.rain.khusainov.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        DBConnection dbConnection = new DBConnection();

        context.addServlet(new ServletHolder(new AddProductServlet(dbConnection)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(dbConnection)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(dbConnection)), "/query");

        server.start();
        server.join();
    }
}