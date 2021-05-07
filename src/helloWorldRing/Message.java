package helloWorld;

import peersim.edsim.*;

public class Message {

    public final static int HELLOWORLD = 0;

    private int type;
    private String content;

    public Node dest;

    Message(int type, String content) {
	this.type = type;
	this.content = content;
    }

    public String getContent() {
	return this.content;
    }

    public int getType() {
	return this.type;
    }
    
}
