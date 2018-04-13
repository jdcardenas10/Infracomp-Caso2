package servidorCliente;

import javax.xml.ws.ProtocolException;

public class Protocolo {
	
	private int estado;
	
	private Certificado certificado;
	
	private String algSimetrico;
	private String algAsimetrico;
	private String algHmac;
	
	private static final int INICIAR_SESION = 0;
	private static final int AUTENTICAR_CLIENTE= 1;
	private static final int AUTENTICAR_SERVIDOR=2;

	public void setAlgSimetrico(String simetrico) {
		// TODO Auto-generated method stub
		
	}

	public void setAlgHmac(String hmac) {
		algHmac=hmac;
		
	}

	public String procesarCadena(String respuesta, byte[] bites) {
		switch(estado){
		case INICIAR_SESION:
			if (respuesta == null) {
				return "HOLA";
			} else if (respuesta.equals("INICIO")) {
				return "ALGORITMOS:" + algSimetrico + ":" + algAsimetrico + ":" + algHmac;
			} else if(respuesta.equals("ESTADO:OK")){
				estado=AUTENTICAR_CLIENTE;
				return "CERTCLNT";
			} else {
				throw new ProtocolException("La entrada :" + respuesta + " no es valida en el inicio");
			}
		case AUTENTICAR_CLIENTE:
			if(respuesta.equals("ESTADO:OK")){
				estado=AUTENTICAR_SERVIDOR;
				return null;
			}
		case AUTENTICAR_SERVIDOR:
			
		}
		
		
		return null;
	}	
}
