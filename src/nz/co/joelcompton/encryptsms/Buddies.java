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
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import android.util.Base64;
        
/**
 *
 * @author pnv8813
 */
public class Buddies {

	private static final String infClass = "encryptsms";
    File buddiesList;
    HashMap<String, BigInteger> buddiesHashMap = new HashMap<>();
    String message = "";
    BigInteger publickey;

    public Buddies(Context c) {
        buddiesList = new File(c.getFilesDir().getPath().toString() + "buddiesList.txt");
        //File file = new File("newfile.txt");
        try {
        	if (!buddiesList.exists())
        		buddiesList.createNewFile();
        } catch (IOException ioe) {
            Log.i(infClass, "making file buddies");
        }
        loadBuddies();
    }

    //adds new public key to hashmap
    @SuppressLint("NewApi")
	public void storeNewKey(String phoneNumber, BigInteger newpublickey) {
        buddiesHashMap.put(phoneNumber, newpublickey);

        //get keys and values from hashmap(separate with commas or colons)
        //get set of keys from hashmap
        Set keys = buddiesHashMap.keySet();
        Object key[] = keys.toArray();
        String value = "";
        String str = "";
        String newline = "\n";
        BufferedWriter writer = null;
        
        try {
            writer = new BufferedWriter(new FileWriter(buddiesList.getName()));

        } catch (IOException ex) {
            Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
        }

        value = "" + buddiesHashMap.get(phoneNumber);
//        str = phoneNumber + "," + new BASE64Encoder().encodeBuffer(value.getBytes(Charset.forName("UTF-8")))+ newline;
        str = phoneNumber + "," + Base64.encodeToString(value.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);

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
                
//                byte[] bytes = new BASE64Decoder().decodeBuffer(country[1]);
                byte[] bytes = Base64.decode(country[1], Base64.DEFAULT);
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

    //searches hashmap for key(phone number), if phone number is found
    //then they are already buddies(return true) and can send a message right away
    //otherwise we will need to get their public key from the server to encrypt 
    //the message
    public boolean isBuddy(String number) {
        if (buddiesHashMap.containsKey(number)) {
            return true;
        } else {
            return false;
        }
    }

    //encrypts message to send
    public String encryptMessage(String number, String message) {
    	//testing only
    	//return "ENCRYPT: " + message;
     
        try {
            BigInteger key =  (BigInteger)buddiesHashMap.get(number);
            SecretKeySpec skey = new SecretKeySpec(key.toByteArray(), "");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, skey);
            byte[] encVal = c.doFinal(message.getBytes());
            //String encryptedValue = new BASE64Encoder().encode(encVal);
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            return encryptedValue;
        } catch (Exception ex) {
            Logger.getLogger(Buddies.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

//    public static void main(String[] args) {
//        Buddies bud = new Buddies();
//
//    }
    

    
    
}
