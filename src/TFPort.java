public class TFPort {
    public int number;
    public String MAC;
    private String IP;
    private String IPPrefix;
    public TFPort(){
    }

    public void setIPrefix(String ipprefix){
        this.IPPrefix = ipprefix;
        this.IP = ipprefix.substring(0,ipprefix.indexOf("/"));
    }

    public String getIP(){
        return IP;
    }

    public String getIPPrefix(){
        return IPPrefix;
    }

    public String toString(){
        return "["+number+"]MAC: "+MAC+" IP/Prefix:"+IPPrefix;
    }
}
