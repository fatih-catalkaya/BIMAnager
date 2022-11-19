package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Room.Builder.class)
@JsonPropertyOrder({"room_id", "room_name"})
public class Room {
    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("room_name")
    private String roomName;

    public Room(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    @JsonPOJOBuilder
    public static class Builder {
        @JsonProperty("room_id")
        private String roomId;

        @JsonProperty("room_name")
        private String roomName;

        public Builder() {
        }

        public Builder withRoomId(@JsonProperty("room_id") String val) {
            roomId = val;
            return this;
        }

        public Builder withRoomName(@JsonProperty("room_name") String val) {
            roomName = val;
            return this;
        }

        public Room build() {
            return new Room(roomId, roomName);
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
