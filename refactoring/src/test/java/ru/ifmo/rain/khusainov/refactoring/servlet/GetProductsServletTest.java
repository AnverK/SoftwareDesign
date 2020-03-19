package ru.ifmo.rain.khusainov.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ru.ifmo.rain.khusainov.refactoring.DBConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class GetProductsServletTest {
    @Mock
    private GetProductsServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private DBConnection dbConnection;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ResultSet rs;
    @Mock
    private PrintWriter printWriter;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dbConnection = mock(DBConnection.class);
        servlet = new GetProductsServlet(dbConnection);
        rs = mock(ResultSet.class);
        printWriter = mock(PrintWriter.class);
    }

    @Test
    public void testGetProducts() throws SQLException, IOException {
        String name = "test";
        int price = 300;

        when(dbConnection.executeQuery(anyString())).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getInt("price")).thenReturn(price);

        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(printWriter, times(1)).println(name + "\t" + price + "</br>");
        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}