package ru.askir.pro_task1;

import ru.askir.pro_task1.annotation.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestRunner {

    private static boolean testStaticAnnotation(Method declaredMethod, Class annotationClass, Method staticMethod) {
        if(declaredMethod.getAnnotation(annotationClass) != null) {
            if(!Modifier.isStatic(declaredMethod.getModifiers()))
                throw new RuntimeException(String.format("Method with annotation %s must be static", annotationClass.getSimpleName()));

            if(staticMethod != null)
                throw new RuntimeException(String.format("Method with annotation %s must be only one", annotationClass.getSimpleName()));

            return true;
        }
        return false;
    }

    private static void invokeStaticMethod(Method method){
        if(method != null) {
            method.setAccessible(true);
            try {
                method.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void runTests(Class clazz){
        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;

        Method beforeTestMethod = null;
        Method afterTestMethod = null;

        SortedMap<Integer, Method> tests = new TreeMap<>();

        // проверки
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(testStaticAnnotation(declaredMethod, BeforeSuite.class, beforeSuiteMethod))
                beforeSuiteMethod = declaredMethod;

            if(testStaticAnnotation(declaredMethod, AfterSuite.class, afterSuiteMethod))
                afterSuiteMethod = declaredMethod;

            if(testStaticAnnotation(declaredMethod, BeforeTest.class, beforeTestMethod))
                beforeTestMethod = declaredMethod;

            if(testStaticAnnotation(declaredMethod, AfterTest.class, afterTestMethod))
                afterTestMethod = declaredMethod;

            Test annotation = declaredMethod.getAnnotation(Test.class);
            if(annotation != null) {
                int priority = annotation.priority();
                if(priority < 1 || priority > 10)
                    throw new RuntimeException("In annotation @Test parameter priority must be value between 1 and 10");

                tests.put(-priority, declaredMethod);
            }
        }

        // обработка
        invokeStaticMethod(beforeSuiteMethod);

        try {
            Object testObject = clazz.newInstance();

            for (Method method : tests.values()) {
                invokeStaticMethod(beforeTestMethod);

                CsvSource csvSourceAnnotation = method.getAnnotation(CsvSource.class);
                if(csvSourceAnnotation != null) {
                    String csv = csvSourceAnnotation.value();
                    String[] agrs = csv.split(",");
                    List<Object> arrayList = new ArrayList<>();
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    for (int i = 0; i < agrs.length; i++) {
                        arrayList.add(parameterTypes[i].getConstructor(String.class).newInstance(agrs[i]));
                    }
                    method.invoke(testObject, arrayList.toArray());
                }
                else {
                    method.invoke(testObject);
                }

                invokeStaticMethod(afterTestMethod);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        invokeStaticMethod(afterSuiteMethod);
    }
}
