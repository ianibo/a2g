package com.k_int.a2g;

import org.apache.log4j.*
import groovy.util.logging.*
import org.antlr.v4.runtime.*;
import com.k_int.a2g.antlr.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.k_int.a2g.base.BaseDecoder;
import com.k_int.a2g.base.TagAndLength;
import com.k_int.a2g.base.Tag;

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

  /**
   * See if we can find a protocol definition for the given input string -- for example the 'AsnUseful' definitions, or 'Z39-50-APDU-1995'.
   * Once looked up, the resulting object has a .typemap that can be used to look up type definitions within that sepecification.
   */
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
      return decode(spec_defn,type,decoder);
    }
    else {
      throw new RuntimeException("Unable to look up type ${type} in specification ${specification}");
    }
  }

  /**
   * decode - decode a specified type from the input stream
   * @param spec_defn - the parsed ASN.1 specification as a set of map objects -- EG the parsed z39.50 specification, or the parsed ISO10161 specification
   * @param type - the name of the type we want to decode from that specification -- EG InitRequest from the z3950-1993 specification
   * @param decoder - our wrapped input stream that knows about BER constructed types and the decoding stack
   * 
   * This method is our primary entry point for decoding an octet-stream. Streams are always composed of <TAG> <LENGTH> <ContentOctets> so
   * this method reads the leading tag and length, and then delegates the processing of the contents to a later function.
   */
  public Map decode(Map spec_defn, String type, BaseDecoder decoder) {

    def result = null;

    if ( spec_defn ) {
      def type_defn = spec_defn.typemap[type]

      if ( type_defn ) {
        log.debug("Resolved type ${type}");

        // Lets read the first tag and length from the input stream, and then decode the contents.
        TagAndLength tal = decoder.readNextTagAndLength()

        // Now decode the contents
        result = processContents(tal, spec_defn, type_defn, decoder);
      }
      else {
        log.error("Unable to resolve type ${type} in specification ${specification}. Known types: ${spec_defn.keySet()}");
      }
    }
    else {
      log.error("Unable to resolve specification ${specification}. Currently know: ${definitions.keySet()}");
    }

    result
  }

  /**
   * process the contents of a given constructed encoding.
   * We have read a tag and length from the input stream which has allowed us to look up a type declaration.
   * we now need to decode the content octets based upon the type
   */
  private Object processContents(TagAndLength tal, Map spec_defn, Map type_defn, BaseDecoder decoder) {
    Object result = null;
    // getBaseType currently just returns type_defn.type but I suspect we might need to do something more
    // involved in some implicit tagging cases, so the lookup of a base type (SEQUENCE,CHOICE,etc) for a given type is
    // broken out into a function here to avoid pain later on.
    log.debug("processContents(${tal}...)");

    switch ( getBaseType(spec_defn,type_defn) ) {
      case 'CHOICE':
        result = decodeChoice(tal, spec_defn, type_defn, decoder);
        break;
      case 'SEQUENCE':
        result = decodeSequence(tal, spec_defn, type_defn, decoder);
        break;
      default:
        throw new RuntimeException("Unhandled ASN.1 type ${type_defn.type} definition ${type_defn}");
    }
    return result;
  }

  private getBaseType(spec_defn, type_definition) {
    if ( type_definition.elementCat == 'defined' ) {
      // The type is defined elsewhere in the file, so we need to get the base type from that definition
      result = type_definition.elementType
    }

    return type_definition.type;
  }

  /**
   *  We map choice elements to a map containing 1 member which is the name of the choice
   */
  private decodeChoice(TagAndLength tal, Map spec_defn, Map type_defn, BaseDecoder decoder) {
    log.debug("Decode choice ${tal.tag_class} ${tal.tag_value}");

    // The choice will decode to a map with 1 element, which corresponds to the selected type.
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
        // The choice_option defintion looks like this::
        // [elementCat:builtin, elementType:tagged, tag:[tag_class_number:20, tag_class:128], type:[elementCat:defined, elementType:InitializeRequest], tagMode:IMPLICIT, elementName:initRequest]

        // The choice will always decode to a map with 1 element, which corresponds to the selected type.
        result[choice_option.elementName] = this.decode(spec_defn, choice_option.type.elementType, decoder);
      }
      else {
        // log.debug("Not ${choice_option} for ${tal.tag_class} ${tal.tag_value}");
      }
    }

    result;
  }

  private decodeSequence(TagAndLength tal, Map spec_defn, Map type_defn, BaseDecoder decoder) {
    decoder.beginConstructed(tal)

    log.debug("Decode a sequene with definition ${type_defn}");

    // First tag in constructed octets comprising this sequence.
    TagAndLength content_tal = decoder.readNextTagAndLength()
    log.debug("Next tag and length is ${content_tal}");

    type_defn.members.each { member_defn ->

      // We need to work out what tag will correspond to the type of member_defn
      // If the member is a defined type with no explicit tagging, we need to work out what tag to expect for the defined type
      Tag expected_tag = getTagForConstructedMember(spec_defn, member_defn)
      assert( expected_tag != null );

      log.debug("Consider the following member ${member_defn}");

      if ( ( (expected_tag.tag_class?:128) == content_tal.tag_class ) &&
           ( expected_tag.tag_class_number == content_tal.tag_value) ) {
        log.debug("Matched tag");
        // Decode the contents for this tagged element.
      }
      else {
        // If the member was mandatory throw an exception
      }

    }

    if ( decoder.moreContents() ) {
      throw new RuntimeException("Reached end of sequence definition, but there are still contents octets");
    }
    else {
      // All tidy -- we've reached the end of the type declaration for the sequence and there are no contents
      // octets remaining for this constructed type.
    }

    decoder.endConstructed()
  }

  /**
   * This method takes a member of a constructed type (Sequence or Choice currently) and works out what tag we should be
   * looking out for in the octet stream.
   * Examples are:
   *  referenceId        ReferenceId OPTIONAL,
   *  protocolVersion      ProtocolVersion,
   *  options        Options,
   *  preferredMessageSize  [5]  IMPLICIT INTEGER,
   *
   */
  private Tag getTagForConstructedMember(spec_defn,member_defn) {
    Tag result = null;
    // This
    switch ( member_defn.elementCat ) {
      case 'builtin' :
        switch ( member_defn.elementType ) {
          case 'tagged':
            result = new Tag(member_defn.tag.tag_class, member_defn.tag.tag_class_number)
            break;
          default:
            throw new RuntimeException("Unhandled constructed member type (${member_defn.elementType}) in builtin category");
        }
        break;

      case 'defined' :
        // The member is defined by some other type in this specification see if we can look that up
        def typedef_for_member = spec_defn.typemap[member_defn.elementType]
        if ( typedef_for_member == null ) {
          throw new RuntimeException("Failed to resolve type ${member_defn.elementType}. Current definitions contain ${spec_defn.typemap.keySet()}");
        }
        else {
          log.debug("Worked out that the member is of a defined type ${member_defn.elementType} with defn ${typedef_for_member} - now work out the tag");
          if ( typedef_for_member.size() == 0 ) {
            throw new RuntimeException("Found a typemap entry for ${member_defn.elementType} but the entry is blank. This means more work is needed to extract the type information from the ASN definition.. and that the implementation is incomplete");
          }
        }
        throw new RuntimeException("Incomplete getTagForConstructedMember implementation for defined member");
        break;

      default:
        throw new RuntimeException('Unhandled constructed member category');
    }

    log.debug("Result of getTagForConstructedMember: ${result}");
    return result;
  }
}
