package com.k_int.a2g;

import org.apache.log4j.*
import groovy.util.logging.*

@Log4j2
public class AsnCodec {

  public AsnCodec() {
    log.debug("New AsnCodec");
  }

  public AsnCodec registerDefinitions(InputStream is) {
    log.debug("AsnCodec::registerDefinitions(InputStream)");
    return this
  }
}
