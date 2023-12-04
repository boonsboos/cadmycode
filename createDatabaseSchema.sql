--
-- enums
--
CREATE TABLE courseLevel (
	id tinyint PRIMARY KEY,
	difficulty varchar(12) NOT NULL
);

INSERT INTO courseLevel
VALUES
	(0, 'BEGINNER'),
	(1, 'INTERMEDIATE'),
	(2, 'EXPERT');


CREATE TABLE contentStatus (
	id tinyint PRIMARY KEY,
	named varchar(7) NOT NULL
);

INSERT INTO contentStatus
VALUES
	(0, 'CONCEPT'),
	(1, 'ACTIVE'),
	(2, 'ARCHIVE');

CREATE TABLE sex (
	id tinyint PRIMARY KEY,
	named varchar(7)
);

INSERT INTO sex
VALUES
	(0, 'MALE'),
	(1, 'FEMALE'),
	(2, 'UNKNOWN');

--
-- entities
--
CREATE TABLE Course (
    courseID int IDENTITY(1,1) PRIMARY KEY,
    courseName nvarchar(150) NOT NULL,
    subj nvarchar(50) NOT NULL,
    introductionText nvarchar(280),
    courseLevel tinyint NOT NULL,

	CONSTRAINT FK_Course_courseLevel FOREIGN KEY (courseLevel) REFERENCES courseLevel(id)
);

CREATE TABLE Content (
	contentItemID int IDENTITY(1,1) PRIMARY KEY,
	title nvarchar(150) NOT NULL,
	abstract nvarchar(300) NOT NULL,
	publicationDate date NOT NULL CONSTRAINT DF_Content_publicationDate DEFAULT GETDATE(),
	contentStatus tinyint NOT NULL,

	CONSTRAINT FK_Content_contentStatus FOREIGN KEY (contentStatus) REFERENCES contentStatus(id),
	CONSTRAINT CK_Content_contentItemIDValid CHECK(contentItemID > 0)
);

CREATE TABLE Module (
	contentItemID int NOT NULL,
	contactName nvarchar(100) NOT NULL,
	contactEmail varchar(320) NOT NULL,
	courseID int NOT NULL,

	CONSTRAINT FK_Module_Course FOREIGN KEY (courseID) REFERENCES Course(courseID),
	CONSTRAINT FK_Module_Content FOREIGN KEY (contentItemID) REFERENCES Content(contentItemID)
);

CREATE TABLE Webcast (
	contentItemID int NOT NULL,
	contentLength int NOT NULL,
	webAddress varchar(2048) NOT NULL,
	organisation nvarchar(130) NOT NULL,
	speaker nvarchar(100) NOT NULL,

	CONSTRAINT FK_Webcast_Content FOREIGN KEY (contentItemID) REFERENCES Content(contentItemID)
);

CREATE TABLE CMCUser (
	userID int IDENTITY(1,1) PRIMARY KEY,
	username nvarchar(120) NOT NULL,
	email varchar(320) NOT NULL,
	adres nvarchar(120) NOT NULL,
	country char(2) NOT NULL, -- USE ISO 2 LETTER COUNTRY CODES
	city nvarchar(100) NOT NULL,
	dateOfBirth date NOT NULL,
	sex tinyint NOT NULL CONSTRAINT DF_CMCUser_sex DEFAULT 2,

	CONSTRAINT FK_CMCUser_sex FOREIGN KEY (sex) REFERENCES sex(id),
);

CREATE TABLE Enrollment (
	enrollmentID int IDENTITY(1,1) PRIMARY KEY,
	userID int NOT NULL,
	courseID int NOT NULL,
	enrollmentTime datetime NOT NULL CONSTRAINT DF_Enrollment_enrollmentTime DEFAULT GETDATE(),

	CONSTRAINT FK_Enrollment_CMCUser FOREIGN KEY (userID) REFERENCES CMCUser(userID),
	CONSTRAINT FK_Enrollment_Course FOREIGN KEY (courseID) REFERENCES Course(courseID),
	CONSTRAINT UQ_Enrollment UNIQUE(userID, courseID, enrollmentTIme)
);

CREATE TABLE CMCCertificate (
	certificateID int IDENTITY(1,1) PRIMARY KEY,
	enrollmentID int NOT NULL,
	grantedBy nvarchar(100) NOT NULL,
	grade tinyint NOT NULL,

	CONSTRAINT UQ_CMCCertificate_enrollmentID UNIQUE(enrollmentID),
	CONSTRAINT FK_CMCCertificate_Enrollment FOREIGN KEY (enrollmentID) REFERENCES Enrollment(enrollmentID)
);