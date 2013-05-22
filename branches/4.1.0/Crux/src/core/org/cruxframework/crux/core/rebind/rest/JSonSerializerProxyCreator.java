/*
 * Copyright 2013 cruxframework.org.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.cruxframework.crux.core.rebind.rest;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cruxframework.crux.core.client.collection.FastList;
import org.cruxframework.crux.core.client.collection.FastMap;
import org.cruxframework.crux.core.client.service.JsonEncoder;
import org.cruxframework.crux.core.client.utils.EscapeUtils;
import org.cruxframework.crux.core.rebind.AbstractProxyCreator;
import org.cruxframework.crux.core.rebind.CruxGeneratorException;
import org.cruxframework.crux.core.utils.JClassUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.GeneratorContextExt;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.dev.generator.NameFactory;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public class JSonSerializerProxyCreator extends AbstractProxyCreator
{
	private final JType targetObjectType;
	private JClassType jsonEncoderType;
	private JClassType listType;
	private JClassType setType;
	private JClassType mapType;
	private JClassType javascriptObjectType;
	
	private static Set<String> jsonFriendlyTypes = new HashSet<String>();
	private static NameFactory nameFactory = new NameFactory();
	static
	{
		jsonFriendlyTypes.add(Integer.class.getCanonicalName());
		jsonFriendlyTypes.add(Short.class.getCanonicalName());
		jsonFriendlyTypes.add(Byte.class.getCanonicalName());
		jsonFriendlyTypes.add(Long.class.getCanonicalName());
		jsonFriendlyTypes.add(Double.class.getCanonicalName());
		jsonFriendlyTypes.add(Float.class.getCanonicalName());
		jsonFriendlyTypes.add(Boolean.class.getCanonicalName());
		jsonFriendlyTypes.add(Character.class.getCanonicalName());
		jsonFriendlyTypes.add(Integer.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Short.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Byte.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Long.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Double.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Float.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Boolean.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(Character.TYPE.getCanonicalName());
		jsonFriendlyTypes.add(String.class.getCanonicalName());
		jsonFriendlyTypes.add(Date.class.getCanonicalName());
		jsonFriendlyTypes.add(BigInteger.class.getCanonicalName());
		jsonFriendlyTypes.add(BigDecimal.class.getCanonicalName());
	}

	public JSonSerializerProxyCreator(GeneratorContextExt context, TreeLogger logger, JType targetObjectType)
	{
		super(logger, context);
		jsonEncoderType = context.getTypeOracle().findType(JsonEncoder.class.getCanonicalName());
		listType = context.getTypeOracle().findType(List.class.getCanonicalName());
		setType = context.getTypeOracle().findType(Set.class.getCanonicalName());
		mapType = context.getTypeOracle().findType(Map.class.getCanonicalName());
		javascriptObjectType = context.getTypeOracle().findType(JavaScriptObject.class.getCanonicalName());
		this.targetObjectType = targetObjectType;
	}

	@Override
	protected void generateProxyMethods(SourcePrinter srcWriter) throws CruxGeneratorException
	{
		generateEncodeMethod(srcWriter);
		generateDecodeMethod(srcWriter);

	}

	@Override
	public String getProxyQualifiedName()
	{
		return jsonEncoderType.getPackage().getName()+"."+getProxySimpleName();
	}

	@Override
	public String getProxySimpleName()
	{
		String typeName = targetObjectType.getParameterizedQualifiedSourceName().replaceAll("\\W", "_");
		return typeName+"_JsonEncoder";
	}

	@Override
	protected SourcePrinter getSourcePrinter()
	{
		String packageName = jsonEncoderType.getPackage().getName();
		PrintWriter printWriter = context.tryCreate(logger, packageName, getProxySimpleName());

		if (printWriter == null)
		{
			return null;
		}

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, getProxySimpleName());

		String[] imports = getImports();
		for (String imp : imports)
		{
			composerFactory.addImport(imp);
		}

		return new SourcePrinter(composerFactory.createSourceWriter(context, printWriter), logger);
	}

	/**
	 * @return
	 */
	protected String[] getImports()
	{
		String[] imports = new String[] {
				JSONParser.class.getCanonicalName(),	
				JSONValue.class.getCanonicalName(),
				JSONObject.class.getCanonicalName(), 
				JSONArray.class.getCanonicalName()
		};
		return imports;
	}

	private void generateEncodeMethod(SourcePrinter srcWriter)
	{
		srcWriter.println("public JSONValue encode(" + targetObjectType.getParameterizedQualifiedSourceName() + " object){");
		srcWriter.println("return null;");
		srcWriter.println("}");
	}

	private void generateDecodeMethod(SourcePrinter srcWriter)
	{
		srcWriter.println("public " + targetObjectType.getParameterizedQualifiedSourceName() + " decode(JSONValue json){");
		String decodedString = generateDecodeStringForJsonValue(srcWriter, targetObjectType, "json");
		srcWriter.println("return "+decodedString+";");
		srcWriter.println("}");
	}

	private String generateDecodeStringForJsonValue(SourcePrinter srcWriter, JType objectType, String jsonValueVar)
	{
		String resultObjectVar = nameFactory.createName("o");
		String resultSourceName = objectType.getParameterizedQualifiedSourceName();

		srcWriter.println(resultSourceName + " "+resultObjectVar + " = " + JClassUtils.getEmptyValueForType(objectType) +";");
		srcWriter.println("if ("+jsonValueVar+" != null && "+jsonValueVar+".isNull() == null){");

		if (isJsonFriendly(objectType))
		{
			generateDecodeStringForJsonFriendlyType(srcWriter, objectType, jsonValueVar, resultObjectVar);
		}
		else
		{
			JClassType objectClassType = objectType.isClassOrInterface();
			if (objectClassType == null)
			{
				throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. ");
			}
			if (objectClassType.isAssignableTo(javascriptObjectType))
			{
				srcWriter.println(resultObjectVar+" = ("+resultSourceName+")"+jsonValueVar+".isObject().getJavaScriptObject();");
			}
			else if (isCollection(objectClassType))
			{
				generateDecodeStringForCollectionType(srcWriter, objectClassType, jsonValueVar, resultObjectVar, resultSourceName);
			}
			else
			{
				generateDecodeStringForCustomType(srcWriter, objectClassType, jsonValueVar, resultObjectVar, resultSourceName);
			}
		}
		srcWriter.println("}");
		return resultObjectVar;
	}

	private boolean isCollection(JClassType objectType)
	{
		if ((objectType.isAssignableTo(listType)) || (objectType.isAssignableTo(setType)) || (objectType.isAssignableTo(mapType))
				|| (objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName())) 
				|| (objectType.getQualifiedSourceName().equals(FastList.class.getCanonicalName())))
		{
			return true;
		}
		return false;
	}
	
	private void generateDecodeStringForJsonFriendlyType(SourcePrinter srcWriter, JType objectType, String jsonValueVar, String resultObjectVar)
    {
	    try
        {
	    	if (objectType.getQualifiedSourceName().equals("java.lang.String"))
	    	{
	    		srcWriter.println(resultObjectVar + " = " + JClassUtils.getParsingExpressionForSimpleType(jsonValueVar+".isString().stringValue()", objectType) + ";");
	    	}
	    	else
	    	{
	    		srcWriter.println(resultObjectVar + " = " + JClassUtils.getParsingExpressionForSimpleType(jsonValueVar+".toString()", objectType) + ";");
	    	}
        }
        catch (NotFoundException e)
        {
			throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
			"Error Interpreting object type.", e);
        }
    }

	private void generateDecodeStringForCollectionType(SourcePrinter srcWriter, JClassType objectType, String jsonValueVar, String resultObjectVar, String resultSourceName)
	{
		boolean isList = (!objectType.isAssignableTo(mapType)) && (!objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName()));

		String jsonCollectionVar = generateJSONValueCollectionVariableCreation(srcWriter, jsonValueVar, isList);
		JClassType targetObjectType = generateCollectionInstantiation(srcWriter, objectType, resultObjectVar, resultSourceName);
		
		if (isList)
		{
			srcWriter.println("for (int i=0; i < "+jsonCollectionVar+".size(); i++){");
			String arrayItemValueVar = nameFactory.createName("item");
			srcWriter.println("JSONValue " + arrayItemValueVar + " = " + jsonCollectionVar + ".get(i);");
			String serializerName = new JSonSerializerProxyCreator(context, logger, targetObjectType).create();
			srcWriter.println(resultObjectVar+".add(new "+serializerName+"().decode("+arrayItemValueVar+"));");
			srcWriter.println("}");
		}
		else
		{
			srcWriter.println("for (String key : "+jsonCollectionVar+".keySet()){");
			String mapItemValueVar = nameFactory.createName("item");
			srcWriter.println("JSONValue " + mapItemValueVar + " = " + jsonCollectionVar + ".get(key);");
			String serializerName = new JSonSerializerProxyCreator(context, logger, targetObjectType).create();
			srcWriter.println(resultObjectVar+".put(key, new "+serializerName+"().decode("+mapItemValueVar+"));");
			srcWriter.println("}");
		}
	}

	private JClassType generateCollectionInstantiation(SourcePrinter srcWriter, JClassType objectType, String resultObjectVar, String resultSourceName)
    {
	    JClassType targetObjectType;
	    if (objectType.getQualifiedSourceName().equals(FastList.class.getCanonicalName()) 
			|| objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName())
			|| objectType.isInterface() == null)
		{
			targetObjectType = objectType.isParameterized().getTypeArgs()[0];
			srcWriter.println(resultObjectVar+" = new "+resultSourceName+"();");
		}
		else
		{
			if (objectType.isAssignableTo(listType))
			{
				targetObjectType = objectType.isParameterized().getTypeArgs()[0];
				srcWriter.println(resultObjectVar+" = new "+ArrayList.class.getCanonicalName()+"<"+targetObjectType.getParameterizedQualifiedSourceName()+">();");
			}
			else if (objectType.isAssignableTo(setType))
			{
				targetObjectType = objectType.isParameterized().getTypeArgs()[0];
				srcWriter.println(resultObjectVar+" = new "+HashSet.class.getCanonicalName()+"<"+targetObjectType.getParameterizedQualifiedSourceName()+">;");
			}
			else if (objectType.isAssignableTo(mapType))
			{
				JClassType keyObjectType = objectType.isParameterized().getTypeArgs()[0];
				if (!keyObjectType.getQualifiedSourceName().equals("java.lang.String"))
				{
					throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
								"Map Key is invalid. Only Strings are accepted.");
				}
				targetObjectType = objectType.isParameterized().getTypeArgs()[1];
				srcWriter.println(resultObjectVar+" = new "+HashMap.class.getCanonicalName()+"<"+
						keyObjectType.getParameterizedQualifiedSourceName()+","+targetObjectType.getParameterizedQualifiedSourceName()+">();");
			}
			else
			{
				throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
				"Invalid collection type.");
			}
		}
	    return targetObjectType;
    }

	private String generateJSONValueCollectionVariableCreation(SourcePrinter srcWriter, String jsonValueVar, boolean isList)
    {
	    String jsonCollectionVar;
	    if (isList)
		{
			jsonCollectionVar = nameFactory.createName("jsonArray");
			srcWriter.println("JSONArray "+jsonCollectionVar+" = "+jsonValueVar+".isArray();");
		}
		else
		{
			jsonCollectionVar = nameFactory.createName("jsonMap");
			srcWriter.println("JSONObject "+jsonCollectionVar+" = "+jsonValueVar+".isObject();");
		}
	    return jsonCollectionVar;
    }

	private void generateDecodeStringForCustomType(SourcePrinter srcWriter, JClassType objectType, String jsonValueVar, String resultObjectVar, String resultSourceName)
	{
		if (objectType.isInterface() != null || objectType.isAbstract())
		{
			throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
			"Custom types must be concrete classes. Interfaces and abstract classes are not allowed.");
		}
		if (objectType.findConstructor(new JType[]{}) == null)
		{
			throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
			"It must declare a public and empty cosntructor.");
		}

		String jsonObjectVar = nameFactory.createName("jsonObject");
		srcWriter.println("JSONObject "+jsonObjectVar+" = "+jsonValueVar+".isObject();");
		srcWriter.println(resultObjectVar+" = new "+resultSourceName+"();");

		List<JMethod> setterMethods = JClassUtils.getSetterMethods(objectType);
		for (JMethod method : setterMethods)
		{
			String property = JClassUtils.getPropertyForSetterMethod(method);
			JType paramType = method.getParameterTypes()[0];
			String paramObjectVar = nameFactory.createName("param");
			srcWriter.println("JSONValue "+paramObjectVar+" = "+jsonObjectVar+".get("+EscapeUtils.quote(property)+");");
			String serializerName = new JSonSerializerProxyCreator(context, logger, paramType).create();
			srcWriter.println(resultObjectVar+"."+method.getName()+"(new "+serializerName+"().decode("+paramObjectVar+"));");
		}
	}

	public static boolean isJsonFriendly(JType jType)
	{
		return (jsonFriendlyTypes.contains(jType.getQualifiedSourceName()));
	}
}
