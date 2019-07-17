package hackathon.summer2019;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.nio.charset.StandardCharsets;

import java.util.logging.Logger;

public class Cryptor{

  private final int KEY_SIZE = 32;
  private final int IV_SIZE = 16;

  private static final Logger LOGGER = Logger.getLogger(Cryptor.class.getName());

  private static String constantSalt = "This is a constant salt string for demo purposes";
  private static final int iterations = 10000;

  public Cryptor() {
    // default constructor
  }

  public byte[] getKey() throws java.io.UnsupportedEncodingException {
    return RandomStringUtils.randomAlphanumeric(KEY_SIZE).getBytes("UTF-8");
  }

  public byte[] getIV() throws java.io.UnsupportedEncodingException{
    return RandomStringUtils.randomAlphanumeric(IV_SIZE).getBytes("UTF-8");
  }

  public byte[] encrypt(byte[] plainText, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	    Security.addProvider(new BouncyCastleProvider());
	    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(constantSalt.getBytes(), iterations);
	    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
	    SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
	    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
	    Cipher encryptionCipher = Cipher.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
	    encryptionCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
	    return encryptionCipher.doFinal(plainText);
	}

  public byte[] decrypt(byte[] cipher, String password) throws Exception{
	    PKCS12ParametersGenerator pGen = new PKCS12ParametersGenerator(new SHA256Digest());
	    char[] passwordChars = password.toCharArray();
	    final byte[] pkcs12PasswordBytes = PBEParametersGenerator.PKCS12PasswordToBytes(passwordChars);
	    pGen.init(pkcs12PasswordBytes, constantSalt.getBytes(), iterations);
	    CBCBlockCipher aesCBC = new CBCBlockCipher(new AESEngine());
	    ParametersWithIV aesCBCParams = (ParametersWithIV) pGen.generateDerivedParameters(256, 128);
	    aesCBC.init(false, aesCBCParams);
	    PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(aesCBC, new PKCS7Padding());
	    byte[] plainTemp = new byte[aesCipher.getOutputSize(cipher.length)];
	    int offset = aesCipher.processBytes(cipher, 0, cipher.length, plainTemp, 0);
	    int last = aesCipher.doFinal(plainTemp, offset);
	    final byte[] plain = new byte[offset + last];
	    System.arraycopy(plainTemp, 0, plain, 0, plain.length);
	    return plain;
	}
}
