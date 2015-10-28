import java.util.List;
import java.util.ArrayList;

public class ITFNetworkElement {
    public List<ARPTableEntry> arpTable;
    protected String arpMACResponse;
    protected String arpIPResponse;

    public ITFNetworkElement(){
        this.arpTable = new ArrayList<ARPTableEntry>();
    }

    public ARPPackage doARPRequest(ARPPackage request){
        return new ARPPackage(1,arpMACResponse,request.MAC_src,arpIPResponse,request.IP_src);
    }
}
