package com.shikshitha.admin.messagegroup;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.DeletedMessage;
import com.shikshitha.admin.model.Message;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessageInteractorImpl implements MessageInteractor {
    @Override
    public void saveMessage(Message message, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<Message> queue = api.saveMessage(message);
        queue.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()) {
                    listener.onMessageSaved(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentMessages(long groupId, long messageId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<ArrayList<Message>> queue = api.getGroupMessagesAboveId(groupId, messageId);
        queue.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful()) {
                    listener.onRecentMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getMessages(long groupId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<ArrayList<Message>> queue = api.getGroupMessages(groupId);
        queue.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if(response.isSuccessful()) {
                    listener.onMessageReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void deleteMessage(DeletedMessage deletedMessage, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<DeletedMessage> queue = api.deleteMessage(deletedMessage);
        queue.enqueue(new Callback<DeletedMessage>() {
            @Override
            public void onResponse(Call<DeletedMessage> call, Response<DeletedMessage> response) {
                if(response.isSuccessful()) {
                    listener.onMessageDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<DeletedMessage> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedMessages(long groupId, long id, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<ArrayList<DeletedMessage>> queue = api.getDeletedMessagesAboveId(groupId, id);
        queue.enqueue(new Callback<ArrayList<DeletedMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<DeletedMessage>> call, Response<ArrayList<DeletedMessage>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeletedMessage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedMessages(long groupId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<ArrayList<DeletedMessage>> queue = api.getDeletedMessages(groupId);
        queue.enqueue(new Callback<ArrayList<DeletedMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<DeletedMessage>> call, Response<ArrayList<DeletedMessage>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedMessagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeletedMessage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

}
