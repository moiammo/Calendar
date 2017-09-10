package controllers;
//5810404936 Yarnadhis Poolsawat

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.Appointment;

import javax.xml.soap.Text;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;

import java.time.LocalDate;
import java.util.ArrayList;

public class EditPageController {

    @FXML
    private TextField callDataField ;
    @FXML
    private DatePicker datePicker ;

//
    @FXML
    private TextField titleField ;
    @FXML
    private TextField descField ;
    @FXML
    private TextField hourField ;
    @FXML
    private TextField minuteField ;

    private Integer calledDataNumber ;




    @FXML
    private void handleBtnAddChanges() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);

            if (conn != null) {

//                UPDATE Customers
//                SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'
//                WHERE CustomerID = 1;

//                String query = "select * from Appointments where " + "ID=" + callDataField.getText();

//                System.out.println("UPDATE QUERY CALLED ");
                LocalDate time = datePicker.getValue();
                String query = "update Appointments \n" +
                        "set title  =\'" + titleField.getText()   + "\'," +
                        "desc = \'"+ descField.getText()    + "\'," +
                        "dayNum = \'"+ time.getDayOfMonth()   + "\'," +
                        "monthNum = \'"+ time.getMonthValue()   + "\'," +
                        "yearNum = \'"+ time.getYear()         + "\'," +
                        "hour = \'"+ hourField.getText()    + "\'," +
                        "minute  = \'"+ minuteField.getText()  + "\'\n"+
                        "where id = " + calledDataNumber;
                System.out.println(query);


                Statement statement = conn.createStatement();
                statement.executeUpdate(query);

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int collectCalledIdNumber(){
        return Integer.parseInt(callDataField.getText()) ;
    }

    @FXML
    private void handleBtnCallData() {
        calledDataNumber = collectCalledIdNumber() ;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);

            if (conn != null) {
                System.out.println(callDataField.getText()+ " called************");
                String query = "select * from Appointments where " + "ID=" + callDataField.getText();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    System.out.println("TEST");
                    datePicker.setValue(LocalDate.of(resultSet.getInt(5),
                                                     resultSet.getInt(4),
                                                     resultSet.getInt(3)));
                    titleField.setText(resultSet.getString(1));
                    descField.setText(resultSet.getString(2));
                    hourField.setText(resultSet.getString(6));
                    minuteField.setText(resultSet.getString(7));

                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    }
