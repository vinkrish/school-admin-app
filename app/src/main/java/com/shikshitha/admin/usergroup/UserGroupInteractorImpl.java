package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 01-04-2017.
 */

public class UserGroupInteractorImpl implements UserGroupInteractor {

    @Override
    public void getUserGroup(long groupId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<GroupUsers> classList = api.getUserGroup(groupId);
        classList.enqueue(new Callback<GroupUsers>() {
            @Override
            public void onResponse(Call<GroupUsers> call, Response<GroupUsers> response) {
                if(response.isSuccessful()) {
                    listener.onUserGroupReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<GroupUsers> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void saveUserGroup(ArrayList<UserGroup> userGroups, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<Void> classList = api.saveUserGroupList(userGroups);
        classList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onUserGroupSaved();
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void deleteUsers(ArrayList<UserGroup> userGroups, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<Void> deleteUserGroupUsers = api.deleteUserGroupUsers(userGroups);
        deleteUserGroupUsers.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onUsersDeleted();
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

}
