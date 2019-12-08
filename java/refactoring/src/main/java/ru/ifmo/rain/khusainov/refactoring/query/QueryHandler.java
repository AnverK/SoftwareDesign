package ru.ifmo.rain.khusainov.refactoring.query;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHandler {

    public static void handleQuery(ResultSet rs, HttpServletResponse response, String description,
                                   ResultSetHandler handler) throws IOException, SQLException {
        response.getWriter().println("<html><body>");
        if (description != null) {
            response.getWriter().println(description);
        }
        while (rs.next()) {
            handler.handle(rs, response);
        }
        response.getWriter().println("</body></html>");
    }

}
