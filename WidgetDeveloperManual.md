# 1 Introduction #

If you are not familiar with _**Crux**_ framework, please consult the [User Manual](UserManual.md) first.

This documentation will show you how to work with custom widgets inside _**Crux**_.

## 1.1 GWT widgets ##

_**Crux**_ widgets are  [GWT widgets](http://code.google.com/intl/pt-BR/webtoolkit/doc/latest/DevGuideUiWidgets.html). _**Crux**_ just offer an additional engine where those widgets can be plugged.

So, you can plug any custom widgets in _**Crux**_ Declarative Engine. If you are not familiar with GWT widgets development, please consult its documentation  [first](http://code.google.com/intl/pt-BR/webtoolkit/doc/latest/DevGuideUiWidgets.html).

# 2 _Crux_ Declarative Engine #

## 2.1 Overview ##

_**Crux**_ declarative engine uses generators to create the widgets based on informations contained on the `.crux.xml` pages.

To register a custom widget in _**Crux**_ declarative engine, you just have to create a factory class for it.

See the following example:

```
@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)
@TagAttributes({
   @TagAttribute("myWidgetAttribute")
})
public class MyWidgetCreator extends WidgetCreator<WidgetCreatorContext>
{
   @Override
   public WidgetCreatorContext instantiateContext()
   {
      return new WidgetCreatorContext();
   }
}
```

Note that the above class uses the annotation `@DeclarativeFactory` and extends the abstract class `WidgetCreator`. These two conditions are necessary for any _**Crux**_ widget factory.

Through the `@DeclarativeFactory` annotation, you can specify the name of the library where your widget will be registered and the name that will be associated with your widget itself.

Before we go deeper inside the code shown and see more examples, let's see how you could use the widget registered with _**Crux**_ declarative engine:

```
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:mylib="http://www.cruxframework.org/crux/myLibrary" >
   <head>
       <script language="javascript" src="cruxtest/cruxtest.nocache.js"></script>
   </head>
   <body>
       <mylib:myWidget id="myWidgetID" myWidgetAttribute="attributeValue"/>
   </body>
</html>
```


Note that a namespace called `http://www.cruxframework.org/crux/myLibrary` is used on the above page. A `XSD` file defining this namespace is created by the [SchemaGenerator](UserManual#5.1_Schema_Generator.md).

The example shown assumes that the widget `MyWidget` contains a public String property called `myWidgetAttribute` that will be bound to the tag's attribute `myWidgetAttribute` on `crux.xml` file.


## 2.2 WidgetCreator ##

`WidgetCreator` is the basic class for any _**Crux**_ widget factory. `ScreenFactory` delegates to those factories the generation of the code responsible for the creation of the requested widget, based on the widget element on the `.crux.xml` page's DOM.

`WidgetCreator` subclasses can use some annotations to describe the attributes, events and children that the widget accepts. These annotations are used for two purposes:
  1. Automatically bind the information declared into the `.crux.xml` pages with the widgets created by the _**Crux**_ engine.
  1. Inform to _**Crux**_ engine which attributes this factory can handle. This information is used to generate a proper XSD file, that can be used to enable autocompletion for developers (See  [SchemaGenerator](UserManual#5.1_Schema_Generator.md)).

`WidgetCreator.createWidget()` method is called for the widget creation. It processes the attributes, events and children declared through the factory annotations and then executes the following steps:

  1. Calls the method `void instantiateWidget(SourcePrinter out, C context)`
  1. Calls the method `void processAttributes(SourcePrinter out, C context)`
  1. Calls the method `void processEvents(SourcePrinter out, C context)`
  1. Calls the method `void processChildren(SourcePrinter out, C context)`
  1. Calls the method `void postProcess(SourcePrinter out, C context)`

These methods can be used by the factory to specify and process the information about widget's attributes, events and children. All of those methods receive a parameter of type `C extends WidgetCreatorContext`. The base class `WidgetCreatorContext` has the following public methods:

| **Method** | **Description** |
|:-----------|:----------------|
| getWidget | Returns the name of the variable containing the widget to be created  |
| getWidgetElement | Returns the widget metadata element (a JSONObject) |
| getWidgetId | Returns the widget identifier |
| readWidgetProperty(String propertyName) | Read the property value from the widget metadata |
| readChildProperty(String key) | Read the property value from the metadata of the current widget child been processed |

You can extend `WidgetCreatorContext` to add custom properties and methods to your context.

The WidgetCreator's methods can be overridden when you want to add some specific behavior.


### 2.2.1 The WidgetCreator Annotations ###

_**Crux**_ provides annotations to help the processing of attributes, events and children.

#### 2.2.1.1 Processing Attributes ####

To process attributes, you can use the annotations `@TagAttributes` and `@TagAttribute`, like on the following example:

```
@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)
@TagAttributes({
   @TagAttribute("myAttribute"),
   @TagAttribute(value="theRequiredAttribute", required=true),
   @TagAttribute(value="intAttribute", type=Integer.class),
   @TagAttribute(value="otherAttribute", property="widgetProperty"),
   @TagAttribute(value="nested", property="element.widgetProperty"),
   @TagAttribute(value="style", processor=StyleAttributeProcessor.class)
})
public class MyWidgetCreator extends WidgetCreator<WidgetCreatorContext>{
   @Override
   public WidgetCreatorContext instantiateContext()
   {
      return new WidgetCreatorContext();
   }
}
```

The previous example assumes that the factory's target widget (MyWidget) has public setters for the declared attributes, like:
```
public class MyWidget extends Widget{
	public void setMyAttribute(String myAttribute){...}
	public void setTheRequiredAttribute(String theRequiredAttribute){...}
	public void setIntAttribute(int intAttribute){...}
	public void setWidgetProperty(int intAttribute){...}
	public Element getElement(){...}// Where "Element" has the property "widgetProperty"
    ...
}
```

On the previous example, all the binding between the declaration on the `.crux.xml` pages and the widgets created is done automatically by _**Crux**_ engine.

The `@TagAttributes` has one property (value) of type array of `@TagAttribute`. Each `@TagAttribute` is used to inform one attribute of the widget.

`@TagAttribute` accepts the following properties:

| **Name** | **Type** | **Required** | **Default** | **Description** |
|:---------|:---------|:-------------|:------------|:----------------|
| value | String | yes | - | The name of the attribute. |
| defaultValue | String | no | "" | The default value to be used on XSD. |
| type | Class | no | String | The type of the attribute. |
| required | boolean | no | false | Inform if the attribute is required. |
| supportsI18N | boolean | no | false | Inform if the attribute support _**Crux**_ declarative i18n. |
| property | String | no | the name of the attribute (value property) | If informed, it changes the generated code to populate that<br> property on the generated widget (the widget must have <br>the correspondent setter method). <br> <i><b>Crux</b></i> allows nested properties, like <code>property1.property2</code>
<tr><td> processor </td><td> Class </td><td> no </td><td> - </td><td> A subclass of <code>AttributeProcessor</code>. Used to generate the code for the attribute processing.</td></tr>
<tr><td> xsdIgnore </td><td> boolean </td><td> no </td><td> false </td><td> If true, makes the SchemaGenerator ignore this attribute on XSD. </td></tr></tbody></table>


The supported types for an attribute is:<br>
<ul><li>any primitive type or primitive wrapper;<br>
</li><li>String;<br>
</li><li>Date;<br>
</li><li>any enum type.</li></ul>

All enum types will be mapped to simpleTypes on the generated XSD.<br>
<br>
The code generated by <i><b>Crux</b></i> will handle all type conversions needed and all declarative i18n messages.<br>
<br>
When you need a specific processing for an attribute, you can use the annotations <code>@TagAttributesDeclaration</code> and <code>@TagAttributeDeclaration</code>, like on the following example:<br>
<br>
<pre><code>@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagAttributesDeclaration({<br>
   @TagAttributeDeclaration("myAttribute")<br>
})<br>
public class MyWidgetCreator extends WidgetCreator&lt;WidgetCreatorContext&gt;{<br>
   @Override<br>
   public void processAttributes(SourcePrinter out, WidgetCreatorContext context) <br>
                                 throws CruxGeneratorException<br>
   {<br>
      super.processAttributes(context);<br>
      <br>
      MyWidget widget = context.getWidget();<br>
      String attr = context.readWidgetAttribute("myAttribute");<br>
      if (!StringUtils.isEmpty(attr))<br>
      {<br>
         widget.setMyAttribute(attr);<br>
      }<br>
   }<br>
}<br>
</code></pre>

The annotations <code>@TagAttributesDeclaration</code> and <code>@TagAttributeDeclaration</code> are similar to the <code>@TagAttributes</code> and <code>@TagAttribute</code> annotations. However, they only declare the attributes (needed by <a href='UserManual#5.1_Schema_Generator.md'>SchemaGenerator</a>). They don't generate any processing automatically.<br>
<br>
When using <code>@TagAttributesDeclaration</code> annotation, you must write yourself the code for the processing (see <a href='WidgetDeveloperManual#2.2.2.2_Attributes_and_Events_Processing.md'>this section</a>).<br>
<br>
Another way to do a specific processing for an attribute is to use an AttributeProcessor, like:<br>
<br>
<pre><code>@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagAttributes({<br>
	@TagAttribute(value="animation", <br>
	              type=MyWidgetCreator.Animations.class, <br>
	              processor=MyWidgetCreator.AnimationAttributeProcessor.class) <br>
})<br>
public class MyWidgetCreator extends WidgetCreator&lt;WidgetCreatorContext&gt;{<br>
<br>
   public static enum Animations{slide, reveal}<br>
	<br>
   public static class AnimationAttributeProcessor extends AttributeProcessor&lt;WidgetCreatorContext&gt;<br>
   {<br>
      public AnimationAttributeProcessor(WidgetCreator&lt;?&gt; widgetCreator){<br>
         super(widgetCreator);<br>
      }<br>
<br>
      @Override<br>
      public void processAttribute(SourcePrinter out, WidgetCreatorContext context, <br>
                                   String attributeValue){<br>
         Animations animation = Animations.valueOf(attributeValue);<br>
	        <br>
         switch (animation){<br>
            case slide: out.println(context.getWidget()+".setAnimation("+<br>
                                  SlideAnimation.class.getCanonicalName()+".create());"); <br>
            break;<br>
            case reveal: out.println(context.getWidget()+".setAnimation("+<br>
                                  RevealAnimation.class.getCanonicalName()+".create());"); <br>
            break;<br>
         }<br>
      }<br>
   }<br>
}<br>
</code></pre>

The processor must extends the abstract class <code>org.cruxframework.crux.core.rebind.screen.widget.AttributeProcessor</code>.<br>
<br>
<h4>2.2.1.2 Processing Events</h4>

To process events, you can use the annotations <code>@TagEvents</code> and <code>@TagEvent</code>, like on the following example:<br>
<br>
<pre><code>@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagEvents({<br>
	@TagEvent(ClickEvtBind.class)<br>
})	<br>
public class MyWidgetCreator extends WidgetCreator&lt;WidgetCreatorContext&gt;{<br>
   @Override<br>
   public WidgetCreatorContext instantiateContext()<br>
   {<br>
      return new WidgetCreatorContext();<br>
   }<br>
}<br>
</code></pre>

The <code>@TagEvent</code> annotation has a value property of type <code>Class&lt;? extends EvtProcessor&gt;</code>.<br>
<br>
An event processor must extends the abstract class <code>org.cruxframework.crux.core.rebind.screen.widget.EvtProcessor</code>. See the following example.<br>
<br>
<pre><code>public class ClickEvtBind extends EvtProcessor{<br>
    private static final String EVENT_NAME = "onClick";<br>
<br>
    public ClickEvtBind(WidgetCreator&lt;?&gt; widgetCreator){<br>
        super(widgetCreator);<br>
    }<br>
<br>
    public String getEventName(){<br>
        return EVENT_NAME;<br>
    }<br>
	<br>
    public Class&lt;?&gt; getEventClass(){<br>
        return ClickEvent.class;<br>
    }<br>
<br>
    public Class&lt;?&gt; getEventHandlerClass(){<br>
        return ClickHandler.class;<br>
    }<br>
}<br>
</code></pre>

<code>EvtBinders</code> are classes that can automatically bind event declarations to widgets.<br>
<br>
<i><b>Crux</b></i> offers <code>EvtBinders</code> for all GWT events and for  custom <i><b>Crux</b></i> widgets events. The following table shows some of the GWT <code>EvtBinders</code>:<br>
<br>
<table><thead><th> <b>Event type</b> </th><th> <b>Class</b> </th><th> <b>Widget type</b> </th></thead><tbody>
<tr><td> onAttach </td><td> AttachEvtBind </td><td> Widget </td></tr>
<tr><td> onBeforeSelection </td><td> BeforeSelectionEvtBind </td><td> HasBeforeSelectionHandlers<?> </td></tr>
<tr><td> onBlur </td><td> BlurEvtBind </td><td> HasBlurHandlers </td></tr>
<tr><td> onChange </td><td> ChangeEvtBind </td><td> HasChangeHandlers </td></tr>
<tr><td> onClick </td><td> ClickEvtBind </td><td> HasClickHandlers </td></tr>
<tr><td> onFocus </td><td> FocusEvtBind </td><td> HasFocusHandlers </td></tr>
<tr><td> onHighlight </td><td> HighlightEvtBind </td><td> HasHighlightHandlers<?> </td></tr>
<tr><td> onKeyDown </td><td> KeyDownEvtBind </td><td> HasKeyDownHandlers </td></tr>
<tr><td> onKeyPress </td><td> KeyPressEvtBind </td><td> HasKeyPressHandlers </td></tr>
<tr><td> onKeyUp</td><td> KeyUpEvtBind </td><td> HasKeyUpHandlers </td></tr>
<tr><td> onError </td><td> LoadErrorEvtBind </td><td> HasErrorHandlers </td></tr>
<tr><td> onLoad </td><td> LoadEvtBind </td><td> HasLoadHandlers </td></tr>
<tr><td> onMouseDown </td><td> MouseDownEvtBind </td><td> HasMouseDownHandlers </td></tr>
<tr><td> onMouseMove </td><td> MouseMoveEvtBind </td><td> HasMouseMoveHandlers </td></tr>
<tr><td> onMouseOut </td><td> MouseOutEvtBind </td><td> HasMouseOutHandlers </td></tr>
<tr><td> onMouseOver </td><td> MouseOverEvtBind </td><td> HasMouseOverHandlers </td></tr>
<tr><td> onMouseUp </td><td> MouseUpEvtBind </td><td> HasMouseUpHandlers </td></tr>
<tr><td> onMouseWheel </td><td> MouseWheelEvtBind </td><td> HasMouseWheelHandlers </td></tr>
<tr><td> onOpen </td><td> OpenEvtBind </td><td> HasOpenHandlers<?> </td></tr>
<tr><td> onScroll </td><td> ScrollEvtBind </td><td> HasScrollHandlers </td></tr>
<tr><td> onSelection </td><td> SelectionEvtBind </td><td> HasSelectionHandlers<?> </td></tr>
<tr><td> onShowRange </td><td> ShowRangeEvtBind </td><td> HasShowRangeHandlers<?> </td></tr>
<tr><td> onChange </td><td> ValueChangeEvtBind </td><td> HasValueChangeHandlers<?> </td></tr></tbody></table>


The annotation <code>@TagEventDeclaration</code> has one attribute of type <code>String</code>. This attribute only informs to <i><b>Crux</b></i> the name of the event.<br>
<br>
When using <code>@TagEventsDeclaration</code> annotation, you must write yourself the code for the processing (see <a href='WidgetDeveloperManual#2.2.2.2_Attributes_and_Events_Processing.md'>this section</a>).<br>
<br>
<h4>2.2.1.2 Processing Children</h4>

You can declare children to your widget tag on <i><b>Crux</b></i> declarative engine. To do this, you must use the <code>@TagChildren</code> and <code>@TagChild</code> annotations.<br>
<br>
The follow code shows a complete example, declaring a widget tag with children and using a custom context:<br>
<pre><code><br>
class MyWidgetContext extends WidgetCreatorContext{<br>
   int index = 0;<br>
}<br>
<br>
@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagChildren({<br>
   @TagChild(MyWidgetCreator.ItemProcessor.class)<br>
})	<br>
public class MyWidgetCreator extends WidgetCreator&lt;MyWidgetContext&gt;{<br>
   <br>
   @TagConstraints(tagName="item", minOccurs="0", maxOccurs="unbounded")<br>
   @TagAttributesDeclaration({<br>
      @TagAttributeDeclaration("value"),<br>
      @TagAttributeDeclaration(value="label", supportsI18N=true),<br>
      @TagAttributeDeclaration(value="selected", type=Boolean.class)<br>
   })<br>
   public static class ItemProcessor extends WidgetChildProcessor&lt;MyWidgetContext&gt;{<br>
      @Override<br>
      public void processChildren(SourcePrinter out, MyWidgetContext context) <br>
               throws CruxGeneratorException<br>
      {<br>
         String label = context.readChildProperty("label");<br>
         String value = context.readChildProperty("value");<br>
			<br>
         if(label != null &amp;&amp; label.length() &gt; 0){<br>
            label = getWidgetCreator().getDeclaredMessage(label);<br>
         }			<br>
         if (value == null || value.length() == 0){<br>
            value = label;<br>
         }else{<br>
            value = EscapeUtils.quote(value);<br>
         }<br>
         out.println(context.getWidget()+".insertItem("+label+", "+value+", "+context.index+");");<br>
<br>
         String selected = context.readChildProperty("selected");<br>
         if (selected != null &amp;&amp; selected.length() &gt; 0){<br>
            out.println(context.getWidget()+".setItemSelected("+context.index+", "+<br>
                                                             Boolean.parseBoolean(selected)+");");<br>
         }<br>
         context.index += 1;<br>
      }<br>
   }<br>
   <br>
   @Override<br>
   public WidgetCreatorContext instantiateContext() {<br>
      return new WidgetCreatorContext();<br>
   }<br>
}<br>
</code></pre>

That example creates a factory to understand the following code:<br>
<br>
<pre><code>&lt;html xmlns="http://www.w3.org/1999/xhtml" <br>
      xmlns:mylib="http://www.cruxframework.org/crux/myLibrary" &gt;<br>
   &lt;head&gt;<br>
       &lt;script language="javascript" src="cruxtest/cruxtest.nocache.js"&gt;&lt;/script&gt;<br>
   &lt;/head&gt;<br>
   &lt;body&gt;<br>
       &lt;mylib:myWidget id="myWidgetID" &gt;<br>
          &lt;myLib:item label="#{myMessageBundle.label1}" value="value1" /&gt;<br>
          &lt;myLib:item label="#{myMessageBundle.label2}" value="value2" /&gt;<br>
          &lt;myLib:item label="#{myMessageBundle.label3}" value="value3" selected="true"/&gt;<br>
          &lt;myLib:item label="#{myMessageBundle.label4}" value="value4" /&gt;<br>
          &lt;myLib:item label="#{myMessageBundle.label5}" value="value5" /&gt;<br>
       &lt;/mylib:myWidget&gt;<br>
   &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

Note that you can insert attributes and events for child tags, using <code>@TagAttributesDeclaration</code> and <code>@TagEventsDeclaration</code>, and other children, using <code>@TagChildren</code>, on the child processor class.<br>
<br>
A child processor class must extends <code>WidgetChildProcessor</code> class, or one of its sub-classes.<br>
<br>
<br>
<h3>2.2.2 The WidgetCreator Methods</h3>

<h4>2.2.2.1 The instantiateWidget method</h4>


The <code>instantiateWidget</code> method can be overridden to generate the code for instantiate the new widget object. If the widget shown on <a href='WidgetDeveloperManual#2.1_Overview.md'>section 2.1</a> needs some attribute to be instantiated, we could write something like:<br>
<br>
<pre><code>@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagAttributesDeclaration({<br>
   @TagAttributeDeclaration(value="theRequiredAttribute", required=true)<br>
})<br>
public class MyWidgetCreator extends WidgetCreator&lt;WidgetCreatorContext&gt;{<br>
   @Override<br>
   public void instantiateWidget(SourcePrinter out, WidgetCreatorContext context) <br>
                                throws CruxGeneratorException{<br>
      String className = getWidgetClassName();<br>
		<br>
      String theRequiredAttribute = context.readWidgetProperty("theRequiredAttribute");<br>
      out.println("final "+className + " " + context.getWidget()+" = new "+<br>
                   className+"("+EscapeUtils.quote(theRequiredAttribute)+");");<br>
   }<br>
   ...<br>
}<br>
</code></pre>

This method prints the widget instantiation. The widget variable name is passed to the instantiateWidget method on the <code>context.getWidget()</code> method. It is also important to observe that you must create the widget and assign it to a <b>final</b> variable, like shown on the previous example.<br>
<br>
The purpose of this method is just instantiate the widget. The best place to handle the extraction of attributes, events and children is not here. You must use the other methods, shown bellow, in order to take advantage of all benefits this engine offers you.<br>
<br>
<h4>2.2.2.2 Attributes and Events Processing</h4>

The <code>processAttributes</code> and <code>processEvents</code> methods are used to process attributes and events that can not be handled automatically (only declared with the factory annotations).<br>
<br>
When overriding these methods, remember that you need to call <code>super.processAttributes()</code> or <code>super.processEvents()</code> (depending on which method you are overriding).<br>
<br>
<h4>2.2.2.3 Children Processing</h4>

The <code>processChildren</code> method is used to add custom behavior to the children processing. When overriding this method you don't need to call <code>super.processChildren</code> (as you need for attributes and events). Children processing logic is not inherited from super classes (attributes and events processing are).<br>
<br>
<h4>2.2.2.4 Post-processing</h4>

The <code>postProcess</code> method can be overridden to add custom logic after the attributes, events and children are processed.<br>
<br>
<h3>2.2.3 Factories Inheritance</h3>

The annotations for attributes and events processing present on Widget factories super classes and implemented interfaces are also considered by <i><b>Crux</b></i> engine.<br>
<br>
Let's take a look at the following example.<br>
<br>
<pre><code>@TagAttributes({<br>
	@TagAttribute("name")<br>
})	<br>
public interface HasNameFactory&lt;C extends WidgetCreatorContext&gt;<br>
{<br>
}<br>
</code></pre>

<pre><code>@DeclarativeFactory(id="myWidget", library="myLibrary", targetWidget=MyWidget.class)<br>
@TagAttributes({<br>
   @TagAttribute("myWidgetAttribute")<br>
})<br>
public class MyWidgetCreator extends WidgetCreator&lt;WidgetCreatorContext&gt; <br>
                             implements HasNameFactory&lt;WidgetCreatorContext&gt;<br>
{<br>
	...<br>
}<br>
</code></pre>

That factory shown above will accept and automatically bind the attributes <code>myWidgetAttribute</code> and <code>name</code> to <code>MyWidget</code>.<br>
<br>
<i><b>Crux</b></i> libraries use the inheritance support to turn easy the development of its factories. GWTWidgets library has interfaces that binds attributes and events for most of GWT basic interfaces for widgets. Just implementing some of those interfaces is enough to provide your factory with the ability of binding various attributes and events. Some of the provided interfaces are:<br>
<br>
<table><thead><th> <b>Interface</b> </th><th> <b>Attributes</b> </th><th> <b>Events</b> </th></thead><tbody>
<tr><td> HasAllFocusHandlersFactory </td><td> - </td><td> onFocus, onBlur </td></tr>
<tr><td> HasAllKeyHandlersFactory </td><td> - </td><td> onKeyUp, onKeyPress, onKeyDown </td></tr>
<tr><td> HasAllMouseHandlersFactory </td><td> - </td><td> omMouseUp, onMouseDown, onMouseOver, onMouseOut, onMouseMove, onMouseWheel </td></tr>
<tr><td> HasAnimationFactory </td><td> animationEnabled </td><td> - </td></tr>
<tr><td> HasBeforeSelectionHandlersFactory </td><td> - </td><td> onBeforeSelection </td></tr>
<tr><td> HasChangeHandlersFactory </td><td> - </td><td> onChange </td></tr>
<tr><td> HasClickHandlersFactory </td><td> - </td><td> onClick </td></tr>
<tr><td> HasCloseHandlersFactory </td><td> - </td><td> onClose </td></tr>
<tr><td> HasDirectionFactory </td><td> direction </td><td> - </td></tr>
<tr><td> HasHighlightHandlersFactory </td><td> - </td><td> onHighlight </td></tr>
<tr><td> HasNameFactory </td><td> name </td><td> - </td></tr>
<tr><td> HasOpenHandlersFactory </td><td> - </td><td> onOpen </td></tr>
<tr><td> HasScrollHandlersFactory </td><td> - </td><td> onScroll </td></tr>
<tr><td> HasSelectionHandlersFactory </td><td> - </td><td> onSelection </td></tr>
<tr><td> HasShowRangeHandlersFactory </td><td> - </td><td> onShowRange </td></tr>
<tr><td> HasTextFactory </td><td> text </td><td> - </td></tr>
<tr><td> HasValueChangeHandlersFactory </td><td> - </td><td> onChange </td></tr>
<tr><td> HasWordWrapFactory </td><td> wordWrap </td><td> - </td></tr>