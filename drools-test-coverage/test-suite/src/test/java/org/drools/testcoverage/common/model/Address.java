package org.drools.testcoverage.common.model;

import java.io.Serializable;

public class Address implements Serializable {

    private static final long serialVersionUID = -4500788294548913289L;

    private String street;
    private int number;
    private String city;
    private String suburb;
    private String zipCode;

    public Address(final String street) {
        this.street = street;
    }

    public Address() {
        this("", 0, "");
    }

    public Address(final String street, final int number, final String city) {
        super();
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public Address(final String street, final String suburb, final String zipCode) {
        this.street = street;
        this.suburb = suburb;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(final String suburb) {
        this.suburb = suburb;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + number;
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        return result;
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (number != other.number) {
            return false;
        }
        if (street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!street.equals(other.street)) {
            return false;
        }
        return true;
    }

}
