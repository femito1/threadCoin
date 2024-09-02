package Model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.util.function.Function;


public class Wallet implements Serializable {
    public PublicKey public_key;
    private final PrivateKey private_key;
    public volatile double balance = 200;



    public Wallet() {
        KeyPairGenerator KeysGenerator;
        try {
            KeysGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        KeysGenerator.initialize(512);
        KeyPair Keys = KeysGenerator.generateKeyPair();
        public_key = Keys.getPublic();
        private_key = Keys.getPrivate();
    }

    public synchronized byte[][] encrypt(Transaction transaction) throws Exception {


        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();


        Function<Transaction, byte[]> serializeFunc = (trans) ->{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(trans);
                oos.flush();
            } catch (IOException e) {throw new RuntimeException(e);}
            return bos.toByteArray();
        };

        Function<byte[],  byte[]> encryptFunc = (data) -> {

            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(data);
            } catch (Exception e) {throw new RuntimeException(e);}
        };

        Function<SecretKey, byte[]> encryptKey = (k) -> {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, private_key);
                return cipher.doFinal(k.getEncoded());
            } catch (Exception e){throw new RuntimeException(e);}
        };


        byte[] encryptedData = encryptFunc.apply(serializeFunc.apply(transaction));
        byte[] encryptedSymmetricKey = encryptKey.apply(secretKey);

        // Send the encrypted symmetric key and the encrypted data to the recipient...

        return new byte[][] {encryptedData, encryptedSymmetricKey};
    }


    public synchronized void balanceupdate(double v){
        balance += v;
    }//cum
}
