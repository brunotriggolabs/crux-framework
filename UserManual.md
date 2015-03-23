# Crux 4.0.0 #

<font color='#8B0000'>
<b> Crux 5 is now released! Some of the features presented above are slightly different in new release. A new documentation is available in a new  Crux website: <a href='http://www.cruxframework.org/'>www.cruxframework.org</a>.<br>
</b>
</font>

# 1 Introduction #

_**Crux**_ applications are basically GWT applications. It means you must attempt to all GWT restrictions in terms of source code limitations
and project structure directives.
If you are not familiar with GWT, please consult its [documentation](http://code.google.com/intl/pt-BR/webtoolkit/) first.

This documentation will show you, in a detailed and gradual way, how to use the _**Crux**_ framework, by exploring and exemplifying its features.
Here you will also learn the concepts behind the code and understand how does _**Crux**_ work internally.
In order to easy follow this documentation you will need:
  * A [Java SE Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/index.html), version 1.6 or higher;
  * An [Eclipse](http://www.eclipse.org/downloads/packages/) IDE;

To see a live sample of _**Crux**_ features, take a look at the [Showcase Application](http://showcase.cruxframework.org/).
## 1.1 _Crux_ and GWT ##

Current _**Crux**_ version uses GWT 2.2.x and supports all its features.

# 2 Quick Start #

## 2.1 Install ##

  * Download the latest _**Crux**_ release [here](http://code.google.com/p/crux-framework/downloads/list);
  * Unzip the archive into a folder you want;
  * A folder named `crux-x.x.x` will be created;
  * Open a shell command window;
  * Set `crux-x.x.x` as your current folder;
  * Be sure that your default JVM is the version 1.6 or higher (you can do this by running the command `java -version`);
  * Execute the `start.cmd` or `start.sh` file, according to your operating system;
    * The script will ask you about the location of the GWT jars. You will have two options: 1) Download them from the internet; 2) inform an existing GWT folder in your machine. If you don't have the GWT 2.2 jars, select the first option. The next time you run the installer script, this step will no longer be necessary.
  * The _**Crux QuickStart**_ tool will start, opened in your default web browser:

> ![http://crux-framework.googlecode.com/svn/wiki/images/quickstart-begin.gif](http://crux-framework.googlecode.com/svn/wiki/images/quickstart-begin.gif)

  * Click the `Generate New Crux Application` button and follow the instructions. At the end of the wizard, an Eclipse project will be generated for you.
  * Import the freshly generated project into your Eclipse IDE, and you will get something like this:

> ![http://crux-framework.googlecode.com/svn/wiki/images/generated-project.gif](http://crux-framework.googlecode.com/svn/wiki/images/generated-project.gif)

  * Run the `<your project name>.launch` file, located at the project root folder;
  * If everything's gone OK, the GWT DevMode console will appear, and your first application will look like this:

> ![http://crux-framework.googlecode.com/svn/wiki/images/generated-project-running.gif](http://crux-framework.googlecode.com/svn/wiki/images/generated-project-running.gif)

## 2.2 Environment Configuration ##

To enable auto-completion on your _**Crux**_ pages you must add the project catalog to your Eclipse. This project catalog is located under the folder `xsd` (`crux-catalog.xml`) and includes information about all the _**Crux**_ widget libraries that are present in your project classpath.

To add the catalog file to your Eclipse catalogs list, just go to

```
   Window->Preferences...->XML Catalog
```

Then, select `User Specified Entries` and choose `Add...`. Choose `Next Catalog` and inform the path to the `crux-catalog.xml` file.

## 2.3 Sample Application ##

Here we will explore the application generated in the previous section, explaining each single part of it.

  * The `war` folder is the root context of your application and contains:
    * a WEB-INF folder, compliant with JEE specifications, where you will find:
      * the `web.xml` file (more details at [4.3.1 Web.xml](UserManual#4.3.1_Web.xml.md));
      * the `lib` folder, which stores all _**Crux**_ jar needed (more details at section [Setup](UserManual#4.3_Setup.md));
      * a `classes` folder, which is the output for java compilation;
    * a `<the name you gave>.crux.xml` file: the _welcome file_ of your application (more about _**Crux**_ XML files at [3.1.2 Writing XML Pages](UserManual#3.1.2_Writing_XML_Pages.md);
  * the `build` folder, containing:
    * a `lib` folder, containing the files needed by _**Crux**_ for development purposes only;
    * a `build.xml` ant file, which defines the following tasks:
      * **dist**: generates the war file for deployment;
      * **compile-scripts**: invokes the _**Crux**_ compilation, generating the static files that can be tested in browsers;
      * **fast-compile-scripts**: almost equals to the previous, but faster, because generates non-optimal compiled files, by calling GWT compiler with the `-draftCompile` parameter;
      * **generate-schemas**: generates all XSD files you need to auto-complete your XML pages code.
  * the `src` folder, containing the source files:
    * `<the name you gave the module>.gwt.xml` - a GWT module which extends _**Crux**_ modules (like shown at [Coding Client Side](UserManual#3_Coding_Client_Side.md));
    * `<the same module package>.client.controller.MyController.java` - a client-side controller for the welcome page (see [3.3.3 Controller](UserManual#3.3.3_Controller.md));
    * `<the same module package>.client.remote.GreetingService.java` -  a client-side business interface;
    * `<the same module package>.client.remote.GreetingServiceAsync.java` -  a client-side asynchronous interface for accessing the server;
    * `<the same module package>.server.GreetingServiceImpl.java` -  the server-side business class;
> for more about last three items, see the [4.1 Writing Server-Side Code](UserManual#4.1_Writing_Server-Side_Code.md) section.

# 3 Coding Client Side #

Your modules must inherit _**org.cruxframework.crux.core.Crux**_. You don't need to specify an EntryPoint, because _**Crux**_ defines a basic one that loads its engine.

The following example shows a typical module which can use all _**Crux**_ basic features:
```
<module rename-to='mymodule'>
	<inherits name='org.cruxframework.crux.core.Crux'/>
	<inherits name='org.cruxframework.crux.gwt.CruxGWTWidgets'/>
	<inherits name='org.cruxframework.crux.widgets.CruxWidgets'/>
</module>
```

The code above creates a module that inherits the _**Crux**_ core and two sets of widgets included in the default distribution.
  * The [CruxGWTWidgets](Widgets#1_GWT_Widgets.md)  set contains the code to integrate all widgets that are distributed directly with GWT.
  * The  [CruxWidgets](Widgets#2_Crux_Widgets.md) set contains some complex widgets like MaskedTexBox, multi-frame capable dialogs, etc. It's packaged in the `crux-widgets.jar` file.

## 3.1 Building User Interface ##

Any GWT widget can be used in user interface construction. Consult the [Widget Developer Manual](WidgetDeveloperManual.md) for information about how to use custom widgets with _**Crux**_.

To add widgets to your pages, you can use these methods:
  1. Create a page as a XML file and use some XSDs to enable auto completion on your favorite editor.
  1. Programmatically instantiate widgets, exactly as you already do using with pure GWT.

### 3.1.1 Pages as XML Files ###
```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <gwt:textBox id="myBox" />
       <gwt:button id="myButton" text="Hello" onClick="clientHandler.helloWorld" />
   </body>
</html>
```

You must attempt to:
  * Your files must have the extension **_.crux.xml_**.
  * In order to enable auto completion, you will need to configure your IDE to point to all XSD files generated by the [Schema Generator](UserManual#5.1_Schema_Generator.md):
> > If you are using an Eclipse based IDE, it can be done at the menu **Window -> Preferences -> XML -> XML Catalog**

  * Configure the DeclarativeUIFilter in your web.xml files as following (if you use the [Crux Quick Start](UserManual#2_Quick_Start.md) to generate your project, it will already configure the filter for you):
```
<filter>
   <display-name>DeclarativeUIFilter</display-name>
   <filter-name>DeclarativeUIFilter</filter-name>
   <filter-class>org.cruxframework.crux.core.declarativeui.filter.DeclarativeUIFilter</filter-class>
</filter>
<filter-mapping>
   <filter-name>DeclarativeUIFilter</filter-name>
   <url-pattern>*.html</url-pattern>
</filter-mapping>
```

The **.crux.xml** files are used to make development easier. When you generate the final application distribution file, the _**Crux**_ compiler translate those pages into **.html** pages. See [Crux Compiler](UserManual#5.3_Crux_Compiler.md) for more information.

It means that you can, for example, create a page named `index.crux.xml`, but the url you must pass to the browser will refer to `index.html`.

### 3.1.2 Instantiating Widgets Programmatically ###

You can instantiate widgets exactly as you do using pure GWT.
```
   ...
   Button myButton = new Button();
   myButton.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event)
      {
          Window.alert("hello");
      }
   });
   ...
```

## 3.2 Screen ##

_**Crux**_ creates an abstraction over the page that is named **_Screen_**. Declaratively, you can refer to it using a `<screen>` tag:

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen onClose="clientHandler.onClose" onLoad="clientHandler.onLoad" useController="clientHandler" />
       <gwt:textBox id="myBox" />
       <gwt:button id="myButton" text="Hello" onClick="clientHandler.helloWorld" />
   </body>
</html>
```

The screen can be retrieved programmatically by a call to the static method `Screen.get()`.
Through Screen, you can:
  1. Access any widget created declaratively on pages;
  1. Add handlers to Window events, like `load`, `close` or `resize`;
  1. Manage the browser's history, without need to add a hidden frame directly on the page;
  1. Block and unblock the user interaction with the page;
  1. Create data sources programmatically;
  1. Access some other information, like the user's locale.


### 3.2.1 Accessing Screen Widgets ###

To access screen widgets, you can use the static method `Screen.get(screenId)`.

```
	Button myButton = (Button)Screen.get("myButton");
	or
	Button myButton = Screen.get("myButton", Button.class);
```

You can also create an interface to access your widgets on screen. Your interface must extend the interface ScreenWrapper and the methods must follow the pattern: `<widgetType> get<WidgetID>()`. See the example:
```
@Controller("myController")
public class MyClass
{
	public static interface MyScreen extends ScreenWrapper
	{
		Button getMyButton();
		TextBox getMyBox();
	}
	
	@Create
	protected MyScreen myScreen;

	public void myMethod()
	{
		myScreen.getMyBox().setValue("Test");
	}
}
```

We recommend you to write wrappers for your screens because it is more elegant and avoids mismatches and the spread of the widgets' IDs throughout your code.

### 3.2.2 Screen Events ###

_**Crux**_ Screen support the following events:
| **Event** | **Description** |
|:----------|:----------------|
|Load|Called when page loads. It is fired after the screen's building process is completed|
|Close|Called when page is closed|
|Closing|Called before close the page|
|Resize|Called on page resize|
|HistoryChanged|Called when back button is pressed|


### 3.2.3 Shared Context ###

The _**Crux**_ Context is a common area where you can store and retrieve data. Each data entry is associated with a key. You can think this mechanism like a web session, but residing on the client side of the application. It means you can store not only general-use information, but user-related data too.

However, you should not trust in its contents when executing security-sensible operations, like verifying authenticity or permissions of your user, for example.

The data you store in a Context are visible to any Window on your application, no matter if it is a Popup, a Tab Page, or an IFrame. So, it is a very useful mechanism to share data between different HTML documents.

### 3.2.3.1 Using Contexts ###

To turn easier the access to context information, you can define an interface that extends the interface `Context`. Its methods must follow the pattern: `<valueType> get<valueKey>()` or `void set<valueKey>(valueType)`. See the example:

```
public interface MyContextWrapper extends Context
{
	Double getValueOne();
	String[] getValueTwo();
	void setValueTwo(String[] value);
}
```


```
@Controller("myController")
public class MyClass
{	
    @Create
    protected MyContextWrapper context;

	public void myMethod()
	{
		context.setValueTwo(new String[]{"Value One", "Value Two"});
		Window.alert(context.getValueTwo()[0]);
	}
}
```

If you pass null as argument for a setter method of a Context Wrapper object, it will remove that value from context. Example:

```
	context.setValueTwo(null);
```

Note that you can have more than one Context Wrapper. Different modules can use different wrappers if you want. However, the area where context information is written is unique.
It's important to advice that you must initialize the context before using it. This can be done this way:

```
Screen.createContext();
```

If you call `Screen.createContext()` more than once, you will erase the context and create a new one. When we talk about calling it only once, we mean once per user session. For example, you may call it when the user logs in your application successfully.

Screen class has the following static methods to support Context management:

| **Method** | **Description** |
|:-----------|:----------------|
|createContext|Initializes the context. It just needs to be called once in one of the modules that are sharing information.|
|clearContext|Removes all context entries|


### 3.2.4 Control History ###

Screen provides a simple mechanism to manage history. Using the static method `Screen.addToHistory(String token)` you can create a history token (exactly as [GWT tokens](http://code.google.com/docreader/#p=google-web-toolkit-doc-1-5&s=google-web-toolkit-doc-1-5&t=DevGuideHistory)). To handle the changes on history, you can add a handler to HistoryChanged events, as you can see in the next example:

```
	// To put a token on history
	Screen.addToHistory("link1Clicked");
```

```
	// To add a HistoryChanged Handler
	Screen.addHistoryChangedHandler(new addValueChangeHandler<String>(){
		public void onValueChange(ValueChangeEvent<String> event)
		{
			Window.alert(event.getValue());
		}
	});
```

or Declaratively:

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="clientHandler" onHistoryChanged='clientHandler.onHistoryChanged' />
   </body>
</html>
```

Note that you don't need here to add any IFrame to your host page, as in pure GWT.

### 3.2.5 Block and Unblock ###

You can block and unblock the user interaction with the page using the static methods `Screen.blockToUser()` e `Screen.unblockToUser()`. See the following example:

```
  ...
  @Create
   protected TestServiceAsync service;

   public void helloWorld()
   {
        Screen.blockToUser();
        service.hello(new AsyncCallback<String>()
        {
                public void onSuccess(String s) 
                {
                        Screen.unblockToUser();
                        Window.alert(s);
                }
                public void onFailure(Throwable e) 
                {
                        Screen.unblockToUser();
                        Window.alert(e.getMessage());
                }
        });
   }   
   ...
```

### 3.2.6 Fragmenting the Code ###

Screen tag has an attribute named `fragment`, that can be used to help you to fragment your code into different javascript files.

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="clientHandler" fragment="fragment1" />
   </body>
</html>
```

All screens are grouped by its _fragment_ attribute. If the screen declares no fragment, all of its code is put on the main script file.

## 3.3 Managing Events ##

It's possible to add event handlers:
  1. Declaratively.
  1. Programmatically.

### 3.3.1 Add Event Declaratively ###
To add an event declaratively, you must create a [Controller](UserManual#3.3.3_Controller.md) and give it a name.

An event declaration must follow the pattern `on<eventName> = "<controllerName>.<methodName>"`.
For example:

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen onClose='clientHandler.onClose' useController="clientHandler" >
	       <gwt:textBox id="myBox" />
	       <gwt:button id="myButton" text="Hello" onClick="clientHandler.helloWorld" />
	   </crux:screen>
   </body>
</html>
```

Note that you must also "import" your controller through the attribute `useController` of the screen's tag.

### 3.3.2 Add Event Programmatically ###

You can still use the default GWT mechanism to add handlers for events programmatically, like:

```
   ...
   Button myButton = new Button();
   myButton.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event)
      {
          Window.alert("hello");
      }
   });
   ...
```

The only point to observe here is that you need to put this code in some controller method that is called declaratively, unless you has overwritten the _**Crux**_ EntryPoint and call it directly from there.

### 3.3.3 Controller ###
Controller classes are called to handle events.

To Create a Controller, you just create a simple java class with the `@Controller` annotation. That annotation has a value property to inform the name of the controller. That is the name used on pages to point to the controller.

See the following example:

```
@Controller("clientHandler")
public class MyController
{
   ... // event handlers here
}
```

Your controller can have a lot of methods to handle events. These methods must follow the conditions:
  1. It must have public visibility;
  1. It must have zero or one argument. If an argument is present, it must be a GwtEvent and this method only will be able to handle this type of events.
  1. It must be marked with the annotation `@Expose`.

See the example:
```
@Controller("clientHandler")
public class MyController
{
   @Expose
   public void onClose(CloseEvent<Window> event)
   {
       // code here   
   }
   
   @Expose
   public void helloWorld()
   {
       // code here   
   }
   
   @Expose
   public void onClick(ClickEvent event)
   {
       // code here   
   }
   
   protected void myMethod(String string)
   {
       // code here   
   }
}
```

Note that the above controller contains a method that does not follow the conditions to be an event handler (`myMethod`). It cannot be called declaratively.

#### 3.3.3.1 The @Controller Annotation ####

The Controller annotation has the following properties:

| **Property** | **Required** | **Default Value** | **Description** |
|:-------------|:-------------|:------------------|:----------------|
|value|yes| none |defines the name of the controller. Used inside pages to point to the controller|
|stateful|no|`true`|If `true`, one controller object is created and the same instance is <br>used to handle all events. If <code>false</code>, a new instance is used for each new event<br>
<tr><td>autoBind</td><td>no</td><td><code>true</code></td><td>If <code>true</code>, <a href='UserManual#3.3.3.4_Value_Binding.md'>ValueObjects</a> are automatically bound from screen widgets before the event <br>occurs and bound back to screen widgets when the event handling terminates </td></tr>
<tr><td>lazy</td><td>no</td><td> <code>true</code> </td><td>If <code>true</code>, the controller object is built only when first called.</td></tr></tbody></table>

#### 3.3.3.2 The @Create Annotation ####

This annotation can be used to simplify the code for Controllers. It automatically creates an object (according with field type) and initializes the field with this value.

This creation is done by a call to GWT.create method, assuring that any generator eventually associated with the requested type will be called correctly.

See the following example:

```
@Controller("myNewController")
public class MyClass
{
    public static interface MyScreen extends ScreenWrapper
    {
        Button getMyButton();
        TextBox getMyBox();
    }
	
    @Create
    protected MyScreen myScreen;

    @Create
    protected MyContextWrapper context;

    @Create
    protected TestServiceAsync service;

    @Create
    protected MyControllerCrossDoc crossDoc;

    @Expose
    public void myMethod()
    {
        myScreen.getMyBox().setValue("Test");
        context.setValueTwo(new String[]{"Value One", "Value Two"});
    }
}
```

Note that `@Create` can handle service creation too, despite the fact that the variable type is not the same passed to `GWT.create` (in above example would be `TestService`).

The `@Create` annotation does more than simply creating an object. It also makes some initializations for the created object, depending on the field type (e.g. [parameter fields](UserManual#3.3.3.8_Parameters.md) are loaded, etc.)

You must note, however, that to use `@Create` on a field, that field must have public or protected visibility or have a public getter and setter methods.

#### 3.3.3.3 The @Expose Annotation ####

The Expose annotation is used to expose a  method as an event handler.

#### 3.3.3.4 Value Binding ####

_**Crux**_ provides a mechanism to help you to automatically bind values between screen widgets and data objects.

You can create a value object and annotate it with `@ValueObject` annotation. Doing it, you allow _**Crux**_ to populate an object of this type with values present on screen widgets before running the event handler methods. After method execution, the screen is also updated with any change in these objects.

See the following example:

```
@ValueObject
public class Person
{
	private String name;
	private String phone;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
```

```
@Controller("myController")
public class MyClass
{
    @Create
    protected Person person;

    @Expose
    public void myMethod()
    {
    	Window.alert(person.getName());
    	person.setPhone("1234-5678");
    }
}
```

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt">
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="myController" >
	       <gwt:textBox id="name" />
	       <gwt:textBox id="phone" />
	       <gwt:button id="myButton" text="Hello" onClick="myController.myMethod" />
	   </crux:screen>
   </body>
</html>
```

In the above example, the value of the "name" textBox on page will be bound to field "name" of the Person object created by controller (the same is `true` to "phone"). After the handler execution, the changes made in the value object will be reflected on page.

If want, you can use the `@ScreenBind` annotation on value object field to inform _**Crux**_ which widget will be bound to this field. The above example can be changed to:

```
@ValueObject
public class Person
{
	@ScreenBind("person.name")
	private String name;

	@ScreenBind("person.phone")
	private String phone;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
```

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="myController" >
	       <gwt:textBox id="person.name" />
	       <gwt:textBox id="person.phone" />
	       <gwt:button id="myButton" text="Hello" onClick="myController.myMethod" />
	   </crux:screen>
   </body>
</html>
```

You can also control which fields of a value object must be bound to some widget screen. `@ValueObject` annotation has a boolean property named `bindWidgetByFieldName` (default to `true`). Setting this value to `false` make _**Crux**_ to does not bind all value object fields to widgets automatically. If you set this, you must specify for each field, the name of the widget that it will be bound (through `@ScreenBind` annotation).

See the following example:
```
@ValueObject(bindWidgetByFieldName=false)
public class Person
{
	@ScreenBind("person.name")
	private String name;

	@ScreenBind
	private String phone; //will be bound to "phone" widget
	
	private String address; // will not be bound.
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
```

Any field in a value object can be bound to a widget if it:
  * is a primitive type (or a primitive wrapper);
  * is a CharSequence type (String, StringBuilder, StringBuffer, etc);
  * is a Date type (java.util.Date, java.sql.Date, java.sql.Timestamp, etc);
  * is an Enum type;
  * is any type annotated with `@ValueObject` annotation;
  * has public or protected visibility or has public getter and setter methods.

The following code shows more examples:

```
@ValueObject
public class Person 
{
	private String name;
	private String phone;
	private Date birth;
	private Address address; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
```

```
@ValueObject
public class Address
{
	private String street;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
}
```

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux"
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="myController" >
	       <gwt:textBox id="name" />
	       <gwt:dateBox id="birth" />
	       <gwt:textBox id="street" />
	       <gwt:button id="myButton" text="Hello" onClick="myController.myMethod" />
	   </crux:screen>
   </body>
</html>
```

In the above example, the field `phone` will not be bound to any widget, once there is not any widget with id `phone`. Another important point is that each field only can be bound to widgets that are able to return values of the same type of the field.

The field `birth` only can be bound to widgets that implements `HasValue<Date>` (like `DateBox`) or `HasFormatter` and is associated with a formatter that returns `Date` objects. See the [Formatters](UserManual#3.7_Formatters.md) section to more info about formatters).

If you want to disable the automatic value binding mechanism to a specific controller, you can set the `@Controller` property `autoBind` to `false`. You can, later, control the value object and screen updates through methods `Screen.updateScreen(controller)` and
`Screen.updateController(controller)`. See the following example:

```
@Controller(value="myController", autoBind=false)
public class MyClass
{
    @Create
    protected Person person;

    @Expose
    public void myMethod()
    {
    	Screen.updateController(this);
    	Window.alert(person.getName());
    	person.setPhone("1234-5678");
    	Screen.updateScreen(this);
    }
}
```

#### 3.3.3.5 Using Controllers on Screen ####

To inform that a controller will be used on a screen, you must explicitly "import" it using the `useController` attribute.

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:crux="http://www.cruxframework.org/crux" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <crux:screen useController="myController, myOtherController" >
	   </crux:screen>
   </body>
</html>
```

It is necessary for performance reasons.

However, if you want that a controller be imported in every screen in your application, you can put an annotation `@Global` in the Controller class.

```
@Global
@Controller("myController")
public class MyClass {
       // code here
}
```

That will make those controllers be available even if no `useController` declaration is present on your screen.

#### 3.3.3.6 Communication Between Controllers ####

Crux supports communication between different controllers, even if located on different screens (documents).

The first step to implement a cross document communication is creating an interface extending the `CrossDocument` interface.

```
public interface MyControllerCrossDoc extends CrossDocument {
    void myMethod(String str, boolean b, MySerializableDTO dto);
    List<String> myOtherMethod() throws MyCustomException;
}
```

The methods declared on this interface are those which you want to make available for calls from other controllers. All method parameters and return types
must be a primitive type (or a primitive wrapper), an Enum or implement `java.io.Serializable` or `com.google.gwt.user.client.rpc.IsSerializable`.

The second step is to make your controller class implement the new interface.

```
@Controller("myController")
public class MyController implements MyControllerCrossDoc {
    public void myMethod(String str, boolean b, MySerializableDTO dto){
       // code here
    }
    
    public List<String> myOtherMethod() throws MyCustomException{
       // code here
    }
    
    // You can have any other methods here.
    @Expose
    public void myEventHandlerMethod(ClickEvent event){
       // code here
    }
}
```

To ensure that everything will run fine, you must obey the following rules:
  1. Both controller class and CrossDocument interface must be coded on the same package;
  1. The name of the CrossDocument interface must have the form: `"<Controller Name>CrossDoc"`.

See the previous code for an example.

Now we can show how to make the call from a second controller.

```
@Controller("mySecondController")
public class MySecondController {
    @Create
    protected MyControllerCrossDoc crossDoc; // you could also use GWT.create(MyControllerCrossDoc.class)

    @Expose
    public void onClick(ClickEvent event){
       
       crossDoc.myMethod("test", true, new MySerializableDTO());
       
       try{
       	  List<String> result = crossDoc.myOtherMethod();
       	  //do something with result...
       }catch(MyCustomException e){
          // handle error
       }
    }
}
```

On the previous example, the crossDoc object will call the methods on a controller located on the same screen of the caller object.

If you want to inform a new target for the call, you must cast the crossDoc object to `TargetDocument` interface and set the target of the call.

```
@Controller("mySecondController")
public class MySecondController {
    @Create
    protected MyControllerCrossDoc crossDoc; // you could also use GWT.create(MyControllerCrossDoc.class)

    @Expose
    public void onClick(ClickEvent event){
       ((TargetDocument)crossDoc).setTarget(Target.TOP);       
       crossDoc.myMethod("test", true, new MySerializableDTO());
    }
}
```

The previous example calls the method on the "myController" controller located on the top window of the current screen.

The following table shows all methods present on `TargetDocument` interface that can be used to inform the target for the calls.

| **Method** | **Description** |
|:-----------|:----------------|
|void setTarget(Target target)|Sets the target for the cross document call. <br />`Target` is an Enum described on following list.|
|void setTargetFrame(String frame);|Sets a target frame for the cross document call.|
|void setTargetSiblingFrame(String frame);|Sets a target sibling frame for the cross document call.|
|void setTargetWindow(JSWindow jsWindow);|Sets the target window for the cross document call.<br />`JSWindow` is just a `JavaScriptObject` to wrap the native window object|

The `Target` Enum supports the following values:

| **Method** | **Description** |
|:-----------|:----------------|
|  TOP  |  Points the CrossDocument object to the [\_top](http://www.w3.org/TR/html4/types.html#type-frame-target) window  |
|  PARENT  |  Points the CrossDocument object to the [\_parent](http://www.w3.org/TR/html4/types.html#type-frame-target) window  |
|  SELF  |  Points the CrossDocument object to the [\_self](http://www.w3.org/TR/html4/types.html#type-frame-target) (current) window  |
|  OPENER  |  Points the CrossDocument object to the window.opener (useful when working with window.open mechanism)  |
|  ABSOLUTE\_TOP  | Points the CrossDocument object to the outermost window of your application, no matter<br />if executed from inside a frame or other window (e.g. _blank, popup, etc.)_|



Here are some examples:

```
  ((TargetDocument)crossDoc).setTarget(Target.OPENER);
  ((TargetDocument)crossDoc).setTarget(Target.TOP);
  ((TargetDocument)crossDoc).setTargetFrame("myInternalNamedFrame");
  ((TargetDocument)crossDoc).setTargetWindow(Popup.getOpener());       
  ((TargetDocument)crossDoc).setTargetWindow(DynaTabs.getTabWindow("secondTab"));
  ((TargetDocument)crossDoc).setTargetWindow(DynaTabs.getSiblingTabWindow("secondTab"));
```


#### 3.3.3.7 Validation ####

_**Crux**_ supports declaration of validators for a controller handler method. A Validator method is called before the handler method itself. If it runs without problem, the handler is called. If it throws any exception, the handler execution is aborted and a message is reported to the user through the _**Crux**_ [Error Handlers](UserManual#3.6_Handling_Errors.md).

To declare a validator method to a given handler method, you just need to use the `@Validate` annotation :

```
@Controller("clientHandler")
public class MyController
{
   
   @Validate("myValidationMethod")
   @Expose
   public void onClose(CloseEvent<Window> event)
   {
       // code here   
   }
      
   protected void myValidationMethod(CloseEvent<Window> event) throws ValidateException
   {
       // code here   
   }
}
```

If no value is passed to the `@Validate` annotation, _**Crux**_ tries to find a method named `validate<methodName>`.

```
@Controller("clientHandler")
public class MyController
{
   
   @Validate
   @Expose
   public void onClose(CloseEvent<Window> event)
   {
       // code here   
   }
      
   protected void validateOnClose() throws ValidateException
   {
       // code here   
   }
}
```

Note that the validate method can receive a parameter of the same type of the main method parameter type (as in the first example) or no parameter (as in the second example).


#### 3.3.3.8 Parameters Binding ####

_**Crux**_ provides you a mechanism to automatically bind values received from the window's URL into data objects. This can be done using the annotations `@Parameter` and `@ParameterObject` on controller fields and DTO classes.

You can annotate a controller field with the annotation `@Parameter` as in the following example:

For the following URL...

```
	http://myhost.com/myapp/mymodule/mypage.html?person=Thiago&parameterName=123
```

...you may have a Controller like this:

```
@Controller("myController")
public class MyClass
{
    @Parameter
    protected String person;

    @Parameter(value="parameterName", required=true)
    protected int field;

    @Expose
    public void myMethod()
    {
    	Window.alert(person);
    	Window.alert(Integer.toString(field));
    }
}
```

In the above example, the value of the "person" parameter on window URL will be bound to field "person" of the controller (the same is `true` to "field").

The `@Parameter` annotation has two fields:
| **Property** | **Default Value** | **Description** |
|:-------------|:------------------|:----------------|
|value| `empty` |defines the name of the parameter. If not present, the field name is used|
|required| `false` |If `true`, a validation is done to ensure that the parameter is present in the URL.|

If a validation error occurs while binding a parameter, a message is reported to the user through the Crux [Error Handlers](UserManual#3.6_Handling_Errors.md). A validation error can occur due to type conversion error or by the lack of a required parameter.

Another way to bind parameters is to create an object and annotate it with `@ParameterObject` annotation, exactly as you do with [Value Objects](UserManual#3.3.3.4_Value_Binding.md). Doing it, you allow _**Crux**_ to populate an object of this type with values present on window URL parameters.

See the following example:

```
@ParameterObject
public class Parameters
{
	private String person;
	private int field;
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
}
```

```
@Controller("myController")
public class MyClass
{
    @Create
    protected Parameters parameters;

    @Expose
    public void myMethod()
    {
    	Window.alert(parameters.getPerson());
    	Window.alert(Integer.toString(parameters.getField()));
    }
}
```


If want, you can use the `@Parameter` annotation on parameter object field to inform _**Crux**_ which parameter will be bound to this field. The above example can be changed to:

```
@ParameterObject
public class Parameters
{
	@Parameter("personName")
	private String person;
	
	@Parameter("fieldParameter", required=true)
	private int field;
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
}
```

You can also control which fields of a parameter object must be bound to some window parameter. `@ParameterObject` annotation has a boolean property named `bindParameterByFieldName` (default to `true`). Setting this value to `false` makes _**Crux**_ do not bind all parameters automatically. If you set this, you must specify, for each field, the name of the parameter that will be bound to it (through `@Parameter` annotation).

See the following example:
```
@ParameterObject(bindParameterByFieldName=false)
public class Parameters
{
	@Parameter("personName")
	private String person;
	
	@Parameter("fieldParameter", required=true)
	private int field;
	
	private int field2; // not bound
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
	public int getField2() {
		return field2;
	}
	public void setField2(int field2) {
		this.field2 = field2;
	}
}
```

Any field in a parameter object can be bound if it:
  * is a primitive type (or a primitive wrapper);
  * is a CharSequence type (String, StringBuilder, StringBuffer, etc);
  * is a Date type (java.util.Date, java.sql.Date, java.sql.Timestamp, etc);
  * is an Enum type;
  * is any type annotated with `@ParameterObject` annotation;
  * has public or protected visibility or has public getter and setter methods.

## 3.4 I18N ##

_**Crux**_ supports i18n for widgets created declaratively. The native [GWT mechanism](http://code.google.com/intl/pt-BR/webtoolkit/tutorials/1.6/i18n.html) is still valid.

You can use the following pattern to tell _**Crux**_ that you want to use a GWT message or a constant value in a widget tag declaration:

```
   "${<messageResource>.<messageEntry>}"	
```

For example, suppose the following messages interface:

```
public interface MyMessages extends Messages
{
	@DefaultMessage("my message")
	String myMessage();
}
```

And the crux page that uses it:

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <gwt:label id="label" text="${myMessages.myMessage}" />
   </body>
</html>
```

In the above example, the message resource name is derived from interface name. If you want to change this value, you can use the annotation `@MessageName` in your messages interface.

See the example:

```
@Name("msg")
public interface MyConstants extends Constants
{
	@DefaultStringValue("my message")
	String myMessage();
}
```

```
   ...
       <bas:label id="label" text="${msg.myMessage}" />
   ...
```

## 3.5 Client-Server Communication ##

You can use [GWT RPC](http://code.google.com/intl/pt-BR/webtoolkit/tutorials/1.6/RPC.html) and [JSON](http://code.google.com/intl/pt-BR/webtoolkit/tutorials/1.6/JSON.html) support to communicate with server. _**Crux**_, however, adds some few features to pure GWT RPC mechanisms to turn this process easier.

With _**Crux**_, you can use a _Front Controller_ on the server-side. The use of this _Front Controller_ allows you to make just one mapping in your web.xml file.

In other words, you don't need to add a new servlet declaration on your GWT module neither on your web.xml for each new service declared.

Other improvement is the use of the  [3.3.3.2\_The_@Create\_Annotation @Create annotation]. It will create the service object and set its  entry point name to point to the_Front Controller_automatically._

The following example shows all this features together:

```
package crux.examples.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;

public interface GreetingService extends RemoteService
{
        public String getHelloMessage(String name);
}
```

```
public class MyController {
        @Create
        protected GreetingServiceAsync service; 
        
        @Expose
        public void sayHello() {
                service.getHelloMessage("Thiago", new AsyncCallbackAdapter<String>(this){
                                @Override
                                public void onComplete(String result){
                                   Window.alert(result);
                                }
                        }
                );
        }
}
```

Note that the service interface does not use the annotation [RemoteServiceRelativePath](http://code.google.com/intl/pt-BR/webtoolkit/tutorials/1.6/RPC.html). It will assume the mapping to the Front Controller. If that annotation was present, _**Crux**_ would use it to set the entry point name.

Another point to observe in the above example is the use of the abstract class `AsyncCallbackAdapter` in the place of GWT `AsyncCallback` interface.

That class does the following:
  1. Implements a default error handler that will delegate to [Crux Error Handler](UserManual#3.6_Handling_Errors.md) any error received.
  1. If controller autoBind for [ValueObjects](UserManual#3.3.3.4_Value_Binding.md) is enabled, it will automatically update screen with value object properties after process the `onComplete()` method.


So, the two following approach are equivalent:

```
@Controller(value="myController", autoBind=true)
public class MyController {
        @Create
        protected GreetingServiceAsync service; 
        
        @Create
        protected Person aValueObject; 

        @Expose
        public void sayHello() {
                service.getHelloMessage("Thiago", new AsyncCallbackAdapter<String>(this){
                                @Override
                                public void onComplete(String result){
                                   aValueObject.setName("Thiago");
                                }
                        }
                );
        }
}
```

```
@Controller(value="myController", autoBind=false)
public class MyController {
        @Create
        protected GreetingServiceAsync service; 
        
        @Create
        protected Person aValueObject; 

        @Expose
        public void sayHello() {
                Screen.updateController(this);
                service.getHelloMessage("Thiago", new AsyncCallback<String>(this){
                                public void onSuccess(String result){
                                   aValueObject.setName("Thiago");
                                   Screen.updateScreen(this);
                                }
                                public void onFailure(Throwable e){
                                    Crux.getErrorHandler().handleError(e.getLocalizedMessage(), e);
                                    Screen.updateScreen(this);
                                }
                        }
                );
        }
}
```

### 3.5.1 Server Sensitive Methods ###

_**Crux**_ supports the [Synchronizer Token](http://www.corej2eepatterns.com/Design/PresoDesign.htm) pattern for sensitive methods protection.
This pattern helps to avoid the duplicated request problem and [CSRF](http://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet)
attacks.

To inform _**Crux**_ that a server method is sensitive, you just need to put the annotation `@UseSynchronizerToken` on the service interface method.

See the following example:

```
package crux.examples.client.remote;

public interface GreetingService extends RemoteService
{
   @UseSynchronizerToken
   public String getHelloMessage(String name);
}
```

This annotation accepts the following attributes:

| **Attribute** | **Type** | **Default** | **Description** |
|:--------------|:---------|:------------|:----------------|
|notifyCallsWhenProcessing|boolean|`true`|If this property is `true`, when the user tries to send a duplicated request,<br> an informative message is shown. To change the message, use the property <code>methodIsAlreadyBeingProcessed</code><br> on ClientMessages.properties file.<br>
<tr><td>blocksUserInteraction</td><td>boolean</td><td><code>true</code></td><td> If this property is <code>true</code>, when a request to a sensitive method is fired,<br> the screen became blocked to user, until the method finishes.  </td></tr></tbody></table>


<h2>3.6 Handling Errors</h2>

<i><b>Crux</b></i> provides two basic interfaces for client errors reporting.<br>
<ul><li><code>ErrorHandler</code>
</li><li><code>ValidationErrorHandler</code></li></ul>

Those interfaces are used always occurs an error that needs to be reported to user (or to developer).<br>
The interface <code>ErrorHandler</code> is called to report errors in application code (bad use of the framework, or an uncaught exception) and <code>ValidationErrorHandler</code> is called to report errors caused by a bad use of the application (typically validations over the screen state, based in business rules, before performing an action).<br>
<br>
<i><b>Crux</b></i> provides a default <code>ErrorHandler</code>  that implements both interfaces and, for the both types of errors:<br>
<ul><li>Logs in GWT console all exceptions received.<br>
</li><li>Shows the messages using the <code>Window.alert()</code> method.</li></ul>

If you want to change the default error handling class, you can specify in your module file:<br>
<br>
<pre><code>	&lt;!-- Specify the implementation to ErrorHandler.--&gt;<br>
	&lt;replace-with class="YourErrorHandlerClass"&gt;<br>
		&lt;when-type-assignable class="org.cruxframework.crux.core.client.errors.ErrorHandler" /&gt;<br>
	&lt;/replace-with&gt;<br>
<br>
	&lt;!-- Specify the implementation to ValidationErrorHandler.--&gt;<br>
	&lt;replace-with class="YourValidationErrorHandlerClass"&gt;<br>
		&lt;when-type-assignable class="org.cruxframework.crux.core.client.errors.ValidationErrorHandler" /&gt;<br>
	&lt;/replace-with&gt;<br>
<br>
</code></pre>

<h2>3.7 Formatters</h2>

Formatters can be used to provide widgets the following capabilities:<br>
<ul><li>Input/Output formatting/unformatting;<br>
</li><li>Input masking (masks can be defined with regular expressions);<br>
</li><li>Data conversions to/from string.</li></ul>

A widget must implement the interface <code>HasFormatter</code> to be associated with a formatter. Formatters, in turn, can be defined by implementing the interface <code>Formatter</code>.<br>
<br>
Here you can see how a formatter could be used:<br>
<br>
<pre><code>&lt;html xmlns="http://www.w3.org/1999/xhtml" <br>
      xmlns:c="http://www.cruxframework.org/crux"<br>
      xmlns:crux="http://www.cruxframework.org/crux/widgets" &gt;<br>
   &lt;head&gt;<br>
       &lt;script language="javascript" src="cruxtest/cruxtest.nocache.js"&gt;&lt;/script&gt;<br>
   &lt;/head&gt;<br>
   &lt;body&gt;<br>
      &lt;c:screen useFormatter="phone, date" /&gt;<br>
      &lt;crux:maskedTextBox id="maskedTextBox" formatter="phone" width="90"/&gt;   <br>
      &lt;crux:maskedTextBox id="dateMaskedTextBox" formatter="date" width="90"/&gt;   <br>
   &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

The widget <code>MaskedTextBox</code> is delivered with <i><b>Crux</b></i> default distribution. It is like a GWT TextBox, but provides formatting support.<br>
<br>
The following example shows how a custom formatter could be defined:<br>
<br>
<pre><code>@FormatterName("phone")<br>
public class PhoneFormatter implements Formatter<br>
{<br>
	public String format(Object input)<br>
	{<br>
		if (input == null || !(input instanceof String) || ((String)input).length() != 10)<br>
		{<br>
			return "";<br>
		}<br>
		<br>
		String strInput = (String) input;<br>
		<br>
		return "("+strInput.substring(0,2)+")"+strInput.substring(2,6)+"-"+strInput.substring(6);<br>
	}<br>
<br>
	public Object unformat(String input) throws InvalidFormatException<br>
	{<br>
		if (input == null || !(input instanceof String) || ((String)input).length() != 13)<br>
		{<br>
			return "";<br>
		}<br>
		String inputStr = (String)input;<br>
		inputStr = inputStr.substring(1,3)+inputStr.substring(4,8)+inputStr.substring(9,13);<br>
		return inputStr;<br>
	}<br>
}<br>
</code></pre>

The above code, specifies a formatter that will present its associated widget content (a phone number) in the format:<code> (99)9999-9999</code>.<br>
<br>
The given formatter does not provide input masking feature. To create a version of the formatter with such characteristic, you must implement the interface <code>MaskedFormatter</code>. To create masked formatters for <code>MaskedTextBox</code> widgets, an abstract class that already implements <code>MaskedFormatter</code> can be used. The previous formatter example could be re-written as:<br>
<br>
<pre><code>@FormatterName("phone")<br>
public class PhoneFormatter extends MaskedTextBoxBaseFormatter{<br>
	@Override<br>
	protected String getMask()	{<br>
		return "(99)9999-9999";<br>
	}<br>
<br>
	public String format(Object input){<br>
		if (input == null || !(input instanceof String) || ((String)input).length() != 10)	{<br>
			return "";<br>
		}<br>
		<br>
		String strInput = (String) input;<br>
		<br>
		return "("+strInput.substring(0,2)+")"+strInput.substring(2,6)+"-"+strInput.substring(6);<br>
	}<br>
<br>
	public Object unformat(String input) throws InvalidFormatException{<br>
		if (input == null || !(input instanceof String) || ((String)input).length() != 13)	{<br>
			return "";<br>
		}<br>
		String inputStr = (String)input;<br>
		inputStr = inputStr.substring(1,3)+inputStr.substring(4,8)+inputStr.substring(9,13);<br>
		return inputStr;<br>
	}<br>
}<br>
</code></pre>

Another example:<br>
<br>
<pre><code>@FormatterName("date")<br>
public static class DateFormatter extends MaskedTextBoxBaseFormatter {<br>
<br>
    DateTimeFormat format = DateTimeFormat.getFormat("MM/dd/yyyy");<br>
<br>
    protected String getMask(){<br>
        return "99/99/9999";<br>
    }<br>
<br>
    public Object unformat(String input){<br>
        if (input == null || input.length() != 10){<br>
            return null;<br>
        }<br>
            <br>
        return format.parse(input);<br>
    }<br>
<br>
    public String format(Object input) throws InvalidFormatException {<br>
        if(input == null){<br>
            return "";<br>
        }<br>
        if (!(input instanceof Date)){<br>
            throw new InvalidFormatException();<br>
        }<br>
            <br>
        return format.format((Date) input);<br>
    }<br>
}<br>
</code></pre>


Note that the <code>MaskedFormatter</code> methods (<code>applyMask</code> and <code>removeMask</code>) are already implemented by the abstract class. Only the <code>getMask</code> method must be implemented to specify the pattern used to build the mask.<br>
<br>
<br>
<h2>3.8 Data Sources</h2>

DataSources are objects capable of providing a set of data to widgets that implement <code>HasDataSource</code> interface. DataSources support features like pagination, data sorting and editing.<br>
<br>
<i><b>Crux</b></i> provides a wide range of different DataSources that can be classified by the following criteria:<br>
<br>
<ol><li>How them <b>present the data</b>
<ol><li><b>Paged</b> - PagedDataSources can divide the data into pages<br>
</li><li><b>Scrollable</b> - This kind of DataSource handles all data in a single page<br>
</li></ol></li><li>How they <b>fetch the data</b>
<ol><li><b>Local</b> - This kind of DataSource can load data once and keep it locally on user's browser, so it can be paged, sorted or edited locally.<br>
</li><li><b>Remote</b> - RemoteDatasources load data on demand, as widgets request them.</li></ol></li></ol>

To create a DataSource, you can extend one of the abstract DataSource classes provided by <i><b>Crux</b></i>. The class you should choose depends on which categories (between the exposed above) your DataSource will belong.<br>
<br>
For a complete guide about DataSources (including the complete list of basic DataSource classes), consult the following <a href='UsingDataSources.md'>tutorial</a>.<br>
<br>
<br>
<h2>3.9 Templates</h2>

Templates in <i><b>Crux</b></i> are parameterizable XML files that can be used to:<br>
<ol><li>Create simple components in a declarative way;<br>
</li><li>Create smart fragments that can be used to compose greater pages;<br>
</li><li>Define reusable layout pages.</li></ol>

A template must be defined in a file with the extension <code>.template.xml</code> and  can be placed anywhere under your classpath (even inside a jar file).Template files must follow the schema <code>http://www.cruxframework.org/templates</code>.<br>
<br>
Sections <a href='UserManual#3.9.1.1_Creating_a_Simple_Component.md'>Creating a Simple Component</a>, <a href='UserManual#3.9.1.2_Creating_a_Smart_Fragment.md'>Creating a Smart Fragment</a> and<br>
<a href='UserManual#3.9.1.3_Defining_a_Reusable_Layout.md'>Defining a Reusable Layout</a> shows examples of templates usage.<br>
<br>
After creating a template file, you should run the <a href='UserManual#5.1_Schema_Generator.md'>Schema Generator</a> and re-import the <code>crux-catalog.xml</code> file in your Eclipse IDE. This way you will be able to use your templates with the auto-completion feature.<br>
<br>
A template can be defined to receive parameters and include sub-sections, as shown in the following examples:<br>
<br>
<h3>3.9.1 Examples</h3>

<h4>3.9.1.1 Creating a Simple Component</h4>

The file <code>labeledBox.template.xml</code> defines a template for a simple labeled box component:<br>
<pre><code>&lt;t:template xmlns="http://www.w3.org/1999/xhtml"<br>
	  xmlns:t="http://www.cruxframework.org/templates" <br>
	  xmlns:gwt="http://www.cruxframework.org/crux/gwt" <br>
	  library="custom"&gt;<br>
   <br>
   &lt;gwt:horizontalPanel id="#{id}.hPanel" &gt;<br>
      &lt;gwt:label id="#{id}.label" text="#{label}:" /&gt; <br>
      &lt;gwt:textBox id="#{id}" value="#{value}" /&gt;	  <br>
   &lt;/gwt:horizontalPanel&gt;	  <br>
&lt;/t:template&gt;<br>
</code></pre>

Then, you can use it on any crux page:<br>
<pre><code>&lt;html xmlns="http://www.w3.org/1999/xhtml" <br>
      xmlns:c="http://www.cruxframework.org/templates/custom" &gt;<br>
   &lt;head&gt;<br>
       &lt;script language="javascript" src="cruxtest/cruxtest.nocache.js"&gt;&lt;/script&gt;<br>
   &lt;/head&gt;<br>
   &lt;body&gt;<br>
      &lt;c:labeledBox id="personName" label="Name" value="Type your name here..." /&gt;<br>
   &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

<h4>3.9.1.2 Creating a Smart Fragment</h4>

The File <code>userInfo.template.xml</code> defines a simple header, which can show the login and name of the current user.<br>
It has its own Controller and its own business logic:<br>
<pre><code>&lt;t:template xmlns="http://www.w3.org/1999/xhtml"<br>
      xmlns:t="http://www.cruxframework.org/templates" <br>
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" <br>
      library="custom" useController="userController"&gt;<br>
   &lt;b:HTMLPanel id="userPanel" onLoadWidget="userController.loadUserInfo" &gt;<br>
      &lt;table&gt;<br>
   	     &lt;tr&gt;<br>
   	        &lt;td&gt;Login:&lt;/td&gt;<br>
   	        &lt;td&gt;&lt;gwt:label id="login" /&gt;&lt;/td&gt;<br>
   	     &lt;/tr&gt;<br>
   	     &lt;tr&gt;<br>
   	        &lt;td&gt;Name:&lt;/td&gt;<br>
   	        &lt;td&gt;&lt;gwt:label id="name" /&gt;&lt;/td&gt;<br>
   	     &lt;/tr&gt;<br>
      &lt;/table&gt;<br>
   &lt;/b:HTMLPanel&gt;<br>
&lt;/t:template&gt;<br>
</code></pre>

You can use it on any page you want, without any additional configuration:<br>
<pre><code>&lt;html xmlns="http://www.w3.org/1999/xhtml" <br>
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" <br>
      xmlns:c="http://www.cruxframework.org/templates/custom" &gt;<br>
   &lt;head&gt;<br>
       &lt;script language="javascript" src="cruxtest/cruxtest.nocache.js"&gt;&lt;/script&gt;<br>
   &lt;/head&gt;<br>
   &lt;body&gt;<br>
      &lt;c:userInfo /&gt;<br>
      ...<br>
   &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

<h4>3.9.1.3 Defining a Reusable Layout</h4>
The file <code>pageLayout.template.xml</code> defines a common layout that has a menu located on the left and a place to insert the page body:<br>
<pre><code>&lt;t:template xmlns="http://www.w3.org/1999/xhtml"<br>
      xmlns:t="http://www.cruxframework.org/templates" <br>
      xmlns:crux="http://www.cruxframework.org/crux/widgets"<br>
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" <br>
      xmlns:c="http://www.cruxframework.org/templates/custom" <br>
	  library="custom"&gt;<br>
<br>
   &lt;gwt:dockPanel id="centeringPanel" width="100%" height="100%"&gt;<br>
      &lt;gwt:cell direction="north" height="70" verticalAlignment="top"&gt;<br>
          &lt;c:userInfo /&gt;<br>
      &lt;/gwt:cell&gt;<br>
      &lt;gwt:cell direction="south"&gt;<br>
         &lt;gwt:dockPanel id="menuTabsDividerPannel"&gt;<br>
            &lt;gwt:cell direction="west"&gt;<br>
               &lt;crux:stackMenu id="menu" onLoadWidget="#{onLoadMenu}" &gt;&lt;/crux:stackMenu&gt;<br>
            &lt;/gwt:cell&gt;<br>
            &lt;gwt:cell direction="east"&gt;<br>
               &lt;t:section name="body" /&gt;<br>
            &lt;/gwt:cell&gt;<br>
         &lt;/gwt:dockPanel&gt;<br>
      &lt;/gwt:cell&gt;<br>
   &lt;/gwt:dockPanel&gt;<br>
&lt;/t:template&gt;<br>
</code></pre>

Then, you can use it on any crux page:<br>
<pre><code>&lt;html xmlns="http://www.w3.org/1999/xhtml" <br>
      xmlns:crux="http://www.cruxframework.org/crux" <br>
      xmlns:gwt="http://www.cruxframework.org/crux/gwt" <br>
      xmlns:c="http://www.cruxframework.org/templates/custom" &gt;<br>
   &lt;head&gt;<br>
       &lt;script language="javascript" src="cruxtest/cruxtest.nocache.js"&gt;&lt;/script&gt;<br>
   &lt;/head&gt;<br>
   &lt;body&gt;<br>
      &lt;crux:screen useController="myController" /&gt;<br>
      &lt;c:pageLayout onLoadMenu="myController.loadMenuItems"&gt;<br>
         &lt;c:body&gt;<br>
            &lt;!-- Body comes here --&gt;<br>
            &lt;gwt:label id="test" text="Hello World!!" /&gt;<br>
         &lt;/c:body&gt;<br>
      &lt;/c:pageLayout&gt;<br>
   &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

Note that the above template reuses another template (userInfo) defined in previous section.<br>
<br>
<h3>3.9.2 Templates attributes and children</h3>

According with template.xsd file, the tag <code>&lt;template&gt;</code> declare the following attributes:<br>
<br>
<table><thead><th> <b>Attribute</b> </th><th> <b>Description</b> </th></thead><tbody>
<tr><td>library</td><td>Required attribute that inform the library into which this template will be included.<br> This will define the name of the xsd file where the template<br> definition will be put and the namespace associated with this file.<br> (<code>http://www.cruxframework.org/templates/&lt;library&gt;</code>)</td></tr>
<tr><td>useController</td><td>Adds controllers to screen controller list</td></tr>
<tr><td>useSerializable</td><td>Adds serializables to screen serializable list</td></tr>
<tr><td>useFormatter</td><td>Adds formatters to screen formatter list</td></tr>
<tr><td>useDataSource</td><td>Adds DataSources to screen data sources list</td></tr></tbody></table>

As shown in the previous examples, you can create a template that receive attributes and children.<br>
<ul><li>To define an attribute for your template, just write the attribute in the form <code>#{attributeName</code>} wherever you want to apply the attribute value.<br>
</li><li>To define a child tag for your template, use the tag <code>section</code>. It will create a placeholder in your template, which will be replaced by the content of the child tag declared on the page that uses the template. (See the example shown on section <a href='UserManual#3.9.1.3_Defining_a_Reusable_Layout.md'>Defining a Reusable Layout</a>).</li></ul>

<h2>3.10 Running With a Different Server</h2>

To run your <i><b>Crux</b></i> application under DevMode with a different server, you must follow all the steps described at the GWT <a href='http://code.google.com/intl/pt-BR/webtoolkit/doc/latest/FAQ_DebuggingAndCompiling.html'>documentation</a>.<br>
<br>
In addition to these steps, you must add the following JVM argument to your application server: <code>-DCrux.dev=true</code>.<br>
<br>
Note that this parameter just needs to be inserted when running the server with development purposes. In production, you don't need any additional configuration.<br>
<br>
<br>
<h1>4 Coding Server Side</h1>

<h2>4.1 Writing Server-Side Code</h2>

As we said in section <a href='UserManual#3.5_Client-Server_Communication.md'>Client-Server Communication</a>, <i><b>Crux</b></i> supports the <a href='http://code.google.com/intl/pt-BR/webtoolkit/tutorials/1.6/RPC.html'>GWT RPC</a> mechanism with some few features to turn it easier.<br>
<br>
At server-side, the main difference between GWT and <i><b>Crux</b></i> is that your service implementation class does not need to extend <code>RemoteServiceServlet</code>. It is nothing more than a POJO and<br>
just needs to implement the business interface.<br>
<br>
<pre><code>public interface GreetingService extends RemoteService{<br>
	public String getHelloMessage(String name);<br>
}<br>
</code></pre>

<pre><code>public class GreetingServiceImpl implements GreetingService{<br>
   public String getHelloMessage(String name){<br>
      return "Server says: Hello, " + name + "!'";<br>
   }<br>
}<br>
</code></pre>

To find out which implementation will be used for a given service interface, <i><b>Crux</b></i> will search for classes that implements that interface and use the first one found.<br>
<br>
This behavior  can be changed, as shown in section <a href='UserManual#4.3.2.5_serviceFactory.md'>serviceFactory</a>.<br>
<br>
If your service class needs to access the HttpServletRequest, HttpServletResponse or HttpSession objects, it can implement one or more of the following interfaces:<br>
<ul><li><code>RequestAware</code>  - makes your Service able to access the <a href='http://download.oracle.com/javaee/5/api/javax/servlet/http/HttpServletRequest.html'>HttpServletRequest</a> object;<br>
</li><li><code>ResponseAware</code> - makes your Service able to access the <a href='http://download.oracle.com/javaee/5/api/javax/servlet/http/HttpServletResponse.html'>HttpServletResponse</a> object;<br>
</li><li><code>SessionAware</code>  - makes your Service able to access the <a href='http://download.oracle.com/javaee/5/api/javax/servlet/http/HttpSession.html'>HttpSession</a> object.</li></ul>

<pre><code>public class GreetingServiceImpl implements GreetingService, RequestAware, ResponseAware{<br>
   private HttpServletRequest request;<br>
   private HttpServletResponse response;<br>
   	<br>
   public void setRequest(HttpServletRequest request){<br>
      this.request = request;<br>
   }<br>
   public void setResponse(HttpServletResponse response){<br>
      this.response = response;<br>
   }<br>
   ...   <br>
}<br>
</code></pre>

<h2>4.2 I18N</h2>

<i><b>Crux</b></i> provides a mechanism to use i18n in your server-side classes. It's very similar to the GWT i18n for client-side.<br>
<br>
You can create interfaces and use the annotation <code>@org.cruxframework.crux.core.i18n.DefaultServerMessage</code> exactly as you do at client-side.<br>
<br>
The main differences are:<br>
<ul><li>Your interfaces don't need to extend any other interface;<br>
</li><li>You must use the factory method <code>MessagesFactory.getMessages(&lt;interfaceClass&gt;)</code> instead of <code>GWT.create()</code> to create an instance of your interface.</li></ul>

See the following example:<br>
<pre><code>public interface ServerMessages <br>
{<br>
	@DefaultServerMessage("My server message: {0}.")<br>
	String myServerMessage(String message);<br>
}<br>
</code></pre>

<pre><code>public MyServerClass<br>
{<br>
   private static ServerMessages messages = MessagesFactory.getMessages(ServerMessages.class);<br>
   <br>
   public void method()<br>
   {<br>
      System.out.println(messages.myServerMessage("test"));<br>
   }<br>
}<br>
</code></pre>

You can create a resource file named <code>ServerMessages</code> and put it under your application classpath to change messages for a specific locale. Example:<br>
<br>
<pre><code>(file: ServerMessages_pt_BR.properties)<br>
myServerMessage=Minha mensagem no servidor: {0}.<br>
</code></pre>

The mechanism exposed above will work properly for all your classes that are called by a service. <i><b>Crux</b></i> will resolve locale problems in its <a href='UserManual#3.5_Client-Server_Communication.md'>FrontController</a>, before delegating the application control to your service implementation.<br>
<br>
However, if you plan to access i18n messages in classes called by a filter (that executes before the <i><b>Crux</b> FrontController</i> Servlet), you need to configure a filter in your web.xml file. Read the section <a href='UserManual#4.3.1_Web.xml.md'>Web.xml</a> to see how to do this.<br>
<br>
<h2>4.3 Setup</h2>

To use <i><b>Crux</b></i> in your application, you will need the following files:<br>
<br>
<ul><li>In production time, inside the <code>WEB-INF/lib</code> folder:<br>
<ul><li>crux-runtime.jar<br>
</li><li>crux-runtime-deps.jar<br>
</li><li>gwt-servlet.jar<br>
</li><li>gwt-servlet-deps.jar<br>
</li></ul></li><li>In development time:<br>
<ul><li>in any folder, since it is visible for the GWT DevMode Console and for ANT tasks:<br>
<ul><li>crux-dev.jar<br>
</li><li>crux-dev-deps.jar<br>
</li><li>crux-widgets.jar<br>
</li><li>crux-themes.jar (optional)<br>
</li><li>gwt-dev.jar<br>
</li><li>gwt-user.jar</li></ul></li></ul></li></ul>

Using the <a href='UserManual#2.1_Install.md'>Project Generator</a> you will get a ready to use project structure.<br>
<br>
<h3>4.3.1 Web.xml</h3>
To setup the <i><b>Crux</b> Front Controller</i>, shown in [3.5_Client-Server_Communication previous section], you must add the following lines to your web.xml file:<br>
<br>
<pre><code>	&lt;servlet&gt;<br>
		&lt;servlet-name&gt;remoteServiceServlet&lt;/servlet-name&gt;<br>
		&lt;servlet-class&gt;<br>
			org.cruxframework.crux.core.server.dispatch.RemoteServiceServlet<br>
		&lt;/servlet-class&gt;<br>
	&lt;/servlet&gt;<br>
<br>
	&lt;servlet-mapping&gt;<br>
		&lt;servlet-name&gt;remoteServiceServlet&lt;/servlet-name&gt;<br>
		&lt;url-pattern&gt;*.rpc&lt;/url-pattern&gt;<br>
	&lt;/servlet-mapping&gt;<br>
</code></pre>

There are some other configurations you will need to do to run your application in development environment:<br>
<br>
<pre><code>    &lt;context-param&gt; <br>
        &lt;param-name&gt;outputCharset&lt;/param-name&gt; <br>
        &lt;param-value&gt;UTF-8&lt;/param-value&gt; <br>
    &lt;/context-param&gt; <br>
    &lt;listener&gt;<br>
        &lt;listener-class&gt;org.cruxframework.crux.core.rebind.DevModeInitializerListener&lt;/listener-class&gt;<br>
    &lt;/listener&gt;<br>
    &lt;filter&gt;<br>
        &lt;display-name&gt;CruxFilter&lt;/display-name&gt;<br>
        &lt;filter-name&gt;CruxFilter&lt;/filter-name&gt;<br>
        &lt;filter-class&gt;org.cruxframework.crux.core.server.CruxFilter&lt;/filter-class&gt;<br>
    &lt;/filter&gt;<br>
    &lt;filter-mapping&gt;<br>
        &lt;filter-name&gt;CruxFilter&lt;/filter-name&gt;<br>
        &lt;url-pattern&gt;*.html&lt;/url-pattern&gt;<br>
    &lt;/filter-mapping&gt;<br>
</code></pre>

The above lines is needed by <i><b>Crux</b></i> <a href='http://code.google.com/intl/pt-BR/webtoolkit/doc/1.6/DevGuideCodingBasics.html#DevGuideDeferredBinding'>Generators</a> to find out which module is being compiled. This is used for better performance. Because that information is used only for compilation, it just needs to be present in development environment.<br>
<br>
In development time, you will need to have this too:<br>
<pre><code>	&lt;filter&gt;<br>
		&lt;display-name&gt;DeclarativeUIFilter&lt;/display-name&gt;<br>
		&lt;filter-name&gt;DeclarativeUIFilter&lt;/filter-name&gt;<br>
		&lt;filter-class&gt;org.cruxframework.crux.core.declarativeui.filter.DeclarativeUIFilter&lt;/filter-class&gt;<br>
	&lt;/filter&gt;<br>
	&lt;filter-mapping&gt;<br>
		&lt;filter-name&gt;DeclarativeUIFilter&lt;/filter-name&gt;<br>
		&lt;url-pattern&gt;*.html&lt;/url-pattern&gt;<br>
	&lt;/filter-mapping&gt;<br>
</code></pre>

This filter is used to transform your <i>.crux.xml</i> files in pure <i>html</i> files. This process is only done in development. In a production environment, your application can access the generated version of the page directly.<br>
<br>
The two above filters should not be used in a production environment. Still, they are removed by an Ant task generated to deploy your project (if you are using the <a href='UserManual#2.1_Install.md'>*Crux* Project Generator</a>).<br>
<br>
You can add an optional listener named InitializerListener to initialize some <i><b>Crux</b></i> resources to turn the first call to application faster.<br>
<br>
<pre><code>   &lt;listener&gt;<br>
      &lt;listener-class&gt;org.cruxframework.crux.core.server.InitializerListener&lt;/listener-class&gt;<br>
   &lt;/listener&gt;<br>
</code></pre>

If you need i18n before the <i><b>Crux</b> FrontController</i> Servlet, you have to put these lines too:<br>
<br>
<pre><code>	&lt;filter&gt;<br>
		&lt;display-name&gt;I18NFilter&lt;/display-name&gt;<br>
		&lt;filter-name&gt;I18NFilter&lt;/filter-name&gt;<br>
		&lt;filter-class&gt;org.cruxframework.crux.core.i18n.I18NFilter&lt;/filter-class&gt;<br>
	&lt;/filter&gt;<br>
	&lt;filter-mapping&gt;<br>
		&lt;filter-name&gt;I18NFilter&lt;/filter-name&gt;<br>
		&lt;url-pattern&gt;*.rpc&lt;/url-pattern&gt;<br>
	&lt;/filter-mapping&gt;<br>
</code></pre>

<h3>4.3.2 Crux.properties</h3>

<i><b>Crux</b></i> provides some configuration options that can modify behaviors on the framework.<br>
<br>
Those options can be informed through:<br>
<ol><li>A command line argument to JVM, adding a System property like: <code>-DCrux.&lt;propertyName&gt;=&lt;propertyValue&gt;</code>. <code>e.g.: -DCrux.enableChildrenWindowsDebug=false</code>.<br>
</li><li>A file named <code>Crux.properties</code> that can be put (optionally) in any place under your classpath.</li></ol>

If you use both ways shown, the command line has precedence.<br>
<br>
The following properties can be set:<br>
<br>
<table><thead><th> <b>Property</b> </th><th> <b>Description</b> </th><th> <b>Default Value</b> </th></thead><tbody>
<tr><td>localeResolver</td><td>Class used by <i><b>Crux</b></i> to resolve locale for user at the server side</td><td>org.cruxframework.crux.core.i18n.LocaleResolverImpl</td></tr>
<tr><td>screenResourceResolver</td><td>Class used by <i><b>Crux</b></i> to retrieve the screen page files</td><td>org.cruxframework.crux.core.rebind.screen.ScreenResourceResolverImpl</td></tr>
<tr><td>classPathResolver</td><td>Class used by <i><b>Crux</b></i> to resolve classpath files</td><td>org.cruxframework.crux.core.server.classpath.ClassPathResolverImpl</td></tr>
<tr><td>serviceFactory</td><td>Class used by <i><b>Crux</b></i> to instantiate controller classes</td><td>org.cruxframework.crux.core.server.dispatch.ServiceFactoryImpl</td></tr>
<tr><td>enableChildrenWindowsDebug</td><td>If <code>true</code>, propagates the GWT debug parameters to <br>other windows opened while application runs under the DevMode</td><td><code>true</code></td></tr>
<tr><td>enableWebRootScannerCache</td><td>If <code>true</code>, uses a cache for the resources scanner</td><td><code>true</code></td></tr>
<tr><td>enableHotDeploymentForWebDirs</td><td>If <code>true</code>, <i><b>Crux</b></i> enables hot deployment for all resources on web dir, including templates</td><td><code>true</code></td></tr></tbody></table>


<h4>4.3.2.1 localeResolver</h4>

By default, <i><b>Crux</b></i> will use the same mechanism used by <a href='http://code.google.com/intl/pt-BR/webtoolkit/doc/1.6/DevGuideI18nAndA11y.html#DevGuideSpecifyingLocale'>GWT at client side</a> to resolve the user locale.<br>
<br>
It means that you can, for example, pass the locale through an URL parameter, like:<br>
<pre><code>http://www.example.org/myapp.html?locale=pt_BR<br>
</code></pre>

However, if you need to change this behavior , you can specify your own LocaleResolver class through the property <code>localeResolver</code>. That implementation can adopt a custom rule to identify the user's locale.<br>
<br>
Your class just needs to implement the following interface:<br>
<br>
<pre><code>public interface LocaleResolver <br>
{<br>
	void initializeUserLocale(HttpServletRequest request);<br>
	Locale getUserLocale() throws LocaleResolverException;<br>
}<br>
</code></pre>

<h4>4.3.2.2 screenResourceResolver</h4>

The property <code>screenResourceResolver</code> tells <i><b>Crux</b></i> which class will be used to retrieve an InputStream related to a given a page of your application.<br>
It can be useful to create new plugins which may need to do some processing on pages before they are consumed by the framework.<br>
<br>
Your class just needs to implement the following interface:<br>
<br>
<pre><code>public interface ScreenResourceResolver<br>
{<br>
	InputStream getScreenResource(String screenId) throws InterfaceConfigException;<br>
	Set&lt;String&gt; getAllScreenIDs(String module) throws ScreenConfigException;<br>
}<br>
</code></pre>

<h4>4.3.2.3 classPathResolver</h4>

<i><b>Crux</b></i> depends on knowing some paths in your application in order to retrieve the HTML pages, scan for Controllers and other operations. The interface<br>
<code>ClassPathResolver</code> is used for this purpose.<br>
<br>
There are two default ClassPathResolvers delivered with <i><b>Crux</b></i>. The one you need depends on what <a href='UserManual#6_Project_Layouts.md'>project layout</a> your application uses.<br>
<table><thead><th> <b>Project Layout</b> </th><th> <b>ClassPathResolver</b> </th><th> <b>Why?</b> </th></thead><tbody>
<tr><td> Monolithic Application </td><td> ClassPathResolverImpl </td><td> It's the simplest and the fastest one. </td></tr>
<tr><td> Module Application </td><td> ModuleClassPathResolver </td><td> It can find any Crux resource (like pages, templates, etc.) even if they are packaged in jar files.<br>Its necessary because a module can inherit on other modules and those dependencies must be packaged as jar files and placed under the <code>WEB-INF/lib</code> folder. </td></tr>
<tr><td> Module Container Application </td><td> ModuleClassPathResolver </td><td> Exactly the same reason of the previous layout. </td></tr></tbody></table>



<h4>4.3.2.4 serviceFactory</h4>

The section <a href='UserManual#4.1_Writing_Server-Side_Code.md'>Writing Server-Side Code</a> shows the default mechanism used to discovery your service implementation classes.<br>
<br>
However, you can need to change this to, for example, integrate <i><b>Crux</b></i> with some other server framework like <a href='http://www.springsource.org'>spring</a>, <a href='http://code.google.com/p/google-guice/'>guice</a> or to make your service classes EJBs.<br>
<br>
Using the property <code>serviceFactory</code> you can specify your own factory class, which just needs to implement the following interface:<br>
<br>
<pre><code>public interface ServiceFactory <br>
{<br>
	Object getService(String serviceName);<br>
	void initialize(ServletContext context);<br>
}<br>
</code></pre>


<h4>4.3.2.5 enableChildrenWindowsDebug</h4>

GWT 2.x uses a parameter on the browser URL to inform the GWT Plugin that it must enable the debugger. If  <code>enableChildrenWindowsDebug</code> parameter is set to <code>true</code>, <i><b>Crux</b></i> will propagate the GWT debug parameter to other windows, for example, when you invoke <code>Popup.open</code>.<br>
<br>
<h4>4.3.2.6 enableWebRootScannerCache</h4>

<i><b>Crux</b></i> uses a resource scanner to find the application pages, templates and other things. The process of scanning the entire application is not a fast task.<br>
For this reason, <i><b>Crux</b></i> uses an internal cache to avoid the duplicated processing of some resources, enhancing the performance of the scanner. By the way,<br>
if need, you can disable that cache, setting <code>enableWebRootScannerCache</code> to <code>false</code>.<br>
<br>
<h4>4.3.2.7 enableHotDeploymentForWebDirs</h4>

Enabling this options costs a little bit more in performance terms, but can turn easier the development, once you will not need to restart your server to see changes on templates.<br>
<br>
<h1>5 Crux Tools</h1>

<h2>5.1 Schema Generator</h2>

The <code>Schema Generator</code> Tool searches in the project classpath for <i><b>Crux</b></i> widget libraries and generates a XSD file for each of them. It also generates a XSD file for each <a href='UserManual#3.9_Templates.md'>template</a> file found.<br>
<br>
You can invoke SchemaGenerator in two different ways:<br>
<ol><li>calling it with the command line:<br>
<blockquote><code>java org.cruxframework.crux.tools.schema.SchemaGenerator &lt;projectBaseDir&gt; &lt;outputDir&gt;</code>
</blockquote></li><li>calling the ant task <code>&lt;generate-schemas&gt;</code>, that is already defined on the project <code>build.xml</code> file, generated by the <a href='UserManual#2.1_Install.md'>Project Generator</a></li></ol>

SchemaGenerator also produces an eclipse catalog file containing all generated XSD files.<br>
<br>
<h2>5.2 Crux Compiler</h2>

The <code>CruxCompiler</code> tool compiles a crux project. It converts all <code>.crux.xml</code> files to simple <code>.html</code> files and then call the GWT compiler to produce the javascript for all your application modules.<br>
<br>
You can invoke CruxCompiler in two different ways:<br>
<ol><li>calling it with the command line:<br>
<blockquote><code>java org.cruxframework.crux.tools.compile.CruxCompiler &lt;outputDir&gt;</code>
</blockquote></li><li>calling the ant task <code>&lt;compile-scripts&gt;</code>, that is already defined on the project <code>build.xml</code> file, generated by the <a href='UserManual#2.1_Install.md'>Project Generator</a></li></ol>


<h1>6 Project Layouts</h1>

A <i><b>Crux</b></i> application can be built for different purposes. There are some distinct layouts of projects provided by Crux<br>
that you can use for achieve these purposes. Which layout you should choose for your application depends on its characteristics.<br>
The currently supported layouts are:<br>
<br>
<ul><li>Monolithic Application<br>
</li><li>Module Application<br>
</li><li>Module Container Application</li></ul>

Below, we talk about each one:<br>
<br>
<br>
<h2>6.1 Monolithic Application</h2>

This layout is a template for an ordinary Java web project. It has facilities for help you with tasks of<br>
building and deploying the final application. It is the default and simplest layout of project, ideal for<br>
small web applications, coded by small teams, for which the volume of source-code and the parallelization<br>
of coding tasks are not so hard to administer.<br>
<br>
<br>
<h2>6.2 Module Application</h2>

GWT supports modularization, allowing you to divide your application and package its parts separately. This<br>
is especially useful for building large applications. <i><b>Crux</b></i> extends such mechanism adding some other features<br>
to turn the modular development easier.<br>
<br>
The Module Application layout is designed to model fragments, reusable or not, of a large application. A module<br>
project can be coded, tested, versioned and even delivered totally separated of the rest of the application. This<br>
can provide a drastic improvement in the development speed, in the cost of maintenance and, we believe, in the<br>
quality of the final product.<br>
<br>
Every application of this flavor must be packaged as a jar file, named with the following pattern <code>&lt;moduleName&gt;.module.jar</code>.<br>
This way, it will be ready to be used by another module or by a <a href='UserManual#6.3_Module_Container_Application.md'>Module Container Application</a>.<br>
A packaged module can contain pre-compiled resources for improving the compiling speed of the dependent applications.<br>
<br>
<br>
<h2>6.3 Module Container Application</h2>

This can be considered the ideal layout for corporative applications, which are typically built by a larger<br>
number of developers and consist of many dozens of use cases, some of which tend to be reused by multiple<br>
functionalities.<br>
<br>
This kind of application can contain multiple <a href='UserManual#6.2_Module_Application.md'>Module Applications</a> that<br>
just need to be available in its classpath. More specifically, modules must be placed inside the <code>WEB-INF/lib</code>
folder.