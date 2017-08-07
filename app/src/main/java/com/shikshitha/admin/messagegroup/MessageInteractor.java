package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.model.Message;

import java.util.List;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessageInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onMessageSaved(Message message);

        void onRecentMessagesReceived(List<Message> messages);

        void onMessageReceived(List<Message> messages);

        void onFollowupMessagesReceived(List<Message> messages);
    }

    void saveMessage(Message message, MessageInteractor.OnFinishedListener listener);

    void getRecentMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);

    void getMessages(long groupId, MessageInteractor.OnFinishedListener listener);

    void getFollowupMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);
}
