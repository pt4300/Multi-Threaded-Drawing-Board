package Utility;

import java.io.Serializable;

public class MessageObject implements Serializable {
    private String username;
    private String message;
    private String time;
    public MessageObject(String username, String message,String time) {
        this.username = username;
        this.message = message;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public String getTime() {
        return time;
    }
    public String setTime(String time){
        return this.time = time;
    }

}
