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

	CONSTRAINT FK_Course_courseLevel FOREIGN KEY (courseLevel) REFERENCES courseLevel(id),
	CONSTRAINT UQ_Course_courseName UNIQUE(courseName)
);

CREATE TABLE Content (
	contentItemID int IDENTITY(1,1) PRIMARY KEY,
	title nvarchar(150) NOT NULL,
	abstract nvarchar(300) NOT NULL,
	publicationDate date NOT NULL CONSTRAINT DF_Content_publicationDate DEFAULT GETDATE(),
	contentStatus tinyint NOT NULL,
	contentVersion tinyint NOT NULL CONSTRAINT DF_Content_contentVersion DEFAULT 0,

	CONSTRAINT FK_Content_contentStatus FOREIGN KEY (contentStatus) REFERENCES contentStatus(id),
	CONSTRAINT CK_Content_contentItemIDValid CHECK(contentItemID > 0),
	CONSTRAINT UQ_Content_title_version UNIQUE(title, contentVersion),
	CONSTRAINT CK_Content_version_gt_zero CHECK(contentVersion > 0),
);

CREATE TABLE Module (
	contentItemID int PRIMARY KEY,
	contactName nvarchar(100) NOT NULL,
	contactEmail varchar(320) NOT NULL,
	courseID int NOT NULL,

	CONSTRAINT FK_Module_Course FOREIGN KEY (courseID) REFERENCES Course(courseID),
	CONSTRAINT FK_Module_Content FOREIGN KEY (contentItemID) REFERENCES Content(contentItemID)
);

CREATE TABLE Webcast (
	contentItemID int PRIMARY KEY,
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
	CONSTRAINT UQ_CMCUser_email UNIQUE(email)
);

CREATE TABLE Enrollment (
	userID int NOT NULL,
	courseID int NOT NULL,
	enrollmentTime datetime PRIMARY KEY CONSTRAINT DF_Enrollment_enrollmentTime DEFAULT GETDATE(),

	CONSTRAINT FK_Enrollment_CMCUser FOREIGN KEY (userID) REFERENCES CMCUser(userID) ON DELETE CASCADE,
	CONSTRAINT FK_Enrollment_Course FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE NO ACTION,
	CONSTRAINT UQ_Enrollment UNIQUE(userID, courseID, enrollmentTIme)
);

CREATE TABLE CMCCertificate (
	certificateID int IDENTITY(1,1) PRIMARY KEY,
	certificateName nvarchar(100),

	CONSTRAINT UQ_CMCCertificate_certificateName UNIQUE (certificateName)
);

CREATE TABLE Graduation (
	userID int PRIMARY KEY,
	certificateID int NOT NULL,
	grantedBy nvarchar(100) NOT NULL,
	grade tinyint NOT NULL,

	CONSTRAINT UQ_Graduation_userID_certificateID UNIQUE(userID, certificateID),
	CONSTRAINT FK_Graduation_CMCUser FOREIGN KEY (userID) REFERENCES CMCUser(userID) ON DELETE CASCADE,
	CONSTRAINT FK_Graduation_CMCCertificate FOREIGN KEY (certificateID) REFERENCES CMCCertificate(certificateID),
	CONSTRAINT CH_Graduation_grade CHECK(grade <= 10 AND grade >= 1)
);

CREATE TABLE ViewedItems (
	contentItemID int PRIMARY KEY,
	userID int NOT NULL,
	viewed tinyint NOT NULL,

	CONSTRAINT FK_ViewedItems_CMCUser FOREIGN KEY (userID) REFERENCES CMCUser(userID),
	CONSTRAINT FK_ViewedItems_Content FOREIGN KEY (contentItemID) REFERENCES Content(contentItemID),
	CONSTRAINT UQ_ViewedItems_contentItemID_userID UNIQUE(contentItemID, userID),
)