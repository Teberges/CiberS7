import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

public class CriptografiaRSA {
    private PublicKey chavePublica;
    private PrivateKey chavePrivada;
    
    public void gerarChaves() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair parDeChaves = keyGen.generateKeyPair();
        this.chavePublica = parDeChaves.getPublic();
        this.chavePrivada = parDeChaves.getPrivate();
    }

    public String getChavePublica() {
        return Base64.getEncoder().encodeToString(chavePublica.getEncoded());
    }
    public String getChavePrivada() {
        return Base64.getEncoder().encodeToString(chavePrivada.getEncoded());
    }

    public String criptografar(String mensagem) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, chavePublica);
        byte[] mensagemCriptografada = cipher.doFinal(mensagem.getBytes());
        return Base64.getEncoder().encodeToString(mensagemCriptografada);
    }

    public String descriptografar(String mensagemCriptografada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, chavePrivada);
        byte[] mensagemDecifrada = Base64.getDecoder().decode(mensagemCriptografada);
        byte[] mensagemOriginal = cipher.doFinal(mensagemDecifrada);
        return new String(mensagemOriginal);
    }

    public static void main(String[] args){
        try{
            CriptografiaRSA rsa = new CriptografiaRSA();
            rsa.gerarChaves();

            System.out.println("Chave Pública: " + rsa.getChavePublica());
            System.out.println("Chave Privada: " + rsa.getChavePrivada());

            String mensagem = "Mensagem secreta";
            System.out.println("Mensagem original: " + mensagem);

            String mensagemCriptografada = rsa.criptografar(mensagem);
            System.out.println("Mensagem criptografada: " + mensagemCriptografada);

            String mensagemDescriptografada = rsa.descriptografar(mensagemCriptografada);
            System.out.println("Mensagem descriptografada: " + mensagemDescriptografada);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}