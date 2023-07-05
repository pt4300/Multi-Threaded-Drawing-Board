package RMI;
import Utility.*;
import User.Client;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.gson.*;
public class IServerImplementation extends UnicastRemoteObject implements IServer {

    private ConcurrentHashMap<String, Client> clientMap;
    private List<String> messageList;
    private List<ShapeObject> shapeObjectList;



    private Gson gson = new Gson();
    public IServerImplementation() throws RemoteException {
        super();
        clientMap = new ConcurrentHashMap<>();
        messageList = new CopyOnWriteArrayList<>();
        shapeObjectList = new CopyOnWriteArrayList<>();
    }
    public String getClientPrivilege(String username){
        if(clientMap.containsKey(username)){
            return clientMap.get(username).getPrivilege();
        }
        return "null";
    }
    @Override
    public boolean registerClient(String username,String userPrivilege) throws RemoteException {
        if (!clientMap.containsKey(username)) {
            clientMap.put(username, new Client(username,userPrivilege));
            return true;
        }
        return false;
    }
    @Override
    public void disconnect(String username) throws java.rmi.RemoteException{
        if(clientMap.containsKey(username)){
            clientMap.remove(username);
        }
    };
    @Override
    public boolean sendMessage(String username, String message) throws java.rmi.RemoteException{
        if (clientMap.containsKey(username)) {
            SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder sb = new StringBuilder(currentTime.format(new Date()));
            MessageObject mb = new MessageObject(username, message,sb.toString());
            messageList.add(gson.toJson(mb));
            System.out.println("Server message list size: " + messageList.size());
            System.out.println(messageList);
            return true;
        }
        return false;
    };
    @Override
    public List<String> getMessages(String username) throws java.rmi.RemoteException{
        List<String> updatedMessage = new ArrayList<>();
        if(clientMap.containsKey(username)) {

            int startingIdx = clientMap.get(username).messageCounter;
            for(int i = startingIdx; i < messageList.size(); i++){
                updatedMessage.add(messageList.get(i));
            }
//            update the shape counter to the latest shape
            clientMap.get(username).messageCounter = messageList.size();
            return updatedMessage;
        }

        return updatedMessage;
    };
    @Override
    public void addShape(ShapeObject shapeObject) throws java.rmi.RemoteException{
        System.out.println("Server shape list size: " + shapeObjectList.size());
        shapeObjectList.add(shapeObject);
    };

    @Override
    public List<String> getJsonShapes(String username) throws java.rmi.RemoteException{
        List<String> updatedList = new ArrayList<>();
        if(clientMap.containsKey(username)){
            int startingIdx = clientMap.get(username).shapeCounter;

            for(int i = startingIdx; i < shapeObjectList.size(); i++){
                ShapeObject currentShape = shapeObjectList.get(i);
                if(i==0){
                    String jsonObject = gson.toJson(shapeObjectList.get(i));
                    updatedList.add(jsonObject);
                }else{
                    ShapeObject previousShape = shapeObjectList.get(i-1);
                    // preventing the list keep copying last shape after loading
                    if(currentShape.getX1()!=(previousShape.getX1())
                            && currentShape.getY1()!=(previousShape.getY1())
                            && currentShape.getX2()!=(previousShape.getX2())
                            && currentShape.getY2()!=(previousShape.getY2())
                    ){
                        String jsonObject = gson.toJson(shapeObjectList.get(i));
                        updatedList.add(jsonObject);
                    }
                }

            }

            clientMap.get(username).shapeCounter = shapeObjectList.size();
            return updatedList;
        }
        return updatedList;
    };
    @Override
    public List<String> getUsername() throws java.rmi.RemoteException{
        List<String> usernameList = new ArrayList<>();
        for(String username : clientMap.keySet()){
            usernameList.add(username);
        }
        return usernameList;
    };
    @Override
    public String getClientStatus(String username) throws java.rmi.RemoteException{
        if(clientMap.containsKey(username)){
            return clientMap.get(username).getStatus();
        }
        return "null";
    }

    @Override
    public ConcurrentHashMap<String, Client> getClientMap() throws java.rmi.RemoteException{
        return clientMap;
    }
    @Override
    public void clearShape() throws java.rmi.RemoteException{
        for(Client client : clientMap.values()){
            client.setIsClear(true);
        }
        shapeObjectList.clear();

    }
    @Override
    public void updateClientClearFlag(String username, boolean flag) throws java.rmi.RemoteException{
        if(clientMap.containsKey(username)){
            clientMap.get(username).setIsClear(flag);
        }
    }
    @Override
    public Client getClient(String username) throws java.rmi.RemoteException{
        return clientMap.get(username);
    }


    @Override
    public boolean acceptClient(String username) throws java.rmi.RemoteException{
        if(clientMap.containsKey(username)){
            clientMap.get(username).setStatus("ACCEPTED");
            return true;
        }
        return false;
    }
    @Override
    public void loadShapeList(List<ShapeObject> shapeObjectList) throws java.rmi.RemoteException{
        this.shapeObjectList = shapeObjectList;
        System.out.println("Server shape list size: " + shapeObjectList.size());
        for(Client client : clientMap.values()){
            // update the shape counter to re-render the shape
            client.shapeCounter = 0;
            client.setIsLoaded(true);
        }
        System.out.println("Server shape list size: " + shapeObjectList.size());
    }
    @Override
    public void updateClientLoadFlag(String username, boolean flag) throws RemoteException{
        if(clientMap.containsKey(username)){
            clientMap.get(username).setIsLoaded(flag);
        }
    }
    @Override
    public void updateClientLoginAttempts(String username) throws RemoteException{
        if(clientMap.containsKey(username)){
            clientMap.get(username).setLoginAttempts(clientMap.get(username).getLoginAttempts()+1);
            System.out.println("Login attempts: " + clientMap.get(username).getLoginAttempts());
        }
    }
}


