package echoserver;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Database {
public static void create() throws IOException {
    File file = new File("database.db");
    if (!file.exists()) {
        System.out.println("Creating the database...");
        file.createNewFile();
    }
}
public static void addUser(String username, String password) throws IOException {
    byte[] hash= BCrypt.withDefaults().hash(6,password.toCharArray());
    Properties prop=new Properties();
    prop.load(new FileInputStream("database.db"));
    prop.setProperty(username, new String(hash)+"|"+"0");
    prop.store(new FileOutputStream("database.db"), null);
}

public static void addPoints(String username, int add) throws IOException {
    Properties prop=new Properties();
    prop.load(new FileInputStream("database.db"));
    String props=prop.getProperty(username);
    String[] split = props.split("\\|");
    int points=Integer.parseInt(split[1]);
    points+=add;
    String newPoints= split[0] + "|" + points;
    prop.setProperty(username, newPoints);
    prop.store(new FileOutputStream("database.db"), null);
}

}
