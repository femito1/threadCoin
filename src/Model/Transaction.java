package Model;

import java.io.Serializable;
import java.util.Objects;

public class Transaction implements Serializable {
    // Instance variables
    private Node sender;                  // Sender of the transaction
    private Node recipient;               // Recipient of the transaction
    private double amount;                // Amount of the transaction
    private final double fee;                   // Fee of the transaction

    // Constructor
    public Transaction(Node sender, Node recipient, double amount) {
        // Set the sender, recipient, and amount of the transaction
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount * 0.95;
        this.fee = amount * 0.05;
        // Create a new LinkedList to store transactions in the transaction queue
        // this.transactionQueue = new LinkedList<Transaction>();
    }

    public boolean equals(Transaction t){
        return (Objects.equals(t.getSender().id, sender.id)) && (t.getAmount() == amount) && (t.getFee() == fee) && (Objects.equals(t.getRecipient().id, recipient.id));
    }

    // Getter and setter methods for the sender, recipient, and amount instance variables
    public Node getSender() {
        return sender;
    }

    public void setSender(Node sender) {
        this.sender = sender;
    }

    public Node getRecipient() {
        return recipient;
    }

    public void setRecipient(Node recipient) {
        this.recipient = recipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee(){
        return this.fee;
    }
}
