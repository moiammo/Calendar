package controllers;
//5810404936 Yarnadhis Poolsawat

import Services.DataSource;
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

import Services.SqliteDatabase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.crypto.Data;

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
    private DatePicker showDatePicker ;
    @FXML
    private TextField deleteIdField;

//    private int id;
    private ArrayList<String> reptCBoxList = new ArrayList<>();
    private ArrayList<Appointment> apArray = new ArrayList<>();
    private DataSource dataSource;

    @FXML
    private void initialize(){
        ApplicationContext bf = new ClassPathXmlApplicationContext("spring.xml");
        dataSource = (DataSource) bf.getBean("sqlite");
//        dataSource = new SqliteDatabase();
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
    private void handleBtnDelAll() throws ParseException{
        try
        {
            dataSource.establishConnection();
            dataSource.doExecuteUpdate("delete From Appointments");
            dataSource.closeConnection();
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
            insertAppointmentToDB(title,desc,day,month,year,hour,minute);

            if (!("Never".equals(reptCBox.getValue()))){
                repeatEvery(time,reptCBox.getValue(),title,desc,day,month,year,hour,minute,id);
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


    private void insertAppointmentToDB(String title,String desc,int dayNum,int monthNum,int yearNum,int hour,int minute) throws ParseException {
        try
        {
            dataSource.establishConnection();
            String query = "insert into Appointments(title,desc,dayNum,monthNum,yearNum,hour,minute) " +
                        "values (\'"+titleField.getText()+"\',\'"+descField.getText()+"\',\'"+dayNum+"\'"+",\'"+monthNum+"\',\'"+yearNum
                        +"\',\'"+hour+"\',\'"+minute+"\')";
            dataSource.doExecuteUpdate(query);
            dataSource.closeConnection();

            apArray = loadDataFromDB();
        }

        catch (ClassNotFoundException ex) { ex.printStackTrace(); }
        catch (SQLException ex) { ex.printStackTrace();}
        handleBtnShowAll();
    }

    private void repeatEvery(LocalDate time,String repeatition,String title,String desc,int day,int month,int year,
                             int hour,int minute,int id) throws ParseException{
        int reptNum = Integer.parseInt(reptField.getText()) ;

        if ("Day".equals(repeatition)){
            for (int i=1;i<=30;i++){ //i for date adding
                 LocalDate addedDate = time.plusDays(reptNum*i);
                int newDay = addedDate.getDayOfMonth();
                int newMonth = addedDate.getMonthValue();
                int newYear = addedDate.getYear() ;
                insertAppointmentToDB(title,desc,newDay,newMonth,newYear,hour,minute);
            }
        }
        else if ("Week".equals(repeatition)){
            for (int i=1;i<=52;i++){
                LocalDate addedDate = time.plusWeeks(reptNum*i);
                int newDay = addedDate.getDayOfMonth();
                int newMonth = addedDate.getMonthValue();
                int newYear = addedDate.getYear() ;
                insertAppointmentToDB(title,desc,newDay,newMonth,newYear,hour,minute);
            }
        }

        else if ("Month".equals(repeatition)){
            for (int i=1;i<=12;i++){
                LocalDate addedDate = time.plusMonths(reptNum*i);
                int newDay = addedDate.getDayOfMonth();
                int newMonth = addedDate.getMonthValue();
                int newYear = addedDate.getYear() ;
                insertAppointmentToDB(title,desc,newDay,newMonth,newYear,hour,minute);
            }
        }

        else if ("Year".equals(repeatition)){
            for (int i=1;i<=5;i++){
                LocalDate addedDate = time.plusYears(reptNum*i);
                int newDay = addedDate.getDayOfMonth();
                int newMonth = addedDate.getMonthValue();
                int newYear = addedDate.getYear() ;
                insertAppointmentToDB(title,desc,newDay,newMonth,newYear,hour,minute);
            }
        }
    }


    @FXML
    private void handleBtnShowAll() throws ParseException {
        apArray = loadDataFromDB();
        String temp = "" ;
        for (Appointment item : apArray){
            temp += item.getString() + "\n" ;
        }
        apViewer.setText(temp);

    }



    @FXML
    private void handleBtnShow() {
        String strToShow = "" ;
        try {
            dataSource.establishConnection();
            if (!(showDatePicker.getValue() == null)){
                LocalDate selectedDate = showDatePicker.getValue();
                int selDay = selectedDate.getDayOfMonth();
                int selMonth = selectedDate.getMonthValue();
                int selYear = selectedDate.getYear();

                String query = "select * from Appointments where " +
                        "dayNum=" + selDay +
                        " and monthNum=" + selMonth +
                        " and yearNum=" + selYear;
                ResultSet resultSet = dataSource.doExecuteQuery(query);

                while (resultSet.next()) {
                        //"###"+id+"###\nAppointment : " + title + "\nDescription : "+ description + "\nOn : " +dateStr+ "
                        //SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String tempStr = "###" + resultSet.getInt(8) + "###\n" +
                            "Appointment : " + resultSet.getString(1) + "\n" +
                            "Description : " + resultSet.getString(2) + "\n" +
                            "On : " + resultSet.getInt(3) + "/" + resultSet.getInt(4) + "/" + resultSet.getInt(5) +
                            " " + resultSet.getInt(6) + ":" + resultSet.getInt(7)
                            + "\n";
                        strToShow += tempStr;
                    }

                if ("".equals(strToShow)){ apViewer.setText("You not have any appointments today.");}
                else { apViewer.setText(strToShow);}
            }
            dataSource.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBtnDeleteID() throws ParseException {
        String selectedID = deleteIdField.getText();

        if (selectedID != "") {
            try {
                dataSource.establishConnection();
                String query = "DELETE FROM Appointments WHERE id=" + selectedID;
                dataSource.doExecuteUpdate(query);
                dataSource.closeConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        handleBtnShowAll();
    }


    public ArrayList<Appointment> loadDataFromDB() throws ParseException{
        ArrayList<Appointment> tempArray = new ArrayList<>() ;
        try
        {
            dataSource.establishConnection();

                String query = "select * from Appointments ";
                ResultSet resultSet = dataSource.doExecuteQuery(query);
                while (resultSet.next()){
                    tempArray.add(new Appointment(resultSet.getString(1), //title
                            resultSet.getString(2), //desc
                            resultSet.getInt(3), //day
                            resultSet.getInt(4), //month
                            resultSet.getInt(5), //year
                            resultSet.getInt(6), //hour5
                            resultSet.getInt(7), //minute
                            resultSet.getInt(8))); //id
                }
            dataSource.closeConnection();

        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
        return tempArray ;
    }

    private int getLastIdFromDB(){
        int tempID = -1 ;
        try
        {
            dataSource.establishConnection();
            String query = "select max(id) from Appointments" ;
            ResultSet resultSet = dataSource.doExecuteQuery(query);
            tempID = resultSet.getInt(1)+1;
            dataSource.closeConnection();

        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
        return tempID ;
    }

    private void createDBTableIfNotExist(){
        //  CREATE TABLE "Appointments" ( `title` TEXT NOT NULL, `desc` TEXT, `dayNum` INTEGER NOT NULL, `monthNum` INTEGER NOT NULL, `yearNum` INTEGER NOT NULL, `hour` INTEGER NOT NULL, `minute` INTEGER NOT NULL, `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE )
        try
        {
            dataSource.establishConnection();

                ResultSet resultSet = dataSource.getTable(null,null,"Appointments",null);
                while (resultSet.next()){
                    if (resultSet.getString(3).equals("Appointments")){
                        dataSource.closeConnection();
                        return ;
                    }
                }

                String query = "CREATE TABLE \"Appointments\" ( `title` TEXT NOT NULL, `desc` TEXT, `dayNum` INTEGER NOT NULL, `monthNum` INTEGER NOT NULL, `yearNum` INTEGER NOT NULL, `hour` INTEGER NOT NULL, `minute` INTEGER NOT NULL, `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE )";

            dataSource.doExecute(query);
            dataSource.closeConnection();

        }
        catch (ClassNotFoundException ex) {ex.printStackTrace(); }
        catch (SQLException ex) {ex.printStackTrace();}
    }



}
