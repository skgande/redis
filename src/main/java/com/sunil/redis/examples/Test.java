package com.sunil.redis.examples;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Person p = new Person();
        p.setName("name");
        System.out.println(p.getAddressList().size());
        for(Address a: p.getAddressList()) {
            System.out.println(a.getStreet());
        }
    }
}

class Person {
    private String name;
    private List<Address> addressList;

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Address {
    private String street;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}