package com.stock.pycurrent.util;
//
//
//import com.google.common.io.BaseEncoding;
//import com.stock.pycurrent.entity.YfToken;
//import com.stock.pycurrent.entity.YfUsr;
//import com.stock.pycurrent.entity.model.Constants;
//import com.stock.pycurrent.service.YfTokenService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.security.*;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.X509EncodedKeySpec;
//import java.time.LocalDateTime;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Component
//@Slf4j
public class TokenUtil {
//    @Resource
//    private RedisUtil redisUtil;
//    @Resource
//    private YfTokenService yfTokenService;
//
//    public static final String AUTH_HEADER_KEY = "Authorization";
//
//    public static final String TOKEN_PREFIX = "Bearer ";
//
//    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
//        if (count == 0) {
//            return "";
//        } else if (count < 0) {
//            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
//        } else if (chars != null && chars.length == 0) {
//            throw new IllegalArgumentException("The chars array must not be empty");
//        } else {
//            if (start == 0 && end == 0) {
//                if (chars != null) {
//                    end = chars.length;
//                } else if (!letters && !numbers) {
//                    end = 1114111;
//                } else {
//                    end = 123;
//                    start = 32;
//                }
//            } else if (end <= start) {
//                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
//            }
//
//            boolean zero_digit_ascii = true;
//            boolean first_letter_ascii = true;
//            if (chars == null && (numbers && end <= 48 || letters && end <= 65)) {
//                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + 48 + ") for generating digits or greater then (" + 65 + ") for generating letters.");
//            } else {
//                StringBuilder builder = new StringBuilder(count);
//                int gap = end - start;
//
//                while (true) {
//                    while (count-- != 0) {
//                        int codePoint;
//                        if (chars == null) {
//                            codePoint = random.nextInt(gap) + start;
//                            switch (Character.getType(codePoint)) {
//                                case 0:
//                                case 18:
//                                case 19:
//                                    ++count;
//                                    continue;
//                            }
//                        } else {
//                            codePoint = chars[random.nextInt(gap) + start];
//                        }
//
//                        int numberOfChars = Character.charCount(codePoint);
//                        if (count == 0 && numberOfChars > 1) {
//                            ++count;
//                        } else if ((!letters || !Character.isLetter(codePoint)) && (!numbers || !Character.isDigit(codePoint)) && (letters || numbers)) {
//                            ++count;
//                        } else {
//                            builder.appendCodePoint(codePoint);
//                            if (numberOfChars == 2) {
//                                --count;
//                            }
//                        }
//                    }
//
//                    return builder.toString();
//                }
//            }
//        }
//    }
//
//    public Pair<String, String> createToken(YfUsr yfUsr) {
//        KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//        String salt = random(random.nextInt(Constants.MIN_SALT_LENGTH, Constants.MAX_SALT_LENGTH), 0, 0, true, true, null, random);
//        String publicKeyString = BaseEncoding.base64().encode(publicKey.getEncoded());
//        redisUtil.addCache(Constants.TOKEN_KEY_NAME, publicKeyString, BaseEncoding.base64().encode(privateKey.getEncoded()));
//        YfToken yfToken = new YfToken();
//        yfToken.setTokenSalt(salt);
//        yfToken.setUpdateTime(DateUtils.nowDate());
//        yfToken.setExpireDate(DateUtils.toDate(LocalDateTime.now().plusDays(Constants.CACHE_EXPIRE_DAY)));
//        String token = Jwts.builder()
//                .content("typ", "JWT")
//                // 可以将基本不重要的对象信息放到claims
//                .claim("mobile", yfUsr.getPhone())
//                .claim("name", yfUsr.getName())
//                .claim("userId", yfUsr.getSn())
//                .claim("salt", salt)
//                .subject(yfUsr.getPhone())
//                .issuer(Constants.JWT_ISSUER)        // 签发主体
//                .issuedAt(DateUtils.nowDate())
//                .expiration(yfToken.getExpireDate()) // 失效时间
//                .audience().add("anAudience").and()   // 接收对象
//                .signWith(privateKey)
//                .compact();
//        yfToken.setToken(token);
//        yfToken.setUserSn(yfUsr.getSn());
//        yfToken.setPublicKey(publicKeyString);
//        yfTokenService.addToken(yfToken);
//        return new Pair<>(token, publicKeyString);
//    }
//
//    public YfUsr checkToken(String token) {
//
//        String publicKey = yfTokenService.findPublicKeyByToken(token);
//        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(BaseEncoding.base64().decode(publicKey));
//        KeyFactory keyFactory = null;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        RSAPublicKey pubKey = null;
//        try {
//            pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
//        Claims claims = Jwts.parser()
//                .requireIssuer(Constants.JWT_ISSUER)
//                .verifyWith(pubKey)
//                .build()
//                .parseSignedClaims(token).getPayload();
//        YfUsr yfUsr = new YfUsr();
//        yfUsr.setSn(claims.get("userId", Integer.class));
//        yfUsr.setPhone(claims.get("mobile", String.class));
//        yfUsr.setName(claims.get("name", String.class));
//        yfUsr.setSalt(claims.get("salt", String.class));
//        return yfUsr;
//
//    }
//
}
