
import java.util.List;
import java.util.ArrayList;

public class Simulador{
	public static void main(String[] args){
		try{
			List<ITFNetworkElement> l = Reader.read("topologia.txt");
			for (ITFNetworkElement e : l) {
				System.out.println(e.toString());
			}
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

	public  void mains(String arquivo, String comando, String origem, String destino){
		//Reader r = new Reader();
		//r.read(arquivo);
	}
}
