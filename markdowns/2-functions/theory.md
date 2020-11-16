# Functions

## The smaller the better

The first rule of functions is that they should be small. **Do not write a function longer than ~30 lines**. If you write a function that has more than 30 lines, then your function is performing too much tasks and should be **refactored**.

## Do one thing

> FUNCTIONS SHOULD DO ONE THING. THEY SHOULD DO IT WELL. THEY SHOULD DO IT ONLY.
>
> _Robert C. Martin_

If a function does only **steps that are one level below the stated name** of the function, then the function is doing one thing.

**Example:**

```java
public static String renderPageWithSetupsAndTeardowns(
    Page page, boolean isSuite) throws Exception {
    if (isTestPage(page)) {
        includeSetupAndTeardownPages(page, isSuite);
    }

    return page.toHtml();
}
```

## One level of abstraction per function

In order to make sure our functions are doing "one thing", we need to make sure that the
statements within our function are all at the same level of abstraction.

This means that the reader must be able to read the function as if it were a set of TO paragraphs.

Example:

- TO count the words in a file, we extract the content of the file as text, and we count the number of words in this text
  - TO extract the content of the file as text, we check that the file is valid, we read the file contents and we return a string representing the read contents
    - TO check that the file is valid, we ensure that the file exists, we check if the file is readable, and we ensure that the content type of the file is equal to "text/plain"
    - TO read the file, we perform the system call that reads every line of the file
  - TO count the number of words in a text, we need to split the text in an array of words, then we count the number of words in the array

A possible implementation of this function would be:

**bad code:**

```java
public static int countWordsInFile(String filePath) throws Exception {
    Path path = Paths.get(filePath);

    // Check if file is valid
    if (!Files.exists(path)
        || !Files.isReadable(path)
        || !Files.probeContentType(path) == "text/plain";) {
        throw new Exception('Invalid file!');
    }

    // Read all lines in file
    Stream<String> lines = Files.lines(path);
    String fileContent = lines.collect(Collectors.joining(" "));
    lines.close();

    // Split the text in words
    List<String> words = text.split(" ");
    return words.length;
}
```

Can you see the problem? **This function does too much things**, it has **too much responsibilities**. How can you test entirely this single function without risking potential flaws/bugs? How can you assert that this function works in every case?

By splitting this function into different functions by following the rule listed above, we obtain:

**good code:**

```java
public static int countWordsInFile(String filePath) throws Exception {
    String text = extractFileContents(filePath);
    return countNumberOfWords(text);
}

public static String extractFileContents(String filePath) {
    Path path = Paths.get(filePath);
    if (!isValidTextFile(path)) {
        throw new Exception('Invalid file!');
    }

    return readFileContents(path);
}

public static boolean isValidTextFile(Path filePath) {
    return Files.exists(path)
        && Files.isReadable(path)
        && Files.probeContentType(path) == "text/plain";
}

public static String readFileContents(Path filePath) {
    Stream<String> lines = Files.lines(path);
    String fileContent = lines.collect(Collectors.joining(" "));
    lines.close();

    return fileContent;
}

public static int countNumberOfWords(String text) {
    List<String> words = text.split(" ");
    return words.length;
}
```

The code is a bit longer, I admit it. But **each function does only one thing, and does it well**.

Moreover, **each function is independently testable**. You can avoid bugs and flows by testing each function independently rather than writing a gigantic number of test cases for a single function, trying to cover all cases.

### The `switch` statements

Switch statements can be tolerated if they appear only once, are used to create polymorphic objects, and are hidden behind an inheritance relationship so that the rest of the system cannot see them.

Sometimes there can be exceptions about this rule, but generally speaking, there is always a design pattern that allows you not to use switch statements, [which are considered harmful](https://wiki.c2.com/?CaseStatementsConsideredHarmful).

## Use descriptive names

The smaller and more focused a function is, the easier it is to choose a descriptive name.

### Best practices

- Don’t be afraid to make a name long. A long descriptive name is better than a short enigmatic name. A long descriptive name is better than a long descriptive comment.
- Use a naming convention that allows multiple words to be easily read in the function names, and then make use of those multiple words to give the function a name that says what it does.
- Don’t be afraid to spend time choosing a name. Use an IDE to rename functions and experiment with different names until you find one that is as descriptive as you can make it.
- Choosing descriptive names will clarify the design of the module in your mind and help you to improve it.
- Hunting for a good name results in a favorable restructuring of the code.
- Be consistent in your names.

## Fucntion arguments

The ideal number of arguments for a function is zero (_niladic_). Next comes one (_monadic_), followed closely by two (_dyadic_). Three arguments (_triadic_) should be avoided where possible. More than three (_polyadic_) requires very special justification—and then shouldn’t be used anyway.

When a function seems to need more than two or three arguments, it is likely that some of those arguments should be wrapped into a class of their own.

Example:

```java
Circle makeCircle(double x, double y, double radius);
```

can be refactored to:

```java
Circle makeCircle(Point center, double radius);
```

## Have no side effects

When side effects are present, your function promises to do one thing, but it also does other hidden things. Sometimes it will make unexpected changes to the variables of its own class. Sometimes it will change system globals. In either case they are devious and damaging mistruths that often result in strange temporal couplings and order dependencies.

## Command Query Separation

Functions should either do something or answer to a query, but not both.

Either your function should change the state of an object, or it should return some information about that object. Doing both often leads to confusion.

**bad code:**

```java
public boolean set(String attribute, String value);
```

This function sets the value of a named attribute and returns `true` if it is successful and `false` if no such attribute exists. This leads to odd statements like this:

**bad code:**

```java
if (set("username", "unclebob")) {
    // ...
}
```

What does the returned boolean mean? Is it asking whether the `username` attribute was previously set to `unclebob`? Or is it asking whether the `username` attribute was successfully set to `unclebob`? It’s hard to infer the meaning from the call because it’s not clear whether the word "set" is a verb or an adjective.

We could try to resolve this by renaming the set function to `setAndCheckIfExists`, but that doesn’t much help the readability of the `if` statement. The real solution is to separate the command from the query so that the ambiguity cannot occur.

**good code:**

```java
if (attributeExists("username")) {
    setAttribute("username", "unclebob");
    // ...
}
```

## Prefer handling exceptions over returning error codes

When you return an error code, you create the problem that the caller must deal with the error immediately.

On the other hand, if you use exceptions instead of returned error codes, then the error processing code can be separated from the happy path code and can be simplified.

### Best practices

- Extract the bodies of the try and catch blocks out into functions of their own.
- A function that handles errors should not do anything else. This implies that if the keyword `try` exists in a function, it should be the **very first** word in the function and there should be **nothing** after the `catch`/`finally` blocks.
