# javaagent-datacollector
A Java agent demo for tracing invoked methods.

This tools is based on the java agent and instrumentation.

Java agents work at the lowest level by providing services that enable us to intrude into a running Java program in JVM. 
Java agents are part of the Java Instrumentation API. 
The Instrumentation APIs provide a mechanism to modify bytecodes of methods. 
This can be done both statically and dynamically. 
This means that we can change a program by adding code to it without having to touch upon the actual source code of the program. 
The result can have a significant impact on the overall behavior of the application.

### What is it used for?

In this repo, we use it typically to collect the methods that are invoked in the tests. 

### Related work

- Arthas
  [Alibaba/Arthas](https://github.com/alibaba/arthas)
- JProfile
  [JProfile](https://www.ej-technologies.com/products/jprofiler/overview.html)
- Jacoco
  [Jacoco](https://github.com/jacoco/jacoco)
- Maven Command 
  [maven指令](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings)
  
### How to use this tool?

- **Package**

You can use IDE or the command line to package it. Here I take the IDEA for example.

<img width="353" alt="image" src="https://github.com/alexli-77/javaagent-datacollector/assets/13618018/0b0c8669-86ca-4eca-a0e8-9bcf8b5b9df0">

Execute this command and then you can get a jar called "excution-1.0-SNAPSHOT-jar-with-dependencies.jar"

- **Import**

You should import this jar to your target project. Also, The following need to be added to pom.xml

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <argLine>-javaagent:/Path/to/excution-1.0-SNAPSHOT-jar-with-dependencies.jar=org.AgentApplication</argLine>
    </configuration>
</plugin>
```
Notice: <argLine><argLine> is where you can add Java agent parameters.

- **Execution**

In the previous parts, we use the maven-surefire-plugin, now we can execute the command "surefire:test"

<img width="540" alt="image" src="https://github.com/alexli-77/javaagent-datacollector/assets/13618018/847bfc65-8d2d-4827-aab8-522b07628791">

- **TODO**

1. The function for checking if some of these methods belong to the Java API is still too simple and should be enhanced.
2. The amount of outputs of the Java agent is huge. It should be filtered. This case still exists.
3. Considering batch execution, this tool is still too troublesome to use, and it is better to find a way to join automated execution.

- **result**

Here is a simple execution results:

test program:

```
    @Test
    public void search() throws Exception {
        System.out.println("Hello world!");
        String[] args = new String[5];
        args[0] = "java";
        args[1] = "100";

        System.out.println(Math.abs(20));
        HashMap<String, Integer> map = new HashMap<>();
        map.put("John", 30);
        map.put("Mary", 25);
        map.put("Bob", 20);

        map.forEach((key, value) -> {
            System.out.println(key + " is " + value + " years old");
        });
        System.out.println(args[0]);
    }
```

result:

log format : className/Latency/current hashmap size

```
[INFO] Running testcoplit
java.util.Map,java.lang.Class,boolean took 1 milliseconds size: 46
java.util.Map,java.util.Map took 19 milliseconds size: 47
java.lang.Class,boolean,java.util.List took 1 milliseconds size: 48
java.lang.String,long,long took 0 milliseconds size: 49
java.lang.Class,java.lang.String,java.lang.annotation.Annotation[] took 12 milliseconds size: 50
java.lang.Object,java.lang.Class,java.lang.Class took 0 milliseconds size: 51
init ok
java.lang.Object,java.lang.Object[] took 2 milliseconds size: 52
20
Bob is 20 years old
John is 30 years old
Mary is 25 years old
java.lang.Class,org.apache.maven.surefire.common.junit4.Notifier,org.junit.runner.manipulation.Filter took 470 milliseconds size: 53
java.lang.Class,org.apache.maven.surefire.common.junit4.Notifier took 471 milliseconds size: 54
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.477 s - in testcoplit
```


