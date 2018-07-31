package com.techtest.customer.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HeaderUtil {

    public HttpHeaders createHeaderForCRMEndpopint() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        return httpHeaders;
    }
}
