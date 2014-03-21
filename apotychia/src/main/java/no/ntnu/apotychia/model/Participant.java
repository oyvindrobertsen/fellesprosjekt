package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by peteraa on 3/11/14.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Group.class, name = ".Group"),
        @JsonSubTypes.Type(value = User.class, name = ".User")
})
public interface Participant {
    /* Returntype Object, since user has string for ID, while group has long */
    public Object getId();
}
