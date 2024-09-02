import Model.*;
import Network.*;

import java.util.ArrayList;
import java.util.Stack;

public class TestDrive {
    public static void main(String[] args) {

        ArrayList<Object> L = new ArrayList<>();
        Stack<String> x = new Stack<>();
        x.push("Hi");
        L.add(x);
        System.out.println(L);
    }
}
