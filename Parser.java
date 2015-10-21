import java.util.StringTokenizer;

public class Parser{

	pubic void parse(EntryType type){
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

}