import java.net.InetAddress;
import java.net.UnknownHostException;

public class TFNetworkAddress implements ITFNetworkAddress{
    private String MAC;
    private String IP;
    private String name;
    private String IPPrefix;
    private String network;

    public TFNetworkElement getOwner(){
        throw new UnsupportedOperationException();
    }

    public void setIPrefix(String ipprefix) throws UnknownHostException{
        this.IPPrefix = ipprefix;
        this.IP = ipprefix.substring(0, ipprefix.indexOf("/"));
        this.network = extractNetwork(ipprefix);
    }

    public String getIP() {
        return IP;
    }

    public String getIPPrefix() {
        return IPPrefix;
    }

    public String getNetwork() {
        return network;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String extractNetwork(String ipprefix) throws UnknownHostException{
        int netCIDR = Integer.parseInt(ipprefix.substring(ipprefix.indexOf("/")+1));
        String ip = ipprefix.substring(0, ipprefix.indexOf("/"));
        return extractNetwork(ip,netCIDR);
    }

    public static String extractNetwork(String ip,int cidr) throws UnknownHostException{
        byte[] bytesRedeBase = (InetAddress.getByName(ip)).getAddress();
        String binarRedeIP = "";

        for (byte b : bytesRedeBase) {
            String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binarRedeIP += s1;
        }
        return binarRedeIP.substring(0,cidr);
    }

    public static String binaryIPtoStringIPv4(String binaryIP) {
        return IntegerIPv4toStringIPv4(binaryIPtoIntegerIPv4(binaryIP));
    }

    public static String IntegerIPv4toStringIPv4(int[] parts) {
        String ret = "";
        for (int i = 0; i < parts.length; i++) {
            ret += parts[i] + (i == (parts.length-1) ? "" : ".");
        }
        return ret;
    }

    public static int[] binaryIPtoIntegerIPv4(String binaryIP) {
        int[] parts = new int[4];

        int last = 0;
        int next = last + 8;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = bitArrayToInt(binaryIP.substring(last, next).toCharArray());
            last = next;
            next = last + 8;
        }

        return parts;
    }

    public static int bitArrayToInt(char[] bits){
        int ret=0;
        for (int j = 0, e = bits.length - 1; j < bits.length; j++, e--) {
            int bit = Integer.parseInt("" + bits[j]);
            ret += (bit == 1 ? Math.pow(2, e) : 0);
        }
        return ret;
    }
}
