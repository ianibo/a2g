package com.k_int.a2g.base;

import com.k_int.a2g.base.Constants;
import java.util.Stack;

public class BERDecoder extends BaseDecoder {

  private InputStream is;

  public BERDecoder(InputStream is) {
    this.is = is;
  }

  private Stack encoding_info = new Stack();

  /**
   *
   * return encoded length, -1 is indefinite length encoding
   */
  public long decodeLength() {
    long datalen;
    boolean next_is_indefinite;

    byte lenpart = (byte)this.read();

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
        datalen = (datalen << 8) | ((byte)this.read() & 0xFF);
    }

    return datalen;
  }

  public int decodeTag(boolean is_constructed,
                       int tag_class,
                       int tag_value,
                       boolean is_optional) throws java.io.IOException {
    return 0;
  }

  public Integer decodeInteger() {
  }
  
  public Object decodeNull() {
    int tag = decodeTag(false, Constants.UNIVERSAL, Constants.NULL, false)
    long length = decodeLength();
    return null;
  }

  public TagAndLength readNextTagAndLength() throws java.io.IOException {

    TagAndLength result = new TagAndLength();

    byte c = (byte)this.read();
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
        c = (byte)this.read();

        // Shift value 7 bits left
        result.tag_value = result.tag_value << 7;

        // Merge with the octets we just got
        result.tag_value = ( result.tag_value | ( c & 0x7F ) );
        cont = ((c & 0x80) != 0)
      }
    }

    result.length = decodeLength();

    return result
  }

  public int read() {
    return is.read();
  }
}
