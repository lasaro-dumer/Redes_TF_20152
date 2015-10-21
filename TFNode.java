public class TFNode extends ITFNetworkElement {
    public String name;
    public String MAC;
    public String IP;
    //TODO: create a object to this, and store IP in another field (a string)
    public String gateway;

    public TFNode(String name,String MAC,String IP,String gateway){
        this.name=name;
        this.MAC = MAC;
        this.IP = IP;
        this.gateway = gateway;
    }

    public String toString(){
        return "Name:"+name+" MAC:"+MAC+" IP:"+IP+" gateway:"+gateway;
    }
}
