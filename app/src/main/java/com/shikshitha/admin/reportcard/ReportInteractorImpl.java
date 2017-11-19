package com.shikshitha.admin.reportcard;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.ReportApi;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 16-11-2017.
 */

public class ReportInteractorImpl implements ReportInteractor {
    @Override
    public void getClassList(long teacherId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<Clas>> queue = api.getClassList(teacherId);
        queue.enqueue(new Callback<List<Clas>>() {
            @Override
            public void onResponse(Call<List<Clas>> call, Response<List<Clas>> response) {
                if(response.isSuccessful()) {
                    listener.onClassReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<List<Clas>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getSectionList(long classId, long teacherId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<Section>> queue = api.getSectionList(classId);
        queue.enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {
                if(response.isSuccessful()) {
                    listener.onSectionReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getExams(long classId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<Exam>> queue = api.getExams(classId);
        queue.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    listener.onExamReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getExamSubjects(long examId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<ExamSubject>> queue = api.getExamSubjects(examId);
        queue.enqueue(new Callback<ArrayList<ExamSubject>>() {
            @Override
            public void onResponse(Call<ArrayList<ExamSubject>> call, Response<ArrayList<ExamSubject>> response) {
                if(response.isSuccessful()) {
                    listener.onExamSubjectReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ExamSubject>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getMarks(long examId, long subjectId, long sectionId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<Mark>> queue = api.getMarks(examId, subjectId, sectionId);
        queue.enqueue(new Callback<ArrayList<Mark>>() {
            @Override
            public void onResponse(Call<ArrayList<Mark>> call, Response<ArrayList<Mark>> response) {
                if(response.isSuccessful()) {
                    listener.onScoreReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Mark>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getActivityList(long sectionId, long examId, long subjectId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<Activity>> queue = api.getActivities(sectionId, examId, subjectId);
        queue.enqueue(new Callback<ArrayList<Activity>>() {
            @Override
            public void onResponse(Call<ArrayList<Activity>> call, Response<ArrayList<Activity>> response) {
                if(response.isSuccessful()) {
                    listener.onActivityReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Activity>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getActivityScore(long activityId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<ActivityScore>> queue = api.getActivityScores(activityId);
        queue.enqueue(new Callback<ArrayList<ActivityScore>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityScore>> call, Response<ArrayList<ActivityScore>> response) {
                if(response.isSuccessful()) {
                    listener.onActivityScoreReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ActivityScore>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
