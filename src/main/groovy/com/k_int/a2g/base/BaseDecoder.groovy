package com.k_int.a2g.base;


/**
 * BaseDecoder - the interface that arranges to encode a particular set of values.
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
public abstract class BaseDecoder {

  public abstract long decodeLength();

  public abstract int decodeTag(boolean is_constructed,
                                int tag_class,
                                int tag_number,
                                boolean is_optional) throws java.io.IOException;

  public abstract Integer decodeInteger();

  public abstract Object decodeNull();

  public abstract void beginConstructed(TagAndLength tal);
  public abstract void endConstructed();
  public abstract boolean moreContents();
}
