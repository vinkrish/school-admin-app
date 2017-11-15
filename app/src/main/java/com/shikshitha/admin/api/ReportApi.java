package com.shikshitha.admin.api;

import com.shikshitha.admin.reportcard.Activity;
import com.shikshitha.admin.reportcard.ActivityScore;
import com.shikshitha.admin.reportcard.Exam;
import com.shikshitha.admin.reportcard.ExamSubject;
import com.shikshitha.admin.reportcard.Mark;
import com.shikshitha.admin.reportcard.SubActivity;
import com.shikshitha.admin.reportcard.SubActivityScore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Vinay on 15-11-2017.
 */

public interface ReportApi {

    @GET("exam/class/{classId}")
    Call<ArrayList<Exam>> getExams(@Path("classId") long classId);

    @GET("examsubject/exam/{examId}")
    Call<ArrayList<ExamSubject>> getExamSubjects(@Path("examId") long examId);

    @GET("mark/exam/{examId}/subject/{subjectId}/section/{sectionId}")
    Call<ArrayList<Mark>> getMarks(@Path("examId") long examId,
                                   @Path("subjectId") long subjectId,
                                   @Path("sectionId") long sectionId);

    @GET("activity/section/{sectionId}/exam/{examId}/subject/{subjectId}")
    Call<ArrayList<Activity>> getActivities(@Path("sectionId") long sectionId,
                                            @Path("examId") long examId,
                                            @Path("subjectId") long subjectId);

    @GET("activityscore/activity/{activityId}")
    Call<ArrayList<ActivityScore>> getActivityScores(@Path("activityId") long activityId);

    @GET("subactivity/activity/{activityId}")
    Call<ArrayList<SubActivity>> getSubActivities(@Path("activityId") long activityId);

    @GET("subactivityscore/subactivity/{subActivityId}")
    Call<ArrayList<SubActivityScore>> getSubActivityScores(@Path("subActivityId") long subActivityId);

}
