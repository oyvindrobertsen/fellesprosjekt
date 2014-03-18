package no.ntnu.apotychia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CustomJacksonObjectMapper extends ObjectMapper {

    public CustomJacksonObjectMapper() {
        super();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        this.setDateFormat(df);
    }
}