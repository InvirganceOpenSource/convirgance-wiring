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

import com.google.auto.service.AutoService;
import com.invirgance.convirgance.wiring.XMLWiringParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 *
 * @author jbanes
 */
@SupportedAnnotationTypes("com.invirgance.convirgance.wiring.annotation.Wiring")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class WiredProcessor extends AbstractProcessor
{
    private Properties getDefaults()
    {
        Properties properties = new Properties();
        Filer filer = processingEnv.getFiler();
        FileObject file;
        
        try
        {
            file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/wirings.properties");
            
            try(InputStream in = file.openInputStream())
            {
                properties.load(in);
            }
        }
        catch(IOException e)
        {
            System.err.println("No existing wirings.properties found");
        }
        
        try
        {
            file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/wiring.properties");
            
            try(InputStream in = file.openInputStream())
            {
                properties.load(in);
            }
        }
        catch(IOException e)
        {
            System.err.println("No existing wiring.properties found");
        }
        
        return properties;
    }
    
    private void writeWirings(Properties properties)
    {
        Filer filer = processingEnv.getFiler();
        FileObject file;
        
        try
        {
            file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/wirings.properties");

            try(OutputStream out = file.openOutputStream())
            {
                properties.store(out, null);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.err.println("Unable to generate META-INF/wirings.properties!");
        }
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        Properties properties = getDefaults();
        
        Set<? extends Element> elements;
        Wiring wiring;
        
        String name;
        String className;

        System.out.println("Annotation List: ");

        for(TypeElement type : annotations)
        {
            elements = roundEnv.getElementsAnnotatedWith(type);
            
            for(Element element : elements)
            {
                wiring = element.getAnnotation(Wiring.class);
                name = wiring.value().isBlank() ? element.getSimpleName().toString() : wiring.value();
                className = ((TypeElement)element).getQualifiedName().toString();
                
                properties.put(name, className);
            }
        }
            
        System.out.println(properties);
        
        if(!properties.isEmpty()) writeWirings(properties);
        
        return true;
    }
}
