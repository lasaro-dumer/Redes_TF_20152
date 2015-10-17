import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader{
	public static void main(String[] args){
	
		try (BufferedREader br = new BufferedReader(new FileReader("caminho do arquivo")))
		{
	
			String sCurrentLine;

			while((CurrentLine = br.readLine()) != null){
				System.out.println(sCurrentLine);
			}
		}catch (IOException e){
		e.printStackTrace();
		}
	}
}
