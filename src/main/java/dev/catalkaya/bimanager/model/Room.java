package dev.catalkaya.bimanager.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"room_id", "room_name"})
public class Room {
    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("room_name")
    private String roomName;

    @JsonCreator
    public Room(@JsonProperty("room_name") String roomName) {
        this.roomId = null;
        this.roomName = roomName;
    }

    /*
    public Room(@JsonProperty("room_id") String room_id, @JsonProperty("room_name") String room_name) {
        this.roomId = room_id;
        this.roomName = room_name;
    }
     */

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
