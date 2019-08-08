package messenger.model.serverEntity;

/**
 * Enum ClientAction have all possibility actions of clients
 */
public enum ClientAction {
    REGISTER,
    AUTH,
    LOGOUT,
    SEND_MSG,
    CREATE_ROOM,
    ADD_TO_ROOM,
    SWITCH_ROOM,
    LEAVE_ROOM,
    ONLINE_USERS,
    HISTORY,
    BAN_LIST,
    UNBAN_LIST,
    BAN,
    UNBAN,
    DELETE_ROOM,
    USERS_IN_ROOM
}