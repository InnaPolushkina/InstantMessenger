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
    SWITCH_ROOM,
    LEAVE_ROOM,
    ONLINE_USERS
}