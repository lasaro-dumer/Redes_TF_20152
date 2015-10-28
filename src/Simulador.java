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
			for (ITFNetworkElement e : netElements) {
				System.out.println(e.toString());
			}
			System.out.println("Selecting nodes only");
			List<TFNode> nodes = netElements
								.stream()
								.filter(e -> e instanceof TFNode && ((TFNode)e).name.equals("n1"))
								.map(e -> (TFNode) e)
								.collect(Collectors.toList());
			//System.out.println(nodes.toString());
			Optional<ITFNetworkElement> opt = netElements.stream().filter(e -> e instanceof TFNode &&
																				((TFNode)e).name.equals(srcName)).findFirst();
	        if(opt.isPresent())
	            System.out.println("source: "+opt.get().toString());
	        else
				System.out.println("source not found");
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
}
