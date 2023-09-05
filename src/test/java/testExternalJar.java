import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class testExternalJar {

    @Test
    public void getJarClass(){
        List<String> list = new ArrayList<String>();
        try {
            JarFile jarFile = new JarFile("src/main/resources/java_collect-1.0-SNAPSHOT.jar");
            Enumeration enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enu.nextElement();
                String name = jarEntry.getName();
                // 过滤出 class 文件
                if (name.endsWith(".class") && name.indexOf("$") == -1 ) {
                    // 重新格式化文件名
                    name = name.substring(0, name.indexOf(".class"));
                    name = name.replaceAll("/", ".");
                    System.out.println(name);
                    list.add(name);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("list length " + list.size());
    }
    @Test
    public void testExternal() {
        //测试外部jar包的test
        // 加载其他JAR包中的测试类
        String jarFilePath = "src/main/resources/java_collect-1.0-SNAPSHOT-tests.jar"; // 替换为本地JAR文件的路径
        String testClassName = "testcoplit"; // 替换为您要测试的类的全名
        Class<?> externalTestClass = null;
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(jarFilePath).toURI().toURL()});
            externalTestClass = Class.forName(testClassName,true,classLoader); // 替换为您要测试的类的全名
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (externalTestClass != null) {
            // 运行测试类
            Result result = JUnitCore.runClasses(externalTestClass);

            // 处理测试结果
            if (result.wasSuccessful()) {
                System.out.println("测试通过！");
            } else {
                System.out.println("测试失败！");
                for (Failure failure : result.getFailures()) {
                    System.out.println(failure.toString());
                }
            }
        }
    }
}
