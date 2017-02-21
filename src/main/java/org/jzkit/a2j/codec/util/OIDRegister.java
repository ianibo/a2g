/**
 *
 * OIDRegister
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: OIDRegister.java,v 1.4 2005/02/11 14:49:54 ibbo Exp $
 *
 * Copyright:   Copyright (C) 2000, Knowledge Integration Ltd (See the file LICENSE for details.)
 *
 */

package org.jzkit.a2j.codec.util;

import java.util.Hashtable;
import java.io.StringWriter;
import org.jzkit.a2j.codec.runtime.base_codec;

// The idea of this class is to map an OID onto a class that is capable of
// turning a Stream into some kind of structured object

public class OIDRegister
{
  private Hashtable register_by_name = new Hashtable();
  private Hashtable register_by_value = new Hashtable();

  public OIDRegister(String[] resources) {
    for (int i=0; i<resources.length; i++) {
      OIDRegConfigurator.load(this,resources[i]);
    }
  }

  public OIDRegister(String resource) {
    OIDRegConfigurator.load(this,resource);
  }

  public void register_oid(OIDRegisterEntry entry) {
    register_by_name.put(entry.getName(), entry);
    register_by_value.put(entry.getStringValue(), entry);
  }

  public OIDRegisterEntry lookupByOID(String oid_as_string) {
    OIDRegisterEntry e = (OIDRegisterEntry)register_by_value.get(oid_as_string);
    return e;
  }

  public OIDRegisterEntry lookupByOID(int[] oid) {
    // We hope we won't be creating oid strings much bigger than 32!
    StringWriter sw = new StringWriter(32);  

    sw.write("{");

    for ( int i = 0; i<oid.length; i++)
    {
      if ( i > 0 )
        sw.write(",");

      sw.write(""+oid[i]);
    }

    sw.write("}");

    // System.err.println("OID:"+sw.toString());

    return lookupByOID(sw.toString());
  }

  public int[] oidByName(String name) {
    OIDRegisterEntry o = lookupByName(name);

    if ( null != o ) {
      return o.getValue();
    }
    
    return null;
  }

  public OIDRegisterEntry lookupByName(String name) {
    return (OIDRegisterEntry) register_by_name.get(name);
  }

}
