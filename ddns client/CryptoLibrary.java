
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Class used for encryption decryption of username and password.
 *  This uses PBE - Password Based Encryption.
 *
 * @author anonymous_02
 */
public class CryptoLibrary {

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    /**
     * constructor initializes master password.
     *
     * @throws java.lang.SecurityException
     */
    public CryptoLibrary() throws SecurityException {

        java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
        char[] pass = "CHANGE THIS TO A BUNCH OF RANDOM CHARACTERS".toCharArray();
        byte[] salt = {
            (byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c,
            (byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19};
        int iterations = 3;
        init(pass, salt, iterations);
    }

    /**
     * Initializes security related parameters.
     *
     * @param pass master password.
     * @param salt
     * @param iterations
     * @throws java.lang.SecurityException if failed to initialize.
     */
    public void init(char[] pass, byte[] salt, int iterations)
            throws SecurityException {

        try {
            PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

            encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);

            decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, k, ps);
        } catch (Exception e) {
            throw new SecurityException("Could not initialize CryptoLibrary: " + e.getMessage());
        }
    }

    /**
     * convenient method for encrypting a string.
     *
     * @param str String to be encryptes.
     * @return encrypted String.
     * @throws java.lang.SecurityException
     */

    public synchronized String encrypt(String str)
            throws SecurityException {

        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = encryptCipher.doFinal(utf8);
            return encoder.encode(enc);
        } catch (Exception e) {
            throw new SecurityException("Could not encrypt: " + e.getMessage());
        }
    }

    /**
     * convenience method for encrypting a string.
     *
     * @param str an encrypted String.
     * @return String the decrypted string.
     * @throws SecurityException Description of the Exception.
     */
    public synchronized String decrypt(String str)
            throws SecurityException {

        try {
            byte[] dec = decoder.decodeBuffer(str);
            byte[] utf8 = decryptCipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            throw new SecurityException("Could not decrypt: " + e.getMessage());
        }
    }
} 

