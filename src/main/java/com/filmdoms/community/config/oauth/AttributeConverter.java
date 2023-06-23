package com.filmdoms.community.config.oauth;

import java.util.Map;

public class AttributeConverter {

    public static String getEmail(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equals("google")) {
            return (String) attributes.get("email");
        } else {
            throw new IllegalArgumentException("허용되지 않은 Provider 입니다.");
        }
    }
}
