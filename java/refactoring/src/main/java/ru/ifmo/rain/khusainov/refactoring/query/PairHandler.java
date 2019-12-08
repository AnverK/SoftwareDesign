package ru.ifmo.rain.khusainov.refactoring.query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PairHandler implements ResultSetHandler {

    @Override
    public void handle(ResultSet rs, HttpServletResponse response) throws SQLException, IOException {
        String name = rs.getString("name");
        int price = rs.getInt("price");
        response.getWriter().println(name + "\t" + price + "</br>");
    }
}
