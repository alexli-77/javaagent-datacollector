package org;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * @author Alex
 * @version 1.0
 */
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst){
        System.out.println("MyAgent init...");


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
//                System.out.println("onDiscovery");

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
//                System.out.println("onTransformation");

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
//                System.out.println("onError");

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
//                System.out.println("onComplete");

            }

        };

        new AgentBuilder.Default()
                //set filter rules
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(CollectMethods.class)))
                .with(listener)
                .installOn(inst);
        System.out.println("MyAgent over...");
    }
}
