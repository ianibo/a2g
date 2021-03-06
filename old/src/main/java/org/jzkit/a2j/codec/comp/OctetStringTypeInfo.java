/**
 *
 * OctetStringTypeInfo
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: OctetStringTypeInfo.java,v 1.1.1.1 2004/06/18 06:38:13 ibbo Exp $
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

package org.jzkit.a2j.codec.comp;

import java.io.StringWriter;
import java.io.FileWriter;
import java.io.File;              

public class OctetStringTypeInfo extends TypeInfo
{
  public OctetStringTypeInfo(String type_reference,
                        boolean builtin_type,
                        int tag_class,
                        int tag_number,
                        boolean is_implicit,
                        String basetype,
                        String internal_type,
                        ModuleInfo mi)
  {
    super(type_reference,builtin_type, tag_class, tag_number,is_implicit,basetype,internal_type,mi);
    // codec_class_name = "OctetString_codec";
    // type_class_name = "String";             
  }

  public OctetStringTypeInfo(String type_reference,
                        boolean builtin_type,
                        String basetype,
                        String internal_type,
                        ModuleInfo mi)
  {
    super(type_reference, builtin_type, basetype, internal_type, mi);
    // codec_class_name = "OctetString_codec";
    // type_class_name = "String";             
  }

  public void writeTypeSpecificStaticInitialisationCode(StringWriter func, StringWriter declarations)
  {
  }       

  public void createTypeClassFile()
  {
  }  
}

