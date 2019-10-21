package com.myparser.parser.attributes;

import com.myparser.parser.helpers.HtmlRenderHelper;
import com.myparser.script.GroovyScriptExecutor;

public interface DataAttributesStrategy {

    HtmlRenderHelper defineHelper(GroovyScriptExecutor groovyScriptExecutor, String value);

}
