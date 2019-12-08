package ru.ifmo.rain.khusainov.refactoring.servlet;

import ru.ifmo.rain.khusainov.refactoring.DBConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;


public class GetProductsServlet extends WithDBServlet {

    private final static String QUERY = "SELECT * FROM PRODUCT";

    public GetProductsServlet(DBConnection dbConnection) {
        super(dbConnection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (ResultSet rs = dbConnection.executeQuery(QUERY)) {
            response.getWriter().println("<html><body>");
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}