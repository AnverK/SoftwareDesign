package ru.ifmo.rain.khusainov.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ru.ifmo.rain.khusainov.refactoring.DBConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class AddProductServletTest {

    @Mock
    private AddProductServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private DBConnection dbConnection;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;


    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dbConnection = mock(DBConnection.class);
        servlet = new AddProductServlet(dbConnection);
        printWriter = mock(PrintWriter.class);
    }

    @Test
    public void testAddProducts() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("name")).thenReturn("test");
        when(request.getParameter("price")).thenReturn("300");

        when(dbConnection.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"test\",300)")).thenReturn(1);

        servlet.doGet(request, response);

        verify(response, times(1)).getWriter();
        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).println("OK");
        verifyNoMoreInteractions(response);
    }

}
