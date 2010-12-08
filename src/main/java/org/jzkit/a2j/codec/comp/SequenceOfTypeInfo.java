/**
 *
 * SequenceOfTypeInfo
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: SequenceOfTypeInfo.java,v 1.1.1.1 2004/06/18 06:38:13 ibbo Exp $
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

import java.util.logging.*;

public class SequenceOfTypeInfo extends TypeInfo
{
  protected String subtype_reference = null;
  private static Logger log = Logger.getLogger(SequenceOfTypeInfo.class.getName());

  public SequenceOfTypeInfo(String type_reference,
                          boolean builtin_type,
                          int tag_class,
                          int tag_number,
                          boolean is_implicit,
                          String basetype,
                          String internal_type,
                          ModuleInfo mi,
                          String subtype_reference)
  {
    super(type_reference,builtin_type, tag_class, tag_number,is_implicit,basetype,internal_type,mi);
    type_class_name = internal_type;
    this.subtype_reference = subtype_reference;
  }

  public SequenceOfTypeInfo(String type_reference,
                  boolean builtin_type,
                  String basetype,
                  String internal_type,
                  ModuleInfo mi,
                  String subtype_reference)
  {
    super(type_reference, builtin_type, basetype, internal_type,mi);
    type_class_name = internal_type;
    this.subtype_reference = subtype_reference;
  }

  public void writeTypeSpecificStaticInitialisationCode(StringWriter func, StringWriter declarations)
  {
  }       

  public void createTypeClassFile()
  {
  }  

  public void writeSerializeMethod(StringWriter func, StringWriter declarations)
  {
    // Locate the typeinfo for the referenced type (sequence of X)

    TypeInfo sti = parent.lookup(null, subtype_reference, true);


    if ( null != sti )
    {
      declarations.write("  private "+sti.getCodecClassName()+" i_"+sti.getCodecClassName().toLowerCase()+" = "+sti.getCodecClassName()+".getCodec();\n\n");

      func.write("  public Object serialize(SerializationManager sm,\n");
      func.write("                          Object type_instance,\n");
      func.write("                          boolean is_optional,\n");
      func.write("                          String type_name) throws java.io.IOException\n");
      func.write("  {\n");

      func.write("    "+this.type_class_name+" retval = ("+this.type_class_name+")type_instance;\n\n");

      func.write("    if ( ( ( sm.getDirection() == SerializationManager.DIRECTION_ENCODE ) && ( retval != null ) ) ||\n         ( sm.getDirection() == SerializationManager.DIRECTION_DECODE ) )\n    {\n");

      if ( tag_class == -1 )
      {
        func.write("      if ( sm.sequenceBegin() )\n      {\n");
      }
      else
      {
        if ( is_implicit )
        {
          func.write("      sm.implicit_settag("+tag_class+", "+tag_number+");\n");
          func.write("      if ( sm.sequenceBegin() )\n      {\n");
        }
        else
        {
            func.write("      if ( sm.constructedBegin("+tag_class+", "+tag_number+") )\n      {\n");
            func.write("        sm.sequenceBegin();\n");
        }
      }
 
      func.write("        if ( sm.getDirection() == SerializationManager.DIRECTION_DECODE )\n        {\n");
      func.write("            retval = new "+this.type_class_name+"();\n");
      func.write("        }\n\n");

      func.write("        retval = sm.sequenceOf(retval, i_"+sti.getCodecClassName().toLowerCase()+");\n\n");

      if ( tag_class == -1 )
      {
        func.write("        sm.sequenceEnd();\n      }\n\n");
      }
      else
      {
        if ( is_implicit )
        {
          func.write("        sm.sequenceEnd();\n      }\n\n");
        }
        else
        {
            func.write("        sm.constructedEnd();\n");
            func.write("        sm.sequenceEnd();\n      }\n\n");
        }
      }      

      func.write("    }\n    return retval;\n");
      func.write("  }\n");
    }
    else
      log.log(Level.SEVERE,"WARNING: Unable to locate type info for "+ subtype_reference);
  }
}

