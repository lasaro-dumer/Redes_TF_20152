import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;

public class TFNetworkElement {
    private Map<String,String> arpTable;
    private String arpMACResponse;
    private String arpIPResponse;

    public TFNetworkElement(){
        this.arpTable = new HashMap<String,String>();
    }

    public ARPPackage doARPRequest(ARPPackage request){
        return new ARPPackage(1,arpMACResponse,request.MAC_src,arpIPResponse,request.IP_src);
    }

    public void setARPMACResponse(String MACResponse){
        arpMACResponse = MACResponse;
    }

    public void setARPIPResponse(String IPResponse){
        arpIPResponse = IPResponse;
    }

    public void addArpEntry(String IP,String MAC){
        arpTable.put(IP,MAC);
    }

    public String printARPTable(){
        return arpTable.toString();
    }

    public String searchMAC(String IP){
        return arpTable.get(IP);
    }

    public String searchIP(String MAC){
        if(arpTable.containsValue(MAC)){
            Iterator<Entry<String,String>> it = arpTable.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String,String> entry = it.next();
                if(entry.getValue().equals(MAC))
                    return entry.getKey();
            }
        }
        return null;
    }
}
