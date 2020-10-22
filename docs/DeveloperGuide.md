---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<img src="images/ArchitectureDiagram.png" width="450" />

The ***Architecture Diagram*** given above explains the high-level design of the App. Given below is a quick overview of each component.

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

**`Main`** has two classes called [`Main`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/stock/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

Each of the four components,

* defines its *API* in an `interface` with the same name as the Component.
* exposes its functionality using a concrete `{Component Name}Manager` class (which implements the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component (see the class diagram given below) defines its API in the `Logic.java` interface and exposes its functionality using the `LogicManager.java` class which implements the `Logic` interface.

![Class Diagram of the Logic Component](images/LogicClassDiagram.png)

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

The sections below give more details of each component.

### UI component

![Structure of the UI Component](images/UiClassDiagram.png)

**API** :
[`Ui.java`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/stock/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* Executes user commands using the `Logic` component.
* Listens for changes to `Model` data so that the UI can be updated with the modified data.

### Logic component

![Structure of the Logic Component](images/LogicClassDiagram.png)

**API** :
[`Logic.java`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/logic/Logic.java)

1. `Logic` uses the `StockBookParser` class to parse the user command.
1. This results in a `Command` object which is executed by the `LogicManager`.
1. The command execution can affect the `Model` (e.g. adding a stock).
1. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.
1. In addition, the `CommandResult` object can also instruct the `Ui` to perform certain actions, such as displaying help to the user.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Model component

![Structure of the Model Component](images/ModelClassDiagram.png)

**API** : [`Model.java`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/model/Model.java)

The `Model`,

* stores a `UserPref` object that represents the user’s preferences.
* stores the stock book data.
* exposes an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.


<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique `Tag`, instead of each `Person` needing their own `Tag` object.<br>
![BetterModelClassDiagram](images/BetterModelClassDiagram.png)

</div>


### Storage component

![Structure of the Storage Component](images/StorageClassDiagram.png)

**API** : [`Storage.java`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in json format and read it back.
* can save the stock book data in json format and read it back.

### Common classes

Classes used by multiple components are in the `seedu.stock.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### General Features
General features that are used in many of the implemented features are defined here.

#### StockBookParser
The `StockBookParser` is used to parse a full user input to determine if the user input corresponds to any of the
`COMMAND_WORD` in the various command classes. If the the user input does not conform the any of the expected format
required, Warenager will produce an error message.

### Suggestion Feature

The mechanism for suggestion feature is facilitated by `SuggestionCommandParser, SuggestionCommand, SuggestionUtil`.

#### SuggestionCommand

`SuggestionCommand` class extends `Command` interface. `SuggestionCommand` class is tasked with creating a new `CommandResult`
with the suggestion message to be displayed to the user as its argument. The suggestion message to be displayed
is gathered from the result of the parsing stage for suggestion.

Some of the important operations implemented here are:

* `SuggestionCommand#execute()`
  Generates a new `CommandResult` with the suggestion message as its argument.

#### SuggestionCommandParser
`SugestionCommandParser` class extends `Parser` interface. `SuggestionCommandParser` class is tasked with parsing the
user inputs and generate a new `SuggestionCommand`. The main logic of the suggestion feature is encapsulated here.

`SuggestionCommandParser` receives the user input, along with either the faulty command word or parsing error messages
from another `Parser` object. The `parse` method inside the `SuggestionCommandParser` will then try to infer the best
possible correct command format using the minimum edit distance heuristic provided inside `SuggestionUtil`.

Some of the important operations implemented here are:

* `SuggestionCommandParser#parse()` <br>
  Parses the user input and parsing error messages thrown from another `Parser` and returns a new `SuggestionCommand`
  with the suggestion to be shown as its argument. The inference for the command word to be suggested is made in here.
  After the correct command word is inferred, then it will call helper functions to generate the suggestion messages.
* `SuggestionCommandParser#generateAddSuggestion()` <br>
  Generates the suggestion message for an add command.
* `SuggestionCommandParser#generateListSuggestion()` <br>
  Generates the suggestion message for a list command.
* `SuggestionCommandParser#generateHelpSuggestion()` <br>
  Generates the suggestion message for a help command.
* `SuggestionCommandParser#generateExitSuggestion()` <br>
  Generates the suggestion message for a exit command.
* `SuggestionCommandParser#generateUpdateSuggestion()` <br>
  Generates the suggestion message for an update command.
* `SuggestionCommandParser#generateDeleteSuggestion()` <br>
  Generates the suggestion message for a delete command.
* `SuggestionCommandParser#generateFindSuggestion()` <br>
  Generates the suggestion message for a find command.
* `SuggestionCommandParser#generateFindExactSuggestion()` <br>
  Generates the suggestion message for a find exact command.
* `SuggestionCommandParser#generateStatisticsSuggestion()` <br>
  Generates the suggestion message for a stats command.
* `SuggestionCommandParser#generateNoteSuggestion()` <br>
  Generates the suggestion message for a note command.
* `SuggestionCommandParser#generateNoteDeleteSuggestion()` <br>
  Generates the suggestion message for a note delete command.

#### SuggestionUtil

`SuggestionUtil` class contains the utilities needed to infer the suggestion to be displayed to the user.

The utilities provided inside are:

* `SuggestionUtil#min()` <br>
  Computes the minimum of three integers.
* `SuggestionUtil#minimumEditDistance()` <br>
  Computes the minimum edit distance between 2 strings.

#### Example Usage Scenario

Given below are some example usage scenarios and how the suggestion mechanism behaves at each step.

**Example 1: Unknown command word**

Step 1. The user enters `updt n/Milk s/Fairprice` which contains an unknown command word `updt`.

Step 2. The command word `updt` is extracted out in `StockBookParser` and checked if it matches any valid command word.

Step 3. The command word `updt` does not match any valid command word. It is then passed down to `SuggestionCommandParser`
along with `n/Milk s/Fairprice` and `SuggestionCommandParser#parse()` is invoked.

Step 4. Inside `SuggestionCommandParser#parse()` method, the closest command word to `updt` will be inferred.
The inference uses the minimum edit distance heuristic. `SuggestionCommandParser#parse()` will
count the minimum edit distance from `updt` to every other valid command word.

Step 5. The new valid command word generated is the one with the smallest edit distance to `updt`. The command word
to be suggested in this case is `update`.

Step 6. `SuggestionCommandParser#parse()` method will call `SuggestionCommandParser#generateUpdateSuggestion()`
to generate the suggestion message to be displayed to the user.

Step 7. During the generation of suggestion message, `SuggestionCommandParser#generateUpdateSuggestion()` will check
first if the compulsory prefix exist. In this case the compulsory prefix which is `sn/` does not exist.
`sn/<serial number>` is then added to the suggestion message.

Step 8. All prefixes the user entered that is valid for `update` command and its arguments are nonempty will then
be appended to the suggestion message. If there exist prefix whose argument is empty, then `SuggestionCommandParser#generateUpdateSuggestion()`
will fill the argument with a default value. In this case, prefixes `n/ s/` are present and their arguments are nonempty.
`n/Milk s/Fairprice` is then added to the suggestion message.

Step 9. Lastly `SuggestionCommandParser#generateUpdateSuggestion()` will append the usage message for `update` command.

Step 10. `SuggestionCommandParser#parse()` method returns a new `SuggestionCommand` with the suggestion message
to be displayed as its argument.

Step 11. `SuggestionCommand` is executed and produces a new `CommandResult` to display the message to the user.

Step 12. The suggestion `update sn/<serial number> n/Milk s/Fairprice` is displayed to the user along with what kind of
error and the message usage information. In this case the error is `unknown command` and the message usage is from
`UpdateCommand`.

**Example 2: Invalid command format**

Step 1. The user enters `update n/Milk s/` which contains a valid command word `update`.

Step 2. The command word `update` is extracted out in `StockBookParser` and checked if it matches any valid command word.

Step 3. The command word `update` is a valid command word. Input is then passed to `UpdateCommandParser#parse()`.

Step 4. Inside `UpdateCommandParser#parse()`, the user input is then parsed to create a new `UpdateCommand`. However,
since the compulsory prefix `sn/` is not provided, a `ParseException` will be thrown.

Step 5. `ParseException` thrown will be caught in `StockBookParser`. The user input along with parsing error messages
will then be passed into `SuggestionCommandParser#parse()`.

Step 6. Constructor of `SuggestionCommandParser` will separate the parsing error messages header and body and then
`SuggestionCommandParser#parse()` is invoked.

Step 7. Inside `SuggestionCommandParser#parse()` method, the closest command word to `update` will be inferred.
The inference uses the minimum edit distance heuristic. `SuggestionCommandParser#parse()` will
count the minimum edit distance from `update` to every other valid command word.

Step 8. Since `update` is already a valid command, the inference will generate `update` again.

Step 9. `SuggestionCommandParser#parse()` method will call `SuggestionCommandParser#generateUpdateSuggestion()`
to generate the suggestion message to be displayed to the user.

Step 10. During the generation of suggestion message, `SuggestionCommandParser#generateUpdateSuggestion()` will check
first if the compulsory prefix exist. In this case the compulsory prefix which is `sn/` does not exist.
`sn/<serial number>` is then added to the suggestion message.

Step 11. All prefixes the user entered that is valid for `update` command and its arguments are nonempty will then
be appended to the suggestion message. If there exist prefix whose argument is empty, then `SuggestionCommandParser#generateUpdateSuggestion()`
will fill the argument with a default value. In this case, the prefix `n/` is present and its argument is nonempty.
`n/Milk` is then added to the suggestion message. The prefix `s/` is present, but its argument is empty.
`s/<source>` is then added to the suggestion message.

Step 9. Lastly `SuggestionCommandParser#generateUpdateSuggestion()` will append the usage message for `update` command.

Step 10. `SuggestionCommandParser#parse()` method returns a new `SuggestionCommand` with the suggestion message
to be displayed as its argument.

Step 11. `SuggestionCommand` is executed and produces a new `CommandResult` to display the message to the user.

Step 12. The suggestion `update sn/<serial number> n/Milk s/<source>` is displayed to the user along with what kind of
error and the message usage information. In this case the error is `Invalid command format` and the message usage is from
`UpdateCommand`.


#### Sequence Diagram

The following sequence diagram shows how the suggestion feature works for **Example 1**:

![Suggestion Example 1](images/SuggestionSequenceDiagramExample1.png)

The following sequence diagram shows how the suggestion feature works for **Example 2**:

![Suggestion Example 2](images/SuggestionSequenceDiagramExample2.png)

#### Minimum Edit Distance Heuristic

The minimum edit distance between two strings is defined as the minimum cost needed to transform one into the other.

The transformation cost comes from the types of editing operations performed and how many of those operations performed.

There are three types of editing operations:
* **Insertion**: <br>
  Inserts a new character in the string. Insertion has a cost of 1. <br>
  Example: `apple -> apples` and `sly -> slay`.
* **Deletion**: <br>
  Deletes a character from the string. Deletion has a cost of 1. <br>
  Example: `oranges -> orange` and `bandana -> banana`.
* **Substitution**: <br>
  Change a character in the string into another different character. Substitution has a cost of 3. It is more expensive
  to substitute than inserting or deleting a character. <br>
  Example: `prey -> pray` and `like -> lime`.

The smaller the minimum edit distance between two strings, the more similar they are.
The algorithm to compute the minimum edit distance between two strings is implemented on `SuggestionUtil#minimumEditDistance()`.

**Algorithm Explanation**

Suppose there are two strings:
* `X` with length `n`.
* `Y` with length `m`.

Define `D(i, j)` to be the minimum edit distance between the first `i` characters of `X` and the first `j` characters of `Y`.

Consider doing all possible editing operations:
* **Insertion**: <br>
  If `i > j`, then we can insert the character at position `j + 1` in `X` to position `j + 1` at `Y`.
  Hence, `D(i, j) + 1 = D(i, j + 1)` in this case. <br>
  If `i < j`, then we can insert the character at position `i + 1` in `Y` to position `i + 1` at `X`.
  Hence, `D(i, j) + 1 = D(i + 1, j)` in this case. <br>
* **Deletion**: <br>
  If `i > j`, then we can delete the character at position `i` in `X`.
  Hence, `D(i, j) = D(i - 1, j) + 1` in this case. <br>
  If `i < j`, then we can delete the character at position `j` in `Y`.
  Hence, `D(i, j) = D(i, j - 1) + 1` in this case. <br>
* **Substitution**: <br>
  We can change the character at position `i` in X to match the character at position `j` in `Y`, or
  we can change the character at position `j` in `Y` to match the character at position `i` in `X`. <br>
  Hence, `D(i, j) = D(i - 1, j - 1) + 3` in this case. <br>

The base cases for the recursion are:
* `D(i, 0) = i` since the best way is to delete everything from `X` or inserting every character in `X` to `Y`.
* `D(0, j) = j` since the best way is to delete everything from `Y` or inserting every character in `Y` to `X`.

In all possible editing operations, the value `D(i, j)` can only change to:
* `D(i - 1, j) + 1`
* `D(i, j - 1) + 1`
* `D(i - 1, j - 1) + 3`

Since we want to find the minimum edit distance,
`D(i, j) = min(D(i - 1, j) + 1, D(i, j - 1) + 1, D(i - 1, j - 1) + 3)`.

Since it is a recursion, the algorithm is implemented using dynamic programming to improve speed by remembering already
computed states. The current implementation do the following steps:

1. Creates a table to store computed states (2D `dp` array).
2. Fill up the base cases in the table.
3. Using a double for loop, fill up the table according to the recursion formula above.
4. Returns the minimum edit distance between the two strings compared.

**Time complexity**: `O(n * n)` due to the double for loop.

**Space complexity**: `O(n * n)` due to the table.

#### Activity Diagram

The following activity diagram summarizes what happens when the suggestion feature is triggered:

![SuggestionActivityDiagram](images/SuggestionActivityDiagram.png)

#### Design Consideration

##### Aspect: String Comparison Heuristics

* **Alternative 1 (current implementation):** minimum edit distance heuristic
  * Pros: Provides a good estimate how far apart two strings are. A standard dynamic programming algorithm.
  * Cons: Maybe hard to be understood to people who don't understand dynamic programming.

* **Alternative 2:** substring comparison
  * Pros: Very simple to implement. A brute force algorithm that checks every substring.
  * Cons: The distance estimate between two strings is quite bad, especially if no substring overlaps. Slow in speed
    compared to minimum edit distance. Generates worse suggestion compared to minimum edit distance.

### Statistics Feature

The backend mechanism for statistics feature is facilitated by `StockBookParser, StatisticsCommandParser, StatisticsCommand`
and one of the child command classes of StatisticsCommand that includes (as of documentation):
* `SourceStatisticsCommand`
* `SourceQuantityDistributionStatisticsCommand`  

The frontend mechanism for statistics feature mainly facilitated by the controller class `StatisticsWindow` for
`StatisticsWindow.fxml`. The choice of display is `JavaFX Piechart` from the `JavaFX Charts`.

#### StatisticsCommandParser
`StatisticsCommandParser` class extends `Parser` interface. `StatisticsCommandParser` class is tasked with parsing the
user inputs (without the command word) and generates a new `StatisticsCommand` object. The `StatisticsCommand` will be one of the
existing child commands stated above.

Upon successful parsing, the `StatisticsCommand` object will then be passed on to the respective child command classes
for logical execution.

If the user inputs do not correspond to any of the `STATISTICS_TYPE` words in the child command classes, an error message
will be shown and no `StatisticsCommand` object will be created.

Some of the more important operations implemented here are:

* `StatisticsCommandParser#parse()` <br>
  Parses the user input and returns a new `StatisticsCommand` object that can be belongs to either one of the
  child classes of `StatisticsCommand`. This is aided by the `StatisticsCommandParser#getStatisticsType()` method.
  
* `StatisticsCommandParser#getStatisticsType()` <br>
  This is a further abstracted method that reads the input string and determines what is the correct statistical type
  command that the user wants.
  
:warning: It is to note that some child classes requires parameters in their constructors for their respective purposes.
  
#### StatisticsCommand

`StatisticsCommand` abstract class extends `Command` interface. While the `StatisticsCommand` class contains minimal
functionality, it serves as an inheritance bridge between the various types of statistics command, to comply with
`SOLID` principles.

The respective child classes will be tasked with consolidating the required data and storing it in a `CommandResult` object.

#### SourceStatisticsCommand
`SourceStatisticsCommand` class extends `StatisticsCommand` class. The `SourceStatisticsCommand` class is tasked with
consolidating the required data and storing it in a `CommandResult` object. The statistics shown by this class
describes the **percentage of the different sources** existing in Warenager.

The format for this command is fixed and is ensured by the parser. Any errors arising from this command will be an
assertion error.

The main operation implemented in `SourceStatisticsCommand` class is:
* `SourceStatisticsCommand#execute()`
  Generates a new `CommandResult`. Some key attributes of this object consists of:
   1. `statisticsData` The statistics data to be displayed.
   2. `otherStatisticsDetails` that includes:
        * Statistics type

#### SourceQuantityDistributionStatisticsCommand
`SourceQuantityDistributionStatisticsCommand` class extends `StatisticsCommand` class. The
`SourceQuantityDistributionStatisticsCommand` requires a single parameter: `targetSource`. The statistics shown by this class
describes the **distribution among the different stocks** of the given `targetSource`.

If the `targetSource` is not found by Warenager, this will result in an error message to be shown to prompt the
user that Warenager cannot find the target source company.

The main operation implemented in `SourceQuantityDistributionStatisticsCommand` class is:
* `SourceQuantityDistributionStatisticsCommand#execute()`
  Generates a new `CommandResult`. The key attributes of this object consists of:
   1. `statisticsData` The statistics data to be displayed.
   2. `otherStatisticsDetails` that includes:
        * Statistics type
        * Target source
  
#### StatisticsWindow
`StatisticsWindow` is the controller class for the `StatisticsWindow.fxml`. Here, the piechart in the class is updated
with the correct data corresponding to the command the user inputs. The title will also be customised to the
type of statistics the user wants to display. The compiled data from the `CommandResult` returned by the `#execute`
methods will be read here and supplied to the pie chart.

Some of the more important operations implemented here are:

* `StatisticsWindow#refreshData()` <br>
  This method clears all the current data in the piechart and inserts the correct data depending on the Statistics type
  from `otherStatisticsDetails` in the `CommandResult` object. It then calls the respective methods needed to extract
  the compiled data.
  
* `StatisticsCommandParser#updateDataForSourceQuantityDistributionStatistics()` <br>
  This method is called if the type of statistics is `SourceQuantityDistribution Statistics`. Some calculations are done
  here to provide users with more data.
  
* `StatisticsCommandParser#updateDataForSourceStatistics()` <br>
  This method is called if the type of statistics is `Source Statistics`. Some calculations are done here to provide users
  with more data.
  
#### Example Usage Scenario

Given below are some example usage scenarios and how the statistics mechanism behaves at each step.

**Example 1: Calling statistics for Source Companies**

Step 1. The user enters `stats st/source`.

Step 2. The command word `stats` is extracted out in `StockBookParser`, in this case, it matches the `COMMAND_WORD`,
        which is `stats` in the `StatisticsCommand` class.

Step 3. The remaining user input is the given to the `StatisticsCommandParser` to determine which type of statistics
        the user wants.

Step 4. Inside `StatisticsCommandParser#parse()` method, the header will be dropped, resulting in the remaining user
        input to be `source`. This matches to the criteria for `SourceStatisticsCommand`, and returning a
        `SourceStatisticsCommand` object.
        
Step 5. The `SourceStatisticsCommand#execute()` is then called by the `Logic Manager`. Data extraction and compilation
        will be done and stored in the returning `CommandResult` object. The `CommandResult` object will also store
        the type of statistics in `otherStatisticsDetails`, in this case will be `source`, for later usage.

Step 6. When the `UiManager` calls the `SourceQuantityDistributionStatisticsCommand#execute()` method, this will invoke
        `MainWindow#execute()`. This `CommandResult` is of the statistics class, leading to the `MainWindow#handleStatistics()`
         method call. This leads to the `StatisticsWindow#show()` method call.

Step 7. `StatisticsWindow#show()` will then call the `StatisticsWindow#refreshData()` which in turn will determine display the
        data in the desired format, based on the type of statistics from `otherStatisticsDetails` in `CommandResult` from Step 5.
        In this case, `Source Statistics` will be displayed.

Step 8. This will then update the pie chart with both the relevant data, format, and title to suit the type of statistics
        to be shown. The UI of the window to be shown is customised by the styling based on the `StatisticsWindow.fxml` file.
     
Step 9. The updated piechart will be shown in a popup window.

**Example 2: Calling statistics for Source Quantity Distribution**

Step 1. The user enters `stats st/source-qt-ntuc`. `ntuc` is a valid source that exists in Warenager.

Step 2. The command word `stats` is extracted out in `StockBookParser`, in this case, it matches the `COMMAND_WORD`,
        which is `stats` in the `StatisticsCommand` class.

Step 3. The remaining user input is the given to the `StatisticsCommandParser` to determine which type of statistics
        the user wants.

Step 4. Inside `StatisticsCommandParser#parse()` method, the header will be dropped, resulting in the remaining user
        input to be `source-qt-ntuc`. This matches to the criteria for `SourceQuantityDistributionStatisticsCommand`,
        and returning a `SourceQuantityDistributionStatisticsCommand` object.
        
Step 5. The `SourceQuantityDistributionStatisticsCommand#execute()` is then called by the `Logic Manager`. Data
        extraction and compilation will be done and stored in the returning `CommandResult` object. The `CommandResult` object
        will also store the type of statistics in `otherStatisticsDetails`, in this case will be `source-qt-`, for later usage.
        For this command, the `targetSource` will also be stored in `otherStatisticsDetails` as it is needed to customise the
        title for the piechart.

Step 6. When the `UiManager` calls the `SourceQuantityDistributionStatisticsCommand#execute()` method, this will invoke
        `MainWindow#execute()`. This `CommandResult` is of the statistics class, leading to the `MainWindow#handleStatistics()`
        method call. This leads to the `StatisticsWindow#show()` method call.

Step 7. `StatisticsWindow#show()` will then call the `StatisticsWindow#refreshData()` which in turn will determine display the
        data in the desired format, based on the type of statistics from `otherStatisticsDetails` in `CommandResult` from Step 5.
        In this case, `Source Quantity Distribution Statistics` will be displayed.

Step 8. This will then update the pie chart with both the relevant data, format, and title to suit the type of statistics
        to be shown. The UI of the window to be shown is customised by the styling based on the `StatisticsWindow.fxml` file.
     
Step 9. The updated piechart will be shown in a popup window.

#### Sequence Diagram

The following sequence diagram shows how the Logic aspect of the statistics feature works for **Example 1**:

![Statistics-Logic Example 1](images/StatisticsCommandSequenceDiagramLogicExample1.png)

The following sequence diagram shows how the Ui aspect of the statistics feature works for **Example 1**:

![Statistics-Ui Example 1](images/StatisticsCommandSequenceDiagramUiExample1.png

#### Activity Diagram

The following activity diagram summarizes what happens when the statistics feature is triggered:

![StatisticsActivityDiagram](images/StatisticsActivityDiagram.png)

#### Design Considerations

##### Aspect: UI view for statistics

* **Alternative 1 (current implementation):** Pop up window.
  * Pros: Window can be resized for clearer view. Reduces panel usage since it does not share a common space
          with the stockcards display.
  * Cons: May impede typing speed if statistics are viewed very often.

* **Alternative 2:** Side-by-side view beside the stock cards.
  * Pros: Reduce interruption between typing.
  * Cons: Statistical views are not often used but rather, only after huge changes over time. In order to display the
          piechart properly, there needs to be a sufficiently large area. This leads to a huge portion of display space
          not being utilised efficiently when other commands are being used.
    
##### Aspect: Choice of charts as the primary display for statistics
Pie chart is being used as the choice of statistical display to aid the lack of relativity between stocks
in Warenager. Absolute numbers of each stock is already displayed by the stockcards in Warenager. Pie charts
are more useful when working out the compositions of the data.

#### Future statistical features
With the expansion of more data fields for each stock, there will be more varieties of statistics that can be
shown based on these new fields.
--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of stocks
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Allows users to manage stocks faster than a typical mouse/GUI driven app.
Includes higher level features such as ability to bookmark mostly used products and highlights stocks
that are low in quantity to improve user experience.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                     | I want…​                                                                             | So that…​                                                              |
| -------- | ----------------------------------------------------------- | ------------------------------------------------------------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | delivery assistant                                          | to be able to edit the stocks in the inventory in bulk                               | I can save time and do deliveries more efficiently                     |
| `* * *`  | tech savvy warehouse manager                                | to be able to add my stock to the application                                        | I can record new stocks                                                |
| `* * *`  | tech savvy warehouse manager who can type fast              | to be able to delete my stock in the application                                     | I can remove unwanted stock                                            |
| `* * *`  | warehouse manager                                           | to be able to search for stocks easily                                               | I can refer to them quickly                                            |
| `* * *`  | admin                                                       | to print out all the stocks in the inventory                                         | I can keep records of the inventory                                    |
| `* * *`  | warehouse manager                                           | to be able to view all the stocks there are in the warehouse clearly                 | I can make decisions better                                            |
| `* * *`  | forgetful manager                                           | to list the features and the way to use them                                         | I can refer to this feature when I forget how to use certain features  |
| `* * *`  | multi-device user                                           | to transport data from one device to another                                         | I will not have to key in items one by one again                       |
| `* * *`  | tech-savvy warehouse manager                                | to easily type shorter commands                                                      | I am able to execute functions quickly                                 |
| `* * *`  | collaborative user                                          | my inventory to be able to be shared with my collaborators                           | my collaborators can only read and find data                           |
| `* * *`  | tech savvy warehouse manager                                | to be able to change the information of my existing stock in the application         | I can keep my existing inventories updated                             |
| `* *`    | major shareholder                                           | to easily understand how inventory count works                                       | I can determine if the investment is worthy                            |
| `* *`    | manager                                                     | to be able to gather the statistics (eg. profit) of the items in inventory           | I can report the profitability of products                             |
| `* *`    | forgetful stock                                            | to add optional notes at certain stocks                                              | I can be reminded of important information                             |
| `* *`    | busy manager                                                | to be able to see or highlight low stocks at a glance                                | I can replenish them in time                                           |
| `* *`    | busy manager                                                | to automate the calculation of how much stock to restock based on the current stocks | I do not need to spend time manually calculating                       |
| `* *`    | tech savvy warehouse manager                                | to be able to bookmark certain items in the warehouse                                | I can access and augment their information easily                      |
| `* * *`  | beginner user                                               | have an easy-to-understand interface                                                 |                                                                        |
| `* * *`  | multi-OS user                                               | to run the application on popular operating systems in the market                    |                                                                        |
| `* * *`  | tech savvy warehouse manager                                | to have a smooth flowing platform                                                    | I can track my inventories easily (Good UX)                            |
| `* * *`  | new user                                                    | to read the documentation                                                            | I will be able to know how to use the program                          |
| `* * *`  | offline user                                                | to run the application offline without the need to connect to the internet           |                                                                        |
| `* * *`  | warehouse manager                                           | to store my data in a digitalised platform                                           | I do not have to fear for data loss                                    |
| `* * *`  | impatient user                                              | to run the appli cation and execute commands without lag                             |                                                                        |
| `* * *`  | warehouse manager                                           | to have the capacity to store all my inventory data                                  | I am able to expand my range of inventory                              |
| `* * *`  | tech savvy warehouse manager that can type fast             | to have a platform                                                                   | I can track my stocks through typing                                   |
| `* * *`  | tech savvy warehouse manager                                | to digitalize my inventory                                                           | I do not have to find a physical space to store my inventory details   |
| `* * *`  | warehouse manager                                           | to be able to easily teach my subordinates how to use the software                   | they can cover my role when I am not around                            |

### Use cases

(For all use cases below, the **System** is the `Warenager` and the **Actor** is the `user`, unless specified otherwise)

#### Use case 1: Adding a stock

**MSS**

1.  User requests to add a stock
2.  Warenager adds the stock into the inventory

    Use case ends.

**Extensions**

* 1a. The given format is missing any field header.

    * 1a1. Warenager shows an error message.

      Use case resumes at step 1.

* 1b. The argument for any field header is empty.

    * 1b1. Warenager shows an error message.

      Use case resumes at step 1.

* 1c. The argument to the field header is invalid.

    * 1c1. Warenager shows an error message.

      Use case resumes at step 1.

* 1d. The given input has multiple required field headers.

    * 1d1. Warenager shows an error message.

      Use case resumes at step 1.


#### Use case 2: Deleting stocks

**MSS**

1.  User requests to list stocks.
2.  Warenager shows a list of stocks.
3.  User requests to delete stocks in the list.
4.  Warenager deletes the stock.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message and tells user to use the proper format.

      Use case resumes at step 2.

* 3b. All inputted serial numbers are not found.

    * 3b1. Warenager shows an error message and tells user which serial numbers are not found.

      Use case resumes at step 2.

* 3c. Some inputted serial numbers are not found.

     * 3c1. Warenager deletes the found stocks and tells user which serial numbers are not found.

       Use case resumes at step 2.

#### Use case 3: Find a stock by name

**MSS**

1.  User requests to find a stock with name "umbrella".
2.  Warenager shows a list of stocks with names that
    contain the keyword "umbrella".
3.  User views desired stock.

    Use case ends.

**Extensions**
* 1a. The given format is missing field header n/.

    * 1a1. Warenager shows an error message.
    
      Use case resumes at step 1.

* 1b. The given command is invalid (wrong find command).

    * 1b1. Warenager shows an error message.

      Use case resumes at step 1.

* 2a. There is no stock with name that matches keyword.

    Use case ends.

#### Use case 4: Find a stock by serial number

**MSS**

1.  User requests to find a stock with serial number 111111.
2.  Warenager shows the stock with serial number 111111.
3.  User views desired stock.

    Use case ends.

**Extensions**
* 1a. The given format is missing field header sn/.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given command is invalid (wrong find command).

    * 1b1. Warenager shows an error message.

    Use case resumes at step 1.

* 2a. There is no stock with serial number that matches keyword.

    Use case ends.

#### Use case 5: Find a stock by location stored

**MSS**

1.  User requests to find a stock stored at location "Section 312".
2.  Warenager shows all stocks stored at location "Section 312".
3.  User views desired stock.

    Use case ends.

**Extensions**
* 1a. The given format is missing field header l/.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given command is invalid (wrong find command).

    * 1b1. Warenager shows an error message.

    Use case resumes at step 1.

* 2a. There is no stock with storage location that matches keyword.

    Use case ends.

#### Use case 6: Find a stock by source of stock

**MSS**

1.  User requests to find a stock which source is "Company ABC".
2.  Warenager shows all stocks with source "Company ABC".
3.  User views desired stock.

    Use case ends.

**Extensions**
* 1a. The given format is missing field header s/.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given command is invalid (wrong find command).

    * 1b1. Warenager shows an error message.

    Use case resumes at step 1.

* 2a. There is no stock with source that matches keyword.

    Use case ends.

#### Use case 7: Increment or decrement a stock's quantity

**MSS**

1.  User requests to list stocks.
2.  Warenager lists all stocks including their serial number.
3.  User requests to increment or decrement a specific stock's quantity.
4.  Warenager updates the stock's quantity.

    Use case ends.

**Extensions**

* 2a. The list of all stocks is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The stock with the given serial number is not found.

    * 3b1. Warenager shows an error message.

      Use case resumes at step 2.

* 3c. The given format is missing the field header q/.

    * 3c1. Warenager shows an error message.

      Use case resumes at step 2.

* 3d. The given increment or decrement value is not an integer.

    * 3d1. Warenager shows an error message.

      Use case resumes at step 2.
 
* 3e. The given increment or decrement value exceeds the integer limit.

    * 3e1. Warenager shows an error message.

      Use case resumes at step 2.

* 3f. The given increment value plus the stock's current quantity exceeds the integer limit.

    * 3f1. Warenager shows an error message.

      Use case resumes at step 2.

* 3g. The stock's current quantity minus the given decrement value results in a negative value.

    * 3g1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case 8: Rewrite a stock's quantity

**MSS**

1.  User requests to list stocks.
2.  Warenager lists all stocks including their serial number.
3.  User requests to change a specific stock's quantity.
4.  Warenager updates the stock's quantity.

    Use case ends.

**Extensions**

* 2a. The list of all stocks is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The stock with the given serial number is not found.

    * 3b1. Warenager shows an error message.

      Use case resumes at step 2.

* 3c. The given format is missing the field header nq/.

    * 3c1. Warenager shows an error message.

      Use case resumes at step 2.

* 3d. The given quantity value is not an integer.

    * 3d1. Warenager shows an error message.

      Use case resumes at step 2.

* 3e. The given quantity value exceeds the integer limit.

    * 3e1. Warenager shows an error message.

      Use case resumes at step 2.

* 3f. The given quantity value is negative.

    * 3f1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case 9: Update the name of a stock.

**MSS**

1.  User requests to list stocks.
2.  Warenager lists all stocks including their serial number.
3.  User requests to change a specific stock's name.
4.  Warenager updates the stock's name.

    Use case ends.

**Extensions**

* 2a. The list of all stocks is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The stock with the given serial number is not found.

    * 3b1. Warenager shows an error message.

      Use case resumes at step 2.

* 3c. The given format is missing the field header n/.

    * 3c1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case 10: Update the location of a stock

**MSS**

1.  User requests to list stocks.
2.  Warenager lists all stocks including their serial number.
3.  User requests to change a specific stock's location.
4.  Warenager updates the stock's location.

    Use case ends.

**Extensions**

* 2a. The list of all stocks is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The stock with the given serial number is not found.

    * 3b1. Warenager shows an error message.

      Use case resumes at step 2.

* 3c. The given format is missing the field header l/.

    * 3c1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case 11: Update the source of a stock

**MSS**

1.  User requests to list stocks.
2.  Warenager lists all stocks including their serial number.
3.  User requests to change a specific stock's source.
4.  Warenager updates the stock's source.

    Use case ends.

**Extensions**

* 2a. The list of all stocks is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The stock with the given serial number is not found.

    * 3b1. Warenager shows an error message.

      Use case resumes at step 2.

* 3c. The given format is missing the field header s/.

    * 3c1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case 12: Using the stats command

**MSS**

1.  User requests stats from Warenager.
2.  Warenager shows the statistics of the desired field as a pop up.
3.  User views the statistics in the pie chart.

    Use case ends.

**Extensions**
* 1a. The given input has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given input has a wrong header.

    * 1b1. Warenager shows an error message.

     Use case resumes at step 1.

* 1c. The given input has a missing header.

    * 1c1. Warenager shows an error message.

     Use case resumes at step 1.

* 1d. The given input is empty.

    * 1d1. Warenager shows an error message.

     Use case resumes at step 1.

* 1e. The given input contains fields that cannot be found.

    * 1e1. Warenager shows an error message.

     Use case resumes at step 1.

#### Use case 13: Adding a note to a stock

**MSS**

1.  User requests to add a note to a stock.
2.  Warenager adds the note to the stock.

    Use case ends.
    
**Extensions**
* 1a. The given input has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given input has a wrong header.

    * 1b1. Warenager shows an error message.

     Use case resumes at step 1.

* 1c. The given input has a missing header.

    * 1c1. Warenager shows an error message.

     Use case resumes at step 1.

* 1d. The given input is empty.

    * 1d1. Warenager shows an error message.

     Use case resumes at step 1.

* 1e. The stock cannot be found based on given input.

    * 1e1. Warenager shows an error message.

     Use case resumes at step 1.

#### Use case 14: Deleting a note from a stock

**MSS**

1.  User requests to delete a note from a stock.
2.  Warenager deletes the note from the stock.

    Use case ends.
    
**Extensions**
* 1a. The given input has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given input has a wrong header.

    * 1b1. Warenager shows an error message.

     Use case resumes at step 1.

* 1c. The given input has a missing header.

    * 1c1. Warenager shows an error message.

     Use case resumes at step 1.

* 1d. The given input is empty.

    * 1d1. Warenager shows an error message.

     Use case resumes at step 1.

* 1e. The stock cannot be found based on given input.

    * 1e1. Warenager shows an error message.

     Use case resumes at step 1.

* 1f. The note cannot be found based on given input.

    * 1f1. Warenager shows an error message.

     Use case resumes at step 1.

#### Use case 15: Deleting all notes from a stock

**MSS**

1.  User requests to delete all notes from a stock.
2.  Warenager deletes all notes from the stock.

    Use case ends.
    
**Extensions**
* 1a. The given input has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

* 1b. The given input has a wrong header.

    * 1b1. Warenager shows an error message.

     Use case resumes at step 1.

* 1c. The given input has a missing header.

    * 1c1. Warenager shows an error message.

     Use case resumes at step 1.

* 1d. The given input is empty.

    * 1d1. Warenager shows an error message.

     Use case resumes at step 1.

* 1e. The stock cannot be found based on given input.

    * 1e1. Warenager shows an error message.

     Use case resumes at step 1.

* 1f. The stock specified has no notes.

    * 1f1. Warenager shows an error message.

     Use case resumes at step 1.
     
#### Use case 16: Generating a csv file that contains all stocks

**MSS**

1.  User requests to print stocks in stock book.
2.  Warenager generates a csv file containing all stocks.
 
    Use case ends.
 
**Extensions**
 
* 1a. The given input contains has the wrong format.

    * 1a1. Warenager shows an error message and suggested command.
 
      Use case resumes at step 1.
 
* 1b. There is an error when creating the csv file.
 
    * 1b1. Warenager shows an error message.
 
      Use case resumes at step 1.

#### Use case 17: Generating a csv file that contains all stocks sorted in desired order

 **MSS**
 
 1.  User sort stocks in stock book (Use case..) in their desired order.
 2.  User request to generate csv file based on the existing stock book (Use case 16).
 3.  Warenager generates a csv file containing all stocks.
 
     Use case ends.

#### Use case 18: Using the help command

**MSS**

1.  User requests helps from Warenager.
2.  Warenager shows the user guide as a pop up.
3.  User views the user guide.

    Use case ends.

**Extensions**
* 1a. The given format has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.

#### Use case 19: Suggestion feature

**MSS**

1. User types in command.
2. Warenager detects command format is invalid.
3. Warenager shows command suggestion to the user.

   Use case ends.

**Extensions**
* 2a. The command word user provided is not valid.

    * 2a1. Warenager calculates the most related command word to suggest.

    Use case resumes at step 3.

* 2b. The command word provided is valid, but the prefixes are not.

    * 2b1. Warenager prepares to suggest the command word along with only the valid prefixes.

    Use case resumes at step 3.

* 2c. The command word provided is valid, but the some prefixes are missing.

    * 2c1. Warenager prepares to suggest the command word along with only the missing prefixes.

    Use case resumes at step 3.

#### Use case 20: Exit Warenager

**MSS**

1.  User requests to exit Warenager.
2.  Warenager shows exit message.
3.  User exits Warenager.

    Use case ends.

**Extensions**
* 1a. The given format has an additional header.

    * 1a1. Warenager shows an error message.

     Use case resumes at step 1.


*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 stocks without a noticeable sluggishness in performance for smooth typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  A user without online connection should still be able to run the application.
5.  Should be easy to pickup so that a user of managerial role can quickly teach their employees should he/she be absent.
6.  Should have an easy-to-understand interface, for beginner users to use the application comfortably.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Stock**: Item in the inventory.
* **Field**: (name, serial number, quantity, location stored, source) of the stock in inventory

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

### Adding a stock

1. Adding a stock into the inventory.

   1. Test case: `n/Banana s/NUS q/9999 l/Fruit Section`<br>
      Expected: New stock added: Banana SerialNumber: NUS1 Source: NUS Quantity: 9999 Location: Fruit Section.
      Details of the added stock shown in the status message.

   1. Test case: `add n/Banana s/NUS q/9999 l/`<br>
      Expected: Locations can take any values, and it should not be blank.
      Error details shown in the status message. Status bar remains the same.

   1. Test case: ` add n/Banana s/NUS q/9999`<br>
      Expected: Invalid command format!
      add: Adds a stock to the stock book. Parameters: n/NAME s/SOURCE q/QUANTITY l/LOCATION
      Example: add n/Umbrella s/Kc company q/100 l/section B,
      Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `add`, `add sn/absdsa` <br>
      Expected: Similar to previous.

### Deleting stocks

1. Deleting stocks from a given list.

   1. Prerequisites: List all stocks by default or use the `find` command. Multiple stocks in the list.

   1. Test case: `delete sn/1111111`<br>
      Expected: Stock with the serial number 1111111 is deleted from the inventory.
      Details of the deleted stock shown in the status message.

   1. Test case: `delete sn/1111111 sn/11111111`<br>
      Expected: Stock with the serial number 1111111 is deleted from the inventory.
      Duplicate serial number(s) is/are ignored. Details of the deleted stock shown in the status message.

   1. Test case: `delete sn/1111111 sn/22222222`<br>
      Expected: Both stocks with the serial numbers 1111111 and 22222222 are deleted from the inventory.
      Details of the deleted stock shown in the status message.

   1. Test case: `delete sn/1111111 sn/33333333` (no stock has the serial number `33333333`) <br>
        Expected: Only the existing stock with the serial number 1111111 is deleted.
        Details of this deleted stock shown in the status message.
        Serial number `33333333` which does not belong to any stock will be shown in status message as well.

   1. Test case: `delete 1111111`<br>
      Expected: No stock deleted due to invalid format from missing sn/.
      Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete sn/absdsa`
      (where serial number is not an integer or is a negative integer)<br>
      Expected: Similar to previous.

### Finding a stock

1. Finding a stock from the inventory.

   1. Prerequisites: Multiple stocks in the list. Stock exists in inventory.

   1. Test case: `find sn/1111111`<br>
      Expected: Stock of the serial number 1111111 is displayed from the inventory.
      Status message shows success of command.

   1. Test case: `find n/umbrella`<br>
      Expected: All stocks with name containing "umbrella" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `find l/section 3`<br>
      Expected: All stocks with storage location containing "section" and "3" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `find s/company abc`<br>
      Expected: All stocks with field source containing "company" and "abc" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `find n/umbrella l/section 3`<br>
         Expected: All stocks with field name containing "umbrella" OR field location containing "section" and "3"
         are displayed from the inventory.
         Status message shows success of command.
   
   1. Test case: `find 1111111`<br>
      Expected: No stock found due to invalid format from missing field header
      either n/, sn/, l/ or s/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `find n/umbrella n/company abc`<br>
      Expected: No stock found due to invalid format from duplicate field header of n/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `find`<br>
      Expected: No stock found due to missing field headers.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `find q/1111`<br>
      Expected: No stock found due to invalid field header q/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `find n/`<br>
      Expected: No stock found due to empty input for field name.
      Error details shown in the status message. Suggestion message will be shown too.

### Advanced finding a stock

1. Finding a stock from the inventory.

   1. Prerequisites: Multiple stocks in the list. Stock exists in inventory.

   1. Test case: `findexact sn/1111111`<br>
      Expected: Stock of the serial number 1111111 is displayed from the inventory.
      Status message shows success of command.

   1. Test case: `findexact n/umbrella`<br>
      Expected: All stocks with name containing "umbrella" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `findexact l/section 3`<br>
      Expected: All stocks with storage location containing "section" and "3" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `findexact s/company abc`<br>
      Expected: All stocks with field source containing "company" and "abc" are displayed from the inventory.
      Status message shows success of command.

   1. Test case: `findexact n/umbrella l/section 3`<br>
         Expected: All stocks with field name containing "umbrella" AND field location containing "section" and "3"
         are displayed from the inventory.
         Status message shows success of command.
   
   1. Test case: `findexact 1111111`<br>
      Expected: No stock found due to invalid format from missing field header
      either n/, sn/, l/ or s/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `findexact n/umbrella n/company abc`<br>
      Expected: No stock found due to invalid format from duplicate field header of n/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `findexact`<br>
      Expected: No stock found due to missing field headers.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `findexact q/1111`<br>
      Expected: No stock found due to invalid field header q/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `findexact n/`<br>
      Expected: No stock found due to empty input for field name.
      Error details shown in the status message. Suggestion message will be shown too.

### Updating a stock

1. Updating a stock from the inventory.

    1. Prerequisites: Multiple stocks in the list. Stocks exists in inventory.

    1. Test case: `update sn/FLower11 iq/+50`<br>
       Expected: The stock with serial number Flower11 will have an increase of quantity by 50.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/FLower11 iq/-50`<br>
       Expected: The stock with serial number Flower11 will have a decrease of quantity by 50.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/Flower11 nq/2103`<br>
       Expected: The stock with serial number Flower11 will have a new quantity 2103.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/Flower11 n/Rose`
       Expected: The stock with serial number Flower11 will have a new name Rose.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/Flower11 l/Vase 3`
       Expected: The stock with serial number Flower11 will have a new location Vase 3.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/2103 s/Flower Distributor Association`
       Expected: The stock with serial number Flower11 will have a new source Flower Distributor Association.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/FLower11 iq/+50 n/Rose l/Vase 3 s/Flower Distributor Association`
       Expected: The stock with serial number Flower11 will have an increase of quantity by 50, a new name Rose,
       a new location Vase3, a new source Flower Distributor Association.
       Details of the updated stock is shown in the status message.

    1. Test case: `update sn/FLower11 sn/Flower12 iq/+50 n/Rose l/Vase 3 s/Flower Distributor Association`
       Expected: The stock with serial number Flower11 and Flower12 will have an increase of quantity by 50, a new name Rose,
       a new location Vase3, a new source Flower Distributor Association.
       Details of the updated stock is shown in the status message.

### Generate statistics

1. Generating statistics for a target field.

    1. Test case: `stats st/source`<br>
       Expected: A pie chart describing the distribution of source companies for the entire inventory is popped up.
       Details of the successful generation of statistics are shown in the status message.

    1. Test case: `stats st/source-qd-ntuc` (the source company `ntuc` exists) <br>
       Expected: A pie chart describing the distribution of stocks in `ntuc` is popped up.
       Details of the successful generation of statistics are shown in the status message.

    1. Test case: `stats st/source-qd-fair price` (the source company `fair price` does not exist)<br>
       Expected: No pop ups describing the statistics will be given or shown.
       Error details shown in the status message. Suggestion message will be shown too.

   1. Other incorrect statistics commands to try: `stats`, `stats st/absdsa`, `stats st/source st/source`
      Expected: Similar to previous.
      
### Generating unique serial number

1. Generating serial number for a newly added stock.

    1. Test case: `n/Crabs s/Giant q/99 l/Seafood Section`<br> (source `Giant` has been used `50` times)
      Expected: New stock added: Crabs SerialNumber: Giant51 Source: Giant Quantity: 99 Location: Seafood Section.
      Details of the added stock shown in the status message.

    1. Test case: `n/Peaches s/Market q/500 l/Fruits Section`<br> (source `Market` has never been used)
      Expected: New stock added: Peaches SerialNumber: Market51 Source: Market Quantity: 500 Location: Fruits Section.
      Details of the added stock shown in the status message.

### Adding note to stock

1. Adding a note to a stock.

    1. Test case: `note sn/ntuc1 nt/first note`
    Expected: Note is added to the stock with serial number ntuc1 and displayed in the notes column for the stock.
    Details of the stock with successful note added is shown in status message.
   
   1. Test case: `note 1111111`<br>
      Expected: No note added due to invalid format from missing field headers sn/ and nt/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `note sn/umbrella1 sn/company1 nt/first note`<br>
      Expected: No note added due to invalid format from duplicate field header of sn/.
      Error details shown in the status message. Status bar remains the same.

   1. Test case: `note`<br>
      Expected: No note added due to missing field headers.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `note q/1111`<br>
      Expected: No note added due to invalid field header q/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `note sn/ntuc1 nt/`<br>
      Expected: No note added due to empty input for field note.
      Error details shown in the status message. Suggestion message will be shown too.

### Deleting a note from stock

1. Deleting a note from stock.

    1. Test case: `notedelete sn/ntuc1 ni/1`
    Expected: Note with index 1 is deleted from the stock with serial number ntuc1
    and display is removed from the notes column for the stock.
    Details of the stock with successful note deleted is shown in status message.

   1. Test case: `notedelete sn/ntuc1 ni/noninteger`<br>
      Expected: No note deleted as note index given is not a positive integer.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete sn/ntuc1 ni/-99`<br>
      Expected: No note deleted as note index given is not a positive integer.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete sn/ntuc1 ni/9999`<br>
      Expected: No note deleted (if stock does not have note with index 9999) as note index given is not found.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete 1111111`<br>
      Expected: No note deleted due to invalid format from missing field headers sn/ and ni/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete sn/umbrella1 sn/company1 ni/2`<br>
      Expected: No note deleted due to invalid format from duplicate field header of sn/.
      Error details shown in the status message. Status bar remains the same.

   1. Test case: `notedelete`<br>
      Expected: No note deleted due to missing field headers.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete q/1111`<br>
      Expected: No note deleted due to invalid field header q/.
      Error details shown in the status message. Suggestion message will be shown too.

   1. Test case: `notedelete sn/ntuc1 ni/`<br>
      Expected: No note delete due to empty input for field note index.
      Error details shown in the status message. Suggestion message will be shown too.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

