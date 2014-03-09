CREATE TABLE person (
	username VARCHAR(45) NOT NULL,
	password VARCHAR(45) NOT NULL,
	name	 VARCHAR(45) NOT NULL, 
	mail 	 VARCHAR(45) NOT NULL, 

	PRIMARY KEY (username)
)


CREATE TABLE event (
	eventID 		INT  		 NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(45)	 NOT NULL,
	start   		DATETIME	 NOT NULL, 
	end 			DATETIME     NOT NULL,
	isActive 		VARCHAR(45)  NOT NULL,
	description		TINYTEXT	 NULL,
	eventAdmin		VARCHAR(45)  NOT NULL,
	participantID   INT 		 NULL, 

	PRIMARY KEY (eventID),
	FOREIGN KEY(particpant) REFERENCES particpants(id)
)


CREATE TABLE participants (
	id 		 	INT		 		NOT NULL	AUTO_INCREMENT,
	username 	VARCHAR(45) 	NULL,
	groupID	 	INT 			NULL, 

	PRIMARY KEY (id),
	FOREIGN KEY(username) REFERENCES person (username),
	FOREIGN KEY(groupID)  REFERENCES group (groupID)
)


CREATE TABLE group (
	groupID  		INT		NOT NULL 	AUTO_INCREMENT,
	memberID  		INT 	NULL,

	PRIMARY KEY (groupID),
	FOREIGN KEY (memberID) REFERENCES memberOf (id)
)


CREATE TABLE memberOf (
	id 			INT 			NOT NULL	AUTO_INCREMENT,
	username	VARCHAR(45) 	NOT NULL, 

	PRIMARY KEY (id),
	FOREIGN KEY (username)  REFERENCES person(username)
)

CREATE TABLE booked (
	bookedID 	INT 			NOT NULL	AUTO_INCREMENT,
	eventID 	INT  			NOT NULL,
	roomNr		VARCHAR(10)		NOT NULL,

	PRIMARY KEY (bookedID),
	FOREIGN KEY (eventID) REFERENCES (event),
	FOREIGN KEY (roomNr)  REFERENCES (room)	
)


CREATE TABLE room (
	roomNr 		VARCHAR(10)		NOT NULL,
	capacity 	INT 			NOT NULL, 

	PRIMARY KEY (roomNr)
)



