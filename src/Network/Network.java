package Network;

import java.util.*;

import Model.*;

/** In order to not create a centralized system, the network should be <b>ONLY</b> a container of the nodes,
 * but should be able to return also the transactions that were made. In the simulation everything is working
 * towards the goal of making a single blockchain, thus the reason of having the transactions' list / history.*/
public class Network {
    private volatile static ArrayList<Node> nodesList = new ArrayList<>();
    private volatile static ArrayList<Miner> minersList = new ArrayList<>();
    private volatile static ArrayList<Block> blockChain = new ArrayList<>();
    public volatile static Stack<Transaction> pendingTransactions = new Stack<>();


    public static synchronized void addTransactionToPendingTransaction(Transaction transactionId) {
        pendingTransactions.add(transactionId);
    }

    public static synchronized int getNodesLength() {
        return nodesList.size();
    }
    public static synchronized int howManyTransactions(){
      return pendingTransactions.size();
    }

    public static synchronized ArrayList<Node> getNodes() {
        return nodesList;
    }

    public static synchronized ArrayList<Transaction> getFirstTransactions() {

        ArrayList<Transaction> firstFive = new ArrayList<>();
        if (pendingTransactions.size() != 0){
            firstFive.add(pendingTransactions.remove(0));
        }
        return null;
    }

    /**
     * Adds a {@link Miner} to the {@code minersList}
     *
     * @param newMiner the {@link Miner} that will be added to the list
     */
    public static void addMiner(Miner newMiner) {
        minersList.add(newMiner);
    }

    /**
     * Adds a {@link Node} to the {@code nodesList}
     *
     * @param newNode the {@link Node} that will be added to the list
     */
    public static synchronized void addNode(Node newNode) {
        nodesList.add(newNode);
    }

    public static synchronized ArrayList<Block> getBlockChain() {
        return blockChain;
    }

    /**
     * Creates a {@link Network} from zero, given a list of nodes and miners that compose the network.
     *
     * @param composingNodes  an {@link ArrayList} of {@link Node} objects that are part of the network;
     * @param composingMiners an {@link ArrayList} of {@link Miner} objects, working on the same network.
     */
    /**
     * Creates a {@link Network} from zero, without any list of nodes that composes the network.
     */

    public static synchronized void addBlockChain(Block block) {
        if (blockChain.size() == 0 | blockChain.size() == 1) {
            blockChain.add(block);
        } else if (block.isValid(getLastHash())) {
            blockChain.add(block);
        }
    }
    public static void displayChain() {
        for (Block block : blockChain) {
            System.out.println("Block: " + block.getIndex());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Transactions: " + block.getTransactions());
            System.out.println("Nonce: " + block.getNonce());
            System.out.println("------------------------");
        }
    }
    public static synchronized String getLastHash() {
        return blockChain.get(blockChain.size() - 1).getHash();
    }
    public static synchronized int getNextIndex(){
        return blockChain.size();
    }

}

    /** Broadcasts to the whole network a {@link Transaction} that has to be validated
     * @param transaction the {@link Transaction} (of type {@link String}) that has to be broadcast;
     * @param transObj the {@link Transaction} object of the transaction
     * @param broadcaster the {@link Node} object that is sending the content.
     * */


    /** Broadcasts to the whole network a {@link Transaction} that has to be encrypted
     * @param transaction the transaction (of type {@link String})
     * @param transObj the transaction object (of type {@link Transaction})
     * @param broadcaster the {@link Node} that is broadcasting to the whole {@link Network}
     * */
