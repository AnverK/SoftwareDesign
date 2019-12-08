package ru.ifmo.rain.khusainov.refactoring.servlet;

import ru.ifmo.rain.khusainov.refactoring.DBConnection;
import ru.ifmo.rain.khusainov.refactoring.query.PairHandler;
import ru.ifmo.rain.khusainov.refactoring.query.QueryHandler;
import ru.ifmo.rain.khusainov.refactoring.query.ResultSetHandler;
import ru.ifmo.rain.khusainov.refactoring.query.SingleHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

public class QueryServlet extends WithDBServlet {
    public QueryServlet(DBConnection dbConnection) {
        super(dbConnection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        ResultSetHandler pairHandler = new PairHandler();
        ResultSetHandler singleHandler = new SingleHandler();
        if ("max".equals(command)) {
            try (ResultSet rs = dbConnection.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")) {
                QueryHandler.handleQuery(rs, response, "<h1>Product with max price: </h1>", pairHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try (ResultSet rs = dbConnection.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                QueryHandler.handleQuery(rs, response, "<h1>Product with min price: </h1>", pairHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try (ResultSet rs = dbConnection.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                QueryHandler.handleQuery(rs, response, "Summary price: ", singleHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try (ResultSet rs = dbConnection.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                QueryHandler.handleQuery(rs, response, "Number of products: ", singleHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}