import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Simulador{
	public static void main(String[] args){
		try{
			Parser p = new Parser();
			p.parseFile("topologia.txt");
			List<ITFNetworkElement> l = p.getNetworkElements();
			for (ITFNetworkElement e : l) {
				System.out.println(e.toString());
			}
			System.out.println("Selecting nodes only");
			List<TFNode> nodes = l
								.stream()
								.filter(e -> e instanceof TFNode && ((TFNode)e).name.equals("n1"))
								.map(e -> (TFNode) e)
								.collect(Collectors.toList());
			//System.out.println(nodes.toString());
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

	public  void mains(String arquivo, String comando, String origem, String destino){
		//Reader r = new Reader();
		//r.read(arquivo);
	}
}
