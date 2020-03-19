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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class QueryServletTest {

    private class Product {
        private int price;
        private String name;

        public Product(int price, String name) {
            this.price = price;
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }
    }

    @Mock
    private QueryServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private DBConnection dbConnection;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;

    @Mock
    private ResultSet maxRes;
    @Mock
    private ResultSet minRes;
    @Mock
    private ResultSet sumRes;
    @Mock
    private ResultSet countRes;

    private List<Product> products;

    @Before
    public void init() throws SQLException, IOException {
        dbConnection = mock(DBConnection.class);
        when(dbConnection.executeUpdate(startsWith("INSERT INTO PRODUCT")))
                .then(invocation -> {
                    String query = invocation.getArgumentAt(0, String.class);
                    Pattern pattern = Pattern.compile("INSERT INTO PRODUCT \\(NAME, PRICE\\) VALUES \\(\"([^\"]+)\",(\\d+)\\)");
                    Matcher matcher = pattern.matcher(query);
                    assertTrue("Insert query '" + query + "' doesn't match pattern '" + matcher + "'", matcher.matches());
                    String name = matcher.group(1);
                    int price = Integer.parseInt(matcher.group(2));
                    products.add(new Product(price, name));
                    return 1;
                });

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servlet = new QueryServlet(dbConnection);
        products = new ArrayList<>();

        List<Product> products = Arrays.asList(
                new Product(1, "A"),
                new Product(2, "B"),
                new Product(3, "C"),
                new Product(4, "D"),
                new Product(2, "E")
        );

        products.forEach(this::addProduct);
        verify(dbConnection, times(products.size())).executeUpdate(startsWith("INSERT INTO PRODUCT"));

        maxRes = mock(ResultSet.class);
        when(maxRes.next()).thenReturn(true, false);
        when(maxRes.getString("name")).thenReturn(getMaxPriceProduct().getName());
        when(maxRes.getInt("price")).thenReturn(getMaxPriceProduct().getPrice());
        when(dbConnection.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")).thenReturn(maxRes);

        minRes = mock(ResultSet.class);
        when(minRes.next()).thenReturn(true, false);
        when(minRes.getString("name")).thenReturn(getMinPriceProduct().getName());
        when(minRes.getInt("price")).thenReturn(getMinPriceProduct().getPrice());
        when(dbConnection.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")).thenReturn(minRes);

        sumRes = mock(ResultSet.class);
        when(sumRes.next()).thenReturn(true, false);
        when(sumRes.getInt(1)).thenReturn(getSumOfProducts());
        when(dbConnection.executeQuery("SELECT SUM(price) FROM PRODUCT")).thenReturn(sumRes);

        countRes = mock(ResultSet.class);
        when(countRes.next()).thenReturn(true, false);
        when(countRes.getInt(1)).thenReturn(products.size());
        when(dbConnection.executeQuery("SELECT COUNT(*) FROM PRODUCT")).thenReturn(countRes);

        printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testMax() throws IOException {
        when(request.getParameter("command")).thenReturn("max");
        servlet.doGet(request, response);
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with max price: </h1>");
        Product pr = getMaxPriceProduct();
        verify(printWriter).println(pr.name + "\t" + pr.price + "</br>");
        verify(printWriter).println("</body></html>");
        verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void testMin() throws IOException {
        when(request.getParameter("command")).thenReturn("min");
        servlet.doGet(request, response);
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("<h1>Product with min price: </h1>");
        Product pr = getMinPriceProduct();
        verify(printWriter).println(pr.name + "\t" + pr.price + "</br>");
        verify(printWriter).println("</body></html>");
        verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void testSum() throws IOException {
        when(request.getParameter("command")).thenReturn("sum");
        servlet.doGet(request, response);
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Summary price: ");
        verify(printWriter).println(getSumOfProducts().intValue());
        verify(printWriter).println("</body></html>");
        verifyNoMoreInteractions(printWriter);
    }

    @Test
    public void testCount() throws IOException {
        when(request.getParameter("command")).thenReturn("count");
        servlet.doGet(request, response);
        verify(printWriter).println("<html><body>");
        verify(printWriter).println("Number of products: ");
        verify(printWriter).println(products.size());
        verify(printWriter).println("</body></html>");
        verifyNoMoreInteractions(printWriter);
    }

    private Product getMaxPriceProduct() {
        return products.stream().max(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    private Product getMinPriceProduct() {
        return products.stream().min(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    private Integer getSumOfProducts() {
        return products.stream().map(Product::getPrice).reduce(Integer::sum).orElse(null);
    }

    private void addProduct(Product product) {
        String sqlQuery = "INSERT INTO PRODUCT " + "(NAME, PRICE) " +
                "VALUES (\"" + product.name + "\"," + product.price + ")";
        try {
            dbConnection.executeUpdate(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
