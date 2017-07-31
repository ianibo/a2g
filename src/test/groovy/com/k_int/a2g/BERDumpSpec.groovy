package com.k_int.a2g;

import spock.lang.Specification
import spock.lang.Unroll
import java.io.InputStream

import com.k_int.a2g.AsnCodec;

class BERDumpSpec extends Specification {

  def "test BERDump"() {
    when:
      com.k_int.a2g.base.BERDump ber_dumper = new com.k_int.a2g.base.BERDump();
      InputStream init_request_is = this.getClass().getResourceAsStream('/apdus/init_request.raw')
      
    then:
      ber_dumper.dump(init_request_is);
      
    expect:
      1==1
  }

}
