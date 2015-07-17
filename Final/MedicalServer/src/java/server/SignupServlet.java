package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AYA
 */
@WebServlet(name = "SignupServlet", urlPatterns = {"/SignupServlet"})
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("user name" + username);
        System.out.println("password" + password);
        if (username != null && password != null) {
            try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical", "root", "root");
                java.sql.Statement CheckStatement = con.createStatement();
                String checkQuery = "select * from user where userName ='" + username + "'";
                ResultSet rs = CheckStatement.executeQuery(checkQuery);
                if (rs.next()) {
                    out.print("exists");
                } else {
                    java.sql.Statement insertStatement = con.createStatement();
                    String insertQuery = "insert into user values( '" + username + "', '" + password + "' )";
                    int insertResult = insertStatement.executeUpdate(insertQuery);
                    out.print("ok");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
}
