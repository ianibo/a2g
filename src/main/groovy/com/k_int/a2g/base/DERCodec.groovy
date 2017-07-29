package com.k_int.a2g.base;

public class DERCodec extends BaseCodec {

  /**
   * For DER.
   * When the length is between 0 and 127, the short form of length must be used
   * When the length is 128 or greater, the long form of length must be used, and the length must be encoded in the minimum number of octets.
   * For simple string types and implicitly tagged types derived from simple string types, the primitive, definite-length method must be employed.
   * For structured types, implicitly tagged types derived from structured types, and explicitly tagged types derived from anything, the constructed, definite-length method must be employed.
   *
   * @return number of bytes written
   */
  public long encodeLength(long length, java.io.OutputStream os) {

    if(length >= 128) { 
      byte num_length_octets;

      if(length >= 0x1000000)
         num_length_octets = 4;
      else if(length >= 0x10000)
         num_length_octets = 3;
      else if(length >= 256)
         num_length_octets = 2;
      else
         num_length_octets = 1;

      os.write((int)(0x80 | num_length_octets));

      for(int j = (num_length_octets - 1) * 8; j >= 0; j -= 8)
        os.write((int)length >> j);

      return num_length_octets;
    }
    else { 
      os.write((int)length);
      return 1;
    }
  }

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

  public long encodeTag(boolean is_constructed,
                        int tag_class,
                        int tag_value,
                        boolean is_optional,
                        java.io.OutputStream os) throws java.io.IOException {
    int l = 0;
    int k = tag_class;
    if(is_constructed)
        k |= 0x20;

    l=1
    if(tag_value < 31) { // We can encode in a single octet
      os.write((int) ( k | tag_value ) );
    }
    else { // Multiple tag octets
        // In multiple length tags, first octet gives class & cons, bits 1-5 are all 1
        os.write((int)(k | 0x1f));

        // Followed by the integer encoding of the tag, base 128
        l += encodeBase128Int(tag_value);
    }

    return l;
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


  
  public long encodeInteger(java.io.OutputStream os, Integer integer) {
  }

  public Integer decodeInteger(java.io.InputStream is) {
  }
  
  public long encodeNull(java.io.OutputStream os) {
    long bytes_written = encodeTag(false, UNIVERSAL, NULL, false, os)
    bytes_written += encodeLength(0,os);
    return bytes_written
  }

  public Object decodeNull(java.io.InputStream is) {
    int tag = decodeTag(false, UNIVERSAL, NULL, false, is)
    long length = decodeLength(is);
    return null;
  }

}
