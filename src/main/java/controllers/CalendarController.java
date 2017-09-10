package controllers;
//5810404936 Yarnadhis Poolsawat

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Appointment;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarController  {

    @FXML
    private ComboBox<String> reptCBox ;
    @FXML
    private TextField reptField;
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
    private int id;
//
//
//
//    private void increaseIdNum(){
//        id += 1;
//    }
//
//    public int getId(){
//        return id ;
//    }

    private ArrayList<String> reptCBoxList = new ArrayList<>();
    private ArrayList<Appointment> apArray = new ArrayList<>();



    @FXML
    private void initialize(){
        reptCBoxList.add("Never");reptCBoxList.add("Day");reptCBoxList.add("Week");
        reptCBoxList.add("Month");reptCBoxList.add("Year");
        reptCBox.getItems().addAll(reptCBoxList) ;
        reptCBox.getSelectionModel().selectFirst();

        apViewer.setEditable(false);
        datePicker.setValue(LocalDate.now());
        try
        {
            apArray = loadDataFromDB() ;
        }
        catch (ParseException ex) {ex.printStackTrace();}
    }

    @FXML
    private void handleBtnDelAll(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
//                System.out.println("Connected to the database....");
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
        handleBtnShowAll();


    }

    @FXML
    private void handleBtnAdd(){
        LocalDate time = datePicker.getValue();
        String title = titleField.getText();
        String desc = descField.getText();
        int day = time.getDayOfMonth();
        int month = time.getMonthValue();
        int year = time.getYear();
        int hour = Integer.parseInt(hourField.getText());
        int minute = Integer.parseInt(minuteField.getText());
        int id = getLastIdFromDB();

        try {
            addAppointment(title,desc,day,month,year,hour,minute);
//            System.out.println(reptCBox.getValue());

        if (!("Never".equals(reptCBox.getValue()))){
            repeatEvery(reptCBox.getValue(),title,desc,day,month,year,hour,minute,id);
            }

        }
        catch (ParseException e) {e.printStackTrace();}
        titleField.clear();
        descField.clear();
        hourField.clear();
        minuteField.clear();
        reptField.clear();
    }

    @FXML
    private void handleBtnEdit() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editPage.fxml"));
        try {
            stage.initOwner(btnEdit.getScene().getWindow());
            stage.setScene(new Scene((Parent) loader.load()));
            stage.setTitle("Appointment Editor");
//            EditPageController editController = loader.getController();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void insertAppointmentToDB(String title,String desc,int dayNum,int monthNum,int yearNum,int hour,int minute) {
        //Called in addAppointment
        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
//                System.out.println("Connected to the database....");
                String query = "insert into Appointments(title,desc,dayNum,monthNum,yearNum,hour,minute) " +
                        "values (\'"+titleField.getText()+"\',\'"+descField.getText()+"\',\'"+dayNum+"\'"+",\'"+monthNum+"\',\'"+yearNum
                        +"\',\'"+hour+"\',\'"+minute+"\')";
//                System.out.println(query);
                Statement statement = conn.createStatement();
                statement.executeUpdate(query);
                statement.close();
                conn.close();
            }
        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
    }

    private void repeatEvery(String repeatition,String title,String desc,int day,int month,int year,
                             int hour,int minute,int id) throws ParseException{
        int reptNum = Integer.parseInt(reptField.getText()) ;

        if ("Day".equals(repeatition)){
            for (int i=1;i<reptNum;i++){ //i for date adding
                addAppointment(title,desc,day+(i*reptNum),month,year,hour,minute);
            }
//            System.out.println("Done repeat "+ reptNum +"day");
        }
        else if ("Week".equals(repeatition)){
            for (int i=1;i<reptNum;i++){
                addAppointment(title,desc,day+(i*7),month,year,hour,minute);
            }
//            System.out.println("Done repeat "+ reptNum +"week");
        }

        else if ("Month".equals(repeatition)){
            for (int i=1;i<reptNum;i++){
                addAppointment(title,desc,day,month+i,year,hour,minute);
            }
//            System.out.println("Done repeat "+ reptNum +"month");
        }

        else if ("Year".equals(repeatition)){
            for (int i=1;i<reptNum;i++){
                addAppointment(title,desc,day,month,year+i,hour,minute);
            }
//            System.out.println("Done repeat "+ reptNum +"year");
        }


    }


    @FXML
    private void handleBtnShowAll() {
        String temp = "" ;
        for (Appointment item : apArray){
            temp += item.getString() + "\n" ;
        }
        apViewer.setText(temp);

    }

//    private String getStringFromArrayList(ArrayList<Appointment> array){
//        String temp = "" ;
//        for (Appointment item : array){
//            temp += item.getString() + "\n" ;
//        }
//        return temp ;
//    }

    public void addAppointment(String title,String desc,int day,int month,int year,int hour,int minute) throws ParseException {
        insertAppointmentToDB(title,desc,day,month,year,hour,minute);
//        int id = getLastIdFromDB()+1;
//        apArray.add(new Appointment(title,desc,day,month,year,hour,minute,id));
        apArray = loadDataFromDB();

    }


    public ArrayList<Appointment> loadDataFromDB() throws ParseException{
        ArrayList<Appointment> tempArray = new ArrayList<>() ;
        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                String query = "select * from Appointments ";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
//                    System.out.println("TEMP ADDED");
                    tempArray.add(new Appointment(resultSet.getString(1), //title
                            resultSet.getString(2), //desc
                            resultSet.getInt(3), //day
                            resultSet.getInt(4), //month
                            resultSet.getInt(5), //year
                            resultSet.getInt(6), //hour
                            resultSet.getInt(7), //minute
                            resultSet.getInt(8))); //id
                }
                statement.close();
                conn.close();
            }
        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
        return tempArray ;
    }

    private int getLastIdFromDB(){
        int tempID = -1 ;
        try
        {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:AppointmentsDB.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                String query = "select max(id) from Appointments" ;
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                tempID = resultSet.getInt(1)+1;
                statement.close();
                conn.close();
            }
        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
        return tempID ;
    }


}
