package org;
import javassist.*;
import java.lang.instrument.Instrumentation;

public class AgentApplication {
    public static void premain(String arg, Instrumentation instrumentation) {
        instrumentation.addTransformer((loader,className,classBeingRedefined,protectionDomain,classfileBuffer)->{
            String javaClassName = className.replace("/", ".");
            CtClass ctClass= null;
            CtMethod[] declaredMethods = null;
            ClassPool classPool = ClassPool.getDefault();
            // get bytecode
            try {
                ctClass = classPool.getCtClass(javaClassName);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
            declaredMethods = ctClass.getDeclaredMethods();
            //check if java api is used
            ApiCheck.check(declaredMethods);
            return classfileBuffer;
        });
    }
//    public static void premain(String arg, Instrumentation instrumentation) {
//        instrumentation.addTransformer((loader,className,classBeingRedefined,protectionDomain,classfileBuffer)->{
//            if (className == null) {
//                // null
//                return null;
//            }
//            String javaClassName = className.replace("/", ".");
//
//            CtClass ctClass= null;
//            CtMethod[] declaredMethods = null;
//            ClassPool classPool = ClassPool.getDefault();
//            // get bytecode
//            try {
//                ctClass = classPool.getCtClass(javaClassName);
//            } catch (NotFoundException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                declaredMethods = ctClass.getDeclaredMethods();
//                for (CtMethod declaredMethod : declaredMethods) {
//                    declaredMethod.insertBefore("System.out.println(\"before invoke : "+ declaredMethod.getLongName() + "\");");
//                }
//                return ctClass.toBytecode();
//            } catch (Exception e) {
//                System.out.println("error: " + e.getMessage() + " " + className);
//            }
//            //check if java api is used
//            ApiCheck.check(declaredMethods);
//            return classfileBuffer;
//        });
//    }
}
