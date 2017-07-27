package com.k_int.a2g;

import org.apache.log4j.*
import groovy.util.logging.*
import org.antlr.v4.runtime.*;
import com.k_int.a2g.antlr.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Log4j2
public class AsnCodec {

  private Map definitions = [:]

  public AsnCodec() {
    log.debug("New AsnCodec");
  }

  public AsnCodec registerDefinitions(InputStream is) {
    log.debug("AsnCodec::registerDefinitions(InputStream)");
    parseASNInputStream(is);
    return this
  }


  private void parseASNInputStream(is) {
    // Get our lexer
    ASNLexer lexer = new ASNLexer(new ANTLRInputStream(is));
 
    // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);
 
    // Pass the tokens to the parser
    ASNParser parser = new ASNParser(tokens);
 
    // Specify our entry point
    ASNParser.ModuleDefinitionContext module_definition_context = parser.moduleDefinition();
 
    // Walk it and attach our listener
    ParseTreeWalker walker = new ParseTreeWalker();
    A2GListener listener = new A2GListener(definitions);
    walker.walk(listener, module_definition_context);
  }

  public Map generateSample(String defn, String type, Map hints) {
    log.debug("AsnCodec::generateSample(${defn},${type},${hints})");
    def result = [:]
    result;
  }

  public void dumpDefinitions() {
    log.debug("Defintitions follow::");
    log.debug(definitions);
  }
}
