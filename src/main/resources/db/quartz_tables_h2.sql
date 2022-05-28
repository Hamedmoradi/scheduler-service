-- Note, Quartz depends on row-level locking which means you must use the MVCC=TRUE
-- setting on your H2 database, or you will experience dead-locks
--
--
-- In your Quartz properties file, you'll need to set
-- org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate


CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        NUMBER       NOT NULL,
    SCHED_TIME        NUMBER       NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  CHAR(1)      NULL,
    REQUESTS_RECOVERY CHAR(1)      NULL
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER       NOT NULL,
    CHECKIN_INTERVAL  NUMBER       NOT NULL
);

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL
);

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        CHAR(1)      NOT NULL,
    IS_NONCONCURRENT  CHAR(1)      NOT NULL,
    IS_UPDATE_DATA    CHAR(1)      NOT NULL,
    REQUESTS_RECOVERY CHAR(1)      NOT NULL,
    JOB_DATA          BLOB         NULL
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    NUMBER       NOT NULL,
    REPEAT_INTERVAL NUMBER       NOT NULL,
    TIMES_TRIGGERED NUMBER       NOT NULL
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INTEGER        NULL,
    INT_PROP_2    INTEGER        NULL,
    LONG_PROP_1   NUMBER         NULL,
    LONG_PROP_2   NUMBER         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   CHAR(1)        NULL,
    BOOL_PROP_2   CHAR(1)        NULL
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB         NULL
);

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME NUMBER       NULL,
    PREV_FIRE_TIME NUMBER       NULL,
    PRIORITY       INTEGER      NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     NUMBER       NOT NULL,
    END_TIME       NUMBER       NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT     NULL,
    JOB_DATA       BLOB         NULL
);

ALTER TABLE QRTZ_CALENDARS
    ADD
        CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY
            (
             SCHED_NAME,
             CALENDAR_NAME
                );

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_FIRED_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             ENTRY_ID
                );

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS
    ADD
        CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_SCHEDULER_STATE
    ADD
        CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY
            (
             SCHED_NAME,
             INSTANCE_NAME
                );

ALTER TABLE QRTZ_LOCKS
    ADD
        CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY
            (
             SCHED_NAME,
             LOCK_NAME
                );

ALTER TABLE QRTZ_JOB_DETAILS
    ADD
        CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY
            (
             SCHED_NAME,
             JOB_NAME,
             JOB_GROUP
                );

ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_SIMPROP_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;


ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;


ALTER TABLE QRTZ_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS FOREIGN KEY
            (
             SCHED_NAME,
             JOB_NAME,
             JOB_GROUP
                ) REFERENCES QRTZ_JOB_DETAILS (
                                               SCHED_NAME,
                                               JOB_NAME,
                                               JOB_GROUP
                );

CREATE TABLE scheduler_job_info
(
    id              NUMBER(38, 0) GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    job_name        VARCHAR2(255),
    job_group       VARCHAR2(255),
    job_status      VARCHAR2(255),
    job_class       VARCHAR2(255),
    cron_expression VARCHAR2(255),
    description     VARCHAR2(255),
    interface_name  VARCHAR2(255),
    repeat_time     NUMBER(38, 0),
    cron_job        NUMBER(1),
    service_type    VARCHAR2(255),
    CONSTRAINT pk_scheduler_job_info PRIMARY KEY (id)
);

COMMIT;