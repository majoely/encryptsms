package nz.co.joelcompton.encryptsms;

import java.math.BigInteger;

public class KeyHandler {
	private static String filePath;
	private BigInteger privateKey;
	private BigInteger publicKey;

	public KeyHandler() {
		// todo
	}

	public String decryptMessage(String message) {
		return "DECRYPT: " + message;
	}

	public BigInteger getPublicKey() {
		return this.publicKey;
	}
}