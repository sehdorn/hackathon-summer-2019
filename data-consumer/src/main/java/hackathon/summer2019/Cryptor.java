package hackathon.summer2019;

import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class Cryptor{

  private final int KEY_SIZE = 32;
  private final int IV_MIN_LENGTH = 10;
  private final int IV_MAX_LENGTH = 20;

  private byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws Exception {
        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int length2 = cipher.doFinal(outBuf, length1);
        int actualLength = length1 + length2;
        byte[] result = new byte[actualLength];
        return result;
    }

    private byte[] decryptAES(byte[] cipher, byte[] key, byte[] iv) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
        aes.init(false, ivAndKey);
        return cipherData(aes, cipher);
    }

    private byte[] encryptAES(byte[] plain, byte[] key, byte[] iv) throws Exception {
        PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
        aes.init(true, ivAndKey);
        return cipherData(aes, plain);
    }

    public String encodeAES(String plainText, byte[] key, byte[] iv) throws Exception {
        return new String(Base64.encodeBase64(encryptAES(plainText.getBytes("UTF-16LE"), key, iv)));
    }

    public String decodeAES(String encodedText, byte[] key, byte[] iv) throws Exception {
        return new String(decryptAES(Base64.decodeBase64(encodedText.getBytes()), key, iv), "UTF-16LE");
    }

    public byte[] getKey(){
      return RandomStringUtils.randomAlphanumeric(KEY_SIZE).getBytes();
    }

    public byte[] getIV(){
      return RandomStringUtils.randomAlphanumeric(IV_MIN_LENGTH, IV_MAX_LENGTH).getBytes();
    }

}
