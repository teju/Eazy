package eazi.com.eazi.database;


/**
 * Created by BasavRaj on 5/8/2018.
 */

public class Messages {

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getMessage_Text() {
        return Message_Text;
    }

    public void setMessage_Text(String message_Text) {
        Message_Text = message_Text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getISMine() {
        return ISMine;
    }

    public void setISMine(String ISMine) {
        this.ISMine = ISMine;
    }

    String From;
    String To;
    String Message_Text;
    String Date;
    String ISMine;


}
