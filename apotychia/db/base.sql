USE apotychia;

CREATE TABLE person (
    username    VARCHAR(45)     NOT NULL,
    pwd         VARCHAR(45)     NOT NULL,
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

CREATE TABLE memberOf (
    id          INT             NOT NULL    AUTO_INCREMENT,
    username    VARCHAR(45)     NOT NULL, 

    PRIMARY KEY (id),
    FOREIGN KEY (username)  REFERENCES person(username)
        ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE eventGroup (
    groupID     INT     NOT NULL    AUTO_INCREMENT,
    memberID    INT     NULL,

    PRIMARY KEY (groupID),
    FOREIGN KEY (memberID) REFERENCES memberOf (id)
    ON UPDATE cascade ON DELETE SET NULL
);

CREATE TABLE participants (
    id          INT             NOT NULL    AUTO_INCREMENT,
    username    VARCHAR(45)     NULL,
    groupID     INT             NULL, 
    
    PRIMARY KEY (id),
    FOREIGN KEY(username) REFERENCES person (username)
        ON UPDATE cascade ON DELETE SET NULL,
    FOREIGN KEY(groupID)  REFERENCES eventGroup (groupID)
        ON UPDATE cascade ON DELETE SET NULL
);

CREATE TABLE calendarEvent (
    eventID         INT          NOT NULL AUTO_INCREMENT,
    eventName       VARCHAR(45)  NOT NULL,
    startTime       DATETIME     NOT NULL, 
    endTime         DATETIME     NOT NULL,
    isActive        VARCHAR(45)  NOT NULL,
    description     TINYTEXT     NULL,
    eventAdmin      VARCHAR(45)  NOT NULL,
    participantID   INT          NULL, 

    PRIMARY KEY (eventID),
    FOREIGN KEY (participantID) REFERENCES participants (id)
        ON UPDATE cascade ON DELETE SET NULL
);



CREATE TABLE booked (
    bookedID    INT             NOT NULL    AUTO_INCREMENT,
    eventID     INT             NOT NULL,
    roomNr      VARCHAR(10)     NOT NULL,

    PRIMARY KEY (bookedID),
    FOREIGN KEY (eventID) REFERENCES  calendarEvent(eventID)
        ON UPDATE cascade ON DELETE cascade,
    FOREIGN KEY (roomNr)  REFERENCES room(roomNr)
        ON UPDATE cascade ON DELETE cascade
);

