package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.model.DeletedMessage;
import com.shikshitha.admin.model.Message;

import java.util.List;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessageView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void onMessageSaved(Message message);

    void onMessageDeleted(DeletedMessage deletedMessage);

    void showRecentMessages(List<Message> messages);

    void showMessages(List<Message> messages);


}
