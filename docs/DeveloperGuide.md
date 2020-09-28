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

**`Main`** has two classes called [`Main`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/Main.java) and [`MainApp`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/MainApp.java). It is responsible for,
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

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `StockListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/stock/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2021S1-CS2103T-T15-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

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
* exposes an unmodifiable `ObservableList<Stock>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.

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

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current stock book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous stock book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone stock book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial stock book state, and the `currentStatePointer` pointing to that single stock book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th stock in the stock book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the stock book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted stock book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new stock. The `add` command also calls `Model#commitAddressBook()`, causing another modified stock book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the stock book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the stock was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous stock book state, and restores the stock book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the stock book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest stock book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the stock book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all stock book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

![CommitActivityDiagram](images/CommitActivityDiagram.png)

#### Design consideration:

##### Aspect: How undo & redo executes

* **Alternative 1 (current choice):** Saves the entire stock book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the stock being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

#### Use case: Adding a stock

**MSS**

1.  User requests to add a stock
2.  Warenager adds the stock into the inventory

    Use case ends.

**Extensions**

* 1a. The given format is missing any field header.

    * 1a1. Warenager shows an error message.

      Use case resumes at step 1.

* 1b. The given input to the field header is empty.

    * 1b1. Warenager shows an error message.

      Use case resumes at step 1.
      
* 1c. The given input to the field header is invalid.

    * 1c1. Warenager shows an error message.

      Use case resumes at step 1.


#### Use case: Delete a stock

**MSS**

1.  User requests to list stocks
2.  Warenager shows a list of stocks
3.  User requests to delete a specific stock in the list
4.  Warenager deletes the stock

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given format is missing the field header sn/.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

* 3b. The given serial number is not found.

    * 3a1. Warenager shows an error message.

      Use case resumes at step 2.

#### Use case: Find a stock by name

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
    
#### Use case: Find a stock by serial number

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
    
#### Use case: Find a stock by location stored

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
    
#### Use case: Find a stock by source of stock

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

#### Use case: Increment or decrement a stock's quantity

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

#### Use case: Rewrite a stock's quantity

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

#### Use case: Update the name of a stock.

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

#### Use case: Update the location of a stock

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

#### Use case: Update the source of a stock

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

*{More to be added}*

#### Use case: Using the help command

**MSS**

1.  User requests helps from Warenager.
2.  Warenager shows the user guide as a pop up.
3.  User views the user guide.

    Use case ends.

**Extensions**
* 1a. The given format has an additional header.
 
    * 1a1. Warenager shows an error message.
    
     Use case resumes at step 1.


#### Use case: Exit Warenager

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

1. _{ more test cases …​ }_

### Adding a stock

1. Adding a stock into the inventory.

   1. Test case: `n/Banana s/NUS q/9999 l/Fruit Section`<br>
      Expected: New stock added: Banana SerialNumber: 12 Source: NUS Quantity: 9999 Location: Fruit Section.
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

1. _{ more test cases …​ }_

### Deleting a stock

1. Deleting a stock while all relevant stock are shown.

   1. Prerequisites: List all stocks by default or use the `find` command. Multiple stocks in the list.

   1. Test case: `delete sn/1111111`<br>
      Expected: Stock of the serial number 1111111 is deleted from the inventory. 
      Details of the deleted stock shown in the status message.

   1. Test case: `delete 1111111`<br>
      Expected: No stock deleted due to invalid format from missing sn/. 
      Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete sn/absdsa` 
      (where serial number is not an integer or is a negative integer)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

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
      Expected: All stocks with storage location containing "section 3" are displayed from the inventory. 
      Status message shows success of command.  
            
   1. Test case: `find s/company abc`<br>
      Expected: All stocks with field source containing "company abc" are displayed from the inventory. 
      Status message shows success of command.  
            
   1. Test case: `find 1111111`<br>
      Expected: No stock deleted due to invalid format from missing field header
      either n/, sn/, l/ or s/. 
      Error details shown in the status message. Status bar remains the same.

   1. Other incorrect find commands to try: `find`, `find sn/absdsa` 
      (where serial number is not an integer or is a negative integer)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Updating a stock

1. Updating a stock from the inventory.

    1. Prerequisites: Multiple stocks in the list. Stocks exists in inventory.
    
    1. Test case: `update sn/2103 q/50`<br>
       Expected: The stock with serial number 2103 will have an increase of quantity by 50.
       Details of the updated stock is shown in the status message.
    
    1. Test case: `update sn/2103 q/-50`<br>
       Expected: The stock with serial number 2103 will have a decrease of quantity by 50.
       Details of the updated stock is shown in the status message.
    
    1. Test case: `update sn/2103 nq/2103`<br>
       Expected: The stock with serial number 2103 will have a new quantity 2103.
       Details of the updated stock is shown in the status message.
    
    1. Test case: `update sn/2103 n/CS2103T`
       Expected: The stock with serial number 2103 will have a new name CS2103T.
       Details of the updated stock is shown in the status message.
    
    1. Test case: `update sn/2103 l/B1`
       Expected: The stock with serial number 2103 will have a new location B1.
       Details of the updated stock is shown in the status message.
    
    1. Test case: `update sn/2103 s/NUS`
       Expected: The stock with serial number 2103 will have a new source NUS.
       Details of the updated stock is shown in the status message.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
