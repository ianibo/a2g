package com.k_int.a2g;

import spock.lang.Specification
import spock.lang.Unroll
import java.io.InputStream

import com.k_int.a2g.AsnCodec;

class LoadAsnSpec extends Specification {

  def "Test ability to load simple ASN.1 File"() {

    when:
      InputStream simple_asn1_definition_is_1 = this.getClass().getResourceAsStream('/simple_definition_1.asn')

    then:
      AsnCodec asn_codec = new AsnCodec();
      asn_codec.registerDefinitions(simple_asn1_definition_is_1)

    expect:
      1==1

    // expect:
      // This class is in our Java code
    //   MyClass.max(a, b) == c

    // where:
    //   a  | b   | c
    //   1  | 2   | 2
    //   42 | -12 | 42
    //   42 | -12 | -42
  }
}
