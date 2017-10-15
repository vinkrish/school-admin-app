package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.model.DeletedMessage;
import com.shikshitha.admin.model.Message;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessagePresenter {
    void saveMessage(Message message);

    void getRecentMessages(long groupId, long messageId);

    void getMessages(long groupId);

    void deleteMessage(DeletedMessage deletedMessage);

    void getRecentDeletedMessages(long groupId, long id);

    void getDeletedMessages(long groupId);

    void onDestroy();
}
