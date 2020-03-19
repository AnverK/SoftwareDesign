package ru.ifmo.rain.khusainov.refactoring.servlet;

import ru.ifmo.rain.khusainov.refactoring.DBConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductServlet extends WithDBServlet {

    public AddProductServlet(DBConnection dbConnection) {
        super(dbConnection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        int added = 0;
        try {
            String query = "INSERT INTO PRODUCT " + "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            added = dbConnection.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        if (added > 0) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("OK");
        } else {
            assert added == 0;
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            response.getWriter().println("No items were inserted");
        }

    }
}