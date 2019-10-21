 #####Script parser test github.com/almeidaah - twitter.com/almeidajava #####

I've Used JDK8 to develop and Jetty with jdk8
Also, jersey for tests should work with jdk8 as well, If use jdk9+ can be some xmlbind exceptions, but the tests should run anyway.

Considerations:

Major part of documentation is on the java classes. Minor details are here:

- In pom.xml, I've added JSOUP in dependencies, which is a java-html-xml parser opensource. I've used it to help parsing the template.
- Also in the file above, I added groovy-all dependency, which allows me to run GroovyShell scripts inside java classes.
- To tests, I've added JerseyRest to test REST endpoint and jetty embedded with Jersey

- I didn't take much care about the detailed exceptions because I prefer to focus on time and what was proposed to do.

Improvements:

- Consider 1...N tags of <script> in the code.
- I've left in TemplateParser.java the code related to render of data-loop and the 'switch' of data-attributes turn easier to check the code. As we have more data-attribute types we can extract the switch and the 'render' logic from the parser.
- Avoid lots of instanceof between Item/Element. I've tried to use Cloneable(Common interface) as much as possible.
- The strategy classes could be hidden from TemplateParser, we can move it to HTMLRenderParser.

