package Controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/ShowTablesServlet")
public class ShowTablesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String url = "jdbc:mysql://localhost:3306/servlet";
        String user = "root";
        String password = "sql123";

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Retrieve all table names for the specific database ("servlet")
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getTables("servlet", null, "%", new String[] {"TABLE"});

            out.println("<html><body>");
            out.println("<h2>List of Tables in 'servlet' Database</h2>");
            out.println("<ul>");
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                out.println("<li><a href='ShowTableDataServlet?tableName=" + tableName + "'>" + tableName + "</a></li>");
            }
            out.println("</ul>");
            out.println("</body></html>");

            // Close resources
            resultSet.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
