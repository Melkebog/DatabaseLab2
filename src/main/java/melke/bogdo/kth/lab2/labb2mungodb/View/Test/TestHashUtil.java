package melke.bogdo.kth.lab2.labb2mungodb.View.Test;

import melke.bogdo.kth.lab2.labb2mungodb.Controller.HashUtil;

public class TestHashUtil {
    public static void main(String[] args) {
        String input = "password";
        String hashed = HashUtil.hash(input);
        System.out.println("Hashed value: " + hashed);

        // Compare with the expected hash
        boolean matches = hashed.equals("5e884898da28047151d0e56f8dc6292773603d0d6aabbddaa652991a962d9f16");
        System.out.println("Does it match? " + matches);
    }
}
