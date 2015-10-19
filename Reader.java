import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Reader{
    public Object read(String fileName)  throws FileNotFoundException, IOException, Exception {
        return read(fileName,false);
    }

	public Object read(String fileName,boolean fullPath)  throws FileNotFoundException, IOException, Exception {
		if(!fullPath)
			fileName = buildDefaultPath(fileName);

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

        while (line != null) {
            line = line.toLowerCase();
            System.out.println("line="+line);
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
                        break;
                    case Router:
                        break;
                    case RouterTable:
                        break;
                    default:
                        throw new Exception("Erro ao ler o arquivo.");
                }
            }
            line = br.readLine();
        }
        br.close();
        return null;
	}

	private String buildDefaultPath(String fileName){
		File file1 = new File("examples");
	    File file2 = new File(file1, fileName);
	    return file2.getPath();
	}
}
