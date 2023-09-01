package org.drools.ancompiler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.phreak.AbstractReactiveObject;
import org.kie.api.definition.type.Position;

public class Person extends AbstractReactiveObject {

    @Position(0)
    private String name;

    @Position(1)
    private int age;


    private long ageLong;

    private int id = 0;
    private String likes;
    private Boolean employed;


    private Integer salary;

    private BigDecimal money;

    private BigInteger ageInSeconds;

    private Map<Integer, Integer> items = new HashMap<>();
    private Map<String, String> itemsString = new HashMap<>();

    public static int countItems(Map<?, ?> items) {
        return items.size();
    }

    public static boolean evaluate(Map<?, ?> items) {
        return items.size() > 0;
    }

    private int numberOfItems;

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    private Person ParentP;

    public Person() { }

    public Person(String name) {
        this.name = name;
    }


    public Person(String name, BigDecimal money) {
        this.name = name;
        this.money = money;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public Integer getAgeBoxed() {
        return age;
    }

    public Short getAgeAsShort() {
        return (short)age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyModification();
    }

    public long getAgeLong() {
        return ageLong;
    }

    public Person setAgeLong(long ageLong) {
        this.ageLong = ageLong;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public Person setLikes(String likes ) {
        this.likes = likes;
        return this;
    }

    public Boolean getEmployed() {
        return employed;
    }

    public Person setEmployed(Boolean employed) {
        this.employed = employed;
        return this;
    }

    /**
     * WARN: this toString() implementation is actually used in some tests.
     */
    @Override
    public String toString() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Person setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary( Integer salary ) {
        this.salary = salary;
    }

    public void setItems( Map<Integer, Integer> items) {
        this.items = items;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public Person getParentP() {
        return ParentP;
    }

    public Person setParentP(Person parentP) {
        ParentP = parentP;
        return this;
    }

    public BigInteger getAgeInSeconds() {
        return ageInSeconds;
    }

    public Person setAgeInSeconds(BigInteger ageInSeconds) {
        this.ageInSeconds = ageInSeconds;
        return this;
    }

    public Map<String, String> getItemsString() {
        return itemsString;
    }

    public void setItemsString(Map<String, String> itemsString) {
        this.itemsString = itemsString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return age == person.age && name.equals(person.name);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }


}
