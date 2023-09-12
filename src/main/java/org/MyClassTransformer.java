package org;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.common.Logging;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class MyClassTransformer implements ClassFileTransformer {
    public static Logging logging = new Logging();
    public HashMap<String,String> hashMap = new HashMap<>();
    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer
    ) {
        // Implement your filtering logic here
        // You can use libraries like ASM or Byte Buddy to work with bytecode
        // Filter and modify methods as needed
        // Return the modified bytecode or the original bytecode if no changes are needed
        String javaClassName = className.replace("/", ".");
        CtClass ctClass;
        CtMethod[] declaredMethods;
        ClassPool classPool = ClassPool.getDefault();
        // get bytecode
        try {
            ctClass = classPool.getCtClass(javaClassName);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        declaredMethods = ctClass.getDeclaredMethods();
        //check if java api is used
        filterMethods(declaredMethods);
        return classfileBuffer;
    }

    //filter methods
    public void filterMethods(CtMethod[] declaredMethods) {
        if(declaredMethods == null || declaredMethods.length == 0) {
            return;
        }
        for (CtMethod declaredMethod : declaredMethods) {
            if (declaredMethod.getLongName().startsWith("java.") || declaredMethod.getLongName().startsWith("jdk.")) {
                //store contents into a String
                hashMap.put(declaredMethod.getLongName(),"");
                logging.logToFile("java api used: " + declaredMethod.getLongName());
            }
        }
    }
}
