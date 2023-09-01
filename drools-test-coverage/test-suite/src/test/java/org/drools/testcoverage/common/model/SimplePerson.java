package org.drools.testcoverage.common.model;

import java.io.Serializable;

public class SimplePerson implements Serializable {

    private static final long serialVersionUID = -8151397010598370317L;

    private int id = 0;
    private String name = "";
    private int age;

    public SimplePerson() {
    }

    public SimplePerson(final String name) {
        this();
        this.name = name;
    }

    public SimplePerson(final String name, final int age) {
        this(name);
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePerson that = (SimplePerson) o;

        if (id != that.id) return false;
        if (age != that.age) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "SimplePerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
