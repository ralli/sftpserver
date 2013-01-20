package de.ralli.sftpserver.core.util.impl;

import java.math.BigInteger;

public class SSHByteArraySerializer {
    /**
     * SSH2 data.
     */
    private final byte[] data;
    /**
     * Current position in {@link #data}.
     */
    private int pos;

    /**
     * Initialize the SSH2 data buffer.
     *
     * @param _data binaray data blob
     * @see #data
     */
    public SSHByteArraySerializer(final byte[] _data) {
        this.data = _data;
    }

    /**
     * Reads a big integer from {@link #data} starting with {@link #pos}. A big
     * integer is stored as byte array (see {@link #readByteArray()}).
     *
     * @return read big integer
     * @throws PublicKeyParseException if the byte array holds not enough bytes
     * @see #readByteArray()
     */
    public BigInteger readMPint() throws PublicKeyParseException {
        final byte[] raw = this.readByteArray();
        return (raw.length > 0) ? new BigInteger(raw) : BigInteger
                .valueOf(0);
    }

    /**
     * Reads a string from {@link #data} starting with {@link #pos}. A string is
     * stored as byte array (see {@link #readByteArray()}) in UTF8 format.
     *
     * @return read string
     * @throws PublicKeyParseException if the byte array holds not enough bytes
     * @see #readByteArray()
     */
    public String readString() throws PublicKeyParseException {
        return new String(this.readByteArray());
    }

    /**
     * Reads from the {@link #data} starting with {@link #pos} the next four
     * bytes and prepares an integer.
     *
     * @return 32 bit integer value
     */
    private int readUInt32() {
        return ((data[pos++] & 0xFF) << 24) | ((data[pos++] & 0xFF) << 16)
                | ((data[pos++] & 0xFF) << 8) | (data[pos++] & 0xFF);
    }

    /**
     * Reads from the {@link #data} starting with {@link #pos} a byte array. The
     * byte array is defined as: <ul> <li>first the length of the byte array is
     * defined as integer (see {@link #readUInt32()})</li> <li>then the byte
     * array itself is defined</li> </ul>
     *
     * @return read byte array from {@link #data}
     * @throws PublicKeyParseException if the byte array holds not enough bytes
     * @see #readUInt32()
     * @see PublicKeyParseException.ErrorCode#CORRUPT_BYTE_ARRAY_ON_READ
     */
    private byte[] readByteArray() throws PublicKeyParseException {
        final int len = this.readUInt32();
        if ((len < 0) || (len > (this.data.length - this.pos))) {
            throw new PublicKeyParseException(
                    PublicKeyErrorCode.CORRUPT_BYTE_ARRAY_ON_READ);
        }
        final byte[] str = new byte[len];
        System.arraycopy(this.data, this.pos, str, 0, len);
        this.pos += len;
        return str;
    }
}
