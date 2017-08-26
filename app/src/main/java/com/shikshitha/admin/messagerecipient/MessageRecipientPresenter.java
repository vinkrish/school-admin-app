package com.shikshitha.admin.messagerecipient;

/**
 * Created by Vinay on 25-08-2017.
 */

interface MessageRecipientPresenter {
    void getMessageRecipient(long groupId, long groupMessageId);

    void onDestroy();
}
