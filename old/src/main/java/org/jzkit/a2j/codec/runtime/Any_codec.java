/**
 *
 * Any_codec
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: Any_codec.java,v 1.2 2005/05/18 13:47:18 ibbo Exp $
 *
 * Copyright:   Copyright (C) 2000, Knowledge Integration Ltd
 *
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

import org.apache.commons.logging.*;



public class Any_codec extends base_codec {
  private static Any_codec me = null;
  private static Log log = LogFactory.getLog(Any_codec.class);

  public static Any_codec getCodec() {
    if ( me == null )
      me = new Any_codec();

    return me;
  }

  public Object serialize(SerializationManager sm,
                          Object type_instance,
                          boolean is_optional,
                          String type_name) throws java.io.IOException {
    Object retval = type_instance;

    base_codec codec_hint = sm.getHintCodec(); 

    // See if we have been given a hint by some previous value
    if ( null != codec_hint )
    {
      log.debug("Decoding using "+codec_hint.getClass().getName());
      retval = codec_hint.serialize(sm, retval, is_optional, type_name);
      sm.setHintCodec(null);
    }
    else
    {
      // Encode as an array of octets
      retval = sm.any_codec(retval, false);
    }

    if ( ( retval == null ) && ( ! is_optional ) )
      throw new java.io.IOException("Missing mandatory member: "+type_name);

    return (Object)retval;
  }
}
