package com.myparser.parser.helpers;

import com.myparser.enums.DataAttributesEnum;

/**
 * This class is responsible to control if the element will be rendered or not.
 * It also have an 'auxObj' to act as a VO, sending attributes/values when needed
 */
public class HtmlRenderHelper {

    /**
     * this flag is responsbile to control the exibition of an component
     * eg if car.ecoFriendly = false then renderElement = false
     */
    private boolean renderElement;

    /**
     * This auxObj represents the support obj for the render(ie car.models list)
     */
    private Object auxObj;

    /**
     * This String represents the DataAttribute type.
     * It will be used to render regarding of type of data-* attribute
     */
    private DataAttributesEnum dataAttribute;

    public HtmlRenderHelper(DataAttributesEnum dataAttribute){
        this.setRenderElement(true);
        this.dataAttribute = dataAttribute;
    }

    public boolean isRenderElement() {
        return renderElement;
    }

    public void setRenderElement(boolean renderElement) {
        this.renderElement = renderElement;
    }

    public Object getAuxObj() {
        return auxObj;
    }

    public void setAuxObj(Object auxObj) {
        this.auxObj = auxObj;
    }

    public DataAttributesEnum getDataAttribute() {
        return dataAttribute;
    }
}
