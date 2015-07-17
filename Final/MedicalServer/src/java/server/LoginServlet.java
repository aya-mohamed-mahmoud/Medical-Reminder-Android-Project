package server;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AYA
 */
@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/LoginServlet"}
)
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username " + username);
        System.out.println("password " + password);
        if (username != null && password != null) {
            try {
                //connect to DB//
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical", "root", "root");
                java.sql.Statement statement = con.createStatement();
                //select * from user where userName='username' AND password='password'
                String query = "select * from user where userName ='" + username + "' AND password = '" + password + "'";
                ResultSet rs = statement.executeQuery(query);

                if (rs.next()) {
                    //out.print("yes");//for login success
                    System.out.println("yes");
                    String selectQuery = "select * from medicine where userName='" + username + "'";
                    //String selectQuery = "select * from medicine where userName='aya'";       

                    //to get medecine assigned to user//
                    ResultSet resultSet = statement.executeQuery(selectQuery);
                    ArrayList<Medicine> listofMedicine = new ArrayList<Medicine>();
                    Gson gson = new Gson();
                    while (resultSet.next()) {
                        System.out.println("in while");
                        Medicine medicine = new Medicine();
                        medicine.setName(resultSet.getString(2));//get name column from db
                        medicine.setDose(resultSet.getDouble(3));
                        medicine.setType(resultSet.getString(4));
                        medicine.setFrecuency(resultSet.getInt(5));
                        medicine.setInterval(resultSet.getString(6));
                        medicine.setStartTime(resultSet.getLong(7));
                        medicine.setStartDate(resultSet.getLong(8));
                        medicine.setEndDate(resultSet.getLong(9));
                        medicine.setImage(resultSet.getString(10));
                        listofMedicine.add(medicine);
                    }
                    String jsonObj = gson.toJson(listofMedicine);
                    //System.out.println(jsonObj);
                    out.print(jsonObj);

                } else {
                    out.print("no");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }

        }

    }
}
