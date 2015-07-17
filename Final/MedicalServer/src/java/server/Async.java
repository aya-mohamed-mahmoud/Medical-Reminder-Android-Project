/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AYA
 */
@WebServlet(name = "Async", urlPatterns = {"/Async"})
public class Async extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = " ";
        String type = "";
        double dose = 0;
        int frequency = 0;
        String interval = "";
        long startDate = 0;
        long startTime = 0;
        long endDate = 0;
        String image = "default";
        Gson gson = new Gson();
        String jasonData = "";

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical", "root", "root");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            while (br.ready()) {
                jasonData += br.readLine() + "\n";
            }
            System.out.println("json" + jasonData);
            Medicine[] medicineObj = gson.fromJson(jasonData, Medicine[].class);
            System.out.println("len " + medicineObj.length);
            //delete data from db where username=eli gay
            String userName = medicineObj[0].getUserName();//get user name
            String deleteQuery = "delete from medicine where userName = '" + userName + "'";
            Statement deleteSt = con.createStatement();
            deleteSt.executeUpdate(deleteQuery);
            for (int counter = 0; counter < medicineObj.length; counter++) {
                name = medicineObj[counter].getName();
                type = medicineObj[counter].getType();
                dose = medicineObj[counter].getDose();
                frequency = medicineObj[counter].getFrecuency();
                interval = medicineObj[counter].getInterval();
                startDate = medicineObj[counter].getStartDate();
                startTime = medicineObj[counter].getStartTime();
                endDate = medicineObj[counter].getEndDate();
                userName = medicineObj[counter].getUserName();
                image = medicineObj[counter].getImage();
                System.out.println(name);

                //insert new data in DB
                String query = "INSERT INTO medicine (myname,dose,mytype,frequency,myinterval,startTime,startDate,endDate,image,userName) VALUES('" + name + "'," + dose + ",'" + type + "'," + frequency + ",'" + interval + "'," + startTime + "," + startDate + "," + endDate + ",'" + image + "','" + userName + "')";
                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                System.out.println(query);
            }
            System.out.println("done");

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

    }
}
