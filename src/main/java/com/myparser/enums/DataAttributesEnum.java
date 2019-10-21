package com.myparser.enums;

/**
 * Represents a simple data-* attributes that can be find in an HTML5 attribute.
 */
public enum DataAttributesEnum {

    DATA_IF("data-if"),
    DATA_LOOP("data-loop-model");

    private String attributeName;

    DataAttributesEnum(String attributeName){
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

}
