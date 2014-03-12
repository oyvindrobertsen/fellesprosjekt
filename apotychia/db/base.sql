CREATE TABLE person (
    username    VARCHAR(45)     NOT NULL,
    pwd         CHAR(80)        NOT NULL,
    firstName   VARCHAR(45)     NOT NULL,
    lastName    VARCHAR(45)     NOT NULL,
    mail        VARCHAR(45)     NOT NULL,

    PRIMARY KEY (username)
);

CREATE TABLE room (
    roomNr      VARCHAR(10)     NOT NULL,
    capacity    INT             NOT NULL,

    PRIMARY KEY (roomNr)
);

CREATE TABLE eventGroup (
    groupId     INT             NOT NULL    AUTO_INCREMENT,
    groupName   VARCHAR(45)     NULL,
    PRIMARY KEY (groupId)
);

CREATE TABLE memberOf (
    groupId     INT             NOT NULL,
    username    VARCHAR(45)     NOT NULL,

    PRIMARY KEY (groupId),
    FOREIGN KEY (groupId)  REFERENCES eventGroup(groupId)
        ON UPDATE cascade ON DELETE cascade,
    FOREIGN KEY (username)  REFERENCES person(username)
        ON UPDATE cascade ON DELETE cascade

);

CREATE TABLE calendarEvent (
    eventId         INT          NOT NULL AUTO_INCREMENT,
    eventName       VARCHAR(45)  NOT NULL,
    startTime       DATETIME     NOT NULL,
    endTime         DATETIME     NOT NULL,
    isActive        VARCHAR(45)  NOT NULL,
    description     TINYTEXT     NULL,
    eventAdmin      VARCHAR(45)  NOT NULL,

    PRIMARY KEY (eventId),
    FOREIGN KEY (eventAdmin) REFERENCES person (username)
        ON UPDATE cascade ON DELETE cascade
);


CREATE TABLE participants (
    eventId     INT             NOT NULL    AUTO_INCREMENT,
    username    VARCHAR(45)     NULL,
    groupId     INT             NULL,

    PRIMARY KEY (eventId),
    FOREIGN KEY(username) REFERENCES person (username)
        ON UPDATE cascade ON DELETE SET NULL,
    FOREIGN KEY(groupId)  REFERENCES eventGroup (groupId)
        ON UPDATE cascade ON DELETE SET NULL
);

CREATE TABLE booked (
    bookedId    INT             NOT NULL    AUTO_INCREMENT,
    eventId     INT             NOT NULL,
    roomNr      VARCHAR(10)     NOT NULL,

    PRIMARY KEY (bookedId),
    FOREIGN KEY (eventId) REFERENCES  calendarEvent(eventId)
        ON UPDATE cascade ON DELETE cascade,
    FOREIGN KEY (roomNr)  REFERENCES room(roomNr)
        ON UPDATE cascade ON DELETE cascade
);