import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Database {
    static String PATHNAME = "data/database.db";
public static void create() throws IOException {
    Files.createDirectories(Paths.get("data"));
    File file = new File(PATHNAME);
    if (!file.exists()) {
        System.out.println("Creating the database...");
        file.createNewFile();
    }
}
public static String giveProp(String username) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream(PATHNAME));
    return properties.getProperty(username);
}
public static boolean userExists(String username) throws IOException {
Properties properties = new Properties();
properties.load(new FileInputStream(PATHNAME));
    return properties.getProperty(username) != null;
}
public static void addUser(String username, String password) throws IOException {
    byte[] hash= BCrypt.withDefaults().hash(6,password.toCharArray());
    Properties prop=new Properties();
    prop.load(new FileInputStream(PATHNAME));
    prop.setProperty(username, new String(hash)+"|"+"0");
    prop.store(new FileOutputStream(PATHNAME), null);
}

public static void addPoints(String username, int add) throws IOException {
    Properties prop=new Properties();
    prop.load(new FileInputStream(PATHNAME));
    String props=prop.getProperty(username);
    String[] split = props.split("\\|");
    int points=Integer.parseInt(split[1]);
    points+=add;
    String newPoints= split[0] + "|" + points;
    prop.setProperty(username, newPoints);
    prop.store(new FileOutputStream(PATHNAME), null);
}

}
