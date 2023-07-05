package UI;

import RMI.IServer;
import RMI.IServerImplementation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java CreateWhiteBoard <serverIPAddress> <serverPort> username");
            System.exit(1);
        }
        else{
            try{
                int port = Integer.parseInt(args[1]);
                String username = args[2];
                IServer service = new IServerImplementation();
                Registry registry = LocateRegistry.createRegistry(port);
                registry.rebind("ServerRMI", service);
                WhiteboardUI x = new WhiteboardUI(username,service,"ADMIN");

            }catch(Exception e){
                System.out.println("error in initialising server ui");
            }
        }
    }
}
