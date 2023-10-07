package org.deil.utils.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class SM2Signature {
    private static final BouncyCastleProvider bc = new BouncyCastleProvider();

    public static KeyPair generateKeyPair() throws Exception {
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", (Provider)new BouncyCastleProvider());
        kpg.initialize(sm2Spec);
        kpg.initialize(sm2Spec, new SecureRandom());
        return kpg.generateKeyPair();
    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        ParametersWithRandom parametersWithRandom = new ParametersWithRandom((CipherParameters)ECUtil.generatePublicKeyParameter(publicKey));
        SM2Engine engine = new SM2Engine();
        engine.init(true, (CipherParameters)parametersWithRandom);
        return engine.processBlock(data, 0, data.length);
    }

    public static byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        SM2Engine engine = new SM2Engine();
        engine.init(false, (CipherParameters)asymmetricKeyParameter);
        return engine.processBlock(data, 0, data.length);
    }

    public static String sign(String content, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), (Provider)new BouncyCastleProvider());
        signature.initSign(privateKey);
        byte[] plainText = content.getBytes(StandardCharsets.UTF_8);
        signature.update(plainText);
        byte[] signatureValue = signature.sign();
        return Hex.toHexString(signatureValue);
    }

    public static PrivateKey toPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFact = KeyFactory.getInstance("EC", (Provider)bc);
        byte[] bytes = Base64.decodeBase64(privateKey);
        return keyFact.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static PublicKey toPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFact = KeyFactory.getInstance("EC", (Provider)bc);
        byte[] bytes = Base64.decodeBase64(publicKey);
        return keyFact.generatePublic(new X509EncodedKeySpec(bytes));
    }

    public static boolean doCheck(String content, String sign, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), (Provider)bc);
        signature.initVerify(publicKey);
        signature.update(content.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Hex.decode(sign));
    }
}