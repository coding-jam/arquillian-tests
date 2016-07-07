# Arquillian Sample Project

Sample project for investigating Arquillian

## Some How To



### How to generate a project from WildFly archetype

```
mvn archetype:generate -DarchetypeGroupId=org.wildfly.archetype -DarchetypeArtifactId=wildfly-javaee7-webapp-ear-blank-archetype
```

### How to import Maven dependencies into Arquillian Deployment

In order to use Maven dependecies in Arquillian deployment add following dependency to ```pom.xml```

```
<dependency>
    <groupId>org.jboss.shrinkwrap.resolver</groupId>
    <artifactId>shrinkwrap-resolver-impl-maven</artifactId>
    <scope>test</scope>
</dependency>
```

Version is inherited from WildFly BOM

### Hot to use Transactions

In order to clean db after a test, ```UserTransaction``` JTA interface is required (because datasource is JTA even in tests!!)

```
<dependency>
    <groupId>javax.transaction</groupId>
    <artifactId>jta</artifactId>
    <version>1.1</version>
    <scope>test</scope>
</dependency>
```

### How to test web project

In order to enable Arquillian in Web module, copy from ejb project to web project:

 * Arquillian and JUnit dependencies
 * ```arquillian.xml```
 * Arquillian Maven profiles

### How to auto-enable integration tests

To trigger integration tests (\*IT.class, \*ITCase.class, IT\*.class) add ```failsafe``` plugin:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.18.1</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

to profiles:

 * arq-wildfly-managed
 * arq-wildfly-remote


