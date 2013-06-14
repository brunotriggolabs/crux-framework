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
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.dev.generator.NameFactory;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
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

	private static NameFactory nameFactory = new NameFactory();

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
				JSONArray.class.getCanonicalName(),
				JSONNull.class.getCanonicalName(), 
				JSONNumber.class.getCanonicalName(),
				JSONBoolean.class.getCanonicalName(), 
				JSONString.class.getCanonicalName()
		};
		return imports;
	}

	private void generateEncodeMethod(SourcePrinter srcWriter)
	{
		srcWriter.println("public JSONValue encode(" + targetObjectType.getParameterizedQualifiedSourceName() + " object){");
		String encoded = generateEncodeObject(srcWriter, targetObjectType, "object");
		srcWriter.println("return "+encoded+";");
		srcWriter.println("}");
	}

	private void generateDecodeMethod(SourcePrinter srcWriter)
	{
		srcWriter.println("public " + targetObjectType.getParameterizedQualifiedSourceName() + " decode(JSONValue json){");
		String decodedString = generateDecodeJsonValue(srcWriter, targetObjectType, "json");
		srcWriter.println("return "+decodedString+";");
		srcWriter.println("}");
	}

	private String generateDecodeJsonValue(SourcePrinter srcWriter, JType objectType, String jsonValueVar)
	{
		String resultObjectVar = nameFactory.createName("o");
		String resultSourceName = objectType.getParameterizedQualifiedSourceName();

		srcWriter.println(resultSourceName + " "+resultObjectVar + " = " + JClassUtils.getEmptyValueForType(objectType) +";");
		srcWriter.println("if ("+jsonValueVar+" != null && "+jsonValueVar+".isNull() == null){");

		if (JClassUtils.isSimpleType(objectType))
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

	private String generateEncodeObject(SourcePrinter srcWriter, JType objectType, String objectVar)
	{
		String resultJSONValueVar = nameFactory.createName("json");

		srcWriter.println("JSONValue "+resultJSONValueVar + " = JSONNull.getInstance();");
		boolean isPrimitive = objectType.isPrimitive() != null;
		
		if (!isPrimitive)
		{
			srcWriter.println("if ("+objectVar+" != null){");
		}

		if (JClassUtils.isSimpleType(objectType))
		{
			generateEncodeStringForJsonFriendlyType(srcWriter, objectType, objectVar, resultJSONValueVar);
		}
		else
		{
			JClassType objectClassType = objectType.isClassOrInterface();
			if (objectClassType == null)
			{
				throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be serialized by JsonEncoder. ");
			}
			if (objectClassType.isAssignableTo(javascriptObjectType))
			{
				srcWriter.println(resultJSONValueVar+" = new JSONObject("+objectVar+");");
			}
			else if (isCollection(objectClassType))
			{
				generateEncodeStringForCollectionType(srcWriter, objectClassType, objectVar, resultJSONValueVar);
			}
			else
			{
				generateEncodeStringForCustomType(srcWriter, objectClassType, objectVar, resultJSONValueVar);
			}
		}
		if (!isPrimitive)
		{
			srcWriter.println("}");
		}
		return resultJSONValueVar;
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

	private void generateEncodeStringForJsonFriendlyType(SourcePrinter srcWriter, JType objectType, String objectVar, String resultJSONValueVar)
	{
		if (objectType.getQualifiedSourceName().equals(String.class.getCanonicalName()))
		{
			srcWriter.println(resultJSONValueVar + " = new JSONString(" + objectVar + ");");
		}
		else if ((objectType == JPrimitiveType.BYTE) || (objectType.getQualifiedSourceName().equals(Byte.class.getCanonicalName()))
				||(objectType == JPrimitiveType.SHORT) || (objectType.getQualifiedSourceName().equals(Short.class.getCanonicalName()))
				||(objectType == JPrimitiveType.INT) || (objectType.getQualifiedSourceName().equals(Integer.class.getCanonicalName()))
				||(objectType == JPrimitiveType.LONG) || (objectType.getQualifiedSourceName().equals(Long.class.getCanonicalName()))
				||(objectType == JPrimitiveType.FLOAT) || (objectType.getQualifiedSourceName().equals(Float.class.getCanonicalName()))
				||(objectType == JPrimitiveType.DOUBLE) || (objectType.getQualifiedSourceName().equals(Double.class.getCanonicalName())))
		{
			srcWriter.println(resultJSONValueVar + " = new JSONNumber(" + objectVar + ");");
		}
		else if (objectType.getQualifiedSourceName().equals(Date.class.getCanonicalName()))
		{
			srcWriter.println(resultJSONValueVar + " = new JSONNumber(" + objectVar + ".getTime());");
		}
		else if ((objectType == JPrimitiveType.BOOLEAN) || (objectType.getQualifiedSourceName().equals(Boolean.class.getCanonicalName())))
		{
			srcWriter.println(resultJSONValueVar + " = JSONBoolean.getInstance(" + objectVar + ");");
		}
		else if ((objectType == JPrimitiveType.CHAR) || (objectType.getQualifiedSourceName().equals(Character.class.getCanonicalName())))
		{
			srcWriter.println(resultJSONValueVar + " = new JSONString(\"\"+" + objectVar + ");");
		}
		else if (objectType.isEnum() != null)
		{
			srcWriter.println(resultJSONValueVar + " = new JSONString(" + objectVar + ".toString());");
		}
		else if (objectType.getQualifiedSourceName().equals(BigInteger.class.getCanonicalName())
				|| objectType.getQualifiedSourceName().equals(BigDecimal.class.getCanonicalName()))
		{
			srcWriter.println(resultJSONValueVar + " = new JSONString(" + objectVar + ".toString());");
		}
		else
		{
			throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be serialized by JsonEncoder. " +
			"Error Interpreting object type.");
		}
	}

	private void generateDecodeStringForCollectionType(SourcePrinter srcWriter, JClassType objectType, String jsonValueVar, String resultObjectVar, String resultSourceName)
	{
		boolean isList = (!objectType.isAssignableTo(mapType)) && (!objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName()));

		String jsonCollectionVar = generateJSONValueCollectionForDecode(srcWriter, jsonValueVar, isList);
		JClassType targetObjectType = getCollectionTargetType(objectType);
		generateCollectionInstantiation(srcWriter, objectType, resultObjectVar, resultSourceName, targetObjectType);

		String serializerName = new JSonSerializerProxyCreator(context, logger, targetObjectType).create();
		String serializerVar = nameFactory.createName("serializer");
		srcWriter.println(serializerName+" "+serializerVar+" = new "+serializerName+"();");
		if (isList)
		{
			srcWriter.println("for (int i=0; i < "+jsonCollectionVar+".size(); i++){");
			srcWriter.println(resultObjectVar+".add("+serializerVar+".decode("+jsonCollectionVar + ".get(i)));");
			srcWriter.println("}");
		}
		else
		{
			srcWriter.println("for (String key : "+jsonCollectionVar+".keySet()){");
			srcWriter.println(resultObjectVar+".put(key, "+serializerVar+".decode("+jsonCollectionVar + ".get(key)));");
			srcWriter.println("}");
		}
	}

	private void generateCollectionInstantiation(SourcePrinter srcWriter, JClassType objectType, 
													   String resultObjectVar, String resultSourceName, 
													   JClassType targetObjectType)
	{
		if (objectType.getQualifiedSourceName().equals(FastList.class.getCanonicalName()) 
				|| objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName())
				|| objectType.isInterface() == null)
		{
			srcWriter.println(resultObjectVar+" = new "+resultSourceName+"();");
		}
		else
		{
			if (objectType.isAssignableTo(listType))
			{
				srcWriter.println(resultObjectVar+" = new "+ArrayList.class.getCanonicalName()+"<"+targetObjectType.getParameterizedQualifiedSourceName()+">();");
			}
			else if (objectType.isAssignableTo(setType))
			{
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
				srcWriter.println(resultObjectVar+" = new "+HashMap.class.getCanonicalName()+"<"+
						keyObjectType.getParameterizedQualifiedSourceName()+","+targetObjectType.getParameterizedQualifiedSourceName()+">();");
			}
			else
			{
				throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be deserialized by JsonEncoder. " +
				"Invalid collection type.");
			}
		}
	}

	private String generateJSONValueCollectionForDecode(SourcePrinter srcWriter, String jsonValueVar, boolean isList)
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

	private void generateEncodeStringForCollectionType(SourcePrinter srcWriter, JClassType objectType, String objectVar, String resultJSONValueVar)
	{
		boolean isList = (!objectType.isAssignableTo(mapType)) && (!objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName()));

		JClassType targetObjectType = getCollectionTargetType(objectType);
		generateJSONValueCollectionForEncode(srcWriter, resultJSONValueVar, isList);

		String serializerName = new JSonSerializerProxyCreator(context, logger, targetObjectType).create();
		String serializerVar = nameFactory.createName("serializer");
		srcWriter.println(serializerName+" "+serializerVar+" = new "+serializerName+"();");
		if (isList)
		{
			srcWriter.println("for ("+targetObjectType.getParameterizedQualifiedSourceName()+" obj: "+objectVar+"){");
			srcWriter.println(resultJSONValueVar+".isArray().set("+resultJSONValueVar+".isArray().size(), "+serializerVar+".encode(obj));");
			srcWriter.println("}");
		}
		else
		{
			srcWriter.println("for (String key : "+objectVar+".keySet()){");
			srcWriter.println(resultJSONValueVar+".isObject().put(key, "+serializerVar+".encode("+objectVar+".get(key)));");
			srcWriter.println("}");
		}
	}

	private void generateJSONValueCollectionForEncode(SourcePrinter srcWriter, String resultJSONValueVar, boolean isList)
	{
		if (isList)
		{
			srcWriter.println(resultJSONValueVar+" = new JSONArray();");
		}
		else
		{
			srcWriter.println(resultJSONValueVar+" = new JSONObject();");
		}
	}

	private JClassType getCollectionTargetType(JClassType objectType)
	{
		JClassType targetObjectType;
		if (objectType.getQualifiedSourceName().equals(FastList.class.getCanonicalName()) 
				|| objectType.getQualifiedSourceName().equals(FastMap.class.getCanonicalName())
				|| (objectType.isAssignableTo(listType))
				|| (objectType.isAssignableTo(setType)))
		{
			targetObjectType = objectType.isParameterized().getTypeArgs()[0];
		}
		else if (objectType.isAssignableTo(mapType))
		{
			JClassType keyObjectType = objectType.isParameterized().getTypeArgs()[0];
			if (!keyObjectType.getQualifiedSourceName().equals("java.lang.String"))
			{
				throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be serialized by JsonEncoder. " +
				"Map Key is invalid. Only Strings are accepted.");
			}
			targetObjectType = objectType.isParameterized().getTypeArgs()[1];
		} 
		else 
		{
			throw new CruxGeneratorException("Type ["+objectType.getParameterizedQualifiedSourceName()+"] can not be serialized by JsonEncoder. " +
			"Invalid collection type.");
		}
		return targetObjectType;
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
			String property = JClassUtils.getPropertyForGetterOrSetterMethod(method);
			JType paramType = method.getParameterTypes()[0];
			String serializerName = new JSonSerializerProxyCreator(context, logger, paramType).create();
			srcWriter.println(resultObjectVar+"."+method.getName()+"(new "+serializerName+"().decode("+jsonObjectVar+".get("+EscapeUtils.quote(property)+")));");
		}
	}


	private void generateEncodeStringForCustomType(SourcePrinter srcWriter, JClassType objectType, String objectVar, String resultJSONValueVar)
	{
		srcWriter.println(resultJSONValueVar+" = new JSONObject();");
		
		List<JMethod> getterMethods = JClassUtils.getGetterMethods(objectType);
		for (JMethod method : getterMethods)
		{
			String property = JClassUtils.getPropertyForGetterOrSetterMethod(method);
			JType returnType = method.getReturnType();
			String serializerName = new JSonSerializerProxyCreator(context, logger, returnType).create();
			srcWriter.println(resultJSONValueVar+".isObject().put("+EscapeUtils.quote(property)+", new "+serializerName+"().encode("+objectVar+"."+method.getName()+"()));");
		}
	}
}
