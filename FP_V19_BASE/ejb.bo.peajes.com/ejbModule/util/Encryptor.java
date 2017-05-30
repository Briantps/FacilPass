/**
 * 
 */
package util;

import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Frances Zucchet
 * 
 */
public class Encryptor {

	/**
	 * 
	 */
	public Encryptor() {

	}

	/**
	 * Encrypt password using MD5 algorithm
	 * 
	 * @param plainText
	 * @return Encrypted Password
	 */
	public static String getEncrypt(String plainText) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(plainText.getBytes());
			hash = digest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(new BASE64Encoder().encode(hash));
	}

	/**
	 * check an MD5 text
	 * 
	 * @param cryptedText
	 * @param plainText
	 * @return boolean
	 */
	public static boolean verifyPwd(String cryptedText, String plainText) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(plainText.getBytes());
			if (MessageDigest.isEqual(new BASE64Decoder()
					.decodeBuffer(cryptedText), digest.digest())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
