package com.shikshitha.admin.chat;

import com.shikshitha.admin.model.Message;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

class ChatPresenterImpl implements ChatPresenter, ChatInteractorImpl.OnFinishedListener {
    private ChatView mView;
    private ChatInteractor mInteractor;

    ChatPresenterImpl(ChatView view, ChatInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void saveMessage(Message message) {
        mInteractor.saveMessage(message, this);
    }

    @Override
    public void getRecentMessages(String senderRole, long senderId, String recipientRole, long recipeintId, long messageId) {
        mInteractor.getRecentMessages(senderRole, senderId, recipientRole, recipeintId, messageId, this);
    }

    @Override
    public void getMessages(String senderRole, long senderId, String recipientRole, long recipeintId) {
        mInteractor.getMessages(senderRole, senderId, recipientRole, recipeintId, this);
    }

    @Override
    public void getFollowupMessages(String senderRole, long senderId, String recipientRole, long recipeintId, long messageId) {
        mInteractor.getFollowupMessages(senderRole, senderId, recipientRole, recipeintId, messageId, this);
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
        if (mView != null) {
            mView.onMessageSaved(message);
        }
    }

    @Override
    public void onRecentMessagesReceived(List<Message> messages) {
        if (mView != null) {
            mView.showRecentMessages(messages);
        }
    }

    @Override
    public void onMessageReceived(List<Message> messages) {
        if (mView != null) {
            mView.showMessages(messages);
        }
    }

    @Override
    public void onFollowupMessagesReceived(List<Message> messages) {
        if (mView != null) {
            mView.showFollowupMessages(messages);
        }
    }
}
