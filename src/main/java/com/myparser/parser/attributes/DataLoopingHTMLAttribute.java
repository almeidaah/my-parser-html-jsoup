package com.myparser.parser.attributes;

import com.myparser.enums.DataAttributesEnum;
import com.myparser.parser.helpers.HtmlRenderHelper;
import com.myparser.script.GroovyScriptExecutor;

import java.util.List;

/**
 * The DataLoopingHTMLAttribute represents the simple logic of rendering or not an 'data-loop-model' element
 */
public class DataLoopingHTMLAttribute extends SimpleDataAttributeHTMLStrategy{

    @Override
    protected HtmlRenderHelper getHelper(GroovyScriptExecutor groovyScriptExecutor, String valueToEval) {

        HtmlRenderHelper helper = new HtmlRenderHelper(DataAttributesEnum.DATA_LOOP);
        Object eval = groovyScriptExecutor.eval(valueToEval);

        List<String> models = (List<String>) eval;

        if(models.isEmpty()){
            helper.setRenderElement(Boolean.FALSE);
        }else{
            helper.setAuxObj(models);
        }

        return helper;
    }




}
