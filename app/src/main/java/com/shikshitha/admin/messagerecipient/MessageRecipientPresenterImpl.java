package com.shikshitha.admin.messagerecipient;

import com.shikshitha.admin.model.MessageRecipient;

import java.util.List;

/**
 * Created by Vinay on 25-08-2017.
 */

class MessageRecipientPresenterImpl implements MessageRecipientPresenter,
        MessageRecipientInteractor.OnFinishedListener {
    private MessageRecipientView mView;
    private MessageRecipientInteractor mInteractor;

    MessageRecipientPresenterImpl(MessageRecipientView view, MessageRecipientInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getMessageRecipient(long groupId, long groupMessageId) {
        mView.showProgress();
        mInteractor.getMessageRecipient(groupId, groupMessageId, this);
    }

    @Override
    public void getSchoolRecipient(long groupId, long groupMessageId) {
        mView.showProgress();
        mInteractor.getSchoolRecipient(groupId, groupMessageId, this);
    }

    @Override
    public void getSchoolRecipientFromId(long groupId, long groupMessageId, long id) {
        mView.showProgress();
        mInteractor.getSchoolRecipientFromId(groupId, groupMessageId, id, this);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onMessageRecipientReceived(List<MessageRecipient> messageRecipient) {
        if (mView != null) {
            mView.showMessageRecipient(messageRecipient);
            mView.hideProgress();
        }
    }

    @Override
    public void onSchoolRecipientReceived(List<MessageRecipient> messageRecipient) {
        if (mView != null) {
            mView.showSchoolRecipient(messageRecipient);
            mView.hideProgress();
        }
    }

    @Override
    public void onFollowUpRecipientReceived(List<MessageRecipient> messageRecipient) {
        if (mView != null) {
            mView.showFollowUpRecipient(messageRecipient);
            mView.hideProgress();
        }
    }
}
