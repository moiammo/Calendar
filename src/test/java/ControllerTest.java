import controllers.CalendarController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Appointment;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ControllerTest {

    Appointment testAppointment ;
    @Before
    public void setUp() throws ParseException {

       testAppointment = new Appointment("For testing","With JUnit",
               31,8,2017,"11","50");

    }

    @Test
    public void test() {

    //"Appointment : " + title + "\nDescription : "+ description + "\nOn : " +dateStr+ "\n" ;
//     date = dateTimeFormat.parse(dayNum+"/"+monthNum+"/"+yearNum+" "+hour+":"+minute);
    assertEquals("test",testAppointment.getString(), "Appointment : For testing\nDescription : With JUnit\nOn : 8/31/17 11:50 AM\n");
    }

}