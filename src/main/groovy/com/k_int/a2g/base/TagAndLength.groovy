package com.k_int.a2g.base;


/**
 * 
 */
public class TagAndLength {
  public boolean is_constructed
  public int tag_class
  public int tag_value
  public long length

  public String toString() {
    return "${tag_class} [${tag_value}] cons=${is_constructed} len=${length}"
  }
}
