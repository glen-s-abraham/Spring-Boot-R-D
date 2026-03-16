package com.example.restfull_web_services.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

// @JsonIgnoreProperties can statically ignore multiple fields at the class level.
// Any field listed here is ALWAYS excluded from all serialized responses.
@JsonIgnoreProperties("creditCardNumber")
@Data
@AllArgsConstructor
public class BeanWithStaticFilter {

    private Integer id;
    private String username;
    private String email;

    // @JsonIgnore statically ignores this field on every response.
    // Good for a single field that should NEVER leave the server.
    @JsonIgnore
    private String password;

    // Ignored via @JsonIgnoreProperties at the class level.
    private String creditCardNumber;
}
