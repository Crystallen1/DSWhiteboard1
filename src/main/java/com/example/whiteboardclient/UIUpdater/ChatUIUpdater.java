package com.example.whiteboardclient.UIUpdater;

public interface ChatUIUpdater {
    /**
     * Updates the chat box with a new message from a user.
     *
     * @param username The username of the user who sent the message.
     * @param message The message to be displayed in the chat box.
     */
    void updateChatBox(String username,String message);
}
