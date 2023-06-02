package put.poznan.backend.utils;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.DecoderException;
import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class BinaryUtils {
    public static int countLeadingZeros( String hash ) {
        byte[] bytes;
        try {
            bytes = Hex.decodeHex( hash );
            String bin = new BigInteger( bytes ).toString( 2 );
            return 256 - bin.length();
        } catch ( DecoderException e ) {
            throw new RuntimeException( e );
        }
    }
}
