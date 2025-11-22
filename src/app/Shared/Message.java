package app.Shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    protected final MessageType messageType;
    protected Status status;
    protected UserType userType;
    protected String text;
    protected ArrayList<String> list;

    //default constructor
    public Message(){
        this.messageType = null;
        this.status = null;
        this.text = "Undefined";
        this.list = new ArrayList<>();
    }
    //constructor for login responses
    public Message(MessageType messageType, Status status, UserType userType, String text) {
    	this.messageType = messageType;
    	this.status = status;
    	this.userType = userType;
    	this.text = text;
    	this.list = new ArrayList<>();
    }

    //constructor for sending/receiving status updates
    public Message(MessageType type, Status status, String text){
        this.messageType = type;
        this.status = status;
        this.text = text;
        this.list = new ArrayList<>();
    }
    
    // constructor for sending lists of courses or login details
    public Message(MessageType type, Status status, String text, ArrayList<String> list){
        this.messageType = type;
        this.status = status;
        this.text = text;
        this.list = list;
    }

    public MessageType getType(){
    	return messageType;
    }

    public Status getStatus(){
    	return status;
    }
    
    public UserType getUserType() {
    	return userType;
    }

    public String getText(){
    	return text;
    }
    public ArrayList<String> getList() {
        return list;
    }
}