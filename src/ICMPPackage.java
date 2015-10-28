public class ICMPPackage{
	
	public String MAC_src;
	public String MAC_dst;
	public String IP_src;
	public String IP_dst;
	public int TTL;

	public ICMPPackage(String MAC_src,String MAC_dst,String IP_src,String IP_dst){
		this.MAC_src = MAC_src;
		this.MAC_dst = MAC_dst;
		this.IP_src = IP_src;
		this.IP_dst = IP_dst;
		TTL = 8;
	}

	public void decTTL(){
		TTL-=1;
	}
	public String toString(){

		return MAC_src + "," + MAC_dst + "," + IP_src + "," + IP_dst + "," + TTL;
	}
}