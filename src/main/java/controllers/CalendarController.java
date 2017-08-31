package controllers;
//5810404936 Yarnadhis Poolsawat

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Appointment;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarController {


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
    private void handleBtnAdd(){
//        System.out.println("entered");
        try {
//            System.out.println("entered2");
            addAppointment();
//            System.out.println("APPPOINTMENT ADDED");
        } catch (ParseException e) {
//            System.out.println("***********************************");
            e.printStackTrace();
        }
//        System.out.println("entered3");
        apViewer.setText(getStringFromArrayList(apArray));
        titleField.clear();
        descField.clear();
        hourField.clear();
        minuteField.clear();
//        System.out.println("DONE HANDLE ADD BUTTON");


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
//        System.out.println("VALUE GOT ***************************************************");
//        System.out.println(titleField.getText()+"///"+
//                descField.getText()+"///"+
//                time.getDayOfMonth()+"///"+
//                time.getMonthValue()+"///"+
//                time.getYear()+"///"+
//                hourField.getText()+"///"+
//                minuteField.getText());

        apArray.add(new Appointment(
                titleField.getText(),
                descField.getText(),
                time.getDayOfMonth(),
                time.getMonthValue(),
                time.getYear(),
                hourField.getText(),
                minuteField.getText()));
//        System.out.println("OBJECT ADDED ***********************************");
    }

//    private DatePicker datePicker;
//    private TextField titleField;
//    private TextField descField;
//    private TextField hourField;
//    private TextField minuteField;


    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public TextField getDescField() {
        return descField;
    }

    public void setDescField(TextField descField) {
        this.descField = descField;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public TextField getHourField() {
        return hourField;
    }

    public void setHourField(TextField hourField) {
        this.hourField = hourField;
    }

    public TextField getMinuteField() {
        return minuteField;
    }

    public void setMinuteField(TextField minuteField) {
        this.minuteField = minuteField;
    }
}
