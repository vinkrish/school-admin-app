package com.shikshitha.admin.chat;

import com.shikshitha.admin.model.Message;

/**
 * Created by Vinay on 28-04-2017.
 */

interface ChatPresenter {
    void saveMessage(Message message);

    void getRecentMessages(String senderRole, long senderId, String recipientRole, long recipeintId, long messageId);

    void getMessages(String senderRole, long senderId, String recipientRole, long recipeintId);

    void getFollowupMessages(String senderRole, long senderId, String recipientRole, long recipientId, long messageId);

    void onDestroy();
}
