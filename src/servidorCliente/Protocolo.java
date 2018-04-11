package servidorCliente;

import javax.xml.ws.ProtocolException;

public class Protocolo {
	
	private int estado;
	
	private String algSimetrico;
	private String algAsimetrico;
	private String algHmac;
	
	private static final int INICIAR_SESION = 0;
	private static final int AUTENTICAR_CLIENTE_INICIO = 1;
	private static final int AUTENTICAR_CLIENTE_RESPUESTA = 2;

	public void setAlgSimetrico(String simetrico) {
		// TODO Auto-generated method stub
		
	}

	public void setAlgHmac(String hmac) {
		algHmac=hmac;
		
	}

	public String procesarCadena(String respuesta) {
		switch(estado){
		case INICIAR_SESION:
			if (respuesta == null) {
				return "HOLA";
			} else if (respuesta.equals("INICIO")) {
				return "ALGORITMOS:" + algSimetrico + ":" + algAsimetrico + ":" + algHmac;
			} else if(respuesta.equals("ESTADO:OK")){
				estado=AUTENTICAR_CLIENTE_INICIO;
				return "CERTCLNT";
			} else {
				throw new ProtocolException("La entrada :" + respuesta + " no es valida en el inicio");
			}
		case AUTENTICAR_CLIENTE_INICIO:
			estado=AUTENTICAR_CLIENTE_RESPUESTA;
			return null;
		case AUTENTICAR_CLIENTE_RESPUESTA:
			if(respuesta.equals("ESTADO:OK")){
				return null;
			}
		}
		return null;
	}	
}
