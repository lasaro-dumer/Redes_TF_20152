import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class Reader{
	public String  read(String caminho){
	StringBuilder sb = StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(".../Redes_TF_20152/"+caminho)))
		{
			String sCurrentLine;
			while((sCurrentLine = br.readLine()) != null){
				sb.append(sCurrentLine);
			}
		}catch (IOException e){
		e.printStackTrace();
		}
	return sb.toString();
	}
}
