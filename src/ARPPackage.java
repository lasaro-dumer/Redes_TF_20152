public class ARPPackage{
    //Pacotes ARP Request: ARP_REQUEST|<MAC_src>,<MAC_dst>|<IP_src>,<IP_dst>
    //Pacotes ARP Reply: ARP_REPLY|<MAC_src>,<MAC_dst>|<IP_src>,<IP_dst>
    public int type;
    public String MAC_src;
    public String MAC_dst;
    public String IP_src;
    public String IP_dst;

    public ARPPackage(int type,String MAC_src,String MAC_dst,String IP_src,String IP_dst){
        this.type = type;
        this.MAC_src = MAC_src;
        this.MAC_dst = MAC_dst;
        this.IP_src = IP_src;
        this.IP_dst = IP_dst;
    }

    public ARPPackage(String MAC_src,String IP_src,String IP_dst){
        this(0,MAC_src,"FF:FF:FF:FF:FF:FF",IP_src,IP_dst);
    }

    public String toString(){
        return (type==0?"ARP_REQUEST":"ARP_REPLY")+"|"+MAC_src+","+MAC_dst+"|"+IP_src+","+IP_dst;
    }
}
