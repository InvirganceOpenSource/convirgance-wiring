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
package com.invirgance.convirgance.wiring.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to configure custom tags for Wiring files.
 * <br><br>
 * For example, placing the <code>@Wiring</code> annotation at the top of a class
 * called <code>com.example.MyObject</code> would generate the custom 
 * tag <code>&lt;MyObject&gt;&lt;/MyObject&gt;</code>.
 * <br><br>
 * Tag names can be customized by passing the desired name to the annotation. For
 * example, <code>@Wiring("Bob")</code> would generate the custom 
 * tag <code>&lt;Bob&gt;&lt;/Bob&gt;</code> instead of <code>&lt;MyObject&gt;&lt;/MyObject&gt;</code>.
 * 
 * @author jbanes
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Wiring
{
    /** Name of the custom tag **/
    public String value() default "";
}
