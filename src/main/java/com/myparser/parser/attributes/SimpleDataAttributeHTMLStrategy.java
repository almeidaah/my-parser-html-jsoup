package com.myparser.parser.attributes;


import com.myparser.parser.helpers.HtmlRenderHelper;
import com.myparser.script.GroovyScriptExecutor;

abstract class SimpleDataAttributeHTMLStrategy implements DataAttributesStrategy {

    abstract HtmlRenderHelper getHelper(GroovyScriptExecutor groovyScriptExecutor, String valueToEval);

    @Override
    public HtmlRenderHelper defineHelper(GroovyScriptExecutor groovyScriptExecutor, String valueToEval) {
        return getHelper(groovyScriptExecutor, valueToEval);
    }

}
