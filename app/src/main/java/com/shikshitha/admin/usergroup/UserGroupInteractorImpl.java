package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 01-04-2017.
 */

class UserGroupInteractorImpl implements UserGroupInteractor {

    @Override
    public void getUserGroup(long groupId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<GroupUsers> queue = api.getUserGroup(groupId);
        queue.enqueue(new Callback<GroupUsers>() {
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

        Call<Void> queue = api.saveUserGroupList(userGroups);
        queue.enqueue(new Callback<Void>() {
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

        Call<Void> queue = api.deleteUserGroupUsers(userGroups);
        queue.enqueue(new Callback<Void>() {
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

    @Override
    public void deleteGroup(DeletedGroup deletedGroup, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<DeletedGroup> queue = api.deleteGroup(deletedGroup);
        queue.enqueue(new Callback<DeletedGroup>() {
            @Override
            public void onResponse(Call<DeletedGroup> call, Response<DeletedGroup> response) {
                if(response.isSuccessful()) {
                    listener.onGroupDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<DeletedGroup> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedGroups(long schoolId, long id, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<DeletedGroup>> queue = api.getDeletedGroupsAboveId(schoolId, id);
        queue.enqueue(new Callback<List<DeletedGroup>>() {
            @Override
            public void onResponse(Call<List<DeletedGroup>> call, Response<List<DeletedGroup>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedGroupsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedGroup>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedGroups(long schoolId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<DeletedGroup>> queue = api.getDeletedGroups(schoolId);
        queue.enqueue(new Callback<List<DeletedGroup>>() {
            @Override
            public void onResponse(Call<List<DeletedGroup>> call, Response<List<DeletedGroup>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedGroupsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedGroup>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

}
