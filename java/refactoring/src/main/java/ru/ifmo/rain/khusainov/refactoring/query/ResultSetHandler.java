package ru.ifmo.rain.khusainov.refactoring.query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {
    void handle(ResultSet rs, HttpServletResponse response) throws SQLException, IOException;
}
