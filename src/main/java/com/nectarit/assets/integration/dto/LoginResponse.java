package com.nectarit.assets.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
	@JsonProperty("accessToken")
    private String accessToken;

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; 
    }
}
