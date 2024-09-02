
import Model.*;
import Network.*;

import java.util.Date;


/** So, I actually discovered the magic world of {@code docstrings}, and they are pretty neat.
 * They are basically the equivalent of {@code Python}'s block comments (the ones like {@code """comment"""}
 * that you could put before a method / function / attribute to give some documentation about it).
 * I'd like to actually use {@code docstrings} in an efficient way, so please try to learn a thing
 * or two about them, thanks. They could help us while coding. The most important keywords are:
 *
 * <li>{@code @param <parameter>}: for defining attributes;</li>
 * <li>{@code @return <value>}: for defining the return value of a method;</li>
 * <li>{@code @throws <exception}: for defining an eventual thrown exception;</li>
 * <li>{@code @link <valid class>}: for linking a class;</li>
 * <li>{@code @code <anything>}: for writing something with the {@code monospace} font</li>
 *
 * <p></p>Some further documentation may be found
 * <a href="https://www.jetbrains.com/help/idea/working-with-code-documentation.html">here</a></p>, on the
 * <b>JetBrains</b> official documentation.
 *
 * <p>These {@code docstrings} can be used only straight before a class / method / attribute,
 * so act accordingly. Ty :)
 * <br> ~Leo </p>
 * */

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Network.addBlockChain(new Block(0, null, "0000000000000000000000000000000000000000000000000000000000000000", new Date(), 0, null));

        Node Nand = new Node("Nand");


        Node Narges = new Node("Narges");


        Node Leo = new Node("Leo");


        Node Nik = new Node("Nik");


        Node Francesca = new Miner("Francesca");

        Node Chiara = new Miner("Chiara");

        Network.addNode(Nand);
        Network.addNode(Leo);
        Network.addNode(Nik);
        Network.addNode(Narges);
        Network.addNode(Francesca);
        Network.addNode(Chiara);



        Nand.start();
        Francesca.start();
        Chiara.start();
        Narges.start();
        Leo.start();
        Nik.start();

        try {
            Thread.sleep(10000);

        } catch (Exception ignored) {}

        Nand.interrupt();
        Narges.interrupt();
        Leo.interrupt();
        Nik.interrupt();
        Francesca.interrupt();
        Chiara.interrupt();






        try {
            Thread.sleep(3000);

        } catch (Exception ignored) {}

        System.out.println();

        System.out.println("Nand got away with: " + Nand.checkBalance() + " nuggets!");
        System.out.println("Narges managed to gather " + Narges.checkBalance() + " nuggets!");
        System.out.println("Leo somehow got his hands on " + Leo.checkBalance() + " nuggets!");
        System.out.println("I know for a fact that Nik didn't deserve to have " + Nik.checkBalance() + " nuggets!");
        System.out.println("Chiara worked her ass off to get " + Chiara.checkBalance() + " nuggets!");
        System.out.println("Francesca mined like a maniac to earn these " + Francesca.checkBalance() + " nuggets!");



        System.out.println();

        Network.displayChain();

    }

}