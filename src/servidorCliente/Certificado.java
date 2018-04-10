package servidorCliente;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Certificado {

	private PrivateKey privada;
	private PublicKey publica;
	
	
	
	public PrivateKey getPrivada() {
		return privada;
	}
	public void setPrivada(PrivateKey privada) {
		this.privada = privada;
	}
	public PublicKey getPublica() {
		return publica;
	}
	public void setPublica(PublicKey publica) {
		this.publica = publica;
	}
	
}
