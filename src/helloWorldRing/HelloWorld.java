package helloWorld;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.ArrayList;

public class HelloWorld implements EDProtocol {
    
    //identifiant de la couche transport
    private int transportPid;

    //objet couche transport
    private HWTransport transport;

    //identifiant de la couche courante (la couche applicative)
    private int mypid;

    //le numero de noeud
    private int nodeId;

    //prefixe de la couche (nom de la variable de protocole du fichier de config)
    private String prefix;

    //recuperation de la taille du reseau
    private int nodeNb = Network.size();

    //le numero du noeud voisin a droite
    public int noeudDroite;

    //le numero du noeud voisin a gauche
    public int noeudGauche;

    private int cpt=0;

    public ArrayList<Data> listeData;

    public HelloWorld(String prefix) {
	this.prefix = prefix;
	//initialisation des identifiants a partir du fichier de configuration
	this.transportPid = Configuration.getPid(prefix + ".transport");
	this.mypid = Configuration.getPid(prefix + ".myself");
	this.transport = null;
	if (nodeNb==1) {
		this.noeudDroite = this.nodeId;
		this.noeudGauche = this.nodeId;
        }
	if (nodeNb==2) {
		if (nodeId==0){
			this.noeudDroite = 1;
			this.noeudGauche = 1;
		}
		if (nodeId==1){
			this.noeudDroite = 0;
			this.noeudGauche = 0;
		}
	}
	if (nodeId==0){
		this.noeudGauche = nodeNb;
	}
	if (nodeId==NodeNb){
		this.noeudDroite = 0;
	}
	this.listeData = new ArrayList<Data>();		
    }
    //methode appelee lorsqu'un message est recu par le protocole HelloWorld du noeud
    public void processEvent( Node node, int pid, Object event ) {
	this.receive((Message)event);
    }
    
    //methode necessaire pour la creation du reseau (qui se fait par clonage d'un prototype)
    public Object clone() {

	HelloWorld dolly = new HelloWorld(this.prefix);

	return dolly;
    }

    //liaison entre un objet de la couche applicative et un 
    //objet de la couche transport situes sur le meme noeud
    public void setTransportLayer(int nodeId) {
	this.nodeId = nodeId;
	this.transport = (HWTransport) Network.get(this.nodeId).getProtocol(this.transportPid);
    }

    //envoi d'un message (l'envoi se fait via la couche transport)
    public void send(Message msg, Node dest) {
	msg.dest = dest;
	this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    //affichage a la reception
    /*private void receive(Message msg) {
	System.out.println(this + ": Received " + msg.getContent() + "at" + CommonState.getTime());
	if(this.nodeId==0) {
		cpt++;
	}
	if(cpt<1000) {
		send(msg,Network.get((this.nodeId+1)%Network.size()));
	}
    }*/
    private void receive(Message msg) {
	if (msg.dest = this) {
		System.out.println(this + ": Received " + msg.getContent() + "at" + CommonState.getTime());
	}else{
		System.out.println(this + ": Passes the message to node " + this.noeudDroite);
		send(msg, Network.get((this.noeudDroite)%nodeNb));
	}
    }

    //retourne le noeud courant
    private Node getMyNode() {
	return Network.get(this);
    }

    public String toString() {
	return "Node "+ this.nodeId;
    }

    // ajout d'un noeud dans l'anneau
    public void join(Node nouveau) {
	nouveau.nodeId=NodeNb+1;
	Network.set(nouveau);
	nodeNew=NetWork.get(nouveau.nodeId);
	current = (HelloWorld)nodeNew.getProtocol(this.helloWorldPid);
	current.setTransportLayer(nodeNew.nodeId);
	System.out.println("Add node " + nouveau.nodeId);
    }
    // retrait d'un noeud
    public void leave() {
	nodeDroite = Network.get(noeudDroite);
	nodeGauche = Network.get(noeudGauche);
	nodeDroite.noeudGauche = nodeGauche.nodeId;
	nodeGauche.noeudDroite = nodeDroite.nodeId;
	System.out.println("Node "+ this.nodeId+ " quits");
    }

    // ajout de donnee sur un noeud
    public void putData (Data data) {
	idData = data.getId();
	if (idData[0] == this.nodeId) {
		data.setNodeId(this.nodeId);
		this.listeData.add(data);
		// Réplication sur les noeuds voisins
		Network.get((this.noeudDroite)).listeData.add(data);
		Network.get((this.noeudGauche)).listeData.add(data);
	}else{
		Network.get((this.noeudDroite)).putData(data);
	}
    }
    // récupération d'une donnee par son identifiant
    public Data getData (String indentifier) {
	//si l'indentifiant correspond à celui du noeud
	if (indentifier[0] == this.nodeId) {
		for (Data data : this.listeData) {
			if (data.getId() ==indentifier) {
				return data
		}
	}else{
		Network.get((this.noeudDroite)).getData(data);
	}
    }

}
