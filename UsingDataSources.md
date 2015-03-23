# 1 DataSources #

_**Crux**_ DataSources are objects capable of providing a set of data to widgets that implement `HasDataSource` interface. DataSources support features like pagination, data sorting and editing.

The following example shows a grid widget associated with a dataSource:

```
<html 
   xmlns="http://www.w3.org/1999/xhtml"
   xmlns:c="http://www.sysmap.com.br/crux" 
   xmlns:a="http://www.sysmap.com.br/crux/widgets">
      
      <c:screen useDataSource="simpleGridDataSource" useFormatter="birthday"/>
      
      <a:grid id="simpleGrid" height="200" width="100%" dataSource="simpleGridDataSource" pageSize="7">
         <a:dataColumn key="name" label="Name"/>
         <a:dataColumn key="phone" label="Phone"/>
         <a:dataColumn key="birthday" label="Birthday" formatter="birthday"/>
      </a:grid>
</html>
```

The DataSource class:
```
@DataSource("simpleGridDataSource")
@DataSourceRecordIdentifier("name")
public static class SimpleGridDataSource extends LocalPagedDataSource<Contact> {
   @Create
   protected SimpleGridServiceAsync service;

   public void load()
   {
      service.getContactList(new DataSourceAsyncCallbackAdapter<Contact>(this));
   }		
}
```

Note that you must declare a DataSource with the annotation `@DataSource` and then import it into your page using the screen attribute `useDataSource`.  We can compare it with the Annotation `@Controller` and the `useController` screen attribute.

The main difference between DataSources and Controllers is the final purpose of these two classes. Controllers are used to handle widget events and data sources are used to load a set of data to serve a widget.

All basic DataSources implement the interface `br.com.sysmap.crux.core.client.datasource.DataSource` and one of the two interfaces: `br.com.sysmap.crux.core.client.datasource.LocalDataSource` or `br.com.sysmap.crux.core.client.datasource.RemoteDataSource` .

# 2 DataSources Hierarchy #

There are a lot of DataSource interfaces, provided by _**Crux**_, to support extra features, like pagination and data editing. Your DataSource class can implement many of those interfaces. The following figure shows the complete list of available interfaces:

![http://crux-framework.googlecode.com/svn/wiki/images/dataSources.gif](http://crux-framework.googlecode.com/svn/wiki/images/dataSources.gif)

| **Interface** | **Description** |
|:--------------|:----------------|
| DataSource | The basic interface for any DataSource. Contains the basic methods to navigating through records, sorting and editing data.|
| LocalDataSource | Local DataSources loads the data in just one step and keeps it in a local buffer. Contains the method `load()`|
| PagedDataSource | A DataSource that can divide data in blocks called _pages_. Contains the method `fetch(int start, int end)`,<br>which is called by the associated widget when it needs the information contained in a page.<br>
<tr><td> RemoteDataSource </td><td> Remote DataSources are also paged. This interface supports the cases when the data resides on the server and must be fetched on demand. <br>When a page is needed, the DataSource requests it to server. It also keeps a buffer of the <br>already loaded pages. </td></tr>
<tr><td> MeasurableDataSource </td><td> A DataSource that can be measured. In other words, when the number of records of the DataSource can be determined.<br> It contains the method <code>getRecordCount()</code> </td></tr>
<tr><td> MeasurableRemoteDataSource </td><td> A Measurable and Remote Datasource. Contains the methods <code>load</code> and <code>setLoadData</code>,<br> used to load DataSource configuration, that includes the size of the DataSource</td></tr>
<tr><td> StreamingDataSource </td><td> A DataSource that retrieves its data from a stream. The size of this stream is unknow.<br> Streaming DataSources are also paged, but is not possible to know the number of pages before request the last one. <br> All the pages must be requested in order.</td></tr></tbody></table>

<blockquote><br>
<h2>2.1 DataSource Abstract classes</h2></blockquote>

The easiest way of creating a DataSource is extending one of the <i><b>Crux</b></i> provided abstract classes.  Those classes implement different combination of the above interfaces to simplify your implementation. Using one of those classes, you only need to implement one method to retrieve the data itself:<br>
<ul><li><code>void load();</code> - for local DataSources<br>
</li><li><code>void fetch(int start, int end);</code> - for remote DataSources</li></ul>

Inside this loader methods (<code>load</code> or <code>fetch</code>), you must call the <code>updateData</code> method after the data is retrieved.<br>
<br>
If your DataSource implements the interface <code>MeasurableRemoteDataSource</code>, you must implement a <code>load</code> method too, in addition of the <code>fetch</code> method. The load method is used to load DataSource configuration, including the size of the remote DataSource. Inside this method you must call <code>setLoadData</code> once you have loaded the configuration data.<br>
<br>
The following figures show some of those classes:<br>
<br>
<img src='http://crux-framework.googlecode.com/svn/wiki/images/localDataSources.gif' />

and<br>
<br>
<img src='http://crux-framework.googlecode.com/svn/wiki/images/remoteDataSources.gif' />

The complete list of basic abstract DataSources classes provided by <i><b>Crux</b></i>:<br>
<br>
<table><thead><th> <b>Class</b> </th><th> <b>Implemented DataSource Interfaces</b> </th></thead><tbody>
<tr><td> LocaScrollableDataSource </td><td> LocalDataSource </td></tr>
<tr><td> LocaPagedDataSource </td><td> LocalDataSource, PagedDataSource </td></tr>
<tr><td> RemotePagedDataSource </td><td> MeasurableRemoteDataSource </td></tr>
<tr><td> RemoteStreamingDataSource </td><td> StreamingDataSource </td></tr></tbody></table>

See the following examples:<br>
<br>
<pre><code>@DataSource("contactDataSource")<br>
@DataSourceRecordIdentifier("name")<br>
public static class RemoteDS extends RemotePagedDataSource&lt;Contact&gt; {<br>
<br>
   @Create<br>
   protected SimpleGridServiceAsync service;<br>
<br>
   public void load(){<br>
      RemoteDataSourceConfiguration config = getConfig();<br>
      setLoadData(config);<br>
   }<br>
<br>
   public void fetch(int startRecord, int endRecord){<br>
      Contact[] data = getData();<br>
      updateData(data);<br>
   }<br>
}<br>
</code></pre>

<pre><code>@DataSource("contactDataSource")<br>
@DataSourceRecordIdentifier("name")<br>
public static class RemoteDS extends RemoteStreamingDataSource&lt;Contact&gt; {<br>
<br>
   @Create<br>
   protected SimpleGridServiceAsync service;<br>
<br>
   @Parameter(required=true)<br>
   protected String contact;<br>
<br>
   public void fetch(int startRecord, int endRecord)<br>
   {<br>
      service.getContactPage(contact, startRecord, endRecord, new DataSourceAsyncCallbackAdapter&lt;Contact&gt;(this));<br>
   }<br>
}<br>
</code></pre>

<h1>3 DataSources Structure</h1>

When you define a DataSource, you inform a class. <i><b>Crux</b></i> will define columns following the names of the fields of the informed class. The type of each column will be the same type of the respective field. See the following example:<br>
<br>
Every row on a DataSource class must define one identifier field. It is needed to identify a row even if all the set of data were reordered.<br>
The annotation <code>@DataSourceRecordIdentifier</code> must be used to inform which field in the Value Object will be used as row identifier ("name" in the above example).<br>
<br>
<pre><code>@DataSource("simpleGridDataSource")<br>
@DataSourceRecordIdentifier("name")<br>
public static class SimpleGridDataSource extends LocalBindableEditablePagedDataSource&lt;Contact&gt; {<br>
		<br>
   @Create<br>
   protected SimpleGridServiceAsync service;<br>
<br>
   public void load()<br>
   {<br>
      Contact[] data = getData();<br>
      updateData(data);<br>
   }		<br>
}<br>
</code></pre>

<pre><code>public class Contact implements Serializable {<br>
	<br>
	private String name;<br>
	private String phone;<br>
	private Date birthday;<br>
	private Address address;<br>
    <br>
    // Getter and Setters ....	<br>
}<br>
</code></pre>

<pre><code>public class Address implements Serializable {<br>
	<br>
	private String street;<br>
    <br>
    // Getter and Setters ....	<br>
}<br>
</code></pre>

Note that the Contact class has a property of type Address. A widget can refer to a inner property using "dots", like <code>"address.street"</code> when reading from a DataSource.<br>
<br>
The dataSource declared above could be used on a widget like:<br>
<br>
<pre><code>&lt;html <br>
   xmlns="http://www.w3.org/1999/xhtml"<br>
   xmlns:c="http://www.sysmap.com.br/crux" <br>
   xmlns:a="http://www.sysmap.com.br/crux/widgets"&gt;<br>
      <br>
      &lt;c:screen useDataSource="simpleGridDataSource" useFormatter="birthday"/&gt;<br>
      <br>
      &lt;a:grid id="simpleGrid" height="200" width="100%" dataSource="simpleGridDataSource" pageSize="7"&gt;<br>
         &lt;a:dataColumn key="name" label="Name"/&gt;<br>
         &lt;a:dataColumn key="phone" label="Phone"/&gt;<br>
         &lt;a:dataColumn key="birthday" label="Birthday" formatter="birthday"/&gt;<br>
         &lt;a:dataColumn key="address.street" label="Street" /&gt;<br>
      &lt;/a:grid&gt;<br>
&lt;/html&gt;<br>
</code></pre>


<h1>4 Handling Service Responses</h1>

As exposed in the previous sections, once you retrieve the data in the DataSource loader methods, you must call the <code>updateData</code> method of the DataSource.<br>
<br>
To turn easier the task of load data inside the DataSource, <i><b>Crux</b></i> provides a special <code>AsyncCallback</code> implementation (DataSourceAsyncCallbackAdapter) that automatically takes the result of a service call and passes it to the <code>updateData</code> method.<br>
<br>
See the following examples:<br>
<br>
<pre><code>@DataSource("simpleGridDataSource")<br>
@DataSourceRecordIdentifier("name")<br>
public static class SimpleGridDataSource extends LocalPagedDataSource&lt;Contact&gt; {<br>
		<br>
   @Create<br>
   protected SimpleGridServiceAsync service;<br>
		<br>
   public void load()<br>
   {<br>
      service.getContactList(new DataSourceAsyncCallbackAdapter&lt;Contact&gt;(this));<br>
   }		<br>
}<br>
</code></pre>

Another <code>AsyncCallback</code> implementation is provided to do the same job to the <code>RemoteDataSourceConfiguration</code>, that is called <code>RemoteDataSourceLoadAsyncCallbackAdapter</code>. See the following example.<br>
<br>
<pre><code>@DataSource("contactDataSource")<br>
@DataSourceRecordIdentifier("name")<br>
public static class RemoteDS extends RemotePagedDataSource&lt;Contact&gt; {<br>
<br>
   @Create<br>
   protected SimpleGridServiceAsync service;<br>
<br>
   public void load(){<br>
      service.getContactCount(new RemoteDataSourceLoadAsyncCallbackAdapter(this));<br>
   }<br>
<br>
   public void fetch(int startRecord, int endRecord){<br>
      service.getContactPage(startRecord, endRecord, new DataSourceAsyncCallbackAdapter&lt;Contact&gt;(this));<br>
   }<br>
}<br>
</code></pre>