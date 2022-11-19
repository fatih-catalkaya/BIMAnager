package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Person.Builder.class)
@JsonPropertyOrder({"person_id", "person_name", "person_email", "person_password", "person_room_name", "person_is_administrator"})
public class Person {
    @JsonProperty("person_id")
    private String personId;

    @JsonProperty("person_name")
    private String personName;

    @JsonProperty("person_email")
    private String personEmail;

    @JsonProperty("person_password")
    @JsonIgnore
    private String personPassword;

    @JsonProperty("person_room_name")
    private String personRoomName;

    @JsonProperty("person_is_administrator")
    private boolean personIsAdministrator;

    private Person(String personId, String personName, String personEmail,
                   String personPassword, String personRoomName, boolean personIsAdministrator) {
        this.personId = personId;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personPassword = personPassword;
        this.personRoomName = personRoomName;
        this.personIsAdministrator = personIsAdministrator;
    }

    @JsonPOJOBuilder()
    public static class Builder {
        @JsonProperty("person_id")
        private String personId;
        @JsonProperty("person_name")
        private String personName;
        @JsonProperty("person_email")
        private String personEmail;
        @JsonProperty("person_password")
        private String personPassword;
        @JsonProperty("person_room_name")
        private String personRoomName;
        @JsonProperty("person_is_administrator")
        private boolean personIsAdministrator = false;

        public Builder withPersonId(@JsonAlias("person_id") String personId){
            this.personId = personId;
            return this;
        }

        public Builder withPersonName(@JsonProperty("person_name")String personName){
            this.personName = personName;
            return this;
        }

        public Builder withPersonEmail(@JsonProperty("person_email")String personEmail){
            this.personEmail = personEmail;
            return this;
        }

        public Builder withPersonPassword(@JsonProperty("person_password") String personPassword){
            this.personPassword = personPassword;
            return this;
        }

        public Builder withPersonRoomName(@JsonProperty("person_room_name") String personRoomName){
            this.personRoomName = personRoomName;
            return this;
        }

        public Builder withPersonIsAdministrator(@JsonProperty("person_is_administrator") boolean personIsAdministrator){
            this.personIsAdministrator = personIsAdministrator;
            return this;
        }

        public Person build(){
            return new Person(
                    this.personId, this.personName, this.personEmail,
                    this.personPassword, this.personRoomName, this.personIsAdministrator
            );
        }
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonPassword() {
        return personPassword;
    }

    public void setPersonPassword(String personPassword) {
        this.personPassword = personPassword;
    }

    public String getPersonRoomName() {
        return personRoomName;
    }

    public void setPersonRoomName(String personRoomName) {
        this.personRoomName = personRoomName;
    }

    public boolean isPersonIsAdministrator() {
        return personIsAdministrator;
    }

    public void setPersonIsAdministrator(boolean personIsAdministrator) {
        this.personIsAdministrator = personIsAdministrator;
    }
}
