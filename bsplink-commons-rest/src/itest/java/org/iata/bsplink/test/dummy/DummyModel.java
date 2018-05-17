package org.iata.bsplink.test.dummy;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

public class DummyModel {

    @Size(min = 2, max = 2, message = "field should be 2 characters length")
    private String field1;

    @Digits(integer = 1, fraction = 0, message = "field should be an integer of 1 digit length max")
    private int field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }
}
