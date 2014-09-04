package nz.co.joelcompton.encryptsms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import android.util.Base64;
import android.util.Log;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 *
 * @author Dajne Win
 */
public class API {
	private static final int timeOutInMillis = 10000;
	private static final String infClass = "encryptsms";


	/**
	 * Requests a one time token from the public key authority server.
	 * 
	 * @return String Token to be sent to the server for registering
	 * @throws IOException
	 */
	public static String requestToken() throws IOException {
		System.setProperty("jsse.enableSNIExtension", "false");
		URL url = new URL("https://python-dwin.rhcloud.com/requesttoken");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		//conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(timeOutInMillis);
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output = rd.readLine();
		rd.close();
		return output;
	}

	/**
	 * Registers your client to the public key server.
	 * 
	 * @param token
	 *            The token requested earlier.
	 * @param publicKey
	 *            Your public key.
	 * @param number
	 *            Your mobile phone number.
	 * @param nonce
	 *            A random number value that will be returned to verify the
	 *            server received your request.
	 * @return Your nonce value that you sent in your request if registering is
	 *         successful
	 * @throws IOException
	 */
	public static String register(String token, String publicKey,
			String number, int nonce) throws IOException {
		keyValue = token.substring(0, 16).getBytes();
		String raw_data = "{\"token\":\"" + token + "\",\"publickey\":\""
				+ publicKey + "\",\"number\":\"" + number + "\",\"nonce\":\""
				+ nonce + "\"}";
		String data = encrypt(raw_data);
		System.setProperty("jsse.enableSNIExtension", "false");
		URL url = new URL("https://python-dwin.rhcloud.com/register");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(timeOutInMillis);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String output = rd.readLine();
		rd.close();
		return output;
	}

	/**
	 * Gets the public key of another registered client.
	 * 
	 * @param number
	 *            The mobile number of the person you want the public key for.
	 * @return The public key of the client, otherwise -1 if it doesn't exist on
	 *         the server.
	 * @throws IOException
	 */
	public static String getPublicKey(String number) throws IOException {
		String data = "{\"number\":\"" + number + "\"}";
		System.setProperty("jsse.enableSNIExtension", "false");
		URL url = new URL("https://python-dwin.rhcloud.com/getkey");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(timeOutInMillis);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		int status = conn.getResponseCode();
		Log.i(infClass, "responsecode " + status);
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String output = rd.readLine();
		rd.close();
		return output;
	}

	private static final String ALGO = "AES";
	private static byte[] keyValue;

	private static String encrypt(String Data) {
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(Data.getBytes());
//			String encryptedValue = new BASE64Encoder().encode(encVal);
			String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
			return encryptedValue;
		} catch (Exception ex) {
			Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private static String decrypt(String encryptedData) {
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
//			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
			byte[] decordedValue = Base64.decode(encryptedData, Base64.DEFAULT);
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
}
