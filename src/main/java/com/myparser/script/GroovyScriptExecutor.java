package com.myparser.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * Class responsbile for defining the methods of script executions/bindings of Groovy
 * This way I'm able to hide the 'groovy' details from the parser.
 * I'm also could create generic interface in future to represent a generic executor
 * (i.e to support other languages)
 * I could also create a builder to create a script executor in a more fluent way(fluent API concepts)
 */
public class GroovyScriptExecutor implements ScriptExecutor{

    Binding binding = new Binding();
    Script groovyScript;

    public void parseScript(String scriptText){
        GroovyShell shell = new GroovyShell();
        this.groovyScript = shell.parse(scriptText);
    }

    public void bind(String name, Object value){
        binding.setProperty(name, value);
    }

    @Override
    public Object run(){
        groovyScript.setBinding(binding);
        return groovyScript.run();
    }

    @Override
    public Object eval(String exp){
        return groovyScript.evaluate(exp);
    }


}
