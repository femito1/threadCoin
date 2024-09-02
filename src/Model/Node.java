package Model;
import Exceptions.TransactionNotValid;
import Network.Network;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.function.Function;

public class Node extends Thread implements Serializable {
    private volatile int count = 0;
    public String id;
    private volatile int broadcast_signal;

    public volatile Stack<Object[]> received;
    Wallet mywallet;
    boolean running = false;

    public Node(String id) {
        this.id = id;
        this.mywallet = new Wallet();
        this.received = new Stack<>();

    }

    @Override
    public void run() {
        running = true;
        while (count < 20 && running) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (broadcast_signal) {
                case 0:             // for making a transaction (?) (or in general if Node has to do smth before)
                    byte[][] encrypted;
                    int low = 1;
                    int high = 50;
                    int toSend = (int) (Math.random() * (high - low)) + low;
                    int len = Network.getNodesLength();
                    ArrayList<Node> my_list = Network.getNodes();
                    ArrayList<Node> l = new ArrayList<>();
                    for (Node n:
                         my_list) {
                        if (!n.equals(this)) l.add(n);
                    }

                    Random random = new Random();
                    int index = random.nextInt(len - 1);


                    Node receiver = l.get(index);
                    if (mywallet.balance >= toSend) {
                        Transaction x = make_transaction(receiver, toSend);
                        try {
                            encrypted = mywallet.encrypt(x);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        assert (receiver != null);
                        {
                            receiver.uploadNewTransaction(x, encrypted);
                            receiver.changeSignal(1);
                        }
                        count++;
                        System.out.println(id + " paid " + toSend + " to " + receiver.id);
                    } else {
                        System.out.println(id + ": Oops, I am too generous");
                    }

                case 1:             // Validates then adds to trans-q
                    if (!received.isEmpty()) {
                        Object[] tuple = received.pop();
                        Transaction toCheck = (Transaction) tuple[0];
                        byte[][] enc = (byte[][]) tuple[1];
                        Transaction beCheck = decrypt(toCheck, enc);
                        if (toCheck.equals(beCheck)) {
                            mywallet.balanceupdate(toCheck.getAmount());
                            Network.addTransactionToPendingTransaction(toCheck);
                        } else System.out.println("Transaction Invalid!");
                    }
                    broadcast_signal = 0;
            }
        }
    }

    @Override
    public synchronized void interrupt(){
        running = false;
    }
    public synchronized void changeBalance(double amount) {
        mywallet.balanceupdate(amount);
    }

    public double checkBalance() {
        return mywallet.balance;
    }

    public PublicKey getPublicKey() {
        return mywallet.public_key;
    }

    public synchronized Transaction make_transaction(Node recipient, double amount) {
        double amountToSubtract = amount * (-1);

        this.changeBalance(amountToSubtract);
        Transaction transaction = new Transaction(this, recipient, amount);
        return transaction;

    }

    public synchronized static Transaction decrypt(Transaction x, byte[][] tuple) {
        byte[] encryptedData = tuple[0];
        byte[] encryptedSymmetricKey = tuple[1];

        Function<byte[], SecretKey> decryptKeyFunc = (encryptedKey) -> {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, x.getSender().getPublicKey());
                byte[] decryptedSymmetricKeyBytes = cipher.doFinal(encryptedKey);
                return new SecretKeySpec(decryptedSymmetricKeyBytes, "AES");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        SecretKey decryptedSymmetricKey = decryptKeyFunc.apply(encryptedSymmetricKey);

        Function<byte[], byte[]> decryptDataFunc = (encData) -> {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, decryptedSymmetricKey);
                return cipher.doFinal(encData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        Function<byte[], Transaction> deserializeFunc = (bytes) -> {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return (Transaction) ois.readObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };


        // Decrypt the data using the decrypted symmetric key
        // Deserialize the Transaction object

        return deserializeFunc.apply(decryptDataFunc.apply(encryptedData));

    }


    // implement something inside node that when a TRANSACTION IS VALIDATED THE BALANCE SHOULD BE REDUCED

    /**
     * Changes the signal of one {@link Node}
     *
     * @param newSignal the new signal (of type {@code int})
     */
    public synchronized void changeSignal(int newSignal) {
        this.broadcast_signal = newSignal; //cum
    }

    /**
     * Changes the content that must overwrite the old transaction
     *
     * @param content the new content (as an {@link Object} array)
     */
    public synchronized void uploadNewTransaction(Transaction trans, byte[][] content) {
        received.add(new Object[] {trans, content});
    }

}

    /** Either {@code STOP}s or {@code KILL}s the thread, depending on the value of {@code killable}
     * @param killable if {@code true}, then the {@code broadcast_signal} will become {@code 3} and the {@code assets} will be
     *                 erased. If {@code false}, then the {@code broadcast_signal} will be saved and the {@code assets}
     *                 won't be modified */
    /** Continues the process if there is one stopped */
