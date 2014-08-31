/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.joelcompton.encryptsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pnv8813
 */
public class Buddies {

    File buddiesList;
    HashMap buddiesHashMap = new HashMap<String, BigInteger>();

    public Buddies() {
        File buddiesList = new File("buddiesList.txt");
        File file = new File("newfile.txt");
        try {
            file.createNewFile();
        } catch (IOException ioe) {
            System.out.println("Error while creating empty file: " + ioe);
        }
        loadBuddies();
    }

    //adds new public key to hashmap
    public void storeNewKey(String phoneNumber, BigInteger newpublickey) {
        buddiesHashMap.put(phoneNumber, newpublickey);
        
                //get keys and values from hashmap(separate with commas or colons)
        //get set of keys from hashmap
        Set keys = buddiesHashMap.keySet();
        Object key[] = keys.toArray();
        String values = "";
        String str = "";
        String newline = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(buddiesList.getName()));

        } catch (IOException ex) {
            Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //iterate through the set of keys to get the corresponding key value
        for (Object obj : key) {
            values = "" + buddiesHashMap.get(obj);
            str = obj + "," + java.util.Arrays.toString(values.getBytes(Charset.forName("UTF-8"))) + newline;

            try {
                //write each key/value pair to the file
                writer.write(str);
            } catch (IOException ex) {
                Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    // Close the writer regardless of what happens...
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        
    }

    //writes all the keys(phone numbers) and values(public keys)
    //listed in the text file into the empty hashmap, called in the constructor
    public void loadBuddies() {
        BufferedReader br = null;
        String splitBy = ",";
        String line = "";
        BigInteger key;
        //extract each key/value pair from the file
        try {

            br = new BufferedReader(new FileReader(buddiesList.getName()));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] country = line.split(splitBy);
                byte[] bytes = country[1].getBytes(Charset.forName("UTF-8"));
                        //~not sure if byte array has
                //the two's-complement binary representation of a BigInteger
                key = new BigInteger(bytes);
                //add these key/value pairs to the hashmap
                buddiesHashMap.put(country[0], key);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //saves the current hashmap keys(phone numbers) and values(public keys)
    //to the textfile for when the application is used next
    public void saveNewBuddies() {

        //get keys and values from hashmap(separate with commas or colons)
        //get set of keys from hashmap
        Set keys = buddiesHashMap.keySet();
        Object key[] = keys.toArray();
        String values = "";
        String str = "";
        String newline = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(buddiesList.getName()));

        } catch (IOException ex) {
            Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //iterate through the set of keys to get the corresponding key value
        for (Object obj : key) {
            values = "" + buddiesHashMap.get(obj);
            str = obj + "," + java.util.Arrays.toString(values.getBytes(Charset.forName("UTF-8"))) + newline;

            try {
                //write each key/value pair to the file
                writer.write(str);
            } catch (IOException ex) {
                Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    // Close the writer regardless of what happens...
                    writer.close();
                } catch (Exception e) {
                }
            }
        }

    }

    //searches hashmap for key(phone number), if phone number is found
    //then they are already buddies(return true) and can send a message right away
    //otherwise we will need to get their public key and we will send them 
    //our key also
    public boolean isBuddy(String number) {
        if (buddiesHashMap.containsKey(number)) {
            return true;
        } else {
            return false;
        }
    }

    //encrypts message to send
    public void encryptMessage(String number, String message) {

    }

    public static void main(String[] args) {
        Buddies bud = new Buddies();

    }
}
