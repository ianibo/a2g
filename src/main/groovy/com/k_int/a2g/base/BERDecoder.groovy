package com.k_int.a2g.base;

import com.k_int.a2g.base.Constants;
import java.util.Stack;
import org.apache.log4j.*
import groovy.util.logging.*

@Log4j2
public class BERDecoder extends BaseDecoder {

  private InputStream is;
  private Stack<TagAndLength> encoding_stack = new Stack();

  public BERDecoder(InputStream is) {
    this.is = is;
  }

  /**
   *
   * return encoded length, -1 is indefinite length encoding
   */
  public long decodeLength() {
    long datalen;
    boolean next_is_indefinite;

    byte lenpart = (byte)this.read();

    if ((lenpart & 0x80) == 0)  { // If bit 8 is 0
      // Single octet length encoding
      // System.err.println("Single octet length encoding");
      datalen = lenpart;
    }
    else if ( ( lenpart & 0x7F ) == 0 ) { // Otherwise we are multiple octets (Maybe 0, which = indefinite)
      datalen=-1;
    }
    else { 
      next_is_indefinite=false;
      lenpart &= 0x7F;

      datalen = 0;
      while (lenpart-- > 0)
        datalen = (datalen << 8) | ((byte)this.read() & 0xFF);
    }

    return datalen;
  }

  public int decodeTag(boolean is_constructed,
                       int tag_class,
                       int tag_value,
                       boolean is_optional) throws java.io.IOException {
    return 0;
  }

  public Integer decodeInteger() {
  }
  
  public Object decodeNull() {
    int tag = decodeTag(false, Constants.UNIVERSAL, Constants.NULL, false)
    long length = decodeLength();
    return null;
  }

  public TagAndLength readNextTagAndLength() throws java.io.IOException {

    TagAndLength result = new TagAndLength();

    byte c = (byte)this.read();
    if ( c == -1 ) {
      return null;
    }

    c &= 0xFF;
    result.tag_class = c & 0xC0;
    result.is_constructed = (c & 0x20) != 0;

    result.tag_value = c & 0x1F;

    // If there are multiple octets to encode the tag
    if (result.tag_value == 0x1F) {
      result.tag_value = 0;

      // Groovy has no do-while :((
      boolean cont=true;
      while ( cont ) {
        c = (byte)this.read();

        // Shift value 7 bits left
        result.tag_value = result.tag_value << 7;

        // Merge with the octets we just got
        result.tag_value = ( result.tag_value | ( c & 0x7F ) );
        cont = ((c & 0x80) != 0)
      }
    }

    result.length = decodeLength();

    if ( ( result.length == 0 ) && 
         ( result.tag_value == 0 ) ) {
      // Possible 00 - end of indefinite length encoding marker - check the stack and mark appropriately if so
      // if ( encoding_stack.peek().is_indefinite_length ) { }
    }

    return result
  }

  public int read() {
    
    int result = is.read();
    if ( ( result >= 0 ) && ( !encoding_stack.empty() ) ) {
      encoding_stack.peek().bytes_read++
    }

    return result;
  }

  public void mark(int i) {
    is.mark(5)
  }

  public void reset() {
    is.reset();
  }

  public void beginConstructed(TagAndLength tal) {
    encoding_stack.push(tal)
  }

  public void endConstructed() {
    // Pop off the top of the stack
    TagAndLength tal = encoding_stack.pop()

    // If we're a part of a larger constructed encoding
    if ( !encoding_stack.empty() ) {
      // Add the bytes we read in processing the constructed type onto the bytes read for the container
      encoding_stack.peek().bytes_read += tal.bytes_read;
    }
  }

  public boolean moreContents() {
    boolean result = false;
    if ( !encoding_stack.empty() ) {
      TagAndLength tal = encoding_stack.peek()

      if ( tal.is_indefinite_length ) {
        // See if we can read the 00 terminating octets for an indefinite length encoding
        this.mark(5);
        int i1 = this.read();
        int i2 = this.read();
        if ( ( i1 == 0 ) && ( i2 == 0 ) ) {
          // debug("MoreData... false ( Next octets are 00 )");
          // csi.bytes_processed += 2;
          result = false;
        }
        else {
          // The two octets weren't 00 - so reset the stream
          result = true;
          this.reset();
        }

        log.debug("moreContents() - stack size is ${encoding_stack.size()} read=${tal.bytes_read} length=${tal.length} (Indefinite) result=${result}");
      }
      else {
        result = ( tal.bytes_read <= tal.length )
        log.debug("moreContents() - stack size is ${encoding_stack.size()} read=${tal.bytes_read} length=${tal.length} result=${result}");
      }
    }

    return result;
  }

}
