package com.k_int.a2g.base;


/**
 * BaseCodec - the interface that arranges to encode a particular set of values.
 *
 * Most BER encodings consist of 4 parts: (taken from http://luca.ntop.org/Teaching/Appunti/asn1.html)
 * Identifier octets. These identify the class and tag number of the ASN.1 value, and indicate whether the method is primitive or constructed.
 * Length octets. For the definite-length methods, these give the number of contents octets. For the constructed, indefinite-length method, these indicate that the length is indefinite.
 * 
 * Contents octets. For the primitive, definite-length method, these give a concrete representation of the value. 
 * For the constructed methods, these give the concatenation of the BER encodings of the components of the value.
 * 
 * End-of-contents octets. For the constructed, indefinite- length method, these denote the end of the contents. For the other methods, these are absent.
 * 
 * 
 */
public abstract class BaseCodec {

  public static final Integer TAGMODE_NONE = new Integer(-1);
  public static final Integer IMPLICIT = new Integer(0);
  public static final Integer EXPLICIT = new Integer(1);

  public static final int UNIVERSAL = 0;

  public static final int BOOLEAN = 1;
  public static final int INTEGER = 2;
  public static final int BITSTRING = 3;
  public static final int OCTETSTRING = 4;
  public static final int NULL = 5;
  public static final int OID = 6;
  public static final int OBJECT_DESCRIPTOR = 7;
  public static final int EXTERNAL = 8;
  public static final int REAL = 9;
  public static final int ENUMERATED = 10;
  public static final int SEQUENCEOF = 16;
  public static final int SEQUENCE = 16;
  public static final int SET = 17;
  public static final int NUMERIC_STRING = 18;
  public static final int PRINTABLE_STRING = 19;
  public static final int GENERALIZED_TIME = 24;
  public static final int GRAPHIC_STRING = 25;
  public static final int VISIBLE_STRING = 26;
  public static final int GENERAL_STRING = 27;



  /**
   *  @return number of bytes written
   */
  public abstract long encodeLength(long length, java.io.OutputStream os);

  /**
   * @return decoded length value
   */
  public abstract long decodeLength(java.io.InputStream os);

  public abstract long encodeTag(boolean is_constructed,
                                 int tag_class,
                                 int tag_number,
                                 boolean is_optional,
                                 java.io.OutputStream os) throws java.io.IOException;

  public abstract int decodeTag(boolean is_constructed,
                                int tag_class,
                                int tag_number,
                                boolean is_optional,
                                java.io.InputStream is) throws java.io.IOException;

 
  public abstract long encodeInteger(java.io.OutputStream os, Integer integer);
  public abstract Integer decodeInteger(java.io.InputStream is);

  public abstract long encodeNull(java.io.OutputStream os);
  public abstract Object decodeNull(java.io.InputStream is);

}
