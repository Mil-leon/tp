---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# PowerBake Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Acknowledgements**

* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `PastryListPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI. Due to the sheer number of components inhering fromthe abstract `UiPart` class, the inheritance has been shown in the diagram below instead.

<puml src="diagrams/UiClassDiagram2.puml" alt="Inheritance from UiPart class"/>

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/ui/common/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="1000" />


The `Model` component,

* stores the address book data.
1. all `Person` objects (which are contained in a `UniquePersonList` object).
2. all `Pastry` objects (which are contained in a `UniquePastryList` object).
3. all `Order` objects (which are contained in a `UniqueOrderList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* same idea applies to `Pastry` and `Order` objects.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-F13-2/tp/blob/master/src/main/java/powerbake/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="750" />

The `Storage` component,
* can save both address book data and user preference data in [JSON](#glossary) format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `powerbake.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

_{To be added}_

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

* is a small home bakery
* sells their own pastries online through home deliveries
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Keeping track of customers and their orders is hard for small business owners, this provides them with fast access to their orders that they can track.

* Provides a customer list where they are able to track customers and their orders
* Provides a order list so they can keep track of customers orders
* Daily pastry list (Products list)

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …        | I want to …                                          | So that I can…                                                           |
|----------|---------------|------------------------------------------------------|--------------------------------------------------------------------------|
| `* * *`  | Bakery Owner  | add client details                                   | I can keep track of their delivery location and order details            |
| `* * *`  | Bakery Owner  | delete client details                                | I can get rid of irrelevant clients                                      |
| `* * *`  | Bakery Owner  | I can create new pastries                            | when I make new pastries I can add them to the list of availabe pastries |
| `* * *`  | Bakery Owner  | I can delete pastries                                | I can remove no longer available pastries                                |
| `* * *`  | Bakery Owner  | view a list of pasteries                             | I can access the types of pasteries I have                               |
| `* * *`  | Bakery Owner  | view a list of clients                               | I can access the client information easily                               |
| `* * *`  | Bakery Owner  | access the system via a GUI on macOS/Linux           | I don’t need specialized hardware/software to manage my business         |
| `* * *`  | Bakery Owner  | use the system offline                               | I can have access to the system at all times                             |
| `* *`    | Bakery Owner  | come back to the system with all my previous entries | I do not have to re-enter all client and pasteries entries every time    |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `PowerBake` and the **Actor** is the `user`, unless specified otherwise)

**Use case: View Customer Details**

**MSS**

1.  User requests to list all customers
2.  PowerBake shows a list of customers

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* Use case: Add Customer

**MSS**

1.  User types add customer command into PowerBake
2.  PowerBake adds customer details

    Use case ends.


* Use case: Delete Customer
**MSS**

1.  User types delete customer command (with index) into PowerBake
2.  PowerBake delete customer details

    Use case ends.

**Extensions**

* 1a. Index is invalid
* 1a1. PowerBake prints error message

  Use case ends.

**Use case: View Pastries Details**

**MSS**

1.  User requests to list all pastries
2.  PowerBake shows a list of pastries

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* Use case: Add Pastries

**MSS**

1.  User types add pastries command into PowerBake
2.  PowerBake adds pastries details

    Use case ends.


* Use case: Delete Pastries
  **MSS**

1.  User types delete pastries command (with index) into PowerBake
2.  PowerBake delete pastries details

    Use case ends.

**Extensions**

* 1a. Index is invalid
* 1a1. PowerBake prints error message

  Use case ends.

*{More to be added}*

### Non-Functional Requirements

1. The system shall provide a graphical user interface compatible with macOS and Linux operating systems.
2. The system must function offline with full capabilities.
3. All data must persist between sessions, requiring no re-entry of client or pastry information.
4. Customer data including addresses and payment details must be encrypted using industry-standard encryption.
5. The interface must be intuitive enough that users unfamiliar with computers can navigate it with minimal confusion.
6. The system must load order and customer pages in under 2 seconds, even when handling 500+ entries.
7. Daily automatic backups of order histories and customer data must be performed.
8. Access to customer details must be restricted to authorized users only.
9. The system must implement appropriate authentication mechanisms to ensure data security.

*{More to be added}*

### Glossary

| Term                              | Explanation                                                                                                                                                                                                    |
|-----------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Client/Customer                   | A client/customer in a bakery is a regular or business customer who places orders for baked goods, often in bulk or on a recurring basis.                                                                      |
| Command                           | A user input that directs Powerbake to carry out a particular action.                                                                                                                                       |
| Command Line Interface (CLI)      | An interface where users interact with a system by entering text commands into a terminal or console.                                                                                                          |
| Command Terminal                  | A text-based interface for interacting with the computer. Examples include the Command Prompt on Windows, Terminal on macOS, and bash on Linux.                                                                |
| Graphical User Interface (GUI)    | An interface that enables users to engage with a system using visual elements like icons and other graphical indicators.                                                                                       |
| Home Folder                       | The folder where Powerbake is saved. The home folder is used to store data files used by Powerbake.                                                                                                         |
| Hyperlink                         | A clickable reference on a webpage that directs users to another location or file, either on the same page or a different one.                                                                                 |
| JSON (JavaScript Object Notation) | A simple, human-readable data format used for data exchange that is easily parsed and generated by machines.                                                                                                   |
| Local Storage                     | Local storage in an application refers to a method of storing data on a user's device, allowing the application to save information persistently without requiring a server connection.                        |
| Mainstream OS                     | Mainstream operating systems such as Windows, macOS and Linux.                                                                                                                                                        |
| Orders                            | Orders in a bakery refer to customer requests for specific baked goods, either for immediate purchase or scheduled pickup/delivery, often including custom or bulk requests.                                   |
| Parameter                         | A variable in a command that stands in for a specific piece of information that must be supplied when the command is executed.                                                                                 |
| Pastry                            | A Pastry in a bakery refers to baked goods made from dough or batter, often enriched with butter, sugar, and fillings like cream, fruit, or chocolate, including items such as croissants, danishes, and tarts. |
| Person                            | Used interchangeably between client or customers in development |


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file or run `java -jar powerbake.jar`
       Expected: Shows the GUI with a set of sample clients, pastry and order.


### Adding a Client

1. Pre-requisite: Launched the app

2. Test case: `add client Baobao -p 12345678 -e bao@gmail.com -a Sengkang Central`.  
   Expected: New client is added and displayed in client scrolling panel. Success message is displayed.

3. Test case: `add client Baobao -p 12345678`  
   Expected: Error message is displayed. Correct format is displayed in display box.

4. Other incorrect delete commands to try: `add Client`, `add baobao`  
   Expected: Similar to previous.

### Adding a Pastry

1. Pre-requisite: Launched the app

2. Test case: `add pastry Croissant -pr 5.00`.  
   Expected: New pastry is added and displayed in pastry scrolling panel. Success message is displayed.

3. Test case: `add pastry Croissant`  
   Expected: Error message is displayed. Correct format is displayed in display box.

4. Other incorrect delete commands to try: `add Pastry`, `add pastry Bao -pr 5.111`  
   Expected: Similar to previous.

### Adding an Order

1. Pre-requisite:
    1. Launched the app
    2. Add at least one Client and Pastry

2. Test case: `add order 1 -pn Croissant -q 100`.  
   Expected: New order for client of index 1 is added and displayed in order scrolling panel. Success message is shown.

3. Test case: `add order 1`  
   Expected: Error message is displayed. Correct format is displayed in display box.

4. Other incorrect delete commands to try: `add order 1 -pn Croissant`, `add order`  
   Expected: Similar to previous.

### Delete a Client

1. Pre-requisite:
    1. Launched the app
    2. Have at least one client

2. Test case: `delete client 1`.  
   Expected: Delete client of index one and removed from client scrolling panel. Success message displayed.

2. Test case: `delete client -1`.  
   Expected: Error message displaying invalid index.

4. Other incorrect delete commands to try: `delete Client`   
   Expected: Similar to previous.

### Delete a Pastry

1. Pre-requisite:
    1. Launched the app
    2. Have at least one pastry

2. Same as client command above

### Delete an Order

1. Pre-requisite:
    1. Launched the app
    2. Have at least one order

2. Same as client command above

### Edit a Client

1. Pre-requisite:
    1. Launched the app
    2. Have at least one client

2. Test case: `edit client 1 -n Bobby`.  
   Expected: Edit client's name of index one to Bobby. Success message displayed.

2. Test case: `edit client Bobby`.  
   Expected: Error message displaying invalid format.

4. Other incorrect delete commands to try: `edit client 1`   
   Expected: Similar to previous.

### Edit a Pastry

1. Pre-requisite:
    1. Launched the app
    2. Have at least one pastry

2. Test case: `edit pastry 1 -n Pie`.  
   Expected: Edit Pastry's name of index one to Pie. Success message displayed.

2. Test case: `edit pastry Pie`.  
   Expected: Error message displaying invalid format.

4. Other incorrect delete commands to try: `edit pastry`   
   Expected: Similar to previous.

### Edit a Order

1. Pre-requisite:
    1. Launched the app
    2. Have at least one order

2. Test case: `edit order 1 -s delivered`.  
   Expected: Edit order's status to delivered. Success message displayed.

2. Test case: `edit order 1`.  
   Expected: Error message displaying invalid format.

4. Other incorrect delete commands to try: `edit order`   
   Expected: Similar to previous.

### Find a Client

1. Pre-requisite:
    1. Launched the app

2. Test case: `find client Bobby`.  
   Expected: Display client list with matching keywords.

2. Test case: `find Bobby`.  
   Expected: Error message displaying invalid format.


### Find a Pastry, Order

1. Same as with finding a client above.

### View all Clients

1. Pre-requisite:
    1. Launched the app

2. Test case: `view client`.  
   Expected: Display full client list.

2. Test case: `view`.  
   Expected: Error message displaying invalid format.


### View all Pastry, Orders

1. Same as with viewing a client above.
