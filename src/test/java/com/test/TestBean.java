package com.test;

import java.util.List;
import java.util.Map;

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

/**
 *
 * @author jbanes
 */
public class TestBean
{
    private String string;
    
    private int primitiveInt;
    private long primitiveLong;
    private boolean primitiveBoolean;
    private float primitiveFloat;
    private double primitiveDouble;
    
    private Integer objectInteger;
    private Long objectLong;
    private Boolean objectBoolean;
    private Float objectFloat;
    private Double objectDouble;
    
    private List<String> list;
    private Map<String,Integer> map;
    
    private TestBean object;

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        this.string = string;
    }

    public int getPrimitiveInt()
    {
        return primitiveInt;
    }

    public void setPrimitiveInt(int primitiveInt)
    {
        this.primitiveInt = primitiveInt;
    }

    public long getPrimitiveLong()
    {
        return primitiveLong;
    }

    public void setPrimitiveLong(long primitiveLong)
    {
        this.primitiveLong = primitiveLong;
    }

    public boolean isPrimitiveBoolean()
    {
        return primitiveBoolean;
    }

    public void setPrimitiveBoolean(boolean primitiveBoolean)
    {
        this.primitiveBoolean = primitiveBoolean;
    }

    public Integer getObjectInteger()
    {
        return objectInteger;
    }

    public void setObjectInteger(Integer objectInteger)
    {
        this.objectInteger = objectInteger;
    }

    public Long getObjectLong()
    {
        return objectLong;
    }

    public void setObjectLong(Long objectLong)
    {
        this.objectLong = objectLong;
    }

    public Boolean getObjectBoolean()
    {
        return objectBoolean;
    }

    public void setObjectBoolean(Boolean objectBoolean)
    {
        this.objectBoolean = objectBoolean;
    }

    public float getPrimitiveFloat()
    {
        return primitiveFloat;
    }

    public void setPrimitiveFloat(float primitiveFloat)
    {
        this.primitiveFloat = primitiveFloat;
    }

    public double getPrimitiveDouble()
    {
        return primitiveDouble;
    }

    public void setPrimitiveDouble(double primitiveDouble)
    {
        this.primitiveDouble = primitiveDouble;
    }

    public Float getObjectFloat()
    {
        return objectFloat;
    }

    public void setObjectFloat(Float objectFloat)
    {
        this.objectFloat = objectFloat;
    }

    public Double getObjectDouble()
    {
        return objectDouble;
    }

    public void setObjectDouble(Double objectDouble)
    {
        this.objectDouble = objectDouble;
    }

    public List<String> getList()
    {
        return list;
    }

    public void setList(List<String> list)
    {
        this.list = list;
    }

    public Map<String, Integer> getMap()
    {
        return map;
    }

    public void setMap(Map<String, Integer> map)
    {
        this.map = map;
    }

    public TestBean getObject()
    {
        return object;
    }

    public void setObject(TestBean object)
    {
        this.object = object;
    }

    @Override
    public String toString()
    {
        return "[" + 
                "\n    string=" + string +
                ",\n    primitiveInt=" + primitiveInt +
                ",\n    primitiveLong=" + primitiveLong +
                ",\n    primitiveBoolean=" + primitiveBoolean +
                ",\n    primitiveFloat=" + primitiveFloat +
                ",\n    primitiveDouble=" + primitiveDouble +
                ",\n    objectInteger=" + objectInteger +
                ",\n    objectLong=" + objectLong +
                ",\n    objectBoolean=" + objectBoolean +
                ",\n    objectFloat=" + objectFloat +
                ",\n    objectDouble=" + objectDouble +
                ",\n    list=" + list +
                ",\n    map=" + map +
                ",\n    object=" + object +
                "\n]";
    }
    
}
