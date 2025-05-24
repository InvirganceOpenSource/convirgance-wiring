# Convirgance (Wiring)

![Version](https://img.shields.io/badge/Version-pre&dash;release-blue) ![License](https://img.shields.io/badge/License-MIT-green) ![Repository](https://img.shields.io/badge/Platform-Java-gold) ![Repository](https://img.shields.io/badge/Repository-Maven_Central-red)

Solution for compositional configurations that wire up complex Java object hierarchies like ETL pipelines, web services, and OLAP schemas. Used with 
[Convirgance (OLAP)](https://github.com/InvirganceOpenSource/convirgance-olap/) and 
[Convirgance (Web Services)](https://github.com/InvirganceOpenSource/convirgance-web/) for configuration. 


## Installation

Add the following dependency to your Maven `pom.xml` file:

```xml
<dependency>
    <groupId>com.invirgance</groupId>
    <artifactId>convirgance-wiring</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Example

Let's say we want to develop a system for building ETL pipelines. Operations might include loading a CSV file and running a SQL statement to transform the data.

First we would create a library of the operations we want. We can optionally use the `@Wiring` annotation to create custom tags rather than referencing the
fully qualified Java name.

![Example Wiring](https://github.com/user-attachments/assets/ad75422c-9cc1-4d1f-a6d8-8e063a2da0e5)

With our library in hand, we can now create configuration files to wire up these pipelines:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<ETLPipeline>
    <operations>
        <list>
            <CSVLoad>
                <filename>customer_data.csv</filename>
            </CSVLoad>
            <UpdateCommand>
                <sql>
                <![CDATA[
                    insert into PROCESSED
                    select * from RAW;
                ]]>
                </sql>
            </UpdateCommand>
        </list>
    </operations>
</ETLPipeline>

```

Our pipeline runner then just needs to obtain the object graph from the configuration file and run it:

```java
var source = new FileSource("mypipeline.xml");
var pipeline = (ETLOperation)new XMLWiringParser(source).getRoot();

pipeline.execute(datasource);
```

## Documentation

- [Documentation](https://docs.invirgance.com/convirgance/latest/#/convirgance-wiring?id=wiring-compositional-configuration)
- [JavaDocs](https://docs.invirgance.com/javadocs/convirgance-wiring/)


## License

Convirgance is available under the MIT License. See [License](LICENSE.md) for more details.
