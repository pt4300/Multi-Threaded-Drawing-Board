package UI;

import RMI.IServer;
import Utility.ShapeObject;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

public class EventListener implements MouseListener, MouseMotionListener, ActionListener {
    private int x1,x2,y1,y2;
    protected Color color = Color.BLACK;
    protected LinkedList<ShapeObject> shapeObjectList;
    protected Thread monitorThread;
    protected String current_user;
    protected ShapeObject currentShapeObject;
    protected Graphics2D g2d;
    private   Gson gson = new Gson();

    String currentAction = "";
    public EventListener(String user, IServer serverRegistry) {
        this.current_user = user;
        shapeObjectList = new LinkedList<>();
        monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (shapeObjectList.size() > 0) {
                        currentShapeObject = shapeObjectList.poll();
                        try {
                            serverRegistry.addShape(currentShapeObject);
                            System.out.println("ServerListener: " + currentShapeObject);
                        }catch (Exception e){
                            System.out.println("ServerListener Failed to update shape list");
                            System.out.println("Error message: " + e.getMessage());
                        }


                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();


    }
    public void setG2d(Graphics2D g2d) {
        this.g2d = g2d;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String tmpAction = currentAction;
        currentAction = e.getActionCommand();

        if (currentAction.equals("Color")){
            JFrame colorFrame = new JFrame();
            color = JColorChooser.showDialog(colorFrame, "Choose a color", color);
            if (color == null){
                color = Color.BLACK;
            }
            // restore the previous action
            if(!tmpAction.equals("")){
                currentAction = tmpAction;
            }
        }
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();

    }
    public void mouseReleased(java.awt.event.MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();

        String jsonColor = gson.toJson(Integer.toString(color.getRGB()));
        if (currentAction.equals("Line")) {
            ShapeObject line = new ShapeObject("Line",x1,y1, x2, y2, jsonColor);
            System.out.println(gson.toJson(line));
            shapeObjectList.add(line);
        }else if(currentAction.equals("Rectangle")){
            ShapeObject rectangle = new ShapeObject("Rectangle",x1,y1, x2, y2, jsonColor);
            shapeObjectList.add(rectangle);
        }else if(currentAction.equals("Oval")){
            ShapeObject oval = new ShapeObject("Oval",x1,y1, x2, y2, jsonColor);
            shapeObjectList.add(oval);
        }
        else if(currentAction.equals("Circle")){
            ShapeObject circle = new ShapeObject("Circle",x1,y1, x2, y2, jsonColor);
            shapeObjectList.add(circle);
        }else if(currentAction.equals("Text")){
            String text = JOptionPane.showInputDialog("Enter your text");
            ShapeObject textShape = new ShapeObject("Text",x1,y1, x2, y2, jsonColor,text);
            shapeObjectList.add(textShape);
        }


    }
    public void mouseClicked(java.awt.event.MouseEvent e) {

    }

    public void mouseDragged(java.awt.event.MouseEvent e) {

    }

    public void mouseMoved(java.awt.event.MouseEvent e) {

    }
    public void mouseEntered(java.awt.event.MouseEvent e) {

    }
    public void mouseExited(java.awt.event.MouseEvent e) {

    }

}
