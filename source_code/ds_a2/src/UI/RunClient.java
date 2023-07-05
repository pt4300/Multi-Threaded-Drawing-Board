package UI;

import RMI.IServer;
import RMI.IServerImplementation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunClient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java JoinWhiteBoard <serverIPAddress> <serverPort> username");
            System.exit(1);
        }
        else{
            try{
                int port = Integer.parseInt(args[1]);
                String username = args[2];
                Registry registry = LocateRegistry.getRegistry(args[0], port);
                IServer service = (IServer) registry.lookup("ServerRMI");
                WhiteboardUI x = new WhiteboardUI(username,service,"USER");

            }catch(Exception e){
                System.out.println("error in initialising client ui");
            }
        }

    }
}
