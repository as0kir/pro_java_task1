package ru.askir.pro_task2;

public class Employee {
    private String name;
    private String post;
    private int age;

    public Employee(String name, String post, int age) {
        this.name = name;
        this.post = post;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "EmployeeClass{" +
                "name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", age=" + age +
                '}';
    }
}
