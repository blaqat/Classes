package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class RowTest {
  @Test
  public void initialRow(){
    Row row = new Row(4);
    assertEquals(4,row.getIndex());
  }


}
