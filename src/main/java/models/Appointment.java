package models;
//5810404936 Yarnadhis Poolsawat
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//5810404936 Yarnadhis Poolsawat
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private String title ;
    private String description ;
    private Integer id ;
    private  DateFormat dateTimeFormat ;


    //    private String hour ;
//    private String minute ;
    private Date date;
//    private int dayNum ;
//    private int monthNum ;
//    private int yearNum ;

    public Appointment(String title,String desc,int dayNum,int monthNum,int yearNum,int hour,int minute,int id) throws ParseException {
        this.id = id ;
        this.title = title ;
        description = desc ;


        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = dateTimeFormat.parse(dayNum+"/"+monthNum+"/"+yearNum+" "+hour+":"+minute);
    }

    public String getString() {
        String dateStr = dateTimeFormat.format(date);

//        System.out.println("Reached here");
        return "###"+id+"###\nAppointment : " + title + "\nDescription : "+ description + "\nOn : " +dateStr+ "\n" ;

    }

    public Integer getId(){
        return this.id ;
    }






}
