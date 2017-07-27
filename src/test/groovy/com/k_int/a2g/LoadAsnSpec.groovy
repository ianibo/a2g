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


  def "Test ability to load ASN.1 Useful Types"() {
    when:
      InputStream useful_definition_is = this.getClass().getResourceAsStream('/AsnUseful.asn')

    then:
      AsnCodec asn_codec = new AsnCodec();
      asn_codec.registerDefinitions(useful_definition_is)

    expect:
      1==1
  }

  def "Test ability to load Z39.50"() {
    when:
      InputStream z3950_definition_is = this.getClass().getResourceAsStream('/z3950v3.asn')

    then:
      AsnCodec asn_codec = new AsnCodec();
      asn_codec.registerDefinitions(z3950_definition_is)

    expect:
      1==1
  }
}
