package User;

import com.google.gson.Gson;

import java.io.Serializable;

public class Client implements Serializable {
    private String username;
    // PENDING, ACCEPTED, REJECTED
    private String status;
    private String privilege;
    private Gson gson;
    public int shapeCounter = 0;
    public int messageCounter = 0;
    private int loginAttempts = 0;
    private boolean isClear = false;
    private boolean isLoaded = false;


    public Client(String username) {
        this.username = username;
        this.status = "PENDING";
    }
    public Client(String username, String privilege) {
        this.username = username;
        this.status = "PENDING";
        this.privilege = privilege;
        if(privilege.equals("ADMIN")){
            // admin will have default access to server
            this.status = "ACCEPTED";
        }
    }
    public String getUsername() {
        return username;
    }
    public String getStatus() {
        return status;
    }
    public String getPrivilege() {
        return privilege;
    }
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public boolean getIsClear(){
        return this.isClear;
    }
    public void setIsClear(boolean isClear){
        this.isClear = isClear;
    }
    public boolean getIsLoaded(){
        return this.isLoaded;
    }
    public void setIsLoaded(boolean isLoaded){
        this.isLoaded = isLoaded;
    }
    public int getLoginAttempts(){
        return this.loginAttempts;
    }
    public void setLoginAttempts(int loginAttempts){
        this.loginAttempts = loginAttempts;
    }
    public boolean isActive(){
        return this.status.equals("ACCEPTED");
    }

}
