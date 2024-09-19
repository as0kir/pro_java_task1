package ru.askir.pro_task1.test;

import ru.askir.pro_task1.annotation.*;

public class TestClass {
    @Test(priority = 10)
    public void test1(){
        System.out.println("test1");
    }

    @Test(priority = 2)
    public void test2(){
        System.out.println("test2");
    }

    @Test()
    @CsvSource("10,Java,20,true")
    public void test3(Integer a, String b, Integer c, Boolean d){
        System.out.println(String.format("test3 %s %s %s %s", a, b, c, d));
    }

    @BeforeSuite
    public static void before(){
        System.out.println("before suite");
    }

    @AfterSuite
    public static void after(){
        System.out.println("after suite");
    }

    @BeforeTest
    public static void beforeTest(){
        System.out.println("before test");
    }

    @AfterTest
    public static void afterTest(){
        System.out.println("after test");
    }
}
