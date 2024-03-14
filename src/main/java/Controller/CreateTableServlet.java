package Controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/CreateTableServlet")
public class CreateTableServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

       
        String tableName = request.getParameter("table");

        
        String url = "jdbc:mysql://localhost:3306/servlet";
        String user = "root";
        String password = "sql123";

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Create table query
            StringBuilder query = new StringBuilder("CREATE TABLE ");
            query.append(tableName).append(" (");

            String[] columnNames = request.getParameterValues("name");
            String[] dataTypes = request.getParameterValues("datatype");
            String[] lengths = request.getParameterValues("len");
            String[] defaults = request.getParameterValues("default");

            for (int i = 0; i < columnNames.length; i++) {
                if (!columnNames[i].isEmpty() && !dataTypes[i].isEmpty()) {
                    query.append(columnNames[i]).append(" ").append(dataTypes[i]);
                    if (dataTypes[i].equals("varchar") || dataTypes[i].equals("char")) {
                        if (!lengths[i].isEmpty()) {
                            query.append("(").append(lengths[i]).append(")");
                        }
                    }
                    if (!defaults[i].isEmpty()) {
                        query.append(" DEFAULT '").append(defaults[i]).append("'");
                    }
                    query.append(",");
                }
            }
            query.deleteCharAt(query.length() - 1); // Remove the last comma
            query.append(")");

            // Execute query
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query.toString());

            out.println("<html><body>");
            out.println("<h3>Table " + tableName + " created successfully!</h3>");
            out.println("</body></html>");

            // Close resources
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
