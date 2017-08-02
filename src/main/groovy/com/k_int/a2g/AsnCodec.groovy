package com.k_int.a2g;

import org.apache.log4j.*
import groovy.util.logging.*
import org.antlr.v4.runtime.*;
import com.k_int.a2g.antlr.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.k_int.a2g.base.BaseDecoder;
import com.k_int.a2g.base.TagAndLength;

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

  public void dumpDefinition(String definition_id) {
    log.debug(getDefinition(definition_id));
  }

  public void dumpDefinitions() {
    log.debug("Defintitions follow::");
    log.debug(definitions);
  }

  public Map getDefinition(String definition_id) {
    return definitions[definition_id]
  }

  public byte[] arrayEncode(String specification,String type, Map data) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    encode(specification, type, data, baos)
    baos.toByteArray()
  }

  public Map arrayDecode(String specification,String type, byte[] data) {
    Map result = [:]
    result;
  }

  private encode(String specification, String type, Map data, ByteArrayOutputStream baos) {
  }

  public Map decode(String specification, String type, BaseDecoder decoder) {
    log.debug("decode(${specification},${type},is,dec)");
    def spec_defn = definitions[specification]

    if ( spec_defn ) {
      log.debug("Resolved specification ${specification}");
      def type_defn = spec_defn.typemap[type]

      if ( type_defn ) {
        log.debug("Resolved type ${type}");

        // Lets read the first tag and length from the input stream, and then decode the contents.
        TagAndLength tal = decoder.readNextTagAndLength()

        // Now decode the contents
        processContents(tal, spec_defn, type_defn, decoder);
      }
      else {
        log.error("Unable to resolve type ${type} in specification ${specification}. Known types: ${spec_defn.keySet()}");
      }
    }
    else {
      log.error("Unable to resolve specification ${specification}. Currently know: ${definitions.keySet()}");
    }
  }

  private Object processContents(TagAndLength tal, Map spec_defn, Map type_defn, BaseDecoder decoder) {
    Object result = null;
    switch ( type_defn.type ) {
      case 'CHOICE':
        result = decodeChoice(tal, spec_defn, type_defn, decoder);
        break;
      default:
        throw new RuntimeException("Unhandled ASN.1 type ${type_defn.type}");
    }
    return result;
  }

  /**
   *  We map choice elements to a map containing 1 member which is the name of the choice
   */
  private decodeChoice(TagAndLength tal, Map spec_defn, Map type_defn, BaseDecoder decoder) {
    log.debug("Decode choice ${tal.tag_class} ${tal.tag_value}");
    def result = [:]

    // The encoding of a choice type is the same as the encoding of the selected alternative. TagAndLength therefore is the tagAndLength of the
    // chosen alternative in this case. We must iterate through the possible choices until we find an alternative that matches the decoded tag

    type_defn.members.each { choice_option ->

      log.debug("${tal.tag_class} ${tal.tag_class.class.name} ${tal.tag_value} ${tal.tag_value.class.name} ${choice_option.tag.tag_class} ${choice_option.tag.tag_class?.class?.name} ${choice_option.tag.tag_class_number} ${choice_option.tag.tag_class_number.class.name}");
      // See if the tagging of the encoded data matches this part of the specification
      // default tag
      if ( ( (choice_option.tag.tag_class?:128) == tal.tag_class ) && 
           ( choice_option.tag.tag_class_number == tal.tag_value) ) {
        log.debug("Matched choice ${tal.tag_class} ${tal.tag_value} ${choice_option}");
      }
      else {
        log.debug("Not ${choice_option} for ${tal.tag_class} ${tal.tag_value}");
      }
    }

    result;
  }
}
