package controllers;
//5810404936 Yarnadhis Poolsawat

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Appointment;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarController  {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea apViewer ;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descField;
    @FXML
    private TextField hourField;
    @FXML
    private TextField minuteField;
    @FXML
    private Button btnEdit;

    @FXML
    private Button BtnDelAll ;

    private Integer id = 0 ;



    private void increaseIdNum(){
        id += 1;
    }

    public int updateAndGetId(){
        increaseIdNum();
        return id ;
    }


    private ArrayList<Appointment> apArray = new ArrayList<>();


    public ArrayList<Appointment> getApArray() {
        return apArray;
    }

    @FXML
    private void initialize(){
        apViewer.setEditable(false);
        datePicker.setValue(LocalDate.now());


    }

    @FXML
    private void handleBtnDelAll(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database....");
                String query = "delete From Appointments" ;

                Statement statement = conn.createStatement();
                statement.executeUpdate(query);

                statement.close();
                conn.close();
            }
        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
        apArray.clear();

    }

    @FXML
    private void handleBtnAdd(){
        try {addAppointment(); }
        catch (ParseException e) {e.printStackTrace();}

        apViewer.setText(getStringFromArrayList(apArray));
        titleField.clear();
        descField.clear();
        hourField.clear();
        minuteField.clear();

    }

    @FXML
    private void handleBtnEdit() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editPage.fxml"));

        try {
            stage.initOwner(btnEdit.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Appointment list");

            EditPageController editController = loader.getController();
//            editController.id = Integer.parseInt(editIDField.getText());
//            editController.setEditByID();

            stage.showAndWait();
//            showAppoint();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void insertAppointmentToDB(String title,String desc,String dayNum,String monthNum,String yearNum,int hour,int minute,int id) {

        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database....");
                String query = "insert into Appointments(title,desc,dayNum,monthNum,yearNum,hour,minute) " +
                        "values (\'"+titleField.getText()+"\',\'"+descField.getText()+"\',\'"+dayNum+"\'"+",\'"+monthNum+"\',\'"+yearNum
                        +"\',\'"+hour+"\',\'"+minute+"\')";

                System.out.println(query);

                //apArray.add(new Appointment(titleField.getText(), descField.getText(), time.getDayOfMonth(),
               // time.getMonthValue(), time.getYear(), hourField.getText(), minuteField.getText(), updateAndGetId()));

                Statement statement = conn.createStatement();
                statement.executeUpdate(query);

                statement.close();
                conn.close();
            }
        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}

    }

    private String getStringFromArrayList(ArrayList<Appointment> array){
        String temp = "" ;
        for (Appointment item : array){
            temp += item.getString() + "\n" ;
        }
        return temp ;
    }

    public void addAppointment() throws ParseException {
        LocalDate time = datePicker.getValue();

        insertAppointmentToDB(titleField.getText(), descField.getText(), time.getDayOfMonth()+"",
                time.getMonthValue()+"", time.getYear()+"", Integer.parseInt(hourField.getText())
                , Integer.parseInt(minuteField.getText()),updateAndGetId());

        apArray.add(new Appointment(titleField.getText(), descField.getText(), time.getDayOfMonth(),
                time.getMonthValue(), time.getYear(), hourField.getText(), minuteField.getText(), updateAndGetId()));






    }


}
