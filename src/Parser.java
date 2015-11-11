import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.net.UnknownHostException;

public class Parser{
    private List<TFNetworkElement> netElements;
    private Map<String,TFSwitch> switches;

    public Parser(){
        netElements = new ArrayList<TFNetworkElement>();
        switches = new HashMap<String,TFSwitch>();
    }
    public void parseFile(String fileName)  throws FileNotFoundException, IOException, Exception {
        parseFile(fileName,false);
    }

	public void parseFile(String fileName,boolean fullPath)  throws FileNotFoundException, IOException, Exception {
		if(!fullPath)
			fileName = buildDefaultPath(fileName);

        this.netElements = new ArrayList<TFNetworkElement>();

        FileReader fr = new FileReader(new File(fileName));
        BufferedReader br = new BufferedReader(fr);
        StringTokenizer st;

        String line = br.readLine();
        /*
        #NODE
        <node_name>,<MAC>,<IP/prefix>,<gateway>
        #ROUTER
        <router_name>,<num_ports>,<MAC0>,<IP0/prefix>,<MAC1>,<IP1/prefix>,<MAC2>,<IP2/prefix> …
        #ROUTERTABLE
        <router_name>,<net_dest/prefix>,<nexthop>,<port>
         */

        EntryType type = EntryType.Node;
        TFRouter router = null;

        while (line != null) {
            line = line.toLowerCase();
            if (line.equals("#node")) {
                type = EntryType.Node;
            } else if (line.equals("#router")) {
                type = EntryType.Router;
            } else if (line.equals("#routertable")) {
                type = EntryType.RouterTable;
            } else {
                TFNetworkElement element = parseLine(type,line);
                if(element!=null)
                    this.netElements.add(element);
            }
            line = br.readLine();
        }
        br.close();

        tuneNetwork();
	}

	private String buildDefaultPath(String fileName){
		File file1 = new File("examples");
	    File file2 = new File(file1, fileName);
	    return file2.getPath();
	}

    private TFNetworkElement parseLine(EntryType type,String line) throws Exception{
	    StringTokenizer st = new StringTokenizer(line, ",");
        TFNetworkElement element = null;
        TFSwitch s=null;
        switch (type) {
            case Node:
                element = new TFNode(st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim());
                TFNode n = (TFNode)element;
                s=switches.get(n.getNetwork());
                if(s == null){
                    s = new TFSwitch(n.getNetwork(),n.getNetCIDR());
                    switches.put(s.getNetwork(),s);
                }
                s.addHost(n);
                n.setLAN(s);
                break;
            case Router:
                element = new TFRouter(st.nextToken().trim(),Integer.parseInt(st.nextToken().trim()));
                int pNumber = 0;
                while(st.hasMoreElements()){
                    TFPort port = new TFPort((TFRouter)element);
                    port.number=pNumber;
                    port.setMAC(st.nextToken().trim());
                    port.setIPrefix(st.nextToken().trim());
                    ((TFRouter)element).addPort(port);
                    pNumber++;
                    s=switches.get(port.getNetwork());
                    if(s == null){
                        s = new TFSwitch(port.getNetwork(),port.getNetCIDR());
                        switches.put(s.getNetwork(),s);
                    }
                    s.addHost(port);

                }
                break;
            case RouterTable:
                String routerName = st.nextToken().trim();
                String netDest =st.nextToken().trim();
                String nextHop =st.nextToken().trim();
                int portNumber=Integer.parseInt(st.nextToken().trim());
                TFRouter router = getRouterByName(routerName);
                if(router != null){
                    TFPort port = router.getPortByNumber(portNumber);
                    if(port!=null){
                        TFRouterTableEntry entry = new TFRouterTableEntry(netDest,nextHop,portNumber);
                        entry.port = port;
                        router.addTableEntry(entry);
                    }
                }
                break;
            default:
                throw new Exception("Error reading file.");
        }

        return element;
    }

    public List<TFNetworkElement> getNetworkElements(){
        if(netElements==null)
            netElements = new ArrayList<TFNetworkElement>();
        return netElements;
    }

    public Map<String,TFSwitch> getSwitches(){
        if(switches==null)
            switches = new HashMap<String,TFSwitch>();
        return switches;
    }

    public void tuneNetwork() throws UnknownHostException{
        for (TFNetworkElement ele : netElements) {
            if(ele instanceof TFNode){
                ((TFNode)ele).gateway = getGatewayByIP(((TFNode)ele).gatewayIP);
            }else if(ele instanceof TFRouter){
                ((TFRouter)ele).tuneRouterTable(getSwitches());
            }
        }
    }

    private TFRouter getGatewayByIP(String ip){
        List<TFRouter> routers = getRouters();
        for (TFRouter r : routers)
            if(r.hasIP(ip))
                return r;
        return null;
    }

    private List<TFRouter> getRouters(){
        return netElements
                .stream()
                .filter(e -> e instanceof TFRouter)
                .map(e -> (TFRouter) e)
                .collect(Collectors.toList());
    }

    private TFRouter getRouterByName(String name){
        Optional<TFNetworkElement> opt = netElements
                                            .stream()
                                            .filter(e -> e instanceof TFRouter && ((TFRouter)e).name.equals(name)).findFirst();
        if(opt.isPresent())
            return (TFRouter)opt.get();
        return null;
    }
}
