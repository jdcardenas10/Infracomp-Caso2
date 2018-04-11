package servidorCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Cliente {

	@SuppressWarnings("resource")
	public static void main(String args[]){
		
		String simetrico = args[0];
		String hmac = args[1];
		
		try {
			Socket socket=new Socket("localhost",19999);
			PrintWriter printer=new PrintWriter(socket.getOutputStream(),true);
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Protocolo protocolo = new Protocolo();
			protocolo.setAlgSimetrico(simetrico);
			protocolo.setAlgHmac(hmac);
			
			protocolo.procesarCadena(null);
			
			String respuesta;
			while ((respuesta = reader.readLine()) != null) {
				System.out.println("Servidor contestó: " + respuesta);
				String salida = protocolo.procesarCadena(respuesta);
				if (salida != null) {
					System.out.println("Cliente enviando: " + salida);
					printer.println(salida);
				}
			}	
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
