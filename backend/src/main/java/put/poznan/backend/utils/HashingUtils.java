package put.poznan.backend.utils;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.binary.Hex;
import put.poznan.backend.exception.BlockInvalid;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {

    private final static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance( "SHA-256" );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
            throw new BlockInvalid( "Hashing algorithm not found" );
        }
    }

    public static String hash( String str ) {
        byte[] hash = digest.digest( str.getBytes( StandardCharsets.UTF_8 ) );
        return Hex.encodeHexString( hash );
    }

    public static boolean isEqual( String hashed, String plain ) {
        return hash( plain ).equals( hashed );
    }
}
