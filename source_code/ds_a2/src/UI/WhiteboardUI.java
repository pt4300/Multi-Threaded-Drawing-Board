package UI;

import RMI.IServer;
import javax.swing.JFileChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import RMI.IServerImplementation;
import User.Client;
import Utility.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WhiteboardUI extends JPanel {
    private List<ShapeObject> shapeList;
    private LinkedList<String> messages;
    private Map<String, Client> clientList;
    private EventListener eventListener;
    private Thread shapeMointorThread;
    private Thread utilityThread;
    private String choosenUser;
    boolean ShapeThreadRunning = false;
    boolean UtilityThreadRunning = false;
    private Gson gson = new Gson();
    protected IServer serverRegistry;
    private String username;
    private JList userList;

    private JList chatHistory;
    private String userPrivilege;
    JFrame loginFrame;
    public WhiteboardUI(String username, IServer serverRegistry, String userPrivilege) {
        this.username = username;
        this.serverRegistry = serverRegistry;
        this.shapeList = new CopyOnWriteArrayList<>();
        this.messages = new LinkedList<>();
        this.userList = new JList();
        chatHistory = new JList();
        this.userPrivilege = userPrivilege;
        clientList = new ConcurrentHashMap<String, Client>();


        this.eventListener = new EventListener(username, serverRegistry);
        try {
            if(userPrivilege.equals("ADMIN")){
                serverRegistry.registerClient(username,userPrivilege);
                this.show();
            }else{
                if(serverRegistry.getClientStatus(username).equals("ACCEPTED")){
                    JOptionPane.showMessageDialog(null, "Duplicate Username!");
                    System.exit(0);
                }
                this.getLoginFrame();
            }


        }catch(RemoteException e){
            System.out.println("Error in register client");
            System.out.println(e.getMessage());
        }

    }


    public void show(){
        JFrame frame = new JFrame(userPrivilege + " - "+username);
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setSize(1200, 1000);

        //       add mouse listener to the frame
        this.addMouseListener(eventListener);
        this.addMouseMotionListener(eventListener);

        JPanel chatArea = new JPanel(new BorderLayout());
        chatArea.setPreferredSize(new Dimension(1200,200));
        frame.add(this, BorderLayout.CENTER);
        frame.add(chatArea, BorderLayout.SOUTH);

        this.getButtons(frame);
        this.getChatArea(chatArea);
        this.setBackground(Color.WHITE);
        eventListener.setG2d((Graphics2D) this.getGraphics());

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try{
                    serverRegistry.disconnect(username);
                    UtilityThreadRunning = false;
                    ShapeThreadRunning = false;
                    System.exit(1);
                }catch (RemoteException Exception){
                    System.out.println("Error in remove client when closing");
                    System.out.println(Exception.getMessage());
                }

            }
        });
        frame.setResizable(false);
        frame.setVisible(true);
        backgroundThread();

    }

    public void getLoginFrame(){
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        Container content = loginFrame.getContentPane();
        content.setLayout(new BorderLayout());
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String userStatus = serverRegistry.getClientStatus(username);
                    if(userStatus.equals("ACCEPTED")){
                        serverRegistry.updateClientLoginAttempts(username);
                        loginFrame.dispose();
                        show();
                    }else{
                        serverRegistry.registerClient(username,userPrivilege);
                        JOptionPane.showMessageDialog(loginFrame, "Waiting for manager to accept your request");

                    }
                }catch (RemoteException Exception){
                    System.out.println("Error in get client status");
                    System.out.println(Exception.getMessage());
                }

            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userDisconnect();

            }
        });
        loginFrame.add(loginBtn,BorderLayout.CENTER);
        loginFrame.add(exitBtn,BorderLayout.SOUTH);

        loginFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                userDisconnect();
            }
        });
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);
    }

    public void getButtons(JFrame frame){

        JPanel controls = new JPanel();
        JButton userBtn = new JButton("User");

        // admin buttons
        userBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserList();
            }
        });
        JButton newBtn = new JButton("New");
        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });
        JButton saveAsBtn = new JButton("Save As");
        saveAsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        if(userPrivilege == "ADMIN"){
            controls.add(saveBtn);
            controls.add(saveAsBtn);
            controls.add(loadBtn);
            controls.add(newBtn);
            controls.add(userBtn);
            controls.add(clearBtn);


        }


        // default buttons
        String[] shapeArray = { "Line", "Rectangle", "Oval","Circle","Text"};
        for(String shape: shapeArray){
            JButton shapeButton  = new JButton(shape);
            shapeButton.addActionListener(eventListener);
            controls.add(shapeButton);
        }

        JButton colorTn = new JButton("Color");

        colorTn.addActionListener(eventListener);
        controls.add(colorTn);

        frame.add(controls, BorderLayout.NORTH);
    }



    public void getChatArea(JPanel panel){
        JLabel label = new JLabel("Chat Room");
        JScrollPane scrollPane = new JScrollPane(chatHistory,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        //       add input area
        JPanel inputArea = new JPanel(new BorderLayout());
        JTextField input = new JTextField();
        inputArea.add(input, BorderLayout.CENTER);

        JButton sendBtn = new JButton("Send");
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = input.getText();
                try {
                    if(!message.equals("")){
                        message = message.trim();
                        serverRegistry.sendMessage(username, message);
                    }
                }catch (RemoteException exception){
                    System.out.println("error in send message");
                }
                input.setText("");
            }
        });
        inputArea.add(sendBtn, BorderLayout.EAST);
        panel.add(inputArea, BorderLayout.SOUTH);
    }

    public void getUserList(){

        JFrame userFrame = new JFrame("User List");
        Container content = userFrame.getContentPane();
        content.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(userList);
        userFrame.add(scrollPane, BorderLayout.CENTER);
        userFrame.setSize(new Dimension(200, 400));
        userFrame.setVisible(true);
        userFrame.setResizable(false);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addListSelectionListener(e -> {
            if(userList.getSelectedValue()!=null){
                choosenUser = userList.getSelectedValue().toString();
            }
        });
        userList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(choosenUser!=null){
                    String[] nameArray = choosenUser.split(" ");
                    String currentName = nameArray[0];
                    try {
                        Client client = serverRegistry.getClient(currentName);
                        if(client !=null){
                            if(client.getStatus().equals("PENDING")){
                                int confirmToken = JOptionPane.showConfirmDialog(null, "Are you sure to accept " +currentName+" request?", "Warning", JOptionPane.YES_NO_OPTION);
                                if (confirmToken == JOptionPane.YES_OPTION) {
                                    acceptUser(currentName);
                                }else{
                                    serverRegistry.disconnect(currentName);
                                }

                                acceptUser(currentName);
                            }else{
                                if(!client.getPrivilege().equals("ADMIN") && client.isActive()){
                                    int confirmToken = JOptionPane.showConfirmDialog(null, "Are you sure to disconnect " +currentName+" ?", "Warning", JOptionPane.YES_NO_OPTION);
                                    if(confirmToken == JOptionPane.YES_OPTION){
                                        serverRegistry.disconnect(currentName);
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "Cannot disconnect admin", "Warning", JOptionPane.INFORMATION_MESSAGE);
                                }

                            }
                        }

                    }catch (RemoteException exception){
                        System.out.println("Failed to update client in user list");
                    }


                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
    public void saveFile(){
        String jsonShapes = gson.toJson(shapeList);
        String path = System.getProperty("user.dir");
        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        StringBuilder sb = new StringBuilder(currentTime.format(new Date()));
        String fileName = sb.toString() + "-save.json";
        File file = new File(path, fileName);

        try{
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(jsonShapes);
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error when saving shapeList");
        }


    }

    public void loadFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            String file = fileChooser.getSelectedFile().toString();
            if(file.endsWith(".json")){
                ShapeThreadRunning = false;
                try(BufferedReader br = new BufferedReader(new FileReader(file))){
                    shapeList = gson.fromJson(br, new TypeToken<CopyOnWriteArrayList<ShapeObject>>(){}.getType());
                    serverRegistry.loadShapeList(shapeList);
                    repaint();
                    System.out.println("load file");
                    System.out.println(serverRegistry.getJsonShapes(username).size());
                    ShapeThreadRunning = true;
                    shapeMointorThread = new Thread(new shapeThread());
                    shapeMointorThread.setDaemon(true);
                    shapeMointorThread.start();
                }catch (Exception e){
                    System.out.println("Error when reading file");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Please choose a json file", "Warning", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }
    public void saveAs(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            String file = fileChooser.getSelectedFile().toString();
            if(file.endsWith(".json")){
                try{
                    String jsonShapes = gson.toJson(shapeList);
                    FileWriter writer = new FileWriter(file);
                    writer.write(jsonShapes);
                    writer.close();

                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("Error when saving shapeList");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Please save a json file", "Warning", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }

    public void acceptUser(String username){
        try {
            boolean success = serverRegistry.acceptClient(username);

        }catch(RemoteException e){
            System.out.println("Failed to accept user join request");
        }
    }
    public void userDisconnect(){
        try {
            ShapeThreadRunning = false;
            UtilityThreadRunning = false;
            serverRegistry.disconnect(username);
            System.exit(1);
        }catch (RemoteException Exception){
            System.out.println("Error in remove client");
            System.out.println(Exception.getMessage());
        }
    }

    private void backgroundThread(){
        ShapeThreadRunning = true;
        UtilityThreadRunning = true;
        shapeMointorThread = new Thread(new shapeThread());
        shapeMointorThread.setDaemon(true);
        shapeMointorThread.start();
        utilityThread = new Thread(new utilityThread());
        utilityThread.setDaemon(true);
        utilityThread.start();

    }

    private void clear(){

        try {
            serverRegistry.clearShape();
//            Client currentClient = serverRegistry.getClient(username);
//            if(currentClient.getIsClear()){
//                shapeList.clear();
//                repaint();
//            }
        }catch (RemoteException e){
            System.out.println("error in clear canvas");

        }
    }


    private void update() {
        try{
            Client currentClient = serverRegistry.getClient(username);
            if(currentClient==null){
                userDisconnect();
            }
            CopyOnWriteArrayList<ShapeObject> tmpShapes = new CopyOnWriteArrayList<>();
            List<String> currentJsonShapes = serverRegistry.getJsonShapes(username);
//            getting latest update from shape and reflect on current ui board
            for(String jsonObject: currentJsonShapes){
                ShapeObject shapeObject = gson.fromJson(jsonObject, ShapeObject.class);
                System.out.println(jsonObject);
                shapeList.add(shapeObject);
                tmpShapes.add(shapeObject);
                repaint();
            }

            if(currentClient.getIsLoaded() && !userPrivilege.equals("ADMIN")){
                serverRegistry.updateClientLoadFlag(username, false);
                shapeList=tmpShapes;
                repaint();
            }

        }catch (RemoteException e){
            System.out.println("error in update client ui interface");
            serverStatusMonitor(e);

        }
    }

    private void updateMessageList(){
        try {
            List<String> currentMessages = serverRegistry.getMessages(username);
            for (String message : currentMessages) {
                MessageObject messageObject = gson.fromJson(message, MessageObject.class);
                String formatted = messageObject.getTime() + " " + messageObject.getUsername() + ": " + messageObject.getMessage();
                messages.add(formatted);
            }
            chatHistory.setListData(messages.toArray());
            chatHistory.repaint();
        }catch (RemoteException e){
            System.out.println("error in update message list");
            System.out.println(e.getMessage());
        }

    }

    private void updateUserList(){
        try {
            Map<String, Client> currentClientList = serverRegistry.getClientMap();
            for(Client client:currentClientList.values()){
                if(!clientList.containsKey(client.getUsername())){
                    clientList.put(client.getUsername(), client);
                    if(!client.getPrivilege().equals("ADMIN")){
                        JOptionPane.showMessageDialog(null, "New user request to join, check user list", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if(clientList.size()!=currentClientList.size()){
                clientList = currentClientList;
            }
//            clientList = currentClientList;
            updateUserListUI();

        }catch (RemoteException e){
            System.out.println("error in update user list");
            System.out.println(e.getMessage());
        }
    }
    public void updateUserListUI(){
        List<String>userNameList = new LinkedList<>();
        for (Client currentClient: clientList.values()){
            String displayName = currentClient.getUsername() + " " + currentClient.getPrivilege() + " " + currentClient.getStatus();
            userNameList.add(displayName);
        }
        userList.setListData(userNameList.toArray());
        userList.repaint();
    }


    public void paint(Graphics g) {
        super.paint(g);

        for (ShapeObject shapeObject : shapeList) {
            int colorRgb = Integer.parseInt(shapeObject.getColor().replace("\"", ""));
            Color currentColor = new Color(colorRgb);
            g.setColor(currentColor);
            //prevent crash when shapeObject is null
            if(shapeObject==null){
                continue;
            }
            if (shapeObject.getType().equals("Line")){
                g.drawLine(shapeObject.getX1(), shapeObject.getY1(), shapeObject.getX2(), shapeObject.getY2());
            }
            else if(shapeObject.getType().equals("Rectangle")){
                int x = Math.min(shapeObject.getX1(), shapeObject.getX2());
                int y = Math.min(shapeObject.getY1(), shapeObject.getY2());
                int width = Math.abs(shapeObject.getX2() - shapeObject.getX1());
                int height = Math.abs(shapeObject.getY2() - shapeObject.getY1());
                g.drawRect(x, y, width, height);
            }
            else if(shapeObject.getType().equals("Oval")){
                int x = Math.min(shapeObject.getX1(), shapeObject.getX2());
                int y = Math.min(shapeObject.getY1(), shapeObject.getY2());
                int width = Math.abs(shapeObject.getX2() - shapeObject.getX1());
                int height = Math.abs(shapeObject.getY2() - shapeObject.getY1());
                g.drawOval(x, y, width, height);
            }
            else if(shapeObject.getType().equals("Circle")){
                int x = Math.min(shapeObject.getX1(), shapeObject.getX2());
                int y = Math.min(shapeObject.getY1(), shapeObject.getY2());
                int width = Math.abs(shapeObject.getX2() - shapeObject.getX1());
                int height = Math.abs(shapeObject.getY2() - shapeObject.getY1());
                int diameter = Math.min(width, height);
                g.drawOval(x, y, diameter, diameter);
            }
            else if(shapeObject.getType().equals("Text")){
                String input = shapeObject.getText();
                // prevent crash application due to null input
                if(input!=null){
                    g.drawString(shapeObject.getText(), shapeObject.getX2(), shapeObject.getY2());
                }

            }
        }
    }
    private void serverStatusMonitor(RemoteException e){
        if ( e.getMessage().contains("EOFException")||e.getMessage().contains("Connection refused")) {
            JOptionPane.showMessageDialog(null, "Server down, please join later", "Warning", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
    }

    private class shapeThread implements Runnable{
        @Override
        public void run() {
            while (ShapeThreadRunning) {
                update();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class utilityThread implements Runnable{
        @Override
        public void run() {
            while(UtilityThreadRunning){
                updateMessageList();
                if(userPrivilege.equals("ADMIN")){
                    updateUserList();
                }
                try {
                    Client currentClient = serverRegistry.getClient(username);
                    // check if the user still in the server
                    if(currentClient!=null){
                        if(currentClient.getIsClear()){
                            shapeList.clear();
                            repaint();
                            serverRegistry.updateClientClearFlag(username, false);
                            System.out.println("clearing canvas");
                        }
                    }
                    else{
                        userDisconnect();
                    }

                } catch (RemoteException e) {
                    System.out.println("error in get clear flag");
                    serverStatusMonitor(e);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

