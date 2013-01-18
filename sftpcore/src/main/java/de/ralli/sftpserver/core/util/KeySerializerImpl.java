package de.ralli.sftpserver.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

public class KeySerializerImpl implements KeySerializer {

    private byte[] SSH_RSA = {0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a'};
    private byte[] SSH_DSA = {0, 0, 0, 7, 's', 's', 'h', '-', 'd', 's', 'a'};

    @Override
    public String getFingerprintString(Key key) {
        byte[] bytes = getFingerprint(key);
        return getFingerprintString(bytes);
    }

    @Override
    public String getFingerprintString(byte[] bytes) {
        boolean first = true;
        StringBuilder out = new StringBuilder();
        for (byte b : bytes) {
            if (!first) {
                out.append(':');
            }
            first = false;
            out.append(String.format("%02x", b & 0xFF));
        }
        return out.toString();
    }

    @Override
    public byte[] getFingerprint(byte[] keydata) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(keydata);
            return thedigest;
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @Override
    public byte[] getFingerprint(Key key) {
        String algorithm = key.getAlgorithm();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (!"RSA".equals(algorithm) && !"DSA".equals(algorithm)) {
            throw new RuntimeException("Unknown Algorithm " + algorithm);
        }

        try {
            byte[] bytes = encodePublicKey((PublicKey) key);
            out.write(bytes);

            return getFingerprint(out.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toOpenSSHString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toSecSSHString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeAsOpenSSH(String fileName, Key key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeAsSecSSH(String fileName, Key key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private byte[] encodePublicKey(PublicKey key) throws Exception {
        byte[] result = null;

        if (key instanceof RSAPublicKey) {
            result = encodeRSAPublicKey((RSAPublicKey) key);
        } else if (key instanceof DSAPublicKey) {
            result = encodeDSAPublicKey((DSAPublicKey) key);
        } else {
            throw new PublicKeyParseException(PublicKeyErrorCode.UNKNOWN_PUBLIC_KEY_CLASS);
        }
        return result;
    }

    private byte[] encodeRSAPublicKey(RSAPublicKey key) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(SSH_RSA);
        /* Encode the public exponent */
        BigInteger e = key.getPublicExponent();
        byte[] data = e.toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);
        /* Encode the modulus */
        BigInteger m = key.getModulus();
        data = m.toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);
        return out.toByteArray();
    }

    private byte[] encodeDSAPublicKey(DSAPublicKey key) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(SSH_DSA);
        DSAParams params = key.getParams();
        byte[] data = params.getP().toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        data = params.getQ().toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        data = params.getG().toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        data = key.getY().toByteArray();
        encodeUInt32(data.length, out);
        out.write(data);

        return out.toByteArray();
    }

    private void encodeUInt32(int value, OutputStream out)
            throws IOException {
        byte[] tmp = new byte[4];
        tmp[0] = (byte) ((value >>> 24) & 0xff);
        tmp[1] = (byte) ((value >>> 16) & 0xff);
        tmp[2] = (byte) ((value >>> 8) & 0xff);
        tmp[3] = (byte) (value & 0xff);
        out.write(tmp);
    }
}
