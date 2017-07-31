package com.k_int.a2g.base;

import com.k_int.a2g.base.Constants;
import java.util.Stack;

public class BERDecoder extends BaseDecoder {

  private Stack encoding_info = new Stack();

  /**
   *
   * return encoded length, -1 is indefinite length encoding
   */
  public long decodeLength(java.io.InputStream is) {
    long datalen;
    boolean next_is_indefinite;

    byte lenpart = (byte)is.read();

    if ((lenpart & 0x80) == 0)  { // If bit 8 is 0
      // Single octet length encoding
      // System.err.println("Single octet length encoding");
      datalen = lenpart;
    }
    else if ( ( lenpart & 0x7F ) == 0 ) { // Otherwise we are multiple octets (Maybe 0, which = indefinite)
      datalen=-1;
    }
    else { 
      next_is_indefinite=false;
      lenpart &= 0x7F;

      datalen = 0;
      while (lenpart-- > 0)
        datalen = (datalen << 8) | ((byte)is.read() & 0xFF);
    }

    return datalen;
  }

  public int decodeTag(boolean is_constructed,
                       int tag_class,
                       int tag_value,
                       boolean is_optional,
                       java.io.InputStream is) throws java.io.IOException {
    return 0;
  }

  private long encodeBase128Int(int value, java.io.OutputStream os) throws java.io.IOException {
    int len = 0;
    byte[] enc = new byte[10];
    int pos = 0;

    while ( ( value > 127 ) && ( pos < 9 ) ) { 
      enc[pos++] = (byte) ( value & 127 );
      value = value >> 7;
    }
    enc[pos] = (byte)value;

    for ( ;pos>=0;pos-- ) { 
      os.write( (int) ( enc[pos] | ( pos == 0 ? 0 : 128 ) ) );
      len++
    }

    return len;
  }

  public Integer decodeInteger(java.io.InputStream is) {
  }
  
  public Object decodeNull(java.io.InputStream is) {
    int tag = decodeTag(false, Constants.UNIVERSAL, Constants.NULL, false, is)
    long length = decodeLength(is);
    return null;
  }

  public TagAndLength readNextTagAndLength(InputStream is) throws java.io.IOException {

    TagAndLength result = new TagAndLength();

    byte c = (byte)is.read();
    if ( c == -1 ) {
      return null;
    }

    c &= 0xFF;
    result.tag_class = c & 0xC0;
    result.is_constructed = (c & 0x20) != 0;

    result.tag_value = c & 0x1F;

    // If there are multiple octets to encode the tag
    if (result.tag_value == 0x1F) {
      result.tag_value = 0;

      // Groovy has no do-while :((
      boolean cont=true;
      while ( cont ) {
        c = (byte)is.read();

        // Shift value 7 bits left
        result.tag_value = result.tag_value << 7;

        // Merge with the octets we just got
        result.tag_value = ( result.tag_value | ( c & 0x7F ) );
        cont = ((c & 0x80) != 0)
      }
    }

    result.length = decodeLength(is);

    return result
  }

}
