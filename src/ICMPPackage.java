public class ICMPPackage{
	//Pacotes ICMP Echo Request: ICMP_ECHOREQUEST|<MAC_src>,<MAC_dst>|<IP_src>,<IP_dst>|<TTL>
	//Pacotes ICMP Echo Reply: ICMP_ECHOREPLY|<MAC_src>,<MAC_dst>|<IP_src>,<IP_dst>|<TTL>
	//Pacotes ICMP Time Exceeded: ICMP_TIMEEXCEEDED|<MAC_src>,<MAC_dst>|<IP_src>,<IP_dst>|<TTL>

	public ICMPType type;
	public String MAC_src;
	public String MAC_dst;
	public String IP_src;
	public String IP_dst;
	public int CIDR_src;
	public int CIDR_dst;
	public int TTL;
	private String logString;

	public ICMPPackage(ICMPType type,String MAC_src,String MAC_dst,String IP_src,int CIDR_src,String IP_dst,int CIDR_dst,int ttl){
		this.type = type;
		this.MAC_src = MAC_src;
		this.MAC_dst = MAC_dst;
		this.IP_src = IP_src;
		this.IP_dst = IP_dst;
		this.CIDR_src = CIDR_src;
		this.CIDR_dst = CIDR_dst;
		this.TTL = ttl;
		this.logString = null;
	}

	public ICMPPackage(String MAC_src,String MAC_dst,String IP_src,int CIDR_src,String IP_dst,int CIDR_dst){
		this(ICMPType.ICMPEchoRequest,MAC_src,MAC_dst,IP_src,CIDR_src,IP_dst,CIDR_dst,8);
	}

	public void decTTL(){
		TTL-=1;
	}

	public void log(String line){
		if(this.logString == null)
			this.logString = "";
		this.logString += line + "\n";
	}
	public String toString(){
		return (type != ICMPType.ICMPEchoRequest && logString != null ? logString:"") + type +"|"+ MAC_src + "," + MAC_dst + "|" + IP_src + "," + IP_dst + "|" + TTL;
	}

	public enum ICMPType{
		ICMPEchoRequest("ICMP_ECHOREQUEST"),ICMPEchoReply("ICMP_ECHOREPLY"),ICMPTimeExceeded("ICMP_TIMEEXCEEDED");
		private final String type;
		ICMPType(String type){
			this.type = type;
		}
		public String toString(){
			return type;
		}
	}
}
