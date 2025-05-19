/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.wiring;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.source.Source;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author jbanes
 */
public class XMLWiringParser
{
    private Document document;
    private Object root;
    
    private Map<String,Object> lookup;
    private List<Reference> references;
    
    public XMLWiringParser(Source source)
    {
        this.document = load(source);
        this.lookup = new HashMap<>();
        this.references = new ArrayList<>();
        this.root = parse(this.document.getDocumentElement());
        
        for(Reference reference : references)
        {
            reference.apply();
        }
    }
    
    private Document load(Source source)
    {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        
        try(InputStream in = source.getInputStream())
        {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            
            return builder.parse(in);
        }
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            throw new ConvirganceException(e);
        }
    }
    
    private Object getValue(NodeList list)
    {
        StringBuffer buffer = new StringBuffer();
        Element element = null;
        Node child;
        
        for(int i=0; i<list.getLength(); i++)
        {
            child = list.item(i);
            
            if(child instanceof Comment)
            {
                continue;
            }
            else if(child instanceof Element && element == null)
            {
                element = (Element)child;
            }
            else if(child instanceof Text)
            {
                if(child instanceof CDATASection) buffer.append(child.getNodeValue());
                else if(i == list.getLength()-1 && child.getNodeValue().trim().length() < 1) continue;
                else if(buffer.length() < 1 && child.getNodeValue().trim().length() < 1) continue;
                else buffer.append(child.getNodeValue());
            }
            else
            {
                if(element != null) throw new ConvirganceException("Unexpected element " + child.getNodeName() + ", child element " + element.getTagName() + " already found"); 
                
                throw new ConvirganceException("Unexpected node " + child.getNodeName());
            }
        }

        if(element != null) return parse(element);
        
        return buffer.toString();
    }
    
    private Object coerceValue(Class type, Object value)
    {
        if(!(value instanceof String)) return value;
        
        if(type.isPrimitive())
        {
            if(type.equals(byte.class)) return Byte.valueOf((String)value);
            if(type.equals(short.class)) return Short.valueOf((String)value);
            if(type.equals(int.class)) return Integer.valueOf((String)value);
            if(type.equals(long.class)) return Long.valueOf((String)value);
            if(type.equals(boolean.class)) return Boolean.valueOf((String)value);
            if(type.equals(float.class)) return Float.valueOf((String)value);
            if(type.equals(double.class)) return Double.valueOf((String)value);
            if(type.equals(char.class)) return value.toString().charAt(0);
        }
        else
        {
            if(type.equals(Byte.class)) return Byte.valueOf((String)value);
            if(type.equals(Short.class)) return Short.valueOf((String)value);
            if(type.equals(Integer.class)) return Integer.valueOf((String)value);
            if(type.equals(Long.class)) return Long.valueOf((String)value);
            if(type.equals(Boolean.class)) return Boolean.valueOf((String)value);
            if(type.equals(Float.class)) return Float.valueOf((String)value);
            if(type.equals(Double.class)) return Double.valueOf((String)value);
            if(type.equals(Character.class)) return value.toString().charAt(0);
        }
        
        return value;
    }
    
    private void setValue(Object parent, Method method, Object value)
    {
        var parameter = method.getParameters()[0];
                
        try
        {
            if(value instanceof Reference)
            {
                references.add(new MethodReference(parent, method, (Reference)value));
                return;
            }

            method.invoke(parent, coerceValue(parameter.getType(), value));
        }
        catch(IllegalAccessException | InvocationTargetException ex)
        {
            throw new ConvirganceException(ex);
        }
    }
    
    private Object populateObject(Object object, NodeList list)
    {
        Node child;
        PropertyDescriptor descriptor;
        Method method;
        Object value;
        
        for(int i=0; i<list.getLength(); i++)
        {
            child = list.item(i);
            
            // Skip comments, whitespace, and other unnecessary info
            if(!(child instanceof Element)) continue;
            
            try
            {
                descriptor = new PropertyDescriptor(child.getNodeName(), object.getClass());
                method = descriptor.getWriteMethod();
                value = getValue(child.getChildNodes());
                
                registerId(((Element)child), value, true);
                setValue(object, method, value);
            }
            catch(IntrospectionException e)
            {
                throw new ConvirganceException("Property " + child.getNodeName() + " does not exist on object " + object.getClass().getName());
            }
        }
        
        return object;
    }
    
    private Object parseObject(Element element)
    {
        Class clazz;
        
        try
        {
            clazz = Class.forName(element.getAttribute("class"));

            return populateObject(clazz.getConstructor().newInstance(), element.getChildNodes());
        }
        catch(Exception e)
        {
            throw new ConvirganceException(e);
        }
    }
    
    private List parseList(NodeList children)
    {
        var list = new ArrayList();
        Object value;
        Node child;
        
        for(int i=0; i<children.getLength(); i++)
        {
            child = children.item(i);
            
            if(!(child instanceof Element)) continue;
            
            value = parse((Element)child);
            
            if(value instanceof Reference)
            {
                references.add(new ListReference(list, list.size(), (Reference)value));
                list.add(null);
            }
            else
            {
                list.add(value);
            }
        }
        
        return list;
    }
    
    private Map.Entry parseEntry(NodeList children)
    {
        Object[] keyValue = new Object[2];
        int index = 0;
        Node child;
                
        for(int i=0; i<children.getLength(); i++)
        {
            child = children.item(i);
            
            if(!(child instanceof Element)) continue;
            if(index >= keyValue.length) throw new ConvirganceException("Too many values in Map entry! Should be just key and value.");
            
            keyValue[index++] = parse((Element)child);
        }
        
        return new Map.Entry()
        {
            @Override
            public Object getKey()
            {
                return keyValue[0];
            }

            @Override
            public Object getValue()
            {
                return keyValue[1];
            }

            @Override
            public Object setValue(Object value)
            {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
    
    private Map parseMap(NodeList children)
    {
        var map = new HashMap();
        Node child;
        Map.Entry entry;
        
        for(int i=0; i<children.getLength(); i++)
        {
            child = children.item(i);
            
            if(!(child instanceof Element)) continue;
            if(!child.getNodeName().equals("entry")) throw new ConvirganceException("Unexpected value " + child.getNodeName() + " while parsing Map");
            
            entry = parseEntry(child.getChildNodes());
            
            map.put(entry.getKey(), entry.getValue());
        }
        
        return map;
    }
    
    
    private Object parse(Element element)
    {
        Object value = parseValue(element);
        
        registerId(element, value, false);
        
        return value;
    }
    
    private Object parseValue(Element element)
    {
        String id;
        
        switch(element.getTagName())
        {
            case "object":
                return parseObject(element);
            
            case "list":
                return parseList(element.getChildNodes());
            
            case "map":
                return parseMap(element.getChildNodes());
                
            case "ref":
            case "reference":
                id = element.getAttribute("id");
                
                if(!lookup.containsKey(id)) return new Reference(id);
                
                return lookup.get(id);
                
            case "null":
                return null;
                
            case "string":
                return getValue(element.getChildNodes()).toString();
                
            case "int":
            case "integer":
                return Integer.valueOf(getValue(element.getChildNodes()).toString());
                
            case "long":
                return Long.valueOf(getValue(element.getChildNodes()).toString());
                
            case "boolean":
                return Boolean.valueOf(getValue(element.getChildNodes()).toString());
                
            case "float":
                return Float.valueOf(getValue(element.getChildNodes()).toString());
                
            case "double":
                return Double.valueOf(getValue(element.getChildNodes()).toString());
                
            default:
                throw new ConvirganceException("Unknown object type " + element.getTagName());
        }
    }
    
    private void registerId(Element element, Object value, boolean property)
    {
        String id = element.getAttribute("id");
        String name = element.getTagName();
        boolean check = element.hasAttribute("id");
        
        if(!element.hasAttribute("id")) return;
        if(!property && name.equals("ref")) return;
        if(!property && name.equals("reference")) return;
        
        if(lookup.containsKey(id)) throw new ConvirganceException("Duplicate id " + id + " on " + element.getNodeName() + " tag");

        lookup.put(id, value);
    }
    
    public Object getRoot()
    {
        return root;
    }
    
    public Object get(String id)
    {
        return lookup.get(id);
    }
    
    private class Reference
    {
        private String id;
        
        public Reference(String id)
        {
            this.id = id;
        }
        
        public Object getValue()
        {
            if(!lookup.containsKey(id)) throw new ConvirganceException("Reference to id \"" + id + "\" not found");
            
            return lookup.get(id);
        }
        
        public void apply()
        {
            // Not implemented
        }
    }
    
    private class MethodReference extends Reference
    {
        private Object parent;
        private Method method;
        
        public MethodReference(Object parent, Method method, Reference reference)
        {
            super(reference.id);
            
            this.parent = parent;
            this.method = method;
        }

        @Override
        public void apply()
        {
            var parameter = method.getParameters()[0];
                
            try
            {
                method.invoke(parent, coerceValue(parameter.getType(), getValue()));
            }
            catch(IllegalAccessException | InvocationTargetException ex)
            {
                throw new ConvirganceException(ex);
            }
        }
    }
    
    private class ListReference extends Reference
    {
        private List list;
        private int index;
        
        public ListReference(List list, int index, Reference reference)
        {
            super(reference.id);
            
            this.list = list;
            this.index = index;
        }

        @Override
        public void apply()
        {
            list.set(index, getValue());
        }
    }
}
