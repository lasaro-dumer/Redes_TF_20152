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

public class Parser{
    private List<ITFNetworkElement> netElements;

    public Parser(){
        netElements = new ArrayList<ITFNetworkElement>();
    }
    public void parseFile(String fileName)  throws FileNotFoundException, IOException, Exception {
        parseFile(fileName,false);
    }

	public void parseFile(String fileName,boolean fullPath)  throws FileNotFoundException, IOException, Exception {
		if(!fullPath)
			fileName = buildDefaultPath(fileName);

        this.netElements = new ArrayList<ITFNetworkElement>();

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
                ITFNetworkElement element = parseLine(type,line);
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

    private ITFNetworkElement parseLine(EntryType type,String line) throws Exception{
	    StringTokenizer st = new StringTokenizer(line, ",");
        ITFNetworkElement element = null;
        switch (type) {
            case Node:
                element = new TFNode(st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim());
                break;
            case Router:
                element = new TFRouter(st.nextToken().trim(),Integer.parseInt(st.nextToken().trim()));
                int pNumber = 0;
                while(st.hasMoreElements()){
                    TFPort port = new TFPort();
                    port.number=pNumber;
                    port.MAC=st.nextToken().trim();
                    port.setIPrefix(st.nextToken().trim());
                    ((TFRouter)element).addPort(port);
                    pNumber++;
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
                        TFRouterTableEntry entry = new TFRouterTableEntry();
                        entry.netDest = netDest;
                        //TODO: yet at individual lists, here could search for a Node, to store a object, see TFRouterTableEntry ToDo
                        entry.nextHop = nextHop;
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

    public List<ITFNetworkElement> getNetworkElements(){
        if(netElements==null)
            netElements = new ArrayList<ITFNetworkElement>();
        return netElements;
    }

    public void tuneNetwork(){
        for (ITFNetworkElement ele : netElements) {
            if(ele instanceof TFNode){
                ((TFNode)ele).gateway = getGatewayByIP(((TFNode)ele).gatewayIP);
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
        Optional<ITFNetworkElement> opt = netElements
                                            .stream()
                                            .filter(e -> e instanceof TFRouter && ((TFRouter)e).name.equals(name)).findFirst();
        if(opt.isPresent())
            return (TFRouter)opt.get();
        return null;
    }
}
