package com.shikshitha.admin.api;

import com.shikshitha.admin.attendance.AttendanceSet;
import com.shikshitha.admin.model.AppVersion;
import com.shikshitha.admin.model.Attendance;
import com.shikshitha.admin.model.Authorization;
import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Credentials;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.DeletedMessage;
import com.shikshitha.admin.model.Evnt;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.Groups;
import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.model.MessageRecipient;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Service;
import com.shikshitha.admin.model.Student;
import com.shikshitha.admin.model.TeacherCredentials;
import com.shikshitha.admin.model.Timetable;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Vinay on 29-03-2017.
 */

public interface AdminApi {
    @GET("appversion/{versionId}/{appName}")
    Call<AppVersion> getAppVersion(@Path("versionId") int versionId,
                                   @Path("appName") String appName);

    @Headers("content-type: application/json")
    @POST("teacher/admin/login")
    Call<TeacherCredentials> login(@Body Credentials credentials);

    @Headers("content-type: application/json")
    @POST("sms/teacher/{username}")
    Call<Void> forgotPassword(@Path("username") String username);

    @Headers("content-type: application/json")
    @POST("authorization/fcm")
    Call<Void> updateFcmToken(@Body Authorization authorization);

    @GET("class/school/{schoolId}")
    Call<List<Clas>> getClassList(@Path("schoolId") long schoolId);

    @GET("section/class/{classId}")
    Call<List<Section>> getSectionList(@Path("classId") long classId);

    @GET("student/section/{sectionId}")
    Call<List<Student>> getStudents(@Path("sectionId") long sectionId);

    //Groups and MessageGroup API

    @POST("groups")
    Call<Groups> saveGroup(@Body Groups groups);

    @GET("groups/principal/{schoolId}/group/{id}")
    Call<List<Groups>> getGroupsAboveId(@Path("schoolId") long schoolId,
                                        @Path("id") long id);

    @GET("groups/principal/{schoolId}")
    Call<List<Groups>> getGroups(@Path("schoolId") long schoolId);

    @POST("deletedgroup")
    Call<DeletedGroup> deleteGroup(@Body DeletedGroup deletedGroup);

    @GET("deletedgroup/{id}/school/{schoolId}")
    Call<List<DeletedGroup>> getDeletedGroupsAboveId(@Path("schoolId") long schoolId,
                                                          @Path("id") long id);

    @GET("deletedgroup/school/{schoolId}")
    Call<List<DeletedGroup>> getDeletedGroups(@Path("schoolId") long schoolId);

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

    @POST("deletedmessage")
    Call<DeletedMessage> deleteMessage(@Body DeletedMessage deletedMessage);

    @GET("deletedmessage/{id}/group/{groupId}")
    Call<ArrayList<DeletedMessage>> getDeletedMessagesAboveId(@Path("groupId") long groupId,
                                                     @Path("id") long id);

    @GET("deletedmessage/group/{groupId}")
    Call<ArrayList<DeletedMessage>> getDeletedMessages(@Path("groupId") long groupId);

    @GET("messagerecipient/{groupId}/{groupMessageId}")
    Call<ArrayList<MessageRecipient>> getMessageRecipients(@Path("groupId") long groupId,
                                                           @Path("groupMessageId") long groupMessageId);

    @GET("messagerecipient/school/{groupId}/{groupMessageId}")
    Call<ArrayList<MessageRecipient>> getSchoolRecipients(@Path("groupId") long groupId,
                                                          @Path("groupMessageId") long groupMessageId);

    @GET("messagerecipient/school/{groupId}/{groupMessageId}/{id}")
    Call<ArrayList<MessageRecipient>> getSchoolRecipientsFromId(@Path("groupId") long groupId,
                                                                @Path("groupMessageId") long groupMessageId,
                                                                @Path("id") long id);

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

    @GET("chat/teacher/{id}")
    Call<List<Chat>> getChats(@Path("id") long id);

    @DELETE("chat/{chatId}")
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

    @POST("app/attendance")
    Call<Void> saveAttendance(@Body List<Attendance> attendances);

    @POST("app/attendance/delete")
    Call<Void> deleteAttendance(@Body List<Attendance> attendanceList);

    //Homework API

    @GET("homework/section/{sectionId}/date/{homeworkDate}")
    Call<List<Homework>> getHomework(@Path("sectionId") long sectionId,
                                     @Path("homeworkDate") String homeworkDate);

    @POST("homework")
    Call<Homework> saveHomework(@Body Homework homework);

    @PUT("homework")
    Call<Void> updateHomework(@Body Homework homework);

    @POST("homework/delete")
    Call<Void> deleteHomework(@Body List<Homework> homeworks);

    //Timetable API

    @GET("app/timetable/section/{sectionId}")
    Call<List<Timetable>> getTimetable(@Path("sectionId") long sectionId);

    //Event API

    @GET("event/school/{schoolId}")
    Call<List<Evnt>> getEvents(@Path("schoolId") long schoolId);

    //Service API

    @GET("service/school/{id}")
    Call<Service> getService(@Path("id") long id);

}
