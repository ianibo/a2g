package com.k_int.a2g;

import spock.lang.Specification
import spock.lang.Unroll
import java.io.InputStream

import com.k_int.a2g.AsnCodec;

class DEREncodingSpec extends Specification {

  def "test Length encoding"() {
      
    expect:
      OutputStream os = new ByteArrayOutputStream();
      com.k_int.a2g.base.BaseEncoder der_encoder = new com.k_int.a2g.base.DEREncoder(os);
      der_encoder.encodeLength(lenval);
      byte[] encoded_length = os.toByteArray();
      Arrays.equals(encoded_length, encoding)

    where:
           lenval  | encoding
                0  | (byte[]) [ 0 ]
                1  | (byte[]) [ 1 ]
              127  | (byte[]) [ 127 ]
              128  | (byte[]) [ -127, 128 ]
              256  | (byte[]) [ -126, 1, 0 ]
          0x10000  | (byte[]) [ -125, 1, 0, 0 ]
        0x1000000  | (byte[]) [ -124, 1, 0, 0, 0 ]

  }

  def "test Null encoding"() {

    when:
      OutputStream os = new ByteArrayOutputStream();
      com.k_int.a2g.base.BaseEncoder der_encoder = new com.k_int.a2g.base.DEREncoder(os);

    then:
      der_encoder.encodeNull()
      byte[] encoded_null = os.toByteArray();

      com.k_int.a2g.base.BaseDecoder der_decoder = new com.k_int.a2g.base.DERDecoder(new java.io.ByteArrayInputStream(encoded_null));
      Object o = der_decoder.decodeNull()

    expect:
      Arrays.equals(encoding,encoded_null); 
      o == null;

    where:
        nullval | encoding
        null | (byte[]) [ 5, 0 ]
  }

}
