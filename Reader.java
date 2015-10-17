import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader{
	public static void main(String[] args){
	
		try (BufferedReader br = new BufferedReader(new FileReader("/media/joao/Flash/GitHub/Redes_TF_20152/topologia.txt")))
		{
	
			String sCurrentLine;

			while((sCurrentLine = br.readLine()) != null){
				System.out.println(sCurrentLine);
			}
		}catch (IOException e){
		e.printStackTrace();
		}
	}
}
