package no.ntnu.apotychia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CustomJacksonObjectMapper extends ObjectMapper {

    public CustomJacksonObjectMapper() {
        super();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        this.setDateFormat(df);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}