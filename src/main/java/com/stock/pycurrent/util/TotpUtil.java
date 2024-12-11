package com.stock.pycurrent.util;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.stock.pycurrent.entity.model.Constants;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.Base32;
//import org.apache.commons.codec.binary.Hex;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.UndeclaredThrowableException;
//import java.math.BigInteger;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.security.GeneralSecurityException;
//import java.security.SecureRandom;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author fzc
// * @version 1.0
// * @description
// * @date 2021/4/4 13:00
// */
//@Slf4j
public class TotpUtil {
//    /**
//     * 时间前后偏移量
//     */
//    private static final int timeExcursion = 1;
//
//    /**
//     * 随机生成一个密钥
//     */
//    public static String createSecretKey() {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[20];
//        random.nextBytes(bytes);
//        Base32 base32 = new Base32();
//        String secretKey = base32.encodeToString(bytes);
//        return secretKey.toLowerCase();
//    }
//
//    public static Long getTime() {
//        return System.currentTimeMillis() / 1000 / 30;
//    }
//
//    /**
//     * 根据密钥获取验证码
//     * 返回字符串是因为验证码有可能以 0 开头
//     *
//     * @param secretKey 密钥
//     * @param time      第几个 30 秒 System.currentTimeMillis() / 1000 / 30
//     */
//    public static String getTOTP(String secretKey, long time) {
//        Base32 base32 = new Base32();
//        byte[] bytes = base32.decode(secretKey.toUpperCase());
//        String hexKey = Hex.encodeHexString(bytes);
//        String hexTime = Long.toHexString(time);
//        return generateTOTP(hexKey, hexTime, "6");
//    }
//
//    /**
//     * This method generates a TOTP value for the given
//     * set of parameters.
//     *
//     * @param key:          the shared secret, HEX encoded
//     * @param time:         a value that reflects a time
//     * @param returnDigits: number of digits to return
//     * @return a numeric String in base 10 that includes truncationDigits digits
//     */
//    public static String generateTOTP(String key,
//                                      String time,
//                                      String returnDigits) {
//        return generateTOTP(key, time, returnDigits, "HmacSHA1");
//    }
//
//
//    /**
//     * 生成 Google Authenticator 二维码所需信息
//     * Google Authenticator 约定的二维码信息格式 : otpauth://totp/{issuer}:{account}?secret={secret}&issuer={issuer}
//     * 参数需要 url 编码 + 号需要替换成 %20
//     * 动态密码是基于时间、口令密钥按照特定的算法所生成的随机数字组合。
//     * 现在我们讲一下应用比较广泛的动态密码生成器google-authenticator。
//     * authenticator给我们提供了接入协议，格式如下：
//     * 格式：otpauth://totp/52min.net?secret=IH2AGN774DGNNA2N&period=30&digits=8&issuer=Alias
//     * 52min.net ：密码来源，用于描述密码。
//     * secret ：口令种子密钥，每个用户拥有唯一的secret。用户的唯一表示为secret的生成参数。
//     * period ：【可选参数】密码的有效时间，默认30S
//     * Digits ：【可选参数】密码的长度，默认6字符
//     * issuer ：【可选参数】密码别名，用于描述密码。
//     * 用户可以根据otpauth://totp/52min.net?secret=IH2AGN774DGNNA2N&period=30&digits=8&issuer=Alias
//     * 生成二维码，用于密码生成器扫码获取当前密码。
//     */
//    public static String createGoogleAuthQRCodeData(String secret) {
//        String qrCodeData = "otpauth://totp/%s?secret=%s&period=30&digits=6&issuer=Admin";
//        return String.format(qrCodeData, Constants.JWT_ISSUER, secret);
//        //            return String.format(qrCodeData, URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20"), URLEncoder.encode(secret, "UTF-8")
////                    .replace("+", "%20"), URLEncoder.encode(issuer, "UTF-8").replace("+", "%20"));
//    }
//
//    private static byte[] hmac_sha(String crypto, byte[] keyBytes,
//                                   byte[] text) {
//        try {
//            Mac hmac;
//            hmac = Mac.getInstance(crypto);
//            SecretKeySpec macKey =
//                    new SecretKeySpec(keyBytes, "RAW");
//            hmac.init(macKey);
//            return hmac.doFinal(text);
//        } catch (GeneralSecurityException gse) {
//            throw new UndeclaredThrowableException(gse);
//        }
//    }
//
//    /**
//     * This method converts a HEX string to Byte[]
//     *
//     * @param hex: the HEX string
//     * @return a byte array
//     */
//    private static byte[] hexStr2Bytes(String hex) {
//        // Adding one byte to get the right conversion
//        // Values starting with "0" can be converted
//        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
//
//        // Copy all the REAL bytes, not the "first"
//        byte[] ret = new byte[bArray.length - 1];
//        if (ret.length >= 0) System.arraycopy(bArray, 1, ret, 0, ret.length);
//        return ret;
//    }
//
//    private static final int[] DIGITS_POWER
//            // 0 1  2   3    4     5      6       7        8
//            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
//
//
//    /**
//     * This method generates a TOTP value for the given
//     * set of parameters.
//     *
//     * @param key:          the shared secret, HEX encoded
//     * @param time:         a value that reflects a time
//     * @param returnDigits: number of digits to return
//     * @return a numeric String in base 10 that includes truncationDigits digits
//     */
//    public static String generateTOTP256(String key,
//                                         String time,
//                                         String returnDigits) {
//        return generateTOTP(key, time, returnDigits, "HmacSHA256");
//    }
//
//    /**
//     * This method generates a TOTP value for the given
//     * set of parameters.
//     *
//     * @param key:          the shared secret, HEX encoded
//     * @param time:         a value that reflects a time
//     * @param returnDigits: number of digits to return
//     * @return a numeric String in base 10 that includes truncationDigits digits
//     */
//    public static String generateTOTP512(String key,
//                                         String time,
//                                         String returnDigits) {
//        return generateTOTP(key, time, returnDigits, "HmacSHA512");
//    }
//
//    /**
//     * This method generates a TOTP value for the given
//     * set of parameters.
//     *
//     * @param key:          the shared secret, HEX encoded
//     * @param time:         a value that reflects a time
//     * @param returnDigits: number of digits to return
//     * @param crypto:       the crypto function to use
//     * @return a numeric String in base 10 that includes truncationDigits digits
//     */
//    public static String generateTOTP(String key,
//                                      String time,
//                                      String returnDigits,
//                                      String crypto) {
//        int codeDigits = Integer.decode(returnDigits);
//        StringBuilder result;
//
//        // Using the counter
//        // First 8 bytes are for the movingFactor
//        // Compliant with base RFC 4226 (HOTP)
//        StringBuilder timeBuilder = new StringBuilder(time);
//        while (timeBuilder.length() < 16)
//            timeBuilder.insert(0, "0");
//        time = timeBuilder.toString();
//
//        // Get the HEX in a Byte[]
//        byte[] msg = hexStr2Bytes(time);
//        byte[] k = hexStr2Bytes(key);
//
//        byte[] hash = hmac_sha(crypto, k, msg);
//
//        // put selected bytes into result int
//        int offset = hash[hash.length - 1] & 0xf;
//
//        int binary =
//                ((hash[offset] & 0x7f) << 24) |
//                        ((hash[offset + 1] & 0xff) << 16) |
//                        ((hash[offset + 2] & 0xff) << 8) |
//                        (hash[offset + 3] & 0xff);
//
//        int otp = binary % DIGITS_POWER[codeDigits];
//
//        result = new StringBuilder(Integer.toString(otp));
//        while (result.length() < codeDigits) {
//            result.insert(0, "0");
//        }
//        return result.toString();
//    }
//
//    /**
//     * 校验方法
//     *
//     * @param secretKey 密钥
//     * @param code      用户输入的 TOTP 验证码
//     */
//    public static boolean verify(String secretKey, String code) {
//        long time = System.currentTimeMillis() / 1000 / 30;
//        for (int i = -timeExcursion; i <= timeExcursion; i++) {
//            String totp = getTOTP(secretKey, time + i);
//            if (code.equals(totp)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 生成二维码图片
//     *
//     * @param content  内容
//     * @param height   高度
//     * @param width    宽度
//     * @param filePath 路径
//     * @param fileName 名称
//     */
//    public static void generateMatrixPic(String content, int height, int width, String filePath, String fileName) throws WriterException, IOException {
//        Map<EncodeHintType, Object> hints = new HashMap<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        hints.put(EncodeHintType.MARGIN, 1);
//        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
//                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
//        File uploadDir = new File(filePath);
//        if (!uploadDir.exists()) {
//            log.info("图片路径不存在，创建路径" + uploadDir);
//            boolean writeable = uploadDir.setWritable(true);
//            boolean createDirResult = uploadDir.mkdirs();
//            log.info("文件的读权限：{}，文件的写权限：{}，创建结果：{}",
//                    uploadDir.canRead(), writeable, createDirResult);
//        }
//        Path path = FileSystems.getDefault().getPath(filePath, fileName);
//        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
//    }
}
