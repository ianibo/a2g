package com.k_int.a2g.base;

import org.apache.log4j.*
import groovy.util.logging.*
import java.util.Stack;

/**
 * 
 */
@Log4j2
public class BERDump {

  private Stack encoding_info = new Stack();

  public void dump(InputStream is) {
    BERDecoder ber_decoder = new BERDecoder();

    TagAndLength tal = ber_decoder.readNextTagAndLength(is);
    while( tal ) {
      log.debug(tal);

      if ( tal.is_constructed ) {
        encoding_info.push(tal);
        dumpConstucted(content_is)
        encoding_info.pop();
      }
      else {
        log.debug("Primitive: ${content_octets}");
      }

      log.debug("Reading next tag and length");
      tal = ber_decoder.readNextTagAndLength(is)
    }
  }

  public void dumpConstucted(content_is) {
    int bytes_processed = 0;
    TagAndLength tal = (TagAndLength)encoding_info.peek();
    if ( tal.length == -1 ) {
      // Indefinite length
    }
    else {
      // Definite Length
    }
    
  }
}
