import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class CriptografiaAES {
    
    public static SecretKey gerarChaveAES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    public static String criptografar(String texto, SecretKey chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }

    public static String descriptografar(String mensagemCriptografada, SecretKey chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] mensagemDecifrada = Base64.getDecoder().decode(mensagemCriptografada);
        byte[] mensagemOriginal = cipher.doFinal(mensagemDecifrada);
        return new String(mensagemOriginal);
    }

    public static void main(String[] args){
        try{
            SecretKey chave = gerarChaveAES();

            String mensagem = "Mensagem secreta";
            String mensagemCriptografada = criptografar(mensagem, chave);
            System.out.println("Mensagem criptografada: " + mensagemCriptografada); 

            String mensagemDescriptografada = descriptografar(mensagemCriptografada, chave);
            System.out.println("Mensagem descriptografada: " + mensagemDescriptografada);   

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}