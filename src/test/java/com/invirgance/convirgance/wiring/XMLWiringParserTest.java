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

import com.test.TestBean;
import com.invirgance.convirgance.output.JSONOutput;
import com.invirgance.convirgance.source.ClasspathSource;
import com.invirgance.convirgance.web.binding.QueryBinding;
import com.invirgance.convirgance.web.parameter.RequestParameter;
import com.invirgance.convirgance.web.service.SelectService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class XMLWiringParserTest
{
    @Test
    public void testDatabaseParse()
    {
        var parser = new XMLWiringParser(new ClasspathSource("/database.xml"));
        var object = (SelectService)parser.getRoot();
        
        assertEquals(3, object.getParameters().size());
        assertEquals("zipcode", object.getParameters().get(0).getName());
        assertEquals("state", object.getParameters().get(1).getName());
        assertEquals("discountCode", object.getParameters().get(2).getName());
        assertEquals(3, object.getParameters().size());
        assertEquals("", ((RequestParameter)object.getParameters().get(0)).getDefaultValue());
        assertEquals("", ((RequestParameter)object.getParameters().get(1)).getDefaultValue());
        assertNull(((RequestParameter)object.getParameters().get(2)).getDefaultValue());
        
        assertEquals(QueryBinding.class, object.getBinding().getClass());
        assertEquals("jdbc/sample", ((QueryBinding)object.getBinding()).getJndiName());
        assertEquals("select * from APP.CUSTOMER\n" +
"                    where (:zipcode = '' or ZIP = :zipcode)\n" +
"                    and (:state = '' or STATE = :state)\n" +
"                    and (:discountCode = '' or DISCOUNT_CODE = :discountCode)", ((QueryBinding)object.getBinding()).getSql().trim());

        assertEquals(JSONOutput.class, object.getOutput().getClass());
    }
    
    
    @Test
    public void testBeanParse()
    {
        var parser = new XMLWiringParser(new ClasspathSource("/bean.xml"));
        var object = (TestBean)parser.getRoot();
        
        System.out.println(object);
        System.out.println("Hello: " + parser.get("one"));
        System.out.println("Goodbye: " + parser.get("goodbye"));
    }
}
