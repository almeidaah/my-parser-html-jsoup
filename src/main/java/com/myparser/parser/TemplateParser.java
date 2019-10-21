package com.myparser.parser;

import com.myparser.enums.DataAttributesEnum;
import com.myparser.parser.attributes.DataAttributesStrategy;
import com.myparser.parser.attributes.DataConditionalHTMLAttribute;
import com.myparser.parser.attributes.DataLoopingHTMLAttribute;
import com.myparser.parser.helpers.HtmlRenderHelper;
import com.myparser.script.GroovyScriptExecutor;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * This is the main Template parser class.
 * It is responsbile for receiving the html to be parsed.
 * - First, it starts extracting the script tag from the html(I'm considering just one script tag for now) and running the respective Groovy/Other language script
 * - Then it iterates over the head tag and evaluates the content inside of it(also using GroovyScriptExecutor methods)
 * - When it enters inside the body, we also check for the presence of data-attributes, ie data-if and uses an DataAttributeStrategy
 * to decide what to do(e.g DataConditional and DataLooping). This class make use of HtmlRenderHelper to decide if the element will be
 * rendered or not and checks for the presence of an auxObj eg: list of models.
 */
public class TemplateParser {

    Document templateRaw;
    GroovyScriptExecutor groovyScriptExecutor;
    Pattern expressionPattern;

    final String EXPRESSION_REGEX = "\\$\\{(\\w+\\.\\w+)}?";

    public TemplateParser(Document template){
        this.templateRaw = template;
        groovyScriptExecutor = new GroovyScriptExecutor();
        expressionPattern = Pattern.compile(EXPRESSION_REGEX);
    }

    public Document getTemplateRaw() {
        return templateRaw;
    }

    /**
     * this method is responsible for start template extraction and Groovy script execution.
     * @param httpServletRequest to evaluate groovy script(this parameter will be passed as parameter to script).
     */
    public void parse(HttpServletRequest httpServletRequest) {
        try {
            extractScriptAndRun(httpServletRequest);
            parseElements("head");
            parseElements("body");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Extract from the template html the script section and execute it.
     * The groovyscriptexecutor tries to represent the 'car' variable state optional item of the test.
     * It will carry the state of execution while parsing the html.
     * @see GroovyScriptExecutor
     * @param httpServletRequest
     * @return
     */
    private Object extractScriptAndRun(HttpServletRequest httpServletRequest) {
        Elements scriptEl = this.templateRaw.select("script");

        //In this case I'm assuming that will be one 'script' tag only.
        Element scriptElement = scriptEl.get(0);

        String scriptText = scriptElement.childNodes().get(0).toString().trim();

        groovyScriptExecutor.parseScript(scriptText);
        groovyScriptExecutor.bind("request", httpServletRequest);

        this.removeElement(scriptElement);

        return groovyScriptExecutor.run();
    }

    /**
     * this method receive the element to be selected and appply the parser rules(data-attributes and values bindings/replacements)
     * Used for head and body tags.
     * @param domElement
     */
    private void parseElements(String domElement) {
        Elements selectedElements = this.templateRaw.select(domElement);
        Element element = selectedElements.get(0);

        List<Node> nodeList = element.childNodes()
                .stream()
                .filter(node -> !node.toString().isEmpty())
                .collect(toList());

        for(Node node : nodeList) {
            checkEvalExpressions(node);
        }
    }

    /**
     * This method is responsible for iterating over an Element/Attribute and rendering it.
     * It receives an item as parameter(could be an Element or an Attribute). In attribute cases it is called in a recursive way
     * @param item
     */
    private void checkEvalExpressions(Cloneable item) {

        Matcher matcher = expressionPattern.matcher(item.toString());

        if(item instanceof Element) {
            ((Element)item).attributes().forEach(attribute -> {

                HtmlRenderHelper renderHelper = checkDataAttributes(attribute);
                if(renderHelper.isRenderElement() && Objects.nonNull(renderHelper.getDataAttribute())){
                    switch (renderHelper.getDataAttribute()){
                        case DATA_IF:
                            //Nothing to do here. The 'matchAndFillHTMLElement' will handle the data-if rendering
                            break;
                        case DATA_LOOP:
                            renderLoop(item, renderHelper.getAuxObj());
                            return;
                        //...
                    }
                }

                // When data-attribute is false(car.ecoFriendly is false or models == 0), then the element should not be rendered.
                if(!renderHelper.isRenderElement()){
                    this.removeElement((Element) item);
                    return;
                }

                //this recursive call is here to continue processing the element eg: <h1 title="Mercedes-AMG">${car.brand}</h1>
                checkEvalExpressions(attribute);
            });
        }

        while (matcher.find()) {
            matchAndFillHTMLElement(item, matcher);
        }
    }

    /**
     * For Attribute Elements, will iterate over possible 'data-*' attributes and perform the logic
     * according to the DataAttributeType. the DataAttributeStrategy is responsible to define which 'render' will be chosen.
     * @param attribute
     */
    private HtmlRenderHelper checkDataAttributes(Attribute attribute) {
        Optional<DataAttributesEnum> dataAttribute = Arrays.stream(DataAttributesEnum.values())
                                                        .filter(dataAttributesEnum -> dataAttributesEnum.getAttributeName().equals(attribute.getKey()))
                                                        .findFirst();
        DataAttributesStrategy strategy = null;
        HtmlRenderHelper renderHelper = new HtmlRenderHelper(null);

        if(dataAttribute.isPresent()){
            switch (dataAttribute.get()){
                case DATA_IF:
                    strategy = new DataConditionalHTMLAttribute();
                    break;
                case DATA_LOOP:
                    strategy = new DataLoopingHTMLAttribute();
            }
            renderHelper = strategy.defineHelper(groovyScriptExecutor, attribute.getValue());
        }

        return renderHelper;
    }

    /**
     * This method is reponsible for evaluating the expression and replace the value inside the element.
     * @param item
     * @param matcher
     */
    private void matchAndFillHTMLElement(Cloneable item, Matcher matcher) {
        String rawKey = matcher.group(1);

        Object eval = groovyScriptExecutor.eval(rawKey);
        String value = eval.toString();

        if(item instanceof Element){
            ((Element)item).empty().appendText(value);
        }else{
            ((Attribute)item).setValue(value);
        }
    }

    /**
     * Renders the car models loop. First clean the text the element(empty()) and creates the div for each model.
     * @param item
     * @param auxObj
     */
    private void renderLoop(Cloneable item, Object auxObj) {

        ((Element) item).empty();
        List<String> models = (List<String>) auxObj;
        models.forEach(model -> {
            Element el = new Element(Tag.valueOf("div"), "", null);
            el.text("Model : " + model);
            ((Element) item).appendChild(el);
        });
    }

    /**
     * Remove an element from the rendered html
     */
    private void removeElement(Element e){
        e.remove();
    }

}
