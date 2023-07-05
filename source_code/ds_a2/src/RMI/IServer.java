package RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import User.Client;
import Utility.*;

public interface IServer extends Remote {

    boolean registerClient(String username,String userPrivilege) throws RemoteException;

    void disconnect(String username) throws RemoteException;
    boolean sendMessage(String username, String message) throws RemoteException;
    List<String> getMessages(String username) throws RemoteException;

    void addShape(ShapeObject shapeObject) throws RemoteException;
    String getClientPrivilege(String username) throws RemoteException;
    String getClientStatus(String username) throws RemoteException;

    List<String> getJsonShapes(String username) throws RemoteException;

    List<String> getUsername() throws RemoteException;

    ConcurrentHashMap<String, Client> getClientMap() throws RemoteException;
    void clearShape() throws RemoteException;
    Client getClient(String username) throws RemoteException;
    void updateClientClearFlag(String username, boolean flag) throws RemoteException;

    boolean acceptClient(String username) throws java.rmi.RemoteException;
    void loadShapeList(List<ShapeObject> shapeObjectList) throws RemoteException;
    void updateClientLoadFlag(String username, boolean flag) throws RemoteException;
    void updateClientLoginAttempts(String username) throws RemoteException;
}
