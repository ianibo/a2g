package org.jzkit.a2j.codec.comp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.io.File;
import java.io.FileInputStream;

public class A2JAntTask extends Task
{
  private String base_package = null;
  private String input_asn = null;
  private String output_dir = ".";

  public A2JAntTask()
  {
  }

  public void setBasePackage(String base_package) {
    this.base_package = base_package;
  }

  public String getBasePackage() {
    return base_package;
  }

  public void setInputASNFile(String input_asn) {
    this.input_asn = input_asn;
  }

  public String getInputASNFile() {
    return input_asn;
  }

  public void setOutput(String output_dir) {
    this.output_dir = output_dir;
  }

  public String getOutput() {
    return output_dir;
  }

  public void execute() throws BuildException
  {
    System.err.println("Processing asn source file : "+input_asn);
    System.err.println("Make sure the output package exists : "+output_dir);

    // We might need to create a directory
    File base_dir = new File(output_dir);
    if ( !base_dir.exists() )
      base_dir.mkdirs();

    File next_asn_file = new File(input_asn);
                                                                                                                                        
    if ( next_asn_file.exists() )
    {
      // parser = new AsnParser(System.in);
      try
      {
        AsnParser parser = new AsnParser(new FileInputStream(next_asn_file));
        parser.setPackageName(base_package);
        parser.setOutputDir(output_dir);
        parser.Input();
        System.out.println("ASN.1 file "+next_asn_file+" parsed successfully... Calling pass1");
        parser.jjtree.rootNode().pass1();
      }
      catch(ParseException e)
      {
        System.out.println(e.toString());
        e.printStackTrace();
      }
      catch(java.io.FileNotFoundException fnfe)
      {
        System.out.println(fnfe.toString());
        fnfe.printStackTrace();
      }
    }
                                                                                                                                        
    // Generate codecs for all processed types
    CodecBuilderInfo.getInfo().create();
  }
}
