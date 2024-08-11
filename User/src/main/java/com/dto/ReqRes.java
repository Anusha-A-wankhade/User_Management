//package com.dto;
//
//import java.util.List;
//
//import com.entity.OurUsers;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//
//import lombok.Data;
//
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class ReqRes {
//
//	private int statusCode;
//	private String error;
//	private String messege;
//	private String token;
//	private String refreshToken;
//	private String expirationTime;
//	private String name;
//	private String email;
//	private String city;
//	private String role;
//	private String password;
//	private List<OurUsers> ourUserList;
//	
//	
//}
package com.dto;

import java.util.List;

import com.entity.OurUsers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;  // Corrected typo
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String city;
    private String role;
    private String password;
    private List<OurUsers> ourUserList;  // Ensure consistent usage of List<OurUsers>
}

