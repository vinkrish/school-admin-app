package com.shikshitha.admin.messagerecipient;

import com.shikshitha.admin.model.MessageRecipient;

import java.util.List;

/**
 * Created by Vinay on 25-08-2017.
 */

interface MessageRecipientView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showMessageRecipient(List<MessageRecipient> messageRecipient);

    void showSchoolRecipient(List<MessageRecipient> messageRecipient);

    void showFollowUpRecipient(List<MessageRecipient> messageRecipient);
}
