package ru.ifmo.rain.khusainov.refactoring.query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleHandler implements ResultSetHandler {
    @Override
    public void handle(ResultSet rs, HttpServletResponse response) throws SQLException, IOException {
        response.getWriter().println(rs.getInt(1));
    }
}
