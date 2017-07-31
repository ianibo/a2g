package com.k_int.a2g.base;

import org.apache.log4j.*
import groovy.util.logging.*

/**
 * 
 */
@Log4j2
public class BERDump {

  public void dump(InputStream is) {
    BERDecoder ber_decoder = new BERDecoder();

    TagAndLength tal = ber_decoder.readNextTagAndLength(is);
    while( tal ) {
      log.debug(tal);
      byte[] content_octets = new byte[tal.length]

      is.read(content_octets,(int)0,(int)(tal.length));
      log.debug("Contents:\n${content_octets}");

      log.debug("Reading next tag and length");
      tal = ber_decoder.readNextTagAndLength(is)
    }
  }
}
