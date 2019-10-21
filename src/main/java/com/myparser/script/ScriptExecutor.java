package com.myparser.script;

//Could be used in future as a generic executor for more languages..
public interface ScriptExecutor {

    /**
     * Run a block/sentence of code
     * @return Some result object(if exists)
     */
    Object run();

    /**
     * Evaluate an expression
     * @param exp
     * @return Some result object(if exists)
     */
    Object eval(String exp);

}
