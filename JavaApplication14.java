package javaapplication14;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JavaApplication14 {
    public static void main(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int move_player_int = -1;
        String move_player = null;

        if (args.length < 3 || args.length % 2 == 0) {
            if (!isUnique(args))
                System.out.println("The count of moves must be more than or equal to 3 and must be unpaired and possible moves must be unique.\nFor example: rock paper scissors lizard spock.");
            else
                System.out.println("Error. Try again.\nFor example: rock paper scissors.");
            return;
        }

        if (!isUnique(args)) {
            System.out.println("Possible moves must be unique.\nFor example: rock paper scissors.");
            return;
        }

        int move_opponent_int = (int) ((Math.random() * (args.length - 0)) + 0);
        String move_opponent = args[move_opponent_int];

        KeyGenerator k = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        k.init(random);
        SecretKey secretKey = k.generateKey();
        String key = new BigInteger(1, secretKey.getEncoded()).toString(16);
        System.out.println("HMAC:\n" + calculateHMac(key, move_opponent));
        System.out.println("Available moves:");
        for (int i = 0; i < args.length; i++) {
            System.out.println((i + 1) + " - " + args[i]);
        }
        System.out.println("0 - exit");

        System.out.print("Enter your move: ");
        move_player_int = Integer.parseInt(r.readLine());
        if (move_player_int == 0)
            return;
        while (move_player_int <= 0 || move_player_int > args.length){
            System.out.print("There`s no such move. Try again.\nEnter your move: ");
            move_player_int = Integer.parseInt(r.readLine());
        }
        move_player = args[move_player_int - 1];
        System.out.println("Your move: " + move_player);

        System.out.println("Computer move: " + move_opponent);

        System.out.println(get_results(args, (move_player_int-1), move_opponent_int));

        System.out.println("HMAC key:\n" + key);

    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder s = new StringBuilder(a.length * 2);
        for(byte b: a)
            s.append(String.format("%02x", b));
        return s.toString();
    }
    public static String calculateHMac(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return byteArrayToHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
    public static String get_results(String[] args, int move_user_int, int move_computer_int){
        int CountOfWins = (args.length-1)/2;
        ArrayList<String> winner = new ArrayList<>(CountOfWins);
        ArrayList<String> loser = new ArrayList<>(CountOfWins);
        if (move_user_int == move_computer_int)
            return "Dead heat.";
        if (move_user_int + CountOfWins > args.length-1){
            for (int i = move_user_int-1; i >= move_user_int-CountOfWins; i --){
                loser.add(args[i]);
            }
        } else{
            for (int i = move_user_int+1; i <= move_user_int+CountOfWins; i++) {
                winner.add(args[i]);
            }
        }
        if (winner.isEmpty()){
            if (loser.contains(args[move_computer_int])){
                return "You win!";
            } else {
                return "You lose.";
            }
        } else{
            if (winner.contains(args[move_computer_int])){
                return "You lose.";
            }else {
                return "You win!";
            }
        }
    }
    public static boolean isUnique(String[] args) {
        Set t = new HashSet();

        for (String arg : args) {
            if (!t.add(arg)) {
                return false;
            }
        }
        return true;
    }
}