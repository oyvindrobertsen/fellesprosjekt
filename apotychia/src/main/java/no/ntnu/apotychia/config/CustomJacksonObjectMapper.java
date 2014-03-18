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
        this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        this.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        this.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        this.setDateFormat(df);
    }
}