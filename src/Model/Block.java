package Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Block {
    private String transactions;
    private String hash;
    private Date timeStamp;
    private int nonce;
    private int index;
    String previousHash;

    public Block(int index, String transactions, String hash, Date timestamp, int nonce, String prevhash) {
        this.index = index;
        this.transactions = transactions;
        this.hash = hash;
        this.timeStamp = timestamp;
        this.nonce = nonce;
        this.previousHash = prevhash;
    }

    public int getIndex() {
        return this.index;
    }

    public synchronized String getPreviousHash() {
        return previousHash;
    }

    public synchronized Date getTimeStamp() {
        return timeStamp;
    }

    public synchronized String getTransactions() {
        return transactions;
    }

    public synchronized int getNonce() {
        return nonce;
    }

    public synchronized String getHash() {
        return hash;
    }

    public synchronized boolean isValid(String previousHash) {
        return previousHash.equals(this.previousHash);
    }
}
