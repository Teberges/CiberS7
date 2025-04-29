import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;

public class SimulaçãoCarlosMelissa {

    public static void main(String[] args) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair melissaKeyPair = keyGen.generateKeyPair();

            KeyPair carlosKeyPair = keyGen.generateKeyPair();

            String mensagem = "Olá carlos, esta é uma mensagem segura de melissa!";
            System.out.println("Mensagem original: " + mensagem);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, carlosKeyPair.getPublic());
            byte[] mensagemCriptografada = cipher.doFinal(mensagem.getBytes());
            String mensagemCriptografadaBase64 = Base64.getEncoder().encodeToString(mensagemCriptografada);
            System.out.println("Mensagem criptografada: " + mensagemCriptografadaBase64);

            cipher.init(Cipher.DECRYPT_MODE, carlosKeyPair.getPrivate());
            byte[] mensagemDecriptografada = cipher.doFinal(Base64.getDecoder().decode(mensagemCriptografadaBase64));
            System.out.println("Mensagem decriptografada: " + new String(mensagemDecriptografada));

            Signature assinatura = Signature.getInstance("SHA256withRSA");
            assinatura.initSign(melissaKeyPair.getPrivate());
            assinatura.update(mensagem.getBytes());
            byte[] assinaturaBytes = assinatura.sign();
            String assinaturaBase64 = Base64.getEncoder().encodeToString(assinaturaBytes);
            System.out.println("Assinatura digital: " + assinaturaBase64);

            assinatura.initVerify(melissaKeyPair.getPublic());
            assinatura.update(mensagem.getBytes());
            boolean assinaturaValida = assinatura.verify(Base64.getDecoder().decode(assinaturaBase64));
            System.out.println("Assinatura válida: " + assinaturaValida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}