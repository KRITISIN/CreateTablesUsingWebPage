package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ShowTableDataServlet")
public class ShowTableDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String url = "jdbc:mysql://localhost:3306/servlet";
        String user = "root";
        String password = "sql123";

        String tableName = request.getParameter("tableName");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Retrieve column names
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(null, null, tableName, null);

            // Create a list to store column names
            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }

            // Display table data
            out.println("<html><body>");
            out.println("<h2>Table: " + tableName + "</h2>");

            // Display column names
            out.println("<table border='1'>");
            out.println("<tr>");
            for (String columnName : columnNames) {
                out.println("<th>" + columnName + "</th>");
            }
            out.println("</tr>");

            // Display existing data
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery("SELECT * FROM " + tableName);
            while (data.next()) {
                out.println("<tr>");
                for (String columnName : columnNames) {
                    out.println("<td>" + data.getString(columnName) + "</td>");
                }
                out.println("</tr>");
            }
            out.println("</table>");

            // Insert data form
            out.println("<h2>Insert Data</h2>");
            out.println("<form method='post'>");
            for (String columnName : columnNames) {
                out.println(columnName + ": <input type='text' name='" + columnName + "'><br>");
            }
            out.println("<input type='hidden' name='tableName' value='" + tableName + "'>");
            out.println("<input type='submit' value='Insert'>");
            out.println("<a href=index.html>Home Page</a>");
            out.println("</form>");

            out.println("</body></html>");

            // Close resources
            columns.close();
            data.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "jdbc:mysql://localhost:3306/servlet";
        String user = "root";
        String password = "sql123";

        String tableName = request.getParameter("tableName");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Retrieve column names
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(null, null, tableName, null);

            // Create a list to store column names
            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }

            // Insert data into the table
            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
            for (String columnName : columnNames) {
                query.append(columnName).append(",");
            }
            query.deleteCharAt(query.length() - 1); // Remove the last comma
            query.append(") VALUES (");
            for (String columnName : columnNames) {
                String columnValue = request.getParameter(columnName);
                query.append("'").append(columnValue).append("',");
            }
            query.deleteCharAt(query.length() - 1); // Remove the last comma
            query.append(")");

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query.toString());

            // Close resources
            columns.close();
            stmt.close();
            conn.close();

            // Redirect back to the ShowTableDataServlet
            response.sendRedirect("ShowTableDataServlet?tableName=" + tableName);
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
