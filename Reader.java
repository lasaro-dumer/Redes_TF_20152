import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class Reader{
    public static List<ITFNetworkElement> read(String fileName)  throws FileNotFoundException, IOException, Exception {
        return read(fileName,false);
    }

	public static List<ITFNetworkElement> read(String fileName,boolean fullPath)  throws FileNotFoundException, IOException, Exception {
		if(!fullPath)
			fileName = buildDefaultPath(fileName);

        //TODO: create individual list of Nodes and Routers to turn typed search faster
        List<ITFNetworkElement> netElements = new ArrayList<ITFNetworkElement>();

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
                st = new StringTokenizer(line, ",");
                switch (type) {
                    case Node:
                        netElements.add(new TFNode(st.nextToken().trim(),
                                                   st.nextToken().trim(),
                                                   st.nextToken().trim(),
                                                   st.nextToken().trim()));
                        break;
                    case Router:
                        router = new TFRouter(st.nextToken().trim(),Integer.parseInt(st.nextToken().trim()));
                        int pNumber = 0;
                        while(st.hasMoreElements()){
                            TFPort port = new TFPort();
                            port.number=pNumber;
                            port.MAC=st.nextToken().trim();
                            port.IP = st.nextToken().trim();
                            router.addPort(port);
                            pNumber++;
                        }
                        netElements.add(router);
                        break;
                    case RouterTable:
                        String routerName = st.nextToken().trim();
                        String netDest =st.nextToken().trim();
                        String nextHop =st.nextToken().trim();
                        int portNumber=Integer.parseInt(st.nextToken().trim());
                        //TODO: look at netElements declaration ToDo
                        for (ITFNetworkElement e : netElements)
                            if(e instanceof TFRouter){
                                router = (TFRouter)e;
                                if(router.name.equals(routerName)){
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
                            }
                        break;
                    default:
                        throw new Exception("Error reading file.");
                }
            }
            line = br.readLine();
        }
        br.close();
        return netElements;
	}

	private static String buildDefaultPath(String fileName){
		File file1 = new File("examples");
	    File file2 = new File(file1, fileName);
	    return file2.getPath();
	}
}
