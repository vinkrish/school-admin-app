package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.model.DeletedMessage;
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

        void onMessageDeleted(DeletedMessage deletedMessage);

        void onDeletedMessagesReceived(List<DeletedMessage> messages);
    }

    void saveMessage(Message message, MessageInteractor.OnFinishedListener listener);

    void getRecentMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);

    void getMessages(long groupId, MessageInteractor.OnFinishedListener listener);

    void deleteMessage(DeletedMessage deletedMessage, MessageInteractor.OnFinishedListener listener);

    void getRecentDeletedMessages(long groupId, long id, MessageInteractor.OnFinishedListener listener);

    void getDeletedMessages(long groupId, MessageInteractor.OnFinishedListener listener);
}
