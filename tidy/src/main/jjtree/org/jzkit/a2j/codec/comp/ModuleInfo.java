/**
 *
 * moduleInfo
 *
 * @author Ian Ibbotson ( ibbo@k-int.com )
 * @version $Id: ModuleInfo.java,v 1.1.1.1 2004/06/18 06:38:13 ibbo Exp $
 *
 * Copyright:   Copyright (C) 2000, Knowledge Integration Ltd (See the file LICENSE for details.)
 *
 */ 

package org.jzkit.a2j.codec.comp;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.io.File;

import java.io.StringWriter;

import java.util.logging.*;

public class ModuleInfo
{
  private Hashtable types = new Hashtable();

  String module_reference;
  boolean default_explicit_tagging;
  boolean create_java;

  public String module_package_name = null;
  public String module_package_dir = null;

  public Hashtable imported_types = new Hashtable();

  private static Logger log = Logger.getLogger(ModuleInfo.class.getName());

  public ModuleInfo(String module_reference,
                    boolean default_explicit_tagging,
                    boolean create_java,
                    String base_package,
                    String output_dir)
  {
    this.module_reference=module_reference;
    this.default_explicit_tagging=default_explicit_tagging;
    this.create_java = create_java;

    // lets work out what package this thing will be going in
    // String s =  System.getProperty("CodecBasePackage");

    if ( base_package == null )
      module_package_name = module_reference.replace('-','_');
    else
      module_package_name = base_package+"."+module_reference.replace('-','_');

    if ( output_dir != null )
      module_package_dir = output_dir+File.separatorChar+module_package_name.replace('.',File.separatorChar);
    else
      module_package_dir = module_package_name.replace('.',File.separatorChar);

    log.fine("module package name is "+module_package_name);
    log.fine("module package dir is "+module_package_dir);
  }

  public void registerType(String type_reference, TypeInfo ti)
  {
    log.fine("Adding "+type_reference);
    types.put(type_reference, ti);
  }

  public void createCode()
  {
    log.fine("Creating code for module "+module_reference+" default tagging mode is "+ ( default_explicit_tagging ? "EXPLICIT" : "IMPLICIT" ) );

    // We might need to create a directory
    File pkg_path = new File(module_package_dir);
    if ( !pkg_path.exists() )
    {
      pkg_path.mkdirs();
    } 

    for (Enumeration e = types.elements() ; e.hasMoreElements() ;)
            ((TypeInfo)(e.nextElement())).createCode();
  }

  public String getModulePackageName()
  {
    return module_package_name;
  }

  public String getModulePackageDir()
  {
    return module_package_dir;
  }

  public TypeInfo lookupType(String type_reference)
  {
    return (TypeInfo)(types.get(type_reference));
  }

  // A named type must be explicitly identified, part of the current module, 
  // AsnUseful or a base type.....
  public TypeInfo lookup(String module_name, String type_reference, boolean search)
  {
    log.fine("lookup("+module_name+","+type_reference+","+search+")");

    CodecBuilderInfo info = CodecBuilderInfo.getInfo();
    TypeInfo ti = null;

    // Do we have a module name?
    if ( null != module_name )
    {
      log.fine("Searching in a specific repository");

      // Get hold of the appropriate module from the module registry
      ModuleInfo m = info.lookupModule(module_name);

      // Lookup the named type in that module
      ti = m.lookup(null, type_reference,false);
    }
    else
    {
      log.fine("Searching...... Initially search "+module_reference);
      // Nope.. Initially, assume this module

      // Is the named type present in this module?
      ti = lookupType(type_reference);

      // Is the named type a Useful type?
      if ( ( ti == null ) && ( search == true ) )
      {
        log.fine("Searching......Useful module definitions");

        ModuleInfo m = info.lookupModule("AsnUseful");
        if ( null != m )
        {
          ti =m.lookup(null,type_reference,false);
          log.fine("Searching ASN Useful types for "+type_reference+" yields: "+ti);
        }
      }

      // Is it an imported type?
      if ( ( ti == null ) && ( search == true ) )
      {
        log.fine("Still not found, scan other available modules...");

        for (Enumeration e = imported_types.keys() ; ( ( e.hasMoreElements() ) && ( ti == null ) );)
        {
            String current_module = (String)e.nextElement();

            log.fine("Checking "+current_module);

            Vector v = (Vector) (imported_types.get(current_module));
            if ( v.contains(type_reference) )
            {
              // We have located the imported type
              ti = lookup(current_module, type_reference, false);
            }
        }
      }

      // Is it a base type?
      if ( ( ti == null ) && ( search == true ) )
      {
        log.fine("Last effort... Checking builtin types");

        ModuleInfo m = info.lookupModule("Builtin");
        if ( null != m )
          ti =m.lookup(null,type_reference,false);
      }
    }

    log.fine("lookup returns : "+ti);
    return ti;
  }

  public boolean createJava()
  {
    return create_java;
  }

  public void registerImport(String module, String type_reference)
  {
    log.fine("registerImport("+module+","+type_reference+")");

    Vector types = (Vector)(imported_types.get(module));

    if ( types == null )
    {
      types = new Vector();
      imported_types.put(module, types);
    }

    types.add(type_reference);
  }

  public void setDefaultExplicitTagging(boolean default_explicit_tagging)
  {
    this.default_explicit_tagging = default_explicit_tagging;
  }

  public void setCreateJava(boolean create_java)
  {
    this.create_java = create_java;
  }

  public void addImportStatementsToClass(StringWriter os, boolean for_codec, boolean for_type)
  {
    log.fine("Processing imports");
    CodecBuilderInfo info = CodecBuilderInfo.getInfo();

    for (Enumeration e = imported_types.keys() ; ( e.hasMoreElements() );)
    {
      String current_module = (String)e.nextElement();
      Vector v = (Vector)(imported_types.get(current_module));

      log.fine("Processing imports..."+current_module);

      for (Enumeration e2 = v.elements() ; ( e2.hasMoreElements() );)
      {
        String type_reference = (String)(e2.nextElement());
        TypeInfo ti = lookup(current_module, type_reference, false);
        log.fine("Processing imports..."+current_module+" "+type_reference);


        //
        // LATER: This whole area (imports) needs revisiting and rationalising (somehow)
        //
        if ( null != ti )
        {
          if ( for_codec )
            os.write("import "+ti.getParent().getModulePackageName()+"."+ti.getCodecClassName()+";\n");

          if ( for_type )
          {
            if ( ( ti.getInternalType().startsWith("int") ) ||
                 ( ti.getInternalType().startsWith("byte") ) )
            {
              // Standard internal type... do nothing
            }
            else if ( ( ti.getInternalType().startsWith("java") ) ||
                      (  ti.getInternalType().startsWith("org.jzkit.a2j.codec.runtime") ) )
            {
              os.write("import "+ti.getInternalType()+";\n");
            }
            else
            {
              os.write("import "+ti.getParent().getModulePackageName()+"."+ti.getTypeClassName()+";\n");
            }
          }
        }
      }
    }
  }
}

