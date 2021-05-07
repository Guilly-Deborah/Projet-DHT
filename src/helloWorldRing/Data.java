package helloWorld;

import peersim.edsim.*;

public class Data {

    public final static int HELLOWORLD = 0;

    private int type;
    private String id;
    private int idNode;
    private Strin content;


    Data(int type, String id, String content) {
	this.type = type;
	this.content = content;
	this.id =id;
    }

    public String getId() {
	return this.id;
    }
    
    public Boolean setNodeId(int nodeId) {
	this.idNode =nodeId;
    }
    
}
