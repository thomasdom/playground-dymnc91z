# Meaningful names

## Introduction

It takes quite some time to pick up good names for our variables, classes or files, especially when you're not used to think about it rigorously every day.

But once it is done, it **will** save you a lot of time by not trying to remember every time you read your code:

- What does this code do?
- What is the meaning of `myObject`?

## Use intention-revealing names

**Names should reveal intent.** The reader should immediately understand **what your code do** when reading your code.

Here is an example of poorly-named code:

**bad code:**

```java
// Main.java

public List<int[]> getThem() {
    List<int[]> list1 = new ArrayList<int[]>();
    for (int[] x : theList)
        if (x[0] == 4)
            list1.add(x);
    return list1;
}
```

You are probably asking yourself these questions right now:

1. What kinds of things are in `theList`?
2. What is the significance of the zeroth element of an item in `theList`?
3. What is the significance of the value `4` ?
4. How would I use the list being returned?

We’re working in a mine sweeper game. The board is represented by a list of cells called `theList`. Let’s rename that to `gameBoard`.
Each cell on the board is represented by a simple array. The zeroth element in the array represents a status value. A status value of `4` means “flagged”.

The snippet shown above is too **implicit** and must be rewritten to convey meaning.

Just by giving these concepts names we can improve the code considerably.

**good code:**

```java
// Board.java

public static final int STATUS_VALUE = 0;
public static final int FLAGGED = 4;

public List<int[]> getFlaggedCells() {
    List<int[]> flaggedCells = new ArrayList<int[]>();
    for (int[] cell : gameBoard)
        if (cell[STATUS_VALUE] == FLAGGED)
            flaggedCells.add(cell);
    return flaggedCells;
}
```

We can further improve our code by **encapsulating** a cell data and its behavior into a dedicated **class**. Let's name it `Cell`. The cell statuses can be represented as an **enum** called `CellStatus`, containing the values as integers.

**better code:**

```java
// CellStatus.java

public enum CellStatus {
    FLAGGED (4)
}
```

```java
// Cell.java

public class Cell {
    private CellStatus status;

    public boolean isFlagged() {
        return this.status == CellStatus.FLAGGED;
    }
}
```

```java
// Board.java

public List<Cell> getFlaggedCells() {
    List<Cell> flaggedCells = new ArrayList<Cell>();
    for (Cell cell : gameBoard)
        if (cell.isFlagged())
            flaggedCells.add(cell);
    return flaggedCells;
}
```

## Avoid disinformation

Programmers must avoid leaving false clues that obscure the meaning of code:

- **Do not use abbreviations** (ex: `vhc` for `vehicle`, `an` for a `animal`...)
- Do not use names **which vary in small ways** (ex: `AnimalNamesController` and `AnimalNameController`)
- **Avoid ambiguated letters** combinated (ex: `l` and `1`, `O` and `0`...) in variable names when possible

## Make meaningful distinctions

- **Do not use series**. Series of numbers (ex: `a1`, `a2`, ..., `aN`) are bad names, because they provide no clue about their meaning.
- **Avoid noise words**. For example, `VehicleInfo` and `VehicleData` are different names but they mean the same thing: they contain information about a vehicle.

## Use pronounceable names

Make your names pronounceable. If you can’t pronounce a name, you can’t discuss it without sounding like an idiot.

**bad code:**

```java
int createymdhms = 1605191958;
```

**good code:**

```java
int creationTimestamp = 1605191958;
```

## Use searchable names

Make your names searchable. Single-letter names and numeric constants have a particular problem in that they are not
easy to locate across a body of text

**bad code:**

```java
public static final THIRTY_TWO = 32;
```

**good code:**

```java
public static final HUMAN_NUMBER_OF_TEETH = 32;
```

Here, the reader can find easily how many teeth a human mouth contains, without knowing the value.

## Avoid encodings

- **Do not put the type in the variable name** (ex: `String phoneNumberString` vs `String phoneNumber`). Java is a strongly-typed language, so the variable type is already expressed near the variable.
- **Do not prefix or suffix variables** (ex: `private String p_description` vs `private String description`). Your classes and functions should be small enough that you don’t need them.
- If you do not have the choice (because of coding conventions), it is preferable to **suffix the implementation rather than prefixing the interface** with an `I` (ex: `IShapeFactory` and `ShapeFactory` vs `ShapeFactory` and `ShapeFactoryImpl`).

## Avoid mental mapping

Readers shouldn’t have to mentally translate your names into other names they already know. This problem generally arises from a choice to use neither problem domain terms nor solution domain terms.

## Class names

- Classes and objects should have **noun** or **noun phrase names** like `Customer`, `BlogPost`, `Account`, and `EmailParser`.
- Avoid words like `Manager`, `Processor`, `Data`, or `Info` in the name of a class.
- A class name should not be a _verb_.

## Method names

Methods should have verb or verb phrase names like `postPayment`, `deleteArticle`, or `save`.

Accessors, mutators, and predicates should be named for their value and prefixed with `get`, `set`, and `is` according to the [JavaBeans conventions](https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions).

## Don't be cute

**Choose clarity over entertainment value.**

Cuteness in code often appears in the form of colloquialisms or slang. For example, don’t use the name `whack()` to mean `kill()`. Don’t tell little culture-dependent jokes like `eatMyShorts()` to mean `abort()`.

**Say what you mean. Mean what you say.**

## Pick one word per concept

Pick one word for one abstract concept and stick with it. A consistent lexicon is a great plus to the developers who must use your code.

**bad code:**

```java
public Article fetchArticle();
public Author getAuthor();
public Category retrieveCategory();
```

**good code:**

```java
public Article fetchArticle();
public Author fetchAuthor();
public Category fetchCategory();
```

## Don't pun

Avoid using the same word for two purposes. Using the same term for two different ideas is essentially a **pun**.

## Use solution domain names

Remember that the people who read your code will be developers. So go ahead and use computer science (CS) terms, algorithm names, pattern names, math terms, and so forth.

The name `AccountVisitor` means a great deal to a programmer who is familiar with the "Visitor" design pattern. What programmer would not know what a `JobQueue` is?

Choosing technical names for those things is usually the most appropriate thing to do.

## Use problem domain names

When there is no "developer-ich" name for what you’re doing, use the name from the problem domain (ex: `ChemicalReactor` for a class that is responsible to simulate reactions between chemical elements).

At least the developer who maintains your code can ask a domain expert.

## Add meaningful context

There are a few names which are meaningful in and of themselves — most are not.

You need to **place names in context** for your reader by enclosing them in well-named classes, functions, or namespaces. When all else fails, then **prefixing the name may be necessary as a last resort**.

**Example:**

Imagine that you have variables named `firstName`, `lastName`, `street`, `houseNumber`, `city`, `state`, and `zipcode`. Taken together it’s pretty clear that they form an address.

But what if you just saw the `state` variable being used alone in a method? Would you automatically infer that it was part of an address?

You can add context by using prefixes: `addrFirstName`, `addrLastName`, `addrState`, and so on. At least readers will understand that these variables are part of a larger structure.

Of course, a better solution is to create a class named `Address`. Then, even the compiler knows that the variables belong to a bigger concept.

## Don't add gratuitous context

In an imaginary application called "Software Developer Tools”, it is a bad idea to prefix every class with `SDT`. Frankly, you are working against your tools. You type G and press the completion key and are rewarded with a mile-long list of every class in the system. Is that
wise? Why make it hard for the IDE to help you?

Shorter names are generally better than longer ones, **so long as they are clear**. **Add no more context to a name than is necessary**.
