package messenger.model.entity;

/**
 * The enum with all actions from server side
 */
public enum ServerAction {
    SEND_MSG,
    ONLINE_LIST,
    ADDED_TO_ROOM,
    BAN_LIST,
    UNBAN_LIST,
    BAN,
    UNBAN,
    DELETED_ROOM,
    USERS_IN_ROOM
}
