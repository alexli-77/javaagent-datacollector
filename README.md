# javaagent-datacollector
A Java agent demo for tracing invoked methods.

This tools is based on the java agent and instrumentation.

Java agents work at the lowest level by providing services that enable us to intrude into a running Java program in JVM. 
Java agents are part of the Java Instrumentation API. 
The Instrumentation APIs provide a mechanism to modify bytecodes of methods. 
This can be done both statically and dynamically. 
This means that we can change a program by adding code to it without having to touch upon the actual source code of the program. 
The result can have a significant impact on the overall behavior of the application.

### what is it used for?

In this repo, we use it typically to collect the methods that are invoked in the tests. 

### How to use this tool?

- Package
You can use IDE or the command line to package it. Here I take the IDEA for example.
<img width="353" alt="image" src="https://github.com/alexli-77/javaagent-datacollector/assets/13618018/0b0c8669-86ca-4eca-a0e8-9bcf8b5b9df0">

Execute this command and then you can get a jar called "excution-1.0-SNAPSHOT-jar-with-dependencies.jar"

- Import
You should import this jar to your target project. Also, The following need to be added to pom.xml

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <includes>
            <include>testcoplit.java</include>
        </includes>
        <argLine>-javaagent:/Path/to/excution-1.0-SNAPSHOT-jar-with-dependencies.jar=org.AgentApplication</argLine>
    </configuration>
</plugin>
```
Notice: <argLine><argLine> is where you can add Java agent parameters.

- Execution

In the previous parts, we use the maven-surefire-plugin, now we can execute the command "surefire:test"
<img width="540" alt="image" src="https://github.com/alexli-77/javaagent-datacollector/assets/13618018/847bfc65-8d2d-4827-aab8-522b07628791">

- TODO

1. The function for checking if some of these methods belong to the Java API is still too simple and should be enhanced.
2. The amount of outputs of the Java agent is huge. It should be filtered. This case still exists.
3. Considering batch execution, this tool is still too troublesome to use, and it is better to find a way to join automated execution.

- result

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
```
Hello world!
20
java.util.function.BiConsumer.accept(java.lang.Object,java.lang.Object)
Bob is 20 years old
John is 30 years old
Mary is 25 years old
java
org.junit.runner.notification.RunNotifier$7.notifyListener(org.junit.runner.notification.RunListener)
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.175 s - in testcoplit
org.junit.runner.notification.RunNotifier$2.notifyListener(org.junit.runner.notification.RunListener)
org.apache.maven.surefire.api.suite.RunResult.timeout(org.apache.maven.surefire.api.suite.RunResult)
org.apache.maven.surefire.api.suite.RunResult.failure(org.apache.maven.surefire.api.suite.RunResult,java.lang.Exception)
org.apache.maven.surefire.api.suite.RunResult.errorCode(org.apache.maven.surefire.api.suite.RunResult,java.lang.String,boolean)
org.apache.maven.surefire.api.suite.RunResult.getStackTrace(java.lang.Exception)
org.apache.maven.surefire.api.suite.RunResult.getCompletedCount()
org.apache.maven.surefire.api.suite.RunResult.getErrors()
org.apache.maven.surefire.api.suite.RunResult.getFlakes()
org.apache.maven.surefire.api.suite.RunResult.getFailures()
org.apache.maven.surefire.api.suite.RunResult.getSkipped()
org.apache.maven.surefire.api.suite.RunResult.getFailsafeCode()
org.apache.maven.surefire.api.suite.RunResult.isErrorFree()
org.apache.maven.surefire.api.suite.RunResult.isInternalError()
org.apache.maven.surefire.api.suite.RunResult.isFailureOrTimeout()
org.apache.maven.surefire.api.suite.RunResult.isFailure()
org.apache.maven.surefire.api.suite.RunResult.getFailure()
org.apache.maven.surefire.api.suite.RunResult.isTimeout()
org.apache.maven.surefire.api.suite.RunResult.aggregate(org.apache.maven.surefire.api.suite.RunResult)
org.apache.maven.surefire.api.suite.RunResult.noTestsRun()
org.apache.maven.surefire.api.suite.RunResult.equals(java.lang.Object)
org.apache.maven.surefire.api.suite.RunResult.hashCode()
org.apache.maven.surefire.booter.ForkedBooter$6.update(org.apache.maven.surefire.api.booter.Command)
org.apache.maven.surefire.booter.ForkedBooter$7.run()
java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.getDelay(java.util.concurrent.TimeUnit)
java.util.concurrent.FutureTask.report(int)
java.util.concurrent.Callable.call()
java.util.concurrent.Executors$RunnableAdapter.call()
java.util.concurrent.ThreadPoolExecutor$Worker.run()
java.util.concurrent.locks.LockSupport.setBlocker(java.lang.Thread,java.lang.Object)
sun.nio.ch.Interruptible.interrupt(java.lang.Thread)
org.apache.maven.surefire.booter.ForkedBooter$1.run()
java.util.HashMap$KeyIterator.next()
java.util.IdentityHashMap$KeySet.iterator()
java.util.IdentityHashMap$KeyIterator.next()
java.util.IdentityHashMap$IdentityHashMapIterator.hasNext()
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
```
