package com.k_int.a2g.base;


/**
 * 
 */
public class Tag {
  public int tag_class
  public int tag_value

  public Tag(int tag_class, int tag_value) {
    this.tag_class = tag_class;
    this.tag_value = tag_value;
  }

  public String toString() {
    return "${tag_class} [${tag_value}]"
  }
}
