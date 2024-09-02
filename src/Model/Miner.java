package Model;
import Network.*;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Miner extends Node implements Serializable {
    public Miner(String id){
        super(id);
        mywallet.balance = 0;
    }
    @Override
    public void run() {
        Random random = new Random();
        running = true;

        while (running) {

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<Transaction> transactions = new ArrayList<>();

            int numTransactions = random.nextInt(5) + 1; // Generate 1 to 5 transactions
            for (int i = 0; i < numTransactions; i++) {
                Transaction transaction = new Transaction(new Node("A"), new Node("B"), random.nextDouble());
                transactions.add(transaction);
                mywallet.balanceupdate(0.8);
            }


            Block newBlock = mine(Network.getNextIndex(), transactions.toString());
            Network.addBlockChain(newBlock);
            System.out.println(id + " mined a block with " + numTransactions + " transactions!");

        }
    }

    private synchronized Block mine(int index, String transactions) {
        int targetDifficulty = 4;
        String previousHash = Network.getLastHash();
        String targetPrefix = "0".repeat(targetDifficulty); // = "0000"
        int nonce = 0;
        String hash = "";
        Date date = new Date();
        while (true){
            assert hash != null;
            if (hash.startsWith(targetPrefix)) break;
            date = new Date();
            hash = calculateHash(index, previousHash, date, transactions, nonce);
            nonce++;};
        return new Block(index, transactions, hash ,date, nonce, previousHash);
    }

    private synchronized String calculateHash(int index, String previousHash, Date timeStamp, String transactions, int nonce) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String data = index +
                    formatter.format(timeStamp) +
                    previousHash +
                    transactions +
                    nonce;

            byte[] hashBytes = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }


}
