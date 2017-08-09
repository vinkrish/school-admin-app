package com.shikshitha.admin.chat;

import com.shikshitha.admin.model.Message;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

interface ChatInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onMessageSaved(Message message);

        void onRecentMessagesReceived(List<Message> messages);

        void onMessageReceived(List<Message> messages);

        void onFollowupMessagesReceived(List<Message> messages);
    }

    void saveMessage(Message message, ChatInteractor.OnFinishedListener listener);

    void getRecentMessages(String senderRole, long senderId, String recipientRole, long recipientId,
                           long messageId, ChatInteractor.OnFinishedListener listener);

    void getMessages(String senderRole, long senderId, String recipientRole, long recipientId,
                     ChatInteractor.OnFinishedListener listener);

    void getFollowupMessages(String senderRole, long senderId, String recipientRole, long recipientId,
                             long messageId, ChatInteractor.OnFinishedListener listener);
}
