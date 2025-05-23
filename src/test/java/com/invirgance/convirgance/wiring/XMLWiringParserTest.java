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

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.test.TestBean;
import com.invirgance.convirgance.source.ClasspathSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class XMLWiringParserTest
{   
    @Test
    public void testBeanParse()
    {
        var source = new ClasspathSource("/bean.xml");
        var parser = new XMLWiringParser<TestBean>(source);
        var object = parser.getRoot();
        
        assertEquals("Hello world!", object.getString());
        assertEquals(12, object.getPrimitiveInt());
        assertEquals(1337l, object.getPrimitiveLong());
        assertTrue(object.isPrimitiveBoolean());
        assertEquals(12.3f, object.getPrimitiveFloat());
        assertEquals(13.37, object.getPrimitiveDouble());
        
        assertEquals(12, object.getObjectInteger());
        assertEquals(1337l, object.getObjectLong());
        assertTrue(object.getObjectBoolean());
        assertEquals(12.3f, object.getObjectFloat());
        assertEquals(13.37, object.getObjectDouble());
        
        assertEquals(3, object.getList().size());
        assertEquals("One", object.getList().get(0));
        assertEquals("Two", object.getList().get(1));
        assertEquals("Three", object.getList().get(2));
        
        assertEquals(3, object.getMap().size());
        assertEquals(1, object.getMap().get("One"));
        assertEquals(2, object.getMap().get("Two"));
        assertEquals(3, object.getMap().get("Three"));
        
        assertNotNull(object.getObject());
        assertEquals("Goodbye, Cruel World!", object.getObject().getString());
        assertEquals(12, object.getObject().getPrimitiveInt()); // Mapped from id reference
        
        assertEquals(new JSONObject("{\"value\":\"Hello world!\"}"), object.getJsonObject());
        assertEquals(new JSONArray("[\"One\",\"Two\",\"Three\"]"), object.getJsonArray());
        assertEquals(new JSONObject("{\"one\":1,\"two\":2,\"three\":3}"), object.getObject().getJsonObject());
        assertEquals(new JSONArray("[\"One\",\"Two\",\"Three\"]"), object.getObject().getJsonArray());
        
        // Test ID lookups
        assertEquals("One", parser.get("one"));
        assertEquals("Goodbye, Cruel World!", parser.get("goodbye"));
    }
    
    @Test
    public void testDelayedReference()
    {
        var source = new ClasspathSource("/references.xml");
        var parser = new XMLWiringParser<TestBean>(source);
        var object = parser.getRoot();
        
        assertEquals(12, object.getPrimitiveInt());
        assertEquals(12, object.getObjectInteger());
        
        assertEquals(1, object.getList().size());
        assertEquals("One", object.getList().get(0));
        
        assertEquals(3, object.getMap().size());
        assertEquals(1, object.getMap().get("One"));
        assertEquals(2, object.getMap().get("Two"));
        assertEquals(3, object.getMap().get("Three"));
    }
        
}
