# Unit tests

In 1997 no one had heard of Test Driven Development. For the vast majority of us, unit tests were short bits of throw-away code that we wrote to make sure our programs "worked".

Nowadays we would write a test that made sure that every piece of code worked as I expected it to. We would:

- Isolate our code from the operating system rather than just calling the standard timing functions
- Mock out those timing functions so that we had absolute control over the time
- Schedule commands that set boolean flags, and then we would step the time forward, watching those flags and ensuring that they went from false to true just as we changed the time to the right value
- Make sure that those tests were convenient to run for anyone else who needed to work with the code (Once we got a suite of tests to pass)
- Ensure that the tests and the code were checked in together into the same source package

The Agile and TDD movements have encouraged many developers to write automated unit tests. But many developers have missed some of the more subtle, and important, points of writing good tests.

## The Three Laws of Test-Driven Development

Everyone knows that Test-Driven Development (or TDD) asks us to write unit tests first, before we write production code. But that rule is just the tip of the iceberg.

Consider the following three laws:

1. You may not write production code until you have written a failing unit test.
2. You may not write more of a unit test than is sufficient to fail, and not compiling is failing.
3. You may not write more production code than is sufficient to pass the currently failing test.

The tests and the production code are written together, with the tests just a few seconds ahead of the production code.

If we work this way, we will write dozens of tests every day, hundreds of tests every month, and thousands of tests every year. If we work this way, those tests will cover virtually all of our production code. The sheer bulk of those tests, which can rival the size of the production code itself, can present a daunting management problem.

## Keeping Tests Clean

Having dirty tests is equivalent to, if not worse than, having no tests. The problem is that tests must change as the production code
evolves. The dirtier the tests, the harder they are to change. The more tangled the test code, the more likely it is that you will spend more time cramming new tests into the suite than it takes to write the new production code.

As you modify the production code, old tests start to fail, and the mess in the test code makes it hard to get those tests to pass again. So the
tests become viewed as an ever-increasing liability.

From release to release the cost of maintaining the test suite rise. Eventually it become the single biggest complaint among the developers.

In the end you may be forced to discard the test suite entirely. But, without a test suite you lose the ability to make sure that changes to your code base work as expected. Without a test suite you cannot ensure that changes to one part of your system does not break other parts of your system.

So your defect rate begins to rise. As the number of unintended defects rise, you start to fear making changes. You stop cleaning your production code because you fear the changes would do more harm than good. Your production code begins to rot. In the end you are left with no tests, tangled and bug-riddled production code, frustrated customers, and the feeling that your testing effort has failed you.

In a way you are right. Your testing effort has failed you. But it was your decision to allow the tests to be messy that was the seed of that failure. Had you kept your tests clean, your testing effort would not have failed.

**Test code is just as important as production code.** It is not a second-class citizen. It requires thought, design, and care. It must be kept as clean as production code.

### Tests Enable the -ilities

If you don’t keep your tests clean, you will lose them. And without them, you lose the very thing that keeps your production code flexible.

It is unit tests that keep our code flexible, maintainable, and reusable. If you have tests, you do not fear making changes to the code!

Without tests every change is a possible bug. No matter how flexible your architecture is, no matter how nicely partitioned your design, without tests you will be reluctant to make changes because of the fear that you will introduce undetected bugs.

But with tests that fear virtually disappears. The higher your test coverage, the less your fear. You can make changes with near impunity to code that has a less than stellar architecture and a tangled and opaque design. Indeed, you can improve that architecture and design without fear!

Having an automated suite of unit tests that cover the production code is the key to keeping your design and architecture as clean as possible.

Tests enable all the possibilities, feasabilities, maintainabilities, everything that finished by -ilities, because tests enable change.

So if your tests are dirty, then your ability to change your code is hampered, and you begin to lose the ability to improve the structure of that code. The dirtier your tests, the dirtier your code becomes. Eventually you lose the tests, and your code rots.

## Clean Tests

What makes a clean test? Readability. Readability is perhaps even more important in unit tests than it is in production code.

What makes tests readable? The same thing that makes all code readable: clarity, simplicity, and density of expression. In a test you want to say a lot with as few expressions as possible.

The three following tests are difficult to understand and can certainly be improved. First, there is a terrible amount of duplicate
code in the repeated calls to `addPage` and `assertSubString`. More importantly, this code is just loaded with details that interfere with the expressiveness of the test.

**bad code:**

```java
public void testGetPageHieratchyAsXml() throws Exception
{
    crawler.addPage(root, PathParser.parse("PageOne"));
    crawler.addPage(root, PathParser.parse("PageOne.ChildOne"));
    crawler.addPage(root, PathParser.parse("PageTwo"));
    request.setResource("root");
    request.addInput("type", "pages");
    Responder responder = new SerializedPageResponder();
    SimpleResponse response =
    (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request);
    String xml = response.getContent();
    assertEquals("text/xml", response.getContentType());
    assertSubString("<name>PageOne</name>", xml);
    assertSubString("<name>PageTwo</name>", xml);
    assertSubString("<name>ChildOne</name>", xml);
}

public void testGetPageHieratchyAsXmlDoesntContainSymbolicLinks() throws Exception
{
    WikiPage pageOne = crawler.addPage(root, PathParser.parse("PageOne"));
    crawler.addPage(root, PathParser.parse("PageOne.ChildOne"));
    crawler.addPage(root, PathParser.parse("PageTwo"));
    PageData data = pageOne.getData();
    WikiPageProperties properties = data.getProperties();
    WikiPageProperty symLinks = properties.set(SymbolicPage.PROPERTY_NAME);
    symLinks.set("SymPage", "PageTwo");
    pageOne.commit(data);
    request.setResource("root");
    request.addInput("type", "pages");
    Responder responder = new SerializedPageResponder();
    SimpleResponse response =
    (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request);
    String xml = response.getContent();
    assertEquals("text/xml", response.getContentType());
    assertSubString("<name>PageOne</name>", xml);
    assertSubString("<name>PageTwo</name>", xml);
    assertSubString("<name>ChildOne</name>", xml);
    assertNotSubString("SymPage", xml);
}

public void testGetDataAsHtml() throws Exception
{
    crawler.addPage(root, PathParser.parse("TestPageOne"), "test page");
    request.setResource("TestPageOne");
    request.addInput("type", "data");
    Responder responder = new SerializedPageResponder();
    SimpleResponse response = (SimpleResponse) responder.makeResponse(new FitNesseContext(root), request);
    String xml = response.getContent();
    assertEquals("text/xml", response.getContentType());
    assertSubString("test page", xml);
    assertSubString("<Test", xml);
}
```

For example, look at the `PathParser` calls. They transform strings into `PagePath` instances used by the crawlers. This transformation is completely irrelevant to the test at hand and serves only to obfuscate the intent. The details surrounding the creation of the responder and the gathering and casting of the response are also just noise. Then there’s the ham-handed way that the request URL is built from a resource and an argument.

In the end, this code was not designed to be read. The poor reader is inundated with a swarm of details that must be understood before the tests make any real sense.

Now consider the improved version. These tests do the exact same thing,
but they have been refactored into a much cleaner and more explanatory form.

**good code:**

```java
public void testGetPageHierarchyAsXml() throws Exception {
    makePages("PageOne", "PageOne.ChildOne", "PageTwo");
    submitRequest("root", "type:pages");
    assertResponseIsXML();
    assertResponseContains(
        "<name>PageOne</name>",
        "<name>PageTwo</name>",
        "<name>ChildOne</name>"
    );
}

public void testSymbolicLinksAreNotInXmlPageHierarchy() throws Exception {
    WikiPage page = makePage("PageOne");
    makePages("PageOne.ChildOne", "PageTwo");
    addLinkTo(page, "PageTwo", "SymPage");
    submitRequest("root", "type:pages");
    assertResponseIsXML();
    assertResponseContains(
        "<name>PageOne</name>",
        "<name>PageTwo</name>",
        "<name>ChildOne</name>"
    );
    assertResponseDoesNotContain("SymPage");
}

public void testGetDataAsXml() throws Exception {
    makePageWithContent("TestPageOne", "test page");
    submitRequest("TestPageOne", "type:data");
    assertResponseIsXML();
    assertResponseContains("test page", "<Test");
}
```

The **build-operate-check** pattern is made obvious by the structure of these tests. Each of the tests is clearly split into three parts:

1. The first part builds up the test data
2. The second part operates on that test data
3. The third part checks that the operation yielded the expected results.

The vast majority of annoying detail has been eliminated. The tests get
right to the point and use only the data types and functions that they truly need. Anyone who reads these tests should be able to work out what they do very quickly, without being misled or overwhelmed by details.

## Domain-Specific Testing Language

The tests in the previous example demonstrate the technique of building a domain-specific language (or DSL) for your tests. Rather than using the APIs that programmers use to manipulate the system, we build up a set of functions and utilities that make use of those APIs and that
make the tests more convenient to write and easier to read.

These functions and utilities become a specialized API used by the tests. They are a testing language that developers use to help themselves to write their tests and to help those who must read those
tests later on.

This testing API is not designed up front; rather it evolves from the continued refactoring of test code that has gotten too tainted by obfuscating detail. Disciplined developers refactor their test code into more succinct and expressive forms.

## A Dual Standard

The code within the testing API does have a different set of engineering standards than production code. It must still be simple, succinct, and expressive, but it need not be as efficient as production code. After all, it runs in a test environment, not a production environment, and
those two environment have very different needs.

**example:**

```java
@Test
public void turnOnLoTempAlarmAtThreashold() throws Exception {
    hw.setTemp(WAY_TOO_COLD);
    controller.tic();
    assertTrue(hw.heaterState());
    assertTrue(hw.blowerState());
    assertFalse(hw.coolerState());
    assertFalse(hw.hiTempAlarm());
    assertTrue(hw.loTempAlarm());
}
```

There are, of course, lots of details here. For example, what is that tic function all about? In fact, I’d rather you not worry about that while reading this test. I’d rather you just worry about whether you agree that the end state of the system is consistent with the temperature being "way too cold".

Notice, as you read the test, that your eye needs to bounce back and forth between the name of the state being checked, and the sense of the state being checked. You see `heaterState`, and then your eyes glissade left to `assertTrue`. You see `coolerState` and your
eyes must track left to `assertFalse`. This is tedious and unreliable. It makes the test hard to read.

Here is the improved version:

**good code:**

```java
@Test
public void turnOnLoTempAlarmAtThreshold() throws Exception {
    wayTooCold();
    assertEquals("HBchL", hw.getState());
}
```

Even though this is close to a violation of the rule about mental mapping, it seems appropriate in this case. Notice, once you know the meaning, your eyes glide across that string and you can quickly interpret the results. Reading the test becomes almost a
pleasure. Here is another example:

**good code:**

```java
@Test
public void turnOnCoolerAndBlowerIfTooHot() throws Exception {
    tooHot();
    assertEquals("hBChl", hw.getState());
}

@Test
public void turnOnHeaterAndBlowerIfTooCold() throws Exception {
    tooCold();
    assertEquals("HBchl", hw.getState());
}

@Test
public void turnOnHiTempAlarmAtThreshold() throws Exception {
    wayTooHot();
    assertEquals("hBCHl", hw.getState());
}

@Test
public void turnOnLoTempAlarmAtThreshold() throws Exception {
    wayTooCold();
    assertEquals("HBchL", hw.getState());
}
```

```java
public String getState() {
    String state = "";
    state += heater ? "H" : "h";
    state += blower ? "B" : "b";
    state += cooler ? "C" : "c";
    state += hiTempAlarm ? "H" : "h";
    state += loTempAlarm ? "L" : "l";
    return state;
}
```

We could have used a `StringBuffer` here. `StringBuffer`s are a bit ugly. Even in production code I would avoid them if the cost is small; and you could argue that the cost of the code in the example above is very small.

However, this application is clearly an embedded real-time system, and it is likely that computer and memory resources are very constrained.

The test environment, however, is not likely to be constrained at all.

That is the nature of the dual standard. There are things that you might never do in a production environment that are perfectly fine in a test environment.

Usually they involve issues of memory or CPU efficiency. But they never involve issues of cleanliness.

## One Assert per Test

Every test function in a test should have one and only one assert statement. This rule may seem draconian, but the tests are quick and easy to understand.

But what about testing XML output? It seems unreasonable that we could somehow easily merge the assertion that the output is XML and that it contains certain substrings.

However, we can break the test into two separate tests, each with its own particular assertion:

**good code:**

```java
public void testGetPageHierarchyAsXml() throws Exception {
    givenPages("PageOne", "PageOne.ChildOne", "PageTwo");
    whenRequestIsIssued("root", "type:pages");
    thenResponseShouldBeXML();
}

public void testGetPageHierarchyHasRightTags() throws Exception {
    givenPages("PageOne", "PageOne.ChildOne", "PageTwo");
    whenRequestIsIssued("root", "type:pages");
    thenResponseShouldContain(
        "<name>PageOne</name>",
        "<name>PageTwo</name>",
        "<name>ChildOne</name>"
    );
}
```

Notice that I have changed the names of the functions to use the common **given-when-then** convention. This makes the tests even easier to read. Unfortunately, splitting the tests as shown results in a lot of duplicate code.

We can eliminate the duplication by using the **template method** pattern and putting the given/when parts in the base class, and the then parts in different derivatives.

Or we could create a completely separate test class and put the given and when parts in the `@Before` function, and the when parts in each `@Test` function.

But this seems like too much mechanism for such a minor issue.

The single assert rule is a good guideline. But we should not be afraid to put more than one assert in a test. I think the best thing we can say is that the number of asserts in a test ought to be minimized.

## Single Concept per Test

Perhaps a better rule is that we want to test a single concept in each test function. We don’t want long test functions that go testing one miscellaneous thing after another:

**bad code:**

```java
/**
 * Miscellaneous tests for the addMonths() method.
 */
public void testAddMonths() {
    SerialDate d1 = SerialDate.createInstance(31, 5, 2004);
    SerialDate d2 = SerialDate.addMonths(1, d1);
    assertEquals(30, d2.getDayOfMonth());
    assertEquals(6, d2.getMonth());
    assertEquals(2004, d2.getYYYY());

    SerialDate d3 = SerialDate.addMonths(2, d1);
    assertEquals(31, d3.getDayOfMonth());
    assertEquals(7, d3.getMonth());
    assertEquals(2004, d3.getYYYY());

    SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
    assertEquals(30, d4.getDayOfMonth());
    assertEquals(7, d4.getMonth());
    assertEquals(2004, d4.getYYYY());
}
```

This test should be split up into three independent tests
because it tests three independent things. Merging them all together into the same function forces the reader to figure out why each section is there and what is being tested by that section.

The three test functions probably ought to be like this:

- Given the last day of a month with 31 days (like May):

  1. When you add one month, such that the last day of that month is the 30th (like June), then the date should be the 30th of that month, not the 31st.

  2. When you add two months to that date, such that the final month has 31 days, then the date should be the 31st.

- Given the last day of a month with 30 days in it (like June):
  1. When you add one month such that the last day of that month has 31 days, then the date should be the 30th, not the 31st.

Stated like this, you can see that there is a general rule hiding amidst the miscellaneous tests. When you increment the month, the date can be no greater than the last day of the month. This implies that incrementing the month on February 28th should yield March 28th. That test is missing and would be a useful test to write.

So it’s not the multiple asserts in each section of the example above that causes the problem. Rather it is the fact that there is more than one concept being tested. So probably the best rule is that you should minimize the number of asserts per concept and test just one concept per test function.

## F.I.R.S.T

Clean tests follow five other rules that form the above acronym:

1. **Fast**: Tests should be fast. They should run quickly. When tests run slow, you won’t want to run them frequently. If you don’t run them frequently, you won’t find problems early enough to fix them easily. You won’t feel as free to clean up the code. Eventually the code
   will begin to rot.

2. **Independent**: Tests should not depend on each other. One test should not set up the conditions for the next test. You should be able to run each test independently and run the tests in any order you like. When tests depend on each other, then the first one to fail causes a cascade of downstream failures, making diagnosis difficult and hiding downstream defects.

3. **Repeatable**: Tests should be repeatable in any environment. You should be able to run the tests in the production environment, in the QA environment, and on your laptop while riding home on the train without a network. If your tests aren’t repeatable in any environment, then you’ll always have an excuse for why they fail. You’ll also find yourself unable
   to run the tests when the environment isn’t available.

4. **Self-Validating**: The tests should have a boolean output. Either they pass or fail. You should not have to read through a log file to tell whether the tests pass. You should not have to manually compare two different text files to see whether the tests pass. If the tests aren’t
   self-validating, then failure can become subjective and running the tests can require a long manual evaluation.

5. **Timely**: The tests need to be written in a timely fashion. Unit tests should be written just before the production code that makes them pass. If you write tests after the production code, then you may find the production code to be hard to test. You may decide that some production code is too hard to test. You may not design the production code to be testable.

## Conclusion

We have barely scratched the surface of this topic. An entire book could be written about clean tests.

Tests are as important to the health of a project as the production code is. Perhaps they are even more important, because tests preserve and enhance the flexibility, maintainability, and reusability of the production code.

Keep your tests constantly clean. Work to make them expressive and succinct. Invent testing APIs that act as domain-specific languages that helps you write the tests.

If you let the tests rot, then your code will rot too. Keep your tests clean.
