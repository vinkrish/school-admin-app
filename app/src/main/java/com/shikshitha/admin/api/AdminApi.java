package com.shikshitha.admin.api;

import com.shikshitha.admin.attendance.AttendanceSet;
import com.shikshitha.admin.model.Authorization;
import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Credentials;
import com.shikshitha.admin.model.Evnt;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.Groups;
import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Student;
import com.shikshitha.admin.model.TeacherCredentials;
import com.shikshitha.admin.model.Timetable;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Vinay on 29-03-2017.
 */

public interface AdminApi {

    @Headers("content-type: application/json")
    @POST("teacher/login")
    Call<TeacherCredentials> login(@Body Credentials credentials);

    @Headers("content-type: application/json")
    @POST("sms/teacher/{username}")
    Call<Void> forgotPassword(@Path("username") String username);

    @Headers("content-type: application/json")
    @POST("authorization/fcm")
    Call<Void> updateFcmToken(@Body Authorization authorization);

    @GET("class/school/{schoolId}")
    Call<List<Clas>> getClassList(@Path("schoolId") long schoolId);

    @GET("class/teacher/{teacherId}")
    Call<List<Clas>> getSectionTeacherClasses(@Path("teacherId") long teacherId);

    @GET("section/class/{classId}")
    Call<List<Section>> getSectionList(@Path("classId") long classId);

    @GET("section/class/{classId}/teacher/{teacherId}")
    Call<List<Section>> getSectionTeacherSections(@Path("classId") long classId,
                                                  @Path("teacherId") long teacherId);

    @GET("student/section/{sectionId}")
    Call<List<Student>> getStudents(@Path("sectionId") long sectionId);

    //Groups and MessageGroup API

    @POST("groups")
    Call<Groups> saveGroup(@Body Groups groups);

    @POST("groups/principal/school")
    Call<Groups> saveSchoolGroup(@Body Groups groups);

    @GET("groups/principal/{schoolId}")
    Call<List<Groups>> getGroups(@Path("schoolId") long schoolId);

    @GET("groups/{groupId}")
    Call<Void> deleteGroup(@Path("groupId") long groupId);

    @POST("message")
    Call<Message> saveMessage(@Body Message message);

    @GET("message/group/{groupId}/messagesUp/{messageId}")
    Call<ArrayList<Message>> getGroupMessagesAboveId(@Path("groupId") long groupId,
                                                     @Path("messageId") long messageId);

    @GET("message/group/{groupId}")
    Call<ArrayList<Message>> getGroupMessages(@Path("groupId") long groupId);

    @GET("message/group/{groupId}/message/{messageId}")
    Call<ArrayList<Message>> getGroupMessagesFromId(@Path("groupId") long groupId,
                                                    @Path("messageId") long messageId);

    //UserGroup API

    @GET("usergroup/groupusers/groups/{groupId}")
    Call<GroupUsers> getUserGroup(@Path("groupId") long groupId);

    @POST("usergroup")
    Call<Void> saveUserGroupList(@Body ArrayList<UserGroup> userGroupList);

    @POST("usergroup/delete")
    Call<Void> deleteUserGroupUsers(@Body ArrayList<UserGroup> userGroups);

    //Chat API
    @POST("chat")
    Call<Chat> saveChat(@Body Chat chat);

    @GET("chat/principal/{id}")
    Call<List<Chat>> getChats(@Path("id") long id);

    @GET("chat/{chatId}")
    Call<Void> deleteChat(@Path("chatId") long chatId);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}/messagesUp/{messageId}")
    Call<ArrayList<Message>> getChatMessagesAboveId(@Path("senderRole") String senderRole,
                                                    @Path("senderId") long senderId,
                                                    @Path("recipientRole") String recipientRole,
                                                    @Path("recipientId") long recipientId,
                                                    @Path("messageId") long messageId);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}")
    Call<ArrayList<Message>> getChatMessages(@Path("senderRole") String senderRole,
                                             @Path("senderId") long senderId,
                                             @Path("recipientRole") String recipientRole,
                                             @Path("recipientId") long recipientId);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}/message/{messageId}")
    Call<ArrayList<Message>> getChatMessagesFromId(@Path("senderRole") String senderRole,
                                                   @Path("senderId") long senderId,
                                                   @Path("recipientRole") String recipientRole,
                                                   @Path("recipientId") long recipientId,
                                                   @Path("messageId") long messageId);

    //Attendance API

    @GET("timetable/section/{sectionId}/day/{dayOfWeek}")
    Call<List<Timetable>> getTimetable(@Path("sectionId") long sectionId,
                                       @Path("dayOfWeek") String dayOfWeek);

    @GET("app/attendance/section/{sectionId}/date/{dateAttendance}/session/{session}")
    Call<AttendanceSet> getAttendanceSet(@Path("sectionId") long sectionId,
                                         @Path("dateAttendance") String dateAttendance,
                                         @Path("session") int session);

    //Homework API

    @GET("homework/section/{sectionId}/date/{homeworkDate}")
    Call<List<Homework>> getHomework(@Path("sectionId") long sectionId,
                                     @Path("homeworkDate") String homeworkDate);

    //Timetable API

    @GET("app/timetable/section/{sectionId}")
    Call<List<Timetable>> getTimetable(@Path("sectionId") long sectionId);

    //Event API

    @GET("event/school/{schoolId}")
    Call<List<Evnt>> getEvents(@Path("schoolId") long schoolId);

}
