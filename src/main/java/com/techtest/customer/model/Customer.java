package com.techtest.customer.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main model class which represents the customer profile.
 */

@ToString
public class Customer {

    @Getter
    @Setter
    private String customerId; // assumption
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String dateOfBirth;
    @Getter
    @Setter
    private List<Address> addressList;

    private Map<String, String> customerProfileMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getInfo() {
        return customerProfileMap;
    }

    @JsonAnySetter
    public void setInfo(String name, String value) {
        customerProfileMap.put(name, value);
    }
}
