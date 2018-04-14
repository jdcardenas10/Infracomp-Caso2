package servidorCliente;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.ws.ProtocolException;

import org.bouncycastle.util.encoders.Hex;

public class Protocolo {
	
	private int estado;
	
	private Certificado certificado;
	
	private String algSimetrico;
	private String algAsimetrico;
	private String algHmac;

	private PublicKey publicKeySer;

	private byte[] llaveSimetrica;
	
	private static final int INICIAR_SESION = 0;
	private static final int INTERCAMBIAR_ALGORITMOS=1;
	private static final int AUTENTICAR_CLIENTE= 2;
	private static final int AUTENTICAR_SERVIDOR=3;
	private static final int RECIBIR_LLAVE=4;
	
	

	public Protocolo() throws Exception {
		estado=INICIAR_SESION;
		certificado=new Certificado();
	}

	public void procesarCadena(InputStream in,OutputStream out,PrintWriter printer,BufferedReader reader) throws IOException, CertificateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		//switch(estado){
		//case INICIAR_SESION:
			estado=INTERCAMBIAR_ALGORITMOS;
				printer.println("HOLA");
		//case INTERCAMBIAR_ALGORITMOS:
			if (reader.readLine().equals("INICIO")) {
				printer.println("ALGORITMOS:" + algSimetrico + ":" + algAsimetrico + ":" + algHmac);}
			if(reader.readLine().equals("ESTADO:OK")){
				estado=AUTENTICAR_CLIENTE;
				printer.println("CERTCLNT");
			} else {
				throw new ProtocolException("La entrada no es valida en el inicio");
			}
		//case AUTENTICAR_CLIENTE:
			X509Certificate cert = certificado.certificado;
			byte[] mybyte = cert.getEncoded();
			out.write(mybyte);
			out.flush();
			if(reader.readLine().equals("ESTADO:OK")){
				estado=AUTENTICAR_SERVIDOR;	
			} else {
				throw new ProtocolException("No se pudo mandar el certificado");
			}
		//case AUTENTICAR_SERVIDOR:
			if(!"CERTSRV".equals(reader.readLine())){
				throw new ProtocolException("No ha comenzado a mandar el protocolo del cliente");
			}
			
			try{
			byte[] certificado = new byte[1024];
			in.read(certificado);
			System.out.println(certificado);
			
			//X509Certificate 
			X509Certificate certSer = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(certificado));
			publicKeySer = certSer.getPublicKey();
            certSer.verify(publicKeySer);
			System.out.println(certSer.toString());
			
			printer.println("ESTADO:OK");
			} catch (Exception e) {
				e.printStackTrace();
				printer.println("ESTADO:ERROR");
			}
		//case RECIBIR_LLAVE:
			String entrada = reader.readLine();
			String[] div = entrada.split(":");
			
			Cipher cipher = Cipher.getInstance(algAsimetrico);
			cipher.init(Cipher.DECRYPT_MODE, certificado.getPrivada());
			llaveSimetrica = cipher.doFinal(Hex.decode(div[1]));
			System.out.println("Llave simétrica: " + Arrays.toString(llaveSimetrica));
			
			Cipher cipher1 = Cipher.getInstance(algSimetrico);
			SecretKeySpec keySpec = new SecretKeySpec(llaveSimetrica, algSimetrico);
			cipher1.init(Cipher.ENCRYPT_MODE, keySpec);

			String posicion="41 24.2028, 2 10.4418";

			printer.println("ACT1:"+Hex.toHexString(cipher1.doFinal((posicion).getBytes())));
			
			Cipher cipher2 = Cipher.getInstance(algSimetrico);
			cipher2.init(Cipher.ENCRYPT_MODE, keySpec);
			
			printer.println("ACT2:"+Hex.toHexString(cipher2.doFinal(((posicion).hashCode()+"").getBytes())));
			
			if("ESTADO:OK".equals(reader.readLine())){
				System.out.println("Se acabo");
			}
		//}	
	}

	public String getAlgSimetrico() {
		return algSimetrico;
	}

	public void setAlgSimetrico(String algSimetrico) {
		this.algSimetrico = algSimetrico;
	}

	public String getAlgAsimetrico() {
		return algAsimetrico;
	}

	public void setAlgAsimetrico(String algAsimetrico) {
		this.algAsimetrico = algAsimetrico;
	}

	public String getAlgHmac() {
		return algHmac;
	}

	public void setAlgHmac(String algHmac) {
		this.algHmac = algHmac;
	}	
	
	
}
