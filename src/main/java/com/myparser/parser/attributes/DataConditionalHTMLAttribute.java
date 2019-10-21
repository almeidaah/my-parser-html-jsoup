package com.myparser.parser.attributes;

import com.myparser.enums.DataAttributesEnum;
import com.myparser.parser.helpers.HtmlRenderHelper;
import com.myparser.script.GroovyScriptExecutor;

/**
 * The DataConditionalHTMLAttribute represents the simple logic of rendering or not an 'data-if' element
 */
public class DataConditionalHTMLAttribute extends SimpleDataAttributeHTMLStrategy{

    @Override
    protected HtmlRenderHelper getHelper(GroovyScriptExecutor groovyScriptExecutor, String valueToEval) {

        HtmlRenderHelper helper = new HtmlRenderHelper(DataAttributesEnum.DATA_IF);

        Object eval = groovyScriptExecutor.eval(valueToEval);

        //This case means that if the 'data-if' attribute is false will not be rendered
        if(eval instanceof Boolean && Boolean.FALSE.equals(eval)){
            helper.setRenderElement(Boolean.FALSE);
        }
        return helper;

    }
}
