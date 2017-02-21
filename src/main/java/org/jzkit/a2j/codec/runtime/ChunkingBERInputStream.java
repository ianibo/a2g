/**
 *
 * BERInputStream : An implementation of the SerializationManager class that takes
 *                  an InputStream and can then be used as a parameter to a codec instance.
 *                  The data from the input stream will then be decoded according to the 
 *                  basic encoding rules.
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: ChunkingBERInputStream.java,v 1.3 2005/06/22 12:17:48 ibbo Exp $
 * @see    org.jzkit.a2j.codec.runtime.SerializationManager
 *
 * Copyright:   Copyright (C) 2000, Knowledge Integration Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the license, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite
 * 330, Boston, MA  02111-1307, USA.
 *   
 *
 */

package org.jzkit.a2j.codec.runtime;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.jzkit.a2j.codec.util.OIDRegister;
import org.apache.commons.logging.*;

/**
 * More or less copied verbatim from Yaz ODR Module.
 */
public class ChunkingBERInputStream {

  BufferedInputStream in = null;

  public ChunkingBERInputStream(InputStream is) {
    in = new BufferedInputStream(is);
  }

  public byte[] getNextCompleteAPDU() throws java.io.IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // System.err.println("complete_ber returns "+completeConstructedType(in,baos,0));
    completeConstructedType(in,baos,0);
    byte[] result = baos.toByteArray();
    // System.err.println("length of result array is "+result.length);
    return result;
  }

  public int completeConstructedType(BufferedInputStream in, ByteArrayOutputStream baos, int level) throws java.io.IOException {

    int bytes_written = 0;

    int first_byte = in.read();
    if ( first_byte == -1 )
      throw new java.io.IOException("EOF");
    byte c = (byte)first_byte;
    // byte c = (byte)in.read();
    baos.write(c);
    bytes_written++;

    c &= 0xFF;
    int next_tag_class = c & 0xC0;
    boolean next_is_constructed = (c & 0x20) != 0;
    boolean next_is_indefinite=false;

    int next_tag_number = c & 0x1F;

    // If there are multiple octets to encode the tag
    if (next_tag_number == 0x1F) {
      next_tag_number = 0;
      do {
        c = (byte)in.read();
        baos.write(c);
        bytes_written++;

        // Shift value 7 bits left
        next_tag_number = next_tag_number << 7;

        // Merge with the octets we just got
        next_tag_number = ( next_tag_number | ( c & 0x7F ) );
      } while ((c & 0x80) != 0);
    }

    // dbg("tag: "+next_tag_number+" class:"+next_tag_class, level);

    int datalen;
    byte lenpart = (byte)in.read();
    baos.write(lenpart);
    bytes_written++;

    // System.err.println("First len octet is "+lenpart);
    if ((lenpart & 0x80) == 0)  // If bit 8 is 0
    {
      // Single octet length encoding
      // System.err.println("Single octet length encoding");
      datalen = lenpart;
      next_is_indefinite=false;
    }
    else if ( ( lenpart & 0x7F ) == 0 ) // Otherwise we are multiple octets (Maybe 0, which = indefinite)
    {
      // System.err.println("Indefinite length encoding");
      next_is_indefinite=true;
      datalen=0;
    }
    else
    {
      next_is_indefinite=false;
      // System.err.println("Multiple octet length encoding ("+(lenpart & 0x7F )+"octets)");
      lenpart &= 0x7F;

      datalen = 0;
      while (lenpart-- > 0) {
        byte lenbyte = (byte)in.read();
        datalen = (datalen << 8) | (lenbyte & 0xFF);
        baos.write(lenbyte);
        bytes_written++;
      }
    }

    // System.err.print(" indefinite: "+next_is_indefinite+" cons:"+next_is_constructed+" len:"+datalen);

    // OK, len now contains the size of the octets.
    // If it's definite length encoding, just copy that many bytes
    if ( next_is_indefinite ) {
      if ( next_is_constructed ) {
        // System.err.print(" {\n");
        // Peek ahead looking for terminating octets.
        boolean more_data = true;
        in.mark(5);
        byte i1 = (byte) in.read();
        byte i2 = (byte) in.read();
        in.reset();
        if ( ( i1 == 0 ) && ( i2 == 0 ) ) {
          more_data = false;
        }
        
        while(more_data) {
          completeConstructedType(in, baos, level+1);
          in.mark(5);
          i1 = (byte) in.read();
          i2 = (byte) in.read();
          in.reset();
          if ( ( i1 == 0 ) && ( i2 == 0 ) ) {
            more_data = false;
          }
        }

        // Better consume the terminating octets.
        in.read();
        in.read();

        baos.write(0);
        bytes_written++;
        baos.write(0);
        bytes_written++;
        // dbg("} ("+bytes_written+")\n",level);
      }
      else {
        // Indefinite length primitive type
        // System.err.print(" Indefinite length primitive type");
        byte b1= (byte) in.read();
        baos.write(b1);
        bytes_written++;
        byte b2= (byte) in.read();
        baos.write(b2);
        bytes_written++;
        while( ! ( ( b1 == 0 ) && ( b2 == 0 ) ) ) {
          b1 = b2;
          b2 =  (byte) in.read();
          baos.write(b2);
          bytes_written++;
        }
        // System.err.println("("+bytes_written+")");
      }
    }
    else {
      // System.err.println("copy definite length encoding, remain="+datalen);
      if ( next_is_constructed ) {
        // System.err.println(" {");
        while ( datalen > 0 ) {
          int child_len = completeConstructedType(in, baos, level+1);
          datalen -= child_len;
          bytes_written+=child_len;
        }
        // dbg("} ("+bytes_written+")\n",level);
      }
      else {
        // System.err.print(" Definite length primitive type");
        byte[] buff = new byte[4096];
        while ( datalen > 0 ) {
          int bytes_read = in.read(buff,0, datalen > 4096 ? 4096 : datalen);
          // System.err.println("processed "+bytes_read);
          baos.write(buff,0,bytes_read);
          datalen -= bytes_read;
          bytes_written+=bytes_read;
          // System.err.println("("+bytes_written+")");
        }
      }
    }

    return bytes_written;
  }

  private void dbg(String msg, int level) {
    for ( int i=0; i<level; i++ ) {
      System.err.print("  ");
    }
    System.err.print(msg);
  }
}
