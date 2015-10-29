import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;

public class Simulador{
	public static boolean DEBUG=false;
	public static void main(String[] args){
		try{
			String filename="";
			SimCommand command;
			String sCommand,srcName,dstName;
			boolean example = false;
			if(args.length<4)
				throw new Exception("Invalid arguments");
			else if(args.length > 4){
				DEBUG = args[4].toLowerCase().equals("debug");
				example = args.length > 5 && args[5].toLowerCase().equals("true");
				if(DEBUG)
					for (int i=0;i<args.length;i++)
						System.out.println("args["+i+"]="+args[i]);
			}
			filename = args[0];
			sCommand = args[1].toLowerCase();
			if(sCommand.equals("ping"))
				command = SimCommand.Ping;
			else if(sCommand.equals("trace"))
				command = SimCommand.Trace;
			else
				throw new Exception("Invalid command "+sCommand);
			srcName = args[2];
			dstName = args[3];

			Parser p = new Parser();
			p.parseFile(filename,!example);
			List<ITFNetworkElement> netElements = p.getNetworkElements();
			if(DEBUG)
				for (ITFNetworkElement e : netElements)
					System.out.println(e.toString());

			Optional<ITFNetworkElement> opt = netElements.stream().filter(e -> e instanceof TFNode && ((TFNode)e).name.equals(srcName)).findFirst();
	        if(opt.isPresent()){
	            System.out.println("source: "+opt.get().toString());
				if(!netElements.stream().filter(e -> e instanceof TFNode && ((TFNode)e).name.equals(srcName)).findFirst().isPresent())
					throw new Exception("Destination not found");
				TFNode source = (TFNode)opt.get();
				//TODO: clean up after tests
				//Node requesting gateway MAC
				ARPPackage request = new ARPPackage(source.MAC,source.getIP(),source.gatewayIP);
				ARPPackage response = source.gateway.doARPRequest(request);
				source.addArpEntry(response.IP_src,response.MAC_src);
				System.out.println("arpt="+source.printARPTable());
				System.out.println("request:  " + request);
				System.out.println("response: " + response);

				//Router requesting node MAC
				TFRouter r1 = source.gateway;//(TFRouter)netElements.stream().filter(e -> e instanceof TFRouter && ((TFRouter)e).name.equals("r1")).findFirst().get();
				request = r1.createARPRequest(0,source.getIP());
				response = source.doARPRequest(request);
				System.out.println("request:  " + request);
				System.out.println("response: " + response);

				switch (command) {
					case Ping:
						break;
					case Trace:
						break;
				}
			}else
				throw new Exception("Source not found");
		}catch(Exception ex){
			System.out.println(ex.toString());
			ex.printStackTrace();
		}
	}
}
