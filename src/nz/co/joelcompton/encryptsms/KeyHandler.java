package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Dajne Win
 */
public class KeyHandler {

    // well-known elliptic curve secp256r1 (256 bit prime, 32 byte)
    private static final BigInteger P = new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", 16);
    private static final BigInteger A = new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", 16);
    private static final BigInteger B = new BigInteger("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", 16);
    private static final BigInteger GX = new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", 16);
    private static final BigInteger GY = new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", 16);
    private static final BigInteger N = new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16);
    private static final int H = 1; // cofactor

    private static ECPrivateKey privateKey;
    private static ECPublicKey publicKey;
    private static final File filePublicKey = new File("public.key");
    private static final File filePrivateKey = new File("private.key");

    private static EllipticCurve curve;
    private static ECPoint G;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KeyHandler kh = new KeyHandler();
        //You can delete this method!
    }

    public KeyHandler() {
        if (filePublicKey.exists() && filePrivateKey.exists()) {
            LoadKeyPair();
        } else {
            if (filePublicKey.exists()) {
                filePublicKey.delete();
            }
            if (filePrivateKey.exists()) {
                filePrivateKey.delete();
            }
            generateKeys();
            SaveKeyPair();
        }
    }
    
    public static String decryptMessage(String message, ECPublicKey buddiesPublicKey) throws InvalidKeyException, NoSuchAlgorithmException
    {
        System.arraycopy(calculateSecretKey(buddiesPublicKey), 0, keyValue, 0, 16);
        return decrypt(message);
    }

    private static byte[] calculateSecretKey(ECPublicKey otherPublicKey) throws InvalidKeyException, NoSuchAlgorithmException {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(privateKey);
        ka.doPhase(otherPublicKey, true);
        return ka.generateSecret();
    }

    private static final String ALGO = "AES";
    private static byte[] keyValue = new byte[16];

    /*private static String encrypt(String Data) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            String encryptedValue = new BASE64Encoder().encode(encVal);
            return encryptedValue;
        } catch (Exception ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }*/

    private static String decrypt(String encryptedData) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public static ECPublicKey getPublicKey() {
        return publicKey;
    }

    public static String getPublicKeyEncoded() {
        return getHexString(publicKey.getEncoded());
    }

    private static void generateKeys() {  
        // create the elliptic curve
        ECField field = new ECFieldFp(P);
        curve = new EllipticCurve(field, A, B);
        // use existing precalculated values of G and N
        G = new ECPoint(GX, GY);
        ECParameterSpec ecSpec = new ECParameterSpec(curve, G, N, H);
        //ECGenParameterSpec ecSpec = new ECGenParameterSpec("sect571r1");
        // create public and private keys
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(ecSpec); // size of key in bits
            KeyPair keyPair = kpg.generateKeyPair();
            privateKey = (ECPrivateKey) keyPair.getPrivate();
            publicKey = (ECPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Encryption algorithm not available: " + e);
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("Invalid Parameter in Encryption algorithm: " + e);
        }
    }

    private static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private static void SaveKeyPair() {
        FileOutputStream fos = null;
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                    publicKey.getEncoded());
            fos = new FileOutputStream(filePublicKey);
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            fos = new FileOutputStream(filePrivateKey);
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void LoadKeyPair() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePublicKey);
            byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();

            fis = new FileInputStream(filePrivateKey);
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    encodedPublicKey);
            publicKey = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivateKey);
            privateKey = (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
