package com.k_int.a2g.base;

import com.k_int.a2g.base.Constants;

public class DEREncoder extends BaseEncoder {

  private OutputStream os;

  public DEREncoder(OutputStream os) {
    this.os = os;
  }

  /**
   * For DER.
   * When the length is between 0 and 127, the short form of length must be used
   * When the length is 128 or greater, the long form of length must be used, and the length must be encoded in the minimum number of octets.
   * For simple string types and implicitly tagged types derived from simple string types, the primitive, definite-length method must be employed.
   * For structured types, implicitly tagged types derived from structured types, and explicitly tagged types derived from anything, the constructed, definite-length method must be employed.
   *
   * @return number of bytes written
   */
  public long encodeLength(long length) {

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

      this.write((int)(0x80 | num_length_octets));

      for(int j = (num_length_octets - 1) * 8; j >= 0; j -= 8)
        this.write((int)length >> j);

      return num_length_octets;
    }
    else { 
      this.write((int)length);
      return 1;
    }
  }

  public long encodeTag(boolean is_constructed,
                        int tag_class,
                        int tag_value,
                        boolean is_optional) throws java.io.IOException {
    int l = 0;
    int k = tag_class;
    if(is_constructed)
        k |= 0x20;

    l=1
    if(tag_value < 31) { // We can encode in a single octet
      this.write((int) ( k | tag_value ) );
    }
    else { // Multiple tag octets
        // In multiple length tags, first octet gives class & cons, bits 1-5 are all 1
        this.write((int)(k | 0x1f));

        // Followed by the integer encoding of the tag, base 128
        l += encodeBase128Int(tag_value);
    }

    return l;
  }

  private long encodeBase128Int(int value) throws java.io.IOException {
    int len = 0;
    byte[] enc = new byte[10];
    int pos = 0;

    while ( ( value > 127 ) && ( pos < 9 ) ) { 
      enc[pos++] = (byte) ( value & 127 );
      value = value >> 7;
    }
    enc[pos] = (byte)value;

    for ( ;pos>=0;pos-- ) { 
      this.write( (int) ( enc[pos] | ( pos == 0 ? 0 : 128 ) ) );
      len++
    }

    return len;
  }

  public long encodeInteger(Integer integer) {
  }

  public long encodeNull() {
    long bytes_written = encodeTag(false, Constants.UNIVERSAL, Constants.NULL, false)
    bytes_written += encodeLength(0);
    return bytes_written
  }


  public void write(byte[] b, int off, int len) {
    os.write(b,off,len);
  }

  public  write(int b) {
    os.write(b);
  }
}
