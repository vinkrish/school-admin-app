package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.model.Message;

import java.util.List;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessagePresenterImpl implements MessagePresenter, MessageInteractor.OnFinishedListener {
    private MessageView mView;
    private MessageInteractor mInteractor;

    MessagePresenterImpl(MessageView view, MessageInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void saveMessage(Message message) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.saveMessage(message, this);
        }
    }

    @Override
    public void getRecentMessages(long groupId, long messageId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getRecentMessages(groupId, messageId, this);
        }
    }

    @Override
    public void getMessages(long groupId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getMessages(groupId, this);
        }
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
    public void onMessageSaved(Message message) {
        if(mView != null) {
            mView.onMessageSaved(message);
            mView.hideProgress();
        }
    }

    @Override
    public void onRecentMessagesReceived(List<Message> messages) {
        if(mView != null) {
            mView.showRecentMessages(messages);
            mView.hideProgress();
        }
    }

    @Override
    public void onMessageReceived(List<Message> messages) {
        if(mView != null) {
            mView.showMessages(messages);
            mView.hideProgress();
        }
    }
}
