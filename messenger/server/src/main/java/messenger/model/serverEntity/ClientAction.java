package messenger.model.serverEntity;

/**
 * Enum ClientAction have all possibility actions of clients
 */
public enum ClientAction {
    REGISTER,
    AUTH,
    SEND_MSG,
    MUTE,
    UNMUTE,
    BAN,
    UNBAN,
    CREATE_ROOM,
    ADD_TO_ROOM,
    CHANGE_ROOM,
    LEAVE_ROOM
}
