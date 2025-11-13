package app.Shared;

import java.io.Serializable;

public class Message implements Serializable {
    protected final Type messageType;
    protected Status status;
    protected UserType userType;
    protected String text;

    public Message(){
        this.messageType = null;
        this.status = null;
        this.text = "Undefined";
    }
    
    public Message(Type messageType, Status status, UserType userType, String text) {
    	this.messageType = messageType;
    	this.status = status;
    	this.userType = userType;
    	this.text = text;
    }

    public Message(Type type, Status status, String text){
        this.messageType = type;
        this.status = status;
        this.text = text;
    }
    
    public Message(Type type, Status status, String text, ArrayList<String> list){
        this.messageType = type;
        this.status = status;
        this.text = text;
        this.list = list;
    }

    public Type getType(){
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
}