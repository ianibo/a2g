package com.k_int.a2g.base;


/**
 * 
 */
public class TagAndLength {
  public boolean is_constructed
  public int tag_class
  public int tag_value
  public long length
  public boolean is_indefinite_length=false

  public String toString() {
    return "${tag_class} [${tag_value}] cons=${is_constructed} len=${is_indefinite_length?'indefinite':length}"
  }
}
