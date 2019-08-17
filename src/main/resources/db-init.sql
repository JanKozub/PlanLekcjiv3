create TABLE Events (
    userId UUID NOT NULL,
    id UUID NOT NULL,
    timestamp BIGINT NOT NULL,
    hour INT NOT NULL,
    eventType VARCHAR NOT NULL,
    topic VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);

create TABLE ScheduleEntry (
    userId UUID NOT NULL,
    hour INT NOT NULL,
    day INT NOT NULL,
    subject VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);

create TABLE Users (
    id UUID NOT NULL,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    darkTheme boolean NOT NULL,
    scheduleHours boolean NOT NULL,
    email VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    synWGroup boolean NOT NULL,
    createdDate BIGINT NOT NULL,
    lastLoggedDate BIGINT NOT NULL,
);

create TABLE Groups (
    isAccepted boolean NOT NULL,
    name VARCHAR NOT NULL,
    groupCode INT NOT NULL,
    leaderId UUID NOT NULL,
    hour INT NOT NULL,
    day INT NOT NULL,
    subject VARCHAR NOT NULL,
    createdDate BIGINT NOT NULL
);

--DROP TABLE IF EXISTS EVENTS;
--DROP TABLE IF EXISTS SCHEDULEENTRY;
--DROP TABLE IF EXISTS USERS;
--DROP TABLE IF EXISTS GROUPS;
--
--   UUID DEFAULT_USER_ID = UUID.fromString("d995360d-1c4f-48fe-8760-0fe064770f5b");
--    insert into ScheduleEntry(userId, hour, day, subject) values('d995360d-1c4f-48fe-8760-0fe064770f5b', 0, 0, 'WF')
--
--
--alter table UsersPageView add column userId UUID;
--alter table events add column userId UUID;
--alter table scheduleentry add column userId UUID;
--
--update UsersPageView set id = random_uuid() where id is null;
--
--update events set userId = 'd995360d-1c4f-48fe-8760-0fe064770f5b'
--update scheduleentry set userId = 'd995360d-1c4f-48fe-8760-0fe064770f5b'
--
--alter table events alter column userId set not null;
--alter table scheduleentry alter column userId set not null;
--alter table UsersPageView alter column id set not null;