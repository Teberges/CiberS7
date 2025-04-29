import java.security.*;
import java.util.Base64;

public class AssinaturaDigital{
    public static KeyPair gerarParDeChaves() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static String assinarMensagem(String mensagem, PrivateKey chavePrivada) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(chavePrivada);
        signature.update(mensagem.getBytes());
        byte[] assinatura = signature.sign();
        return Base64.getEncoder().encodeToString(assinatura);
    }
    public static boolean verificarAssinatura(String mensagem, String assinatura, PublicKey chavePublica) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(chavePublica);
        signature.update(mensagem.getBytes());
        byte[] assinaturaDecodificada = Base64.getDecoder().decode(assinatura);
        return signature.verify(assinaturaDecodificada);
    }
    public static void main(String[] args) {
        try {
            KeyPair parDeChaves = gerarParDeChaves();
            PrivateKey chavePrivada = parDeChaves.getPrivate();
            PublicKey chavePublica = parDeChaves.getPublic();

            String mensagem = "Mensagem secreta";

            String assinatura = assinarMensagem(mensagem, chavePrivada);
            System.out.println("Assinatura: " + assinatura);

            boolean assinaturaValida = verificarAssinatura(mensagem, assinatura, chavePublica);
            System.out.println("A assinatura é válida? " + assinaturaValida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}