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
      asn_codec.dumpDefinitions();
      asn_codec.dumpDefinition('SIMPLE_TEST_ASN1');
      byte[] encoded_contact = asn_codec.arrayEncode('SIMPLE_TEST_ASN1','Contact',[ name:'Fred Person' , phone : '012345678' ]);
      Map decoded_contact = asn_codec.arrayDecode('SIMPLE_TEST_ASN1','Contact',encoded_contact);


    expect:
      def example_record = asn_codec.generateSample('SIMPLE_TEST_ASN1','Contact',[:]);
      // Make sure that the Contact class defined in that file has 2 members
      asn_codec.getDefinition('SIMPLE_TEST_ASN1').typemap['Contact'].members.size() == 2

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
      asn_codec.dumpDefinitions();

    expect:
      1==1
  }

  def "Test ability to load Z39.50"() {
    when:
      InputStream useful_definition_is = this.getClass().getResourceAsStream('/AsnUseful.asn')
      InputStream z3950_definition_is = this.getClass().getResourceAsStream('/z3950v3.asn')

    then:
      AsnCodec asn_codec = new AsnCodec();
      asn_codec.registerDefinitions(z3950_definition_is)
      asn_codec.dumpDefinitions();

    expect:
      1==1
  }

  def "Test decoding and encoding of Z3950 APDUs"() {
    when:
      // Load the ASN.1 Definitions
      InputStream useful_definition_is = this.getClass().getResourceAsStream('/AsnUseful.asn')
      InputStream z3950_definition_is = this.getClass().getResourceAsStream('/z3950v3.asn')

      // Load the example APDUs generated by yaz-client - these are BER encoded APDUs 1 per file
      init_request_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/init_request.raw'))
      init_response_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/init_request.raw'))
      search_request_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/search_request.raw'))
      search_response_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/search_response.raw'))
      present_request_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/present_request.raw'))
      present_response_ber = ByteStreams.toByteArray(this.getClass().getResourceAsStream('/apdus/present_response.raw'))
    then:
      // Get the codec
      AsnCodec asn_codec = new AsnCodec();
      // Load useful and z3950
      asn_codec.registerDefinitions(useful_definition_is)
      asn_codec.registerDefinitions(z3950_definition_is)

      // Decode the init request file
      def decoded_init_request = asn_codec.ber('Z39-50-APDU-1995',init_request_ber);

      // Re-encode the decoded message
      def encoded_init_request = asn_codec.ber('Z39-50-APDU-1995',decoded_init_request);

    expect:
      Arrays.equals(init_request_ber, encoded_init_request)

  }
}
