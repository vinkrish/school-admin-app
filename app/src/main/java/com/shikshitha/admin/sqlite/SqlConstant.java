package com.shikshitha.admin.sqlite;

/**
 * Created by Vinay.
 */
interface SqlConstant {

    String DATABASE_NAME = "admin.db";
    int DATABASE_VERSION = 3;

    String CREATE_ATTENDANCE = "CREATE TABLE attendance (" +
            "  Id INTEGER PRIMARY KEY," +
            "  SectionId INTEGER," +
            "  StudentId INTEGER," +
            "  StudentName TEXT," +
            "  SubjectId INTEGER," +
            "  Type TEXT," +
            "  Session INTEGER," +
            "  DateAttendance date," +
            "  TypeOfLeave TEXT" +
            ")";

    String CREATE_CLASS = "CREATE TABLE class (" +
            "  Id INTEGER," +
            "  ClassName TEXT," +
            "  SchoolId INTEGER," +
            "  TeacherId INTEGER," +
            "  AttendanceType TEXT" +
            ")";

    String CREATE_CLASS_SUBJECT_GROUP = "CREATE TABLE class_subject_group (" +
            "  Id INTEGER," +
            "  ClassId INTEGER," +
            "  SubjectGroupId INTEGER," +
            "  SubjectGroupName TEXT" +
            ")";

    String CREATE_HOMEWORK = "CREATE TABLE homework (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT," +
            "  HomeworkMessage TEXT," +
            "  HomeworkDate date" +
            ")";

    String CREATE_SCHOOL = "CREATE TABLE school (" +
            "  Id INTEGER," +
            "  SchoolName TEXT," +
            "  Website TEXT," +
            "  ShortenedSchoolName TEXT," +
            "  ContactPersonName TEXT," +
            "  AdminUsername TEXT," +
            "  AdminPassword TEXT," +
            "  Landline TEXT," +
            "  Mobile1 TEXT," +
            "  Mobile2 TEXT," +
            "  Email TEXT," +
            "  Street TEXT," +
            "  City TEXT," +
            "  District TEXT," +
            "  State TEXT," +
            "  Pincode TEXT," +
            "  PrincipalId INTEGER," +
            "  NumberOfStudents INTEGER" +
            ")";

    String CREATE_SECTION = "CREATE TABLE section (" +
            "  Id INTEGER," +
            "  SectionName TEXT," +
            "  ClassId INTEGER," +
            "  TeacherId INTEGER" +
            ")";

    String CREATE_STUDENT = "CREATE TABLE student (" +
            "  Id INTEGER," +
            "  Name TEXT," +
            "  SchoolId INTEGER," +
            "  ClassId INTEGER," +
            "  SectionId INTEGER," +
            "  AdmissionNo TEXT," +
            "  RollNo INTEGER," +
            "  Username TEXT," +
            "  Password TEXT," +
            "  Image TEXT," +
            "  FatherName TEXT," +
            "  MotherName TEXT," +
            "  DateOfBirth date," +
            "  Gender TEXT," +
            "  Email TEXT," +
            "  Mobile1 TEXT," +
            "  Mobile2 TEXT," +
            "  Street TEXT," +
            "  City TEXT," +
            "  District TEXT," +
            "  State TEXT," +
            "  Pincode TEXT" +
            ")";

    String CREATE_SUBJECT = "CREATE TABLE subject (" +
            "  Id INTEGER," +
            "  SchoolId INTEGER," +
            "  SubjectName TEXT," +
            "  PartitionType INTEGER," +
            "  TheorySubjectId INTEGER," +
            "  PracticalSubjectId INTEGER" +
            ")";

    String CREATE_SUBJECT_GROUP = "CREATE TABLE subject_group (" +
            "  Id INTEGER," +
            "  SchoolId INTEGER," +
            "  SubjectGroupName TEXT" +
            ")";

    String CREATE_SUBJECT_GROUP_SUBJECT = "CREATE TABLE subject_group_subject (" +
            "  Id INTEGER," +
            "  SubjectGroupId INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT" +
            ")";

    String CREATE_SUBJECT_STUDENT = "CREATE TABLE subject_student (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  SubjectId INTEGER," +
            "  StudentIds TEXT" +
            ")";

    String CREATE_SUBJECT_TEACHER = "CREATE TABLE subject_teacher (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT," +
            "  TeacherId INTEGER," +
            "  TeacherName TEXT" +
            ")";

    String CREATE_TEACHER = "CREATE TABLE teacher (" +
            "  Id INTEGER," +
            "  Name TEXT," +
            "  Image TEXT," +
            "  Username TEXT," +
            "  Password TEXT," +
            "  SchoolId INTEGER," +
            "  DateOfBirth date," +
            "  Mobile TEXT," +
            "  Qualification TEXT," +
            "  DateOfJoining date," +
            "  Gender TEXT," +
            "  Email TEXT" +
            ")";

    String CREATE_TIMETABLE = "CREATE TABLE timetable (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  DayOfWeek TEXT," +
            "  PeriodNo INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT," +
            "  TeacherName TEXT," +
            "  TimingFrom time," +
            "  TimingTo time" +
            ")";

    String CREATE_GROUPS = "CREATE TABLE groups (" +
            " Id INTEGER, " +
            " Name TEXT, " +
            " IsSchool INTEGER, " +
            " SectionId INTEGER, " +
            " IsSection INTEGER, " +
            " ClassId INTEGER, " +
            " IsClass INTEGER," +
            " CreatedBy INEGER, " +
            " CreatorName TEXT, " +
            " CreatorRole TEXT, " +
            " CreatedDate TEXT, " +
            " IsActive INTEGER, " +
            " SchoolId INTEGER " +
            ")";

    String CREATE_CHAT = "CREATE TABLE chat (" +
            " Id INTEGER, " +
            " StudentId INTEGER, " +
            " StudentName TEXT, " +
            " ClassName TEXT, " +
            " SectionName TEXT, " +
            " TeacherId INTEGER, " +
            " TeacherName TEXT, " +
            " CreatedBy INTEGER, " +
            " CreatorRole TEXT" +
            ")";

    String CREATE_MESSAGE = "CREATE TABLE message (" +
            " Id INTEGER, " +
            " SenderId INTEGER, " +
            " SenderRole TEXT, " +
            " SenderName TEXT, " +
            " RecipientId INTEGER, " +
            " RecipientRole TEXT, " +
            " GroupId INTEGER, " +
            " MessageType TEXT, " +
            " MessageBody TEXT, " +
            " ImageUrl TEXT, " +
            " VideoUrl TEXT, " +
            " CreatedAt TEXT" +
            ")";

    String CREATE_SERVICE = "CREATE TABLE service (" +
            " Id INTEGER, " +
            " SchoolId INTEGER, " +
            " IsMessage TEXT, " +
            " IsSms TEXT, " +
            " IsChat TEXT, " +
            " IsAttendance TEXT, " +
            " IsAttendanceSms TEXT," +
            " IsHomework TEXT, " +
            " IsHomeworkSms TEXT," +
            " IsTimetable TEXT" +
            ")";

    String CREATE_USER_GROUP = "CREATE TABLE user_group (" +
            " Id INTEGER, " +
            " UserId INTEGER, " +
            " Name TEXT, " +
            " Role TEXT, " +
            " GroupId INTEGER, " +
            " IsActive TEXT" +
            ")";

    String CREATE_EVENT = "CREATE TABLE event (" +
            " Id INTEGER, " +
            " EventTitle TEXT, " +
            " EventDescription TEXT, " +
            " StartDate TEXT, " +
            " EndDate TEXT, " +
            " StartTime INTEGER, " +
            " EndTime INTEGER, " +
            " NoOfDays INTEGER, " +
            " IsContinuousDays TEXT, " +
            " IsFullDayEvent TEXT, " +
            " IsRecurring TEXT, " +
            " CreatedBy TEXT, " +
            " CreatedDate TEXT" +
            ")";

    String CREATE_RECURRING_PATTERN = "CREATE TABLE event_recurring_pattern (" +
            " Id INTEGER, " +
            " EventId INTEGER, " +
            " RecurringType TEXT, " +
            " SeparationCount INTEGER, " +
            " MaxNumOfOccurrences INTEGER, " +
            " DayOfWeek INTEGER, " +
            " WeekOfMonth TEXT, " +
            " DayOfMonth TEXT" +
            ")";

    String CREATE_EVENT_EXCEPTION = "CREATE TABLE event_exception (" +
            " Id INTEGER, " +
            " EventId INTEGER, " +
            " IsRescheduled TEXT, " +
            " IsCancelled TEXT, " +
            " CreatedBy TEXT, " +
            " CreatedDate TEXT" +
            ")";

    String CREATE_EVENT_DATES = "CREATE TABLE event_dates (" +
            " Id INTEGER, " +
            " EventId INTEGER, " +
            " EventDate TEXT" +
            ")";
}
