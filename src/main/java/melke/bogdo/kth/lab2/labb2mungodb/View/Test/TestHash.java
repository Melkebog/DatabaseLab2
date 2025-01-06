package melke.bogdo.kth.lab2.labb2mungodb.View.Test;

import melke.bogdo.kth.lab2.labb2mungodb.Controller.HashUtil;

public class TestHash {
    public static void main(String[] args) {
        String password = "password";
        String hashedPassword = HashUtil.hash(password);
        System.out.println("Hashed Password: " + hashedPassword);
        // Compare this with the hashed password stored in your database
    }
}
