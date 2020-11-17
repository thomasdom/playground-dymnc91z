# Formatting

When people look under the hood of your project, you want them to be impressed with the neatness, consistency, and attention to detail that they perceive. You want them to perceive that professionals have been at work.

You should take care that your code is nicely formatted. You should choose a set of simple rules that govern the format of your code, and then you should consistently apply those rules.

If you are working on a team, then the team should agree to a single set of formatting rules and all members should comply. It helps to have an automated tool that can apply those formatting rules for you.

## The Purpose of Formatting

Code formatting is important. Code formatting is about communication, and communication is the professional developer’s first order of business.

The functionality that you create today has a good chance of changing in the next release, but the readability of your code will have a profound effect on all the changes that will ever be made.

The coding style and readability set precedents that continue to affect maintainability and extensibility long after the original code has been changed beyond recognition. Your style and discipline survives, even though your code does not.

So what are the formatting issues that help us to communicate best?

## Vertical Formatting

How big are most Java source files? It turns out that there is a huge range of sizes and some remarkable differences in style.

It appears to be possible to build significant systems out of files that are typically 200 lines long, with an upper limit of 500.

Although this should not be a hard and fast rule, it should be considered very desirable. Small files are usually easier to understand than large files are.

### The Newspaper Metaphor

We would like a source file to be like a newspaper article:

- The name should be simple but explanatory.
- The topmost parts of the source file should provide the high-level concepts and algorithms.
- Detail should increase as we move downward, until at the end we find the lowest level functions and details in the source file.
- A newspaper is composed of many articles; most are very small. Some are a bit larger.

Very few contain as much text as a page can hold. This makes the newspaper usable. If the newspaper were just one long story containing a disorganized agglomeration of facts, dates, and names, then we simply would not read it.

### Vertical Openness Between Concepts

Nearly all code is read left to right and top to bottom. Each line represents an expression or a clause, and each group of lines represents a complete thought. Those thoughts should be separated from each other with blank lines.

**good code:**

```java
package fitnesse.wikitext.widgets;

import java.util.regex.*;

public class BoldWidget extends ParentWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''",
        Pattern.MULTILINE + Pattern.DOTALL
    );

    public BoldWidget(ParentWidget parent, String text) throws Exception {
        super(parent);
        Matcher match = pattern.matcher(text);
        match.find();
        addChildWidgets(match.group(1));
    }

    public String render() throws Exception {
        StringBuffer html = new StringBuffer("<b>");
        html.append(childHtml()).append("</b>");
        return html.toString();
    }
}
```

**bad code:**

```java
package fitnesse.wikitext.widgets;
import java.util.regex.*;
public class BoldWidget extends ParentWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''",
    Pattern.MULTILINE + Pattern.DOTALL);
    public BoldWidget(ParentWidget parent, String text) throws Exception {
        super(parent);
        Matcher match = pattern.matcher(text);
        match.find();
        addChildWidgets(match.group(1));}
    public String render() throws Exception {
        StringBuffer html = new StringBuffer("<b>");
        html.append(childHtml()).append("</b>");
        return html.toString();
    }
}
```

### Vertical Density

If openness separates concepts, then vertical density implies close association. So lines of code that are tightly related should appear vertically dense.

**bad code:**

```java
public class ReporterConfig {
    /**
     * The class name of the reporter listener
     */
    private String m_className;

    /**
     * The properties of the reporter listener
     */
    private List<Property> m_properties = new ArrayList<Property>();

    public void addProperty(Property property) {
        m_properties.add(property);
    }
}
```

**good code:**

```java
public class ReporterConfig {
    private String m_className;
    private List<Property> m_properties = new ArrayList<Property>();

    public void addProperty(Property property) {
        m_properties.add(property);
    }
}
```

### Vertical Distance

Concepts that are closely related should be kept vertically close to each other.

Clearly this rule doesn’t work for concepts that belong in separate files. But then closely related concepts should not be separated into different files unless you have a very good reason.

For those concepts that are so closely related that they belong in the same source file, their vertical separation should be a measure of how important each is to the understandability of the other. We want to avoid forcing our readers to hop around through our source files and classes.

#### Variable Declarations

Variables should be declared as close to their usage as possible. Because our functions are very short, local variables should appear a the top of each function.

**good code:**

```java
private static void readPreferences() {
    InputStream is = null;
    try {
        is = new FileInputStream(getPreferencesFile());
        setPreferences(new Properties(getPreferences()));
        getPreferences().load(is);
    } catch (IOException e) {
        try {
            if (is != null)
            is.close();
        } catch (IOException e1) {
        }
    }
}
```

Control variables for loops should usually be declared within the loop statement.

```java
public int countTestCases() {
    int count = 0;
    for (Test each : tests)
        count += each.countTestCases();
    return count;
}
```

In rare cases a variable might be declared at the top of a block or just before a loop in a long-ish function.

```java
// ...
for (XmlTest test : m_suite.getTests()) {
    TestRunner tr = m_runnerFactory.newTestRunner(this, test);
    tr.addListener(m_textReporter);
    m_testRunners.add(tr);

    invoker = tr.getInvoker();

    for (ITestNGMethod m : tr.getBeforeSuiteMethods()) {
        beforeSuiteMethods.put(m.getMethod(), m);
    }

    for (ITestNGMethod m : tr.getAfterSuiteMethods()) {
        afterSuiteMethods.put(m.getMethod(), m);
    }
}
// ...
```

#### Instance variables

Instance variables, on the other hand, should be declared at the top of the class. This should not increase the vertical distance of these variables, because in a well-designed class, they are used by many, if not all, of the methods of the class.

The common convention in Java is to put them all at the top of the class. Everybody should know where to go to see the declarations.

**bad code:**

```java
public class TestSuite implements Test {
    static public Test createTest(Class<? extends TestCase> theClass,
    String name) {
        // ...
    }

    public static Constructor<? extends TestCase>
    getTestConstructor(Class<? extends TestCase> theClass)
    throws NoSuchMethodException {
        // ...
    }

    public static Test warning(final String message) {
        // ...
    }

    private static String exceptionToString(Throwable t) {
        // ...
    }

    private String fName;
    private Vector<Test> fTests= new Vector<Test>(10);

    public TestSuite() {
    }

    public TestSuite(final Class<? extends TestCase> theClass) {
        // ...
    }

    public TestSuite(Class<? extends TestCase> theClass, String name) {
        // ...
    }
    // ...
}
```

#### Dependent Functions

If one function calls another, they should be vertically close, and the caller should be above the callee, if at all possible. This gives the program a natural flow. If the convention is followed reliably, readers will be able to trust that function definitions will follow shortly after their use.

**good code:**

```java
public class WikiPageResponder implements SecureResponder {
    protected WikiPage page;
    protected PageData pageData;
    protected String pageTitle;
    protected Request request;
    protected PageCrawler crawler;

    public Response makeResponse(FitNesseContext context, Request request)
        throws Exception {
        String pageName = getPageNameOrDefault(request, "FrontPage");
        loadPage(pageName, context);
        if (page == null)
            return notFoundResponse(context, request);
        else
            return makePageResponse(context);
    }

    private String getPageNameOrDefault(Request request, String defaultPageName) {
        String pageName = request.getResource();
        if (StringUtil.isBlank(pageName))
            pageName = defaultPageName;

        return pageName;
    }

    protected void loadPage(String resource, FitNesseContext context)
        throws Exception {
        WikiPagePath path = PathParser.parse(resource);
        crawler = context.root.getPageCrawler();
        crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
        page = crawler.getPage(context.root, path);
        if (page != null)
            pageData = page.getData();
    }

    private Response notFoundResponse(FitNesseContext context, Request request)
        throws Exception {
        return new NotFoundResponder().makeResponse(context, request);
    }

    private SimpleResponse makePageResponse(FitNesseContext context)
        throws Exception {
        pageTitle = PathParser.render(crawler.getFullPath(page));
        String html = makeHtml(context);
        SimpleResponse response = new SimpleResponse();
        response.setMaxAge(0);
        response.setContent(html);

        return response;
    }
    // ...
}
```

#### Conceptual Affinity

Certain pieces of code want to be near other pieces. They have a certain conceptual affinity. The stronger that affinity, the
less vertical distance there should be between them.

Affinity might be caused because a group of functions per-
form a similar operation.

**good code:**

```java
public class Assert {
    static public void assertTrue(String message, boolean condition) {
        if (!condition)
            fail(message);
    }

    static public void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }

    static public void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    static public void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }

    // ...
}
```

These functions have a strong conceptual affinity because they share a common naming scheme and perform variations of the same basic task. The fact that they call each other is secondary. Even if they didn’t, they would still want to be close together.

### Vertical Ordering

In general we want function call dependencies to point in the downward direction. A function that is called should be below a function that does the calling (Except in some languages like C or C++ that enforce functions to be declared before they are used).

This creates a nice flow down the source code module from high level to low level. We expect the most important concepts to come first, and we expect them to be expressed with the least amount of polluting detail. We expect the low-level details to come last.

## Horizontal Formatting

How wide should a line be? Programmers clearly prefer short lines. We should strive to keep our lines short. The old limit of
80 is a bit arbitrary, so you can write lines edging out to 100 or even 120, but not more.

You should never have to scroll to the right. Monitors
are too wide for that nowadays, and programmers can shrink the font so small that they can get 200 characters across the screen. Don’t do that. I personally set my limit at 120.

### Horizontal Openness and Density

We use horizontal white space to associate things that are strongly related and disassociate things that are more weakly related.

**example:**

```java
private void measureLine(String line) {
    lineCount++;
    int lineSize = line.length();
    totalChars += lineSize;
    lineWidthHistogram.addLine(lineSize, lineCount);
    recordWidestLine(lineSize);
}
```

The assignment operators are surrounded with white space to accentuate them. Assignment statements have two distinct and major elements: the left side and the right side. The spaces make that separation obvious.

There are no spaces between the function names and the opening
parenthesis. This is because the function and its arguments are closely related. Separating them makes them appear disjoined instead of conjoined. Separate arguments within the function call parenthesis to accentuate the comma and show that the arguments are separate.

Another use for white space is to accentuate the precedence of operators.

**example:**

```java
public class Quadratic {
    public static double root1(double a, double b, double c) {
        double determinant = determinant(a, b, c);
        return (-b + Math.sqrt(determinant)) / (2*a);
    }
    public static double root2(int a, int b, int c) {
        double determinant = determinant(a, b, c);
        return (-b - Math.sqrt(determinant)) / (2*a);
    }
    private static double determinant(double a, double b, double c) {
        return b*b - 4*a*c;
    }
}
```

Notice how nicely the equations read. The factors have no white space between them because they are high precedence. The terms are separated by white space because addition and subtraction are lower precedence.

Unfortunately, most tools for reformatting code are blind to the precedence of operators and impose the same spacing throughout. So subtle spacings like those shown above tend to get lost after you reformat the code.

### Horizontal Alignment

This kind of alignment is not useful. The horizontal alignment seems to emphasize the wrong things and leads the eye away from the true intent.

**bad code:**

```java
public class FitNesseExpediter implements ResponseSender
{
    private     Socket          socket;
    private     InputStream     input;
    private     OutputStream    output;
    private     Request         request;
    private     Response        response;
    private     FitNesseContext context;
    protected   long            requestParsingTimeLimit;
    private     long            requestProgress;
    private     long            requestParsingDeadline;
    private     boolean         hasError;

    public FitNesseExpediter(Socket             s,
                             FitNesseContext    context) throws Exception
    {
        this.context =              context;
        socket =                    s;
        input =                     s.getInputStream();
        output =                    s.getOutputStream();
        requestParsingTimeLimit =   10000;
    }

    // ...
}
```

For example, in the list of declarations above you are tempted to read down the list of variable names without looking at their types. Likewise, in the list of assignment statements you are tempted to look down the list of rvalues without ever seeing the assignment operator. To make matters worse, automatic reformatting tools usually eliminate this kind of alignment.

Nowadays it is better to use unaligned declarations and assignments, as shown below, because they point out an important deficiency: if there are long lists of variable declarations that need to be aligned, the problem is the length of the lists, not the lack of alignment.

The length of the list of declarations in FitNesseExpediter below suggests that this class should be split up.

**well-formatted but still bad code:**

```java
public class FitNesseExpediter implements ResponseSender
{
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private Request request;
    private Response response;
    private FitNesseContext context;
    protected long requestParsingTimeLimit;
    private long requestProgress;
    private long requestParsingDeadline;
    private boolean hasError;

    public FitNesseExpediter(Socket s, FitNesseContext context)
        throws Exception {
        this.context = context;
        socket = s;
        input = s.getInputStream();
        output = s.getOutputStream();
        requestParsingTimeLimit = 10000;
    }

    // ...
}
```

### Indentation

A source file is a hierarchy rather like an outline.

Each level of this hierarchy is a scope into which names can be declared and in which declarations and executable statements are interpreted.

To make this hierarchy of scopes visible, we indent the lines of source code in proportion to their position in the hiearchy.

Statements at the level of the file, such as most class declarations, are not indented at all. Methods within a class are indented one level to the right of the class. Implementations of those methods are implemented one level to the right of the method declaration. Block implementations are implemented one level
to the right of their containing block, and so on.

Programmers rely heavily on this indentation scheme. They visually line up lines on the left to see what scope they appear in. This allows them to quickly hop over scopes, such as implementations of if or while statements, that are not relevant to their current
situation. They scan the left for new method declarations, new variables, and even new classes. Without indentation, programs would be virtually unreadable by humans.

**bad code:**

```java
public class FitNesseServer implements SocketServer {
private FitNesseContext context;
public FitNesseServer(FitNesseContext context) {
this.context = context; }
public void serve(Socket s) { serve(s, 10000); }
public void serve(Socket s, long requestTimeout) {
try { FitNesseExpediter sender = new FitNesseExpediter(s, context);
sender.setRequestParsingTimeLimit(requestTimeout);
sender.start(); }
catch(Exception e) { e.printStackTrace(); } } }
```

**good code:**

```java
public class FitNesseServer implements SocketServer {
    private FitNesseContext context;

    public FitNesseServer(FitNesseContext context) {
        this.context = context;
    }

    public void serve(Socket s) {
        serve(s, 10000);
    }

    public void serve(Socket s, long requestTimeout) {
        try {
            FitNesseExpediter sender = new FitNesseExpediter(s, context);
            sender.setRequestParsingTimeLimit(requestTimeout);
            sender.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Your eye can rapidly discern the structure of the indented file. You can almost instantly spot the variables, constructors, accessors, and methods.

The unindented version, however, is virtually impenetrable without intense study.

### Breaking Indentation

It is sometimes tempting to break the indentation rule for short
if statements, short while loops, or short functions.

Avoid collapsing scopes down to one line like this:

**bad code:**

```java
public class CommentWidget extends TextWidget
{
    public static final String REGEXP = "^#[^\r\n]_(?:(?:\r\n)|\n|\r)?";

    public CommentWidget(ParentWidget parent, String text) {super(parent, text);}

    public String render() throws Exception {return "";}
}
```

Prefer expanding and indenting the scopes instead:

**good code:**

```java
public class CommentWidget extends TextWidget {
    public static final String REGEXP = "^#[^\r\n]_(?:(?:\r\n)|\n|\r)?";

    public CommentWidget(ParentWidget parent, String text) {
        super(parent, text);
    }

    public String render() throws Exception {
        return "";
    }
}
```

### Dummy Scopes

Sometimes the body of a while or for statement is a dummy, as shown below. Try to avoid these kind of structures. When you can’t avoid them, make sure that the dummy body is properly indented and surrounded by braces.

**bad code:**

```java
while (dis.read(buf, 0, readBufferSize) != -1)
    ;
```

## Team Rules

Every programmer has his own favorite formatting rules, but if he works in a team, then the team rules.

A team of developers should agree upon a single formatting style, and then every member of that team should use that style. We want the software to have a consistent style. We don’t want it to appear to have been written by a bunch of disagreeing individuals.

A good software system is composed of a set of documents that read nicely. They need to have a consistent and smooth style. The reader needs to be able to trust that the formatting gestures he or she has seen in one source file will mean the same thing in others. The last thing we want to do is add more complexity to the source code by
writing it in different individual styles.
