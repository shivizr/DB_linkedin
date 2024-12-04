CREATE TABLE IF NOT EXISTS MAIN_USER (
                                         UserName INT PRIMARY KEY NOT NULL,
                                         FirstName VARCHAR(60),
                                         LastName VARCHAR(60),
                                         Email VARCHAR(150),
                                         Password VARCHAR(100),
                                         Job VARCHAR(300),
                                         DateOfBirth VARCHAR(200),
                                         Gender VARCHAR(6),
                                         Country VARCHAR(300),
                                         City VARCHAR(400)
);

CREATE TABLE IF NOT EXISTS Profile (
                                       P_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                       UserID INT,
                                       Intro TEXT,
                                       More_Info TEXT,
                                       Accomplishment TEXT,
                                       ContactInfo VARCHAR(255),
                                       Background VARCHAR(255),
                                       Language VARCHAR(255),
                                       FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Company (
                                       ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                       UserID INT,
                                       Name VARCHAR(200) NOT NULL,
                                       FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Company_User (
                                            UserID INT NOT NULL,
                                            CompanyID INT NOT NULL,
                                            Start_Date VARCHAR(200),
                                            End_Date VARCHAR(200),
                                            FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName),
                                            FOREIGN KEY (CompanyID) REFERENCES Company(ID)
);

CREATE TABLE IF NOT EXISTS Skill (
                                     ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                     Name VARCHAR(100),
                                     UserID INT NOT NULL,
                                     FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Endorsement (
                                           ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                           SkillID INT,
                                           UserID INT,
                                           EndorsedBy INT,
                                           FOREIGN KEY (SkillID) REFERENCES Skill(ID),
                                           FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName),
                                           FOREIGN KEY (EndorsedBy) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Post (
                                    ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                    UserID INT,
                                    Content TEXT,
                                    UniqueLink VARCHAR(255) NOT NULL,
                                    PostDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Comment (
                                       ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                       PostID INT,
                                       UserID INT,
                                       SecondId INT,
                                       Text TEXT,
                                       Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (PostID) REFERENCES Post(ID),
                                       FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Likes (
                                     ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                     PostID INT,
                                     UserID INT,
                                     CommentID INT,
                                     FOREIGN KEY (PostID) REFERENCES Post(ID),
                                     FOREIGN KEY (CommentID) REFERENCES Comment(ID),
                                     FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS ConnectionReq (
                                             ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                             UserID1 INT,
                                             UserID2 INT,
                                             Status BOOLEAN,
                                             ConnectionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (UserID1) REFERENCES MAIN_USER(UserName),
                                             FOREIGN KEY (UserID2) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Message (
                                       MessageID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                       SenderID INT,
                                       ReceiverID INT,
                                       Content TEXT,
                                       SentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       IsRead BOOLEAN DEFAULT 0,
                                       IsArchived BOOLEAN DEFAULT 0,
                                       FOREIGN KEY (SenderID) REFERENCES MAIN_USER(UserName),
                                       FOREIGN KEY (ReceiverID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Education (
                                         ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                         UserID INT,
                                         UniversityName VARCHAR(300),
                                         Degree VARCHAR(100),
                                         Major VARCHAR(100),
                                         StartDate VARCHAR(200),
                                         EndDate VARCHAR(200),
                                         FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

CREATE TABLE IF NOT EXISTS Notification (
                                            ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                            UserID INT,
                                            Content TEXT,
                                            Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            Type VARCHAR(50),
                                            FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName)
);

INSERT INTO MAIN_USER (UserName, FirstName, LastName, Email, Password, Job, DateOfBirth, Gender, Country, City) VALUES
    (1, 'John', 'Doe', 'john.doe@example.com', 'password1', 'Engineer', '1985-01-01', 'Male', 'USA', 'New York'),
    (2, 'Jane', 'Smith', 'jane.smith@example.com', 'password2', 'Doctor', '1990-02-02', 'Female', 'USA', 'Los Angeles'),
    (3, 'Alice', 'Johnson', 'alice.johnson@example.com', 'password3', 'Lawyer', '1988-03-03', 'Female', 'USA', 'Chicago'),
    (4, 'Bob', 'Brown', 'bob.brown@example.com', 'password4', 'Teacher', '1975-04-04', 'Male', 'USA', 'Houston'),
    (5, 'Charlie', 'Davis', 'charlie.davis@example.com', 'password5', 'Artist', '1995-05-05', 'Male', 'USA', 'Phoenix'),
    (6, 'David', 'Williams', 'david.williams@example.com', 'password6', 'Scientist', '1987-06-06', 'Male', 'USA', 'San Francisco'),
    (7, 'Eva', 'Martinez', 'eva.martinez@example.com', 'password7', 'Architect', '1992-07-07', 'Female', 'USA', 'Dallas'),
    (8, 'Frank', 'Taylor', 'frank.taylor@example.com', 'password8', 'Chef', '1983-08-08', 'Male', 'USA', 'Austin'),
    (9, 'Grace', 'Anderson', 'grace.anderson@example.com', 'password9', 'Nurse', '1991-09-09', 'Female', 'USA', 'Seattle'),
    (10, 'Henry', 'Thomas', 'henry.thomas@example.com', 'password10', 'Journalist', '1979-10-10', 'Male', 'USA', 'Miami');
INSERT INTO Profile (UserID, Intro, More_Info, Accomplishment, ContactInfo, Background, Language) VALUES
    (1, 'Software Engineer at XYZ', 'More info about John', 'Accomplishments of John', 'john.doe@example.com', 'Background of John', 'English'),
    (2, 'Doctor at ABC', 'More info about Jane', 'Accomplishments of Jane', 'jane.smith@example.com', 'Background of Jane', 'English'),
    (3, 'Lawyer at DEF', 'More info about Alice', 'Accomplishments of Alice', 'alice.johnson@example.com', 'Background of Alice', 'English'),
    (4, 'Teacher at GHI', 'More info about Bob', 'Accomplishments of Bob', 'bob.brown@example.com', 'Background of Bob', 'English'),
    (5, 'Artist at JKL', 'More info about Charlie', 'Accomplishments of Charlie', 'charlie.davis@example.com', 'Background of Charlie', 'English'),
    (6, 'Scientist at MNO', 'More info about David', 'Accomplishments of David', 'david.williams@example.com', 'Background of David', 'English'),
    (7, 'Architect at PQR', 'More info about Eva', 'Accomplishments of Eva', 'eva.martinez@example.com', 'Background of Eva', 'English'),
    (8, 'Chef at STU', 'More info about Frank', 'Accomplishments of Frank', 'frank.taylor@example.com', 'Background of Frank', 'English'),
    (9, 'Nurse at VWX', 'More info about Grace', 'Accomplishments of Grace', 'grace.anderson@example.com', 'Background of Grace', 'English'),
    (10, 'Journalist at YZA', 'More info about Henry', 'Accomplishments of Henry', 'henry.thomas@example.com', 'Background of Henry', 'English');
INSERT INTO Company (UserID, Name) VALUES
    (1, 'XYZ Corporation'),
    (2, 'ABC Hospital'),
    (3, 'DEF Law Firm'),
    (4, 'GHI School'),
    (5, 'JKL Art Studio'),
    (6, 'MNO Research Center'),
    (7, 'PQR Architects'),
    (8, 'STU Culinary Institute'),
    (9, 'VWX Medical Center'),
    (10, 'YZA News Agency');
INSERT INTO Company_User (UserID, CompanyID, Start_Date, End_Date) VALUES
    (1, 1, '2010-01-01', '2020-01-01'),
    (2, 2, '2012-02-02', '2022-02-02'),
    (3, 3, '2014-03-03', '2024-03-03'),
    (4, 4, '2016-04-04', '2026-04-04'),
    (5, 5, '2018-05-05', '2028-05-05'),
    (6, 6, '2011-06-06', '2021-06-06'),
    (7, 7, '2013-07-07', '2023-07-07'),
    (8, 8, '2015-08-08', '2025-08-08'),
    (9, 9, '2017-09-09', '2027-09-09'),
    (10, 10, '2019-10-10', '2029-10-10');
INSERT INTO Skill (Name, UserID) VALUES
    ('Java', 1),
    ('Python', 2),
    ('Legal Research', 3),
    ('Teaching', 4),
    ('Painting', 5),
    ('Biotechnology', 6),
    ('AutoCAD', 7),
    ('Culinary Arts', 8),
    ('Patient Care', 9),
    ('Investigative Journalism', 10),
    ('C++', 1),
    ('Data Science', 2),
    ('Contract Law', 3),
    ('Curriculum Development', 4),
    ('Sculpting', 5),
    ('Genetics', 6),
    ('Building Design', 7),
    ('Food Safety', 8),
    ('Emergency Response', 9),
    ('Photojournalism', 10);
INSERT INTO Endorsement (SkillID, UserID, EndorsedBy) VALUES
    (1, 1, 2),
    (2, 2, 3),
    (3, 3, 4),
    (4, 4, 5),
    (5, 5, 1),
    (6, 6, 2),
    (7, 7, 3),
    (8, 8, 4),
    (9, 9, 5),
    (10, 10, 1),
    (11, 1, 3),
    (12, 2, 4),
    (13, 3, 5),
    (14, 4, 1),
    (15, 5, 2),
    (16, 6, 3),
    (17, 7, 4),
    (18, 8, 5),
    (19, 9, 1),
    (20, 10, 2);
INSERT INTO Likes (PostID, UserID, CommentID) VALUES
    (1, 1, NULL),
    (1, 2, NULL),
    (1, 3, NULL),
    (2, 1, NULL),
    (2, 2, NULL),
    (2, 3, NULL),
    (3, 1, NULL),
    (3, 2, NULL),
    (3, 3, NULL),
    (4, 1, NULL),
    (4, 2, NULL),
    (4, 3, NULL),
    (5, 1, NULL),
    (5, 2, NULL),
    (5, 3, NULL),
    (1, 4, NULL),
    (1, 5, NULL),
    (2, 4, NULL),
    (2, 5, NULL),
    (3, 4, NULL),

    -- Likes on comments
    (NULL, 1, 1),
    (NULL, 2, 2),
    (NULL, 3, 3),
    (NULL, 4, 4),
    (NULL, 5, 5),
    (NULL, 1, 6),
    (NULL, 2, 7),
    (NULL, 3, 8),
    (NULL, 4, 9),
    (NULL, 5, 10),
    (NULL, 1, 11),
    (NULL, 2, 12),
    (NULL, 3, 13),
    (NULL, 4, 14),
    (NULL, 5, 15),
    (NULL, 1, 16),
    (NULL, 2, 17),
    (NULL, 3, 18),
    (NULL, 4, 19),
    (NULL, 5, 20);
INSERT INTO Message (SenderID, ReceiverID, Content, SentDate, IsRead, IsArchived) VALUES
    (1, 2, 'Message 1 from John to Jane', '2023-01-01', FALSE, FALSE),
    (2, 1, 'Reply 1 from Jane to John', '2023-01-02', FALSE, FALSE),
    (1, 2, 'Message 2 from John to Jane', '2023-01-03', FALSE, FALSE),
    (2, 1, 'Reply 2 from Jane to John', '2023-01-04', FALSE, FALSE),
    (1, 2, 'Message 3 from John to Jane', '2023-01-05', FALSE, FALSE),
    (2, 1, 'Reply 3 from Jane to John', '2023-01-06', FALSE, FALSE),
    (1, 2, 'Message 4 from John to Jane', '2023-01-07', FALSE, FALSE),
    (2, 1, 'Reply 4 from Jane to John', '2023-01-08', FALSE, FALSE),
    (1, 3, 'Message 1 from John to Alice', '2023-02-01', FALSE, FALSE),
    (3, 1, 'Reply 1 from Alice to John', '2023-02-02', FALSE, FALSE),
    (1, 3, 'Message 2 from John to Alice', '2023-02-03', FALSE, FALSE),
    (3, 1, 'Reply 2 from Alice to John', '2023-02-04', FALSE, FALSE),
    (2, 3, 'Message 1 from Jane to Alice', '2023-03-01', FALSE, FALSE),
    (3, 2, 'Reply 1 from Alice to Jane', '2023-03-02', FALSE, FALSE),
    (2, 3, 'Message 2 from Jane to Alice', '2023-03-03', FALSE, FALSE),
    (3, 2, 'Reply 2 from Alice to Jane', '2023-03-04', FALSE, FALSE),
    (2, 4, 'Message 1 from Jane to Bob', '2023-04-01', FALSE, FALSE),
    (4, 2, 'Reply 1 from Bob to Jane', '2023-04-02', FALSE, FALSE),
    (2, 4, 'Message 2 from Jane to Bob', '2023-04-03', FALSE, FALSE),
    (4, 2, 'Reply 2 from Bob to Jane', '2023-04-04', FALSE, FALSE),
    (3, 4, 'Message 1 from Alice to Bob', '2023-05-01', FALSE, FALSE),
    (4, 3, 'Reply 1 from Bob to Alice', '2023-05-02', FALSE, FALSE),
    (3, 4, 'Message 2 from Alice to Bob', '2023-05-03', FALSE, FALSE),
    (4, 3, 'Reply 2 from Bob to Alice', '2023-05-04', FALSE, FALSE),
    (4, 5, 'Message 1 from Bob to Charlie', '2023-06-01', FALSE, FALSE),
    (5, 4, 'Reply 1 from Charlie to Bob', '2023-06-02', FALSE, FALSE),
    (4, 5, 'Message 2 from Bob to Charlie', '2023-06-03', FALSE, FALSE),
    (5, 4, 'Reply 2 from Charlie to Bob', '2023-06-04', FALSE, FALSE),
    (5, 1, 'Message 1 from Charlie to John', '2023-07-01', FALSE, FALSE),
    (1, 5, 'Reply 1 from John to Charlie', '2023-07-02', FALSE, FALSE);
INSERT INTO Post (UserID, Content, UniqueLink, PostDate) VALUES
    (1, 'First post by John', 'link1', '2023-01-01'),
    (2, 'First post by Jane', 'link2', '2023-02-02'),
    (3, 'First post by Alice', 'link3', '2023-03-03'),
    (4, 'First post by Bob', 'link4', '2023-04-04'),
    (5, 'First post by Charlie', 'link5', '2023-05-05'),
    (6, 'First post by David', 'link6', '2023-06-06'),
    (7, 'First post by Eva', 'link7', '2023-07-07'),
    (8, 'First post by Frank', 'link8', '2023-08-08'),
    (9, 'First post by Grace', 'link9', '2023-09-09'),
    (10, 'First post by Henry', 'link10', '2023-10-10'),
    (1, 'Second post by John', 'link11', '2023-01-11'),
    (2, 'Second post by Jane', 'link12', '2023-02-12'),
    (3, 'Second post by Alice', 'link13', '2023-03-13'),
    (4, 'Second post by Bob', 'link14', '2023-04-14'),
    (5, 'Second post by Charlie', 'link15', '2023-05-15'),
    (6, 'Second post by David', 'link16', '2023-06-16'),
    (7, 'Second post by Eva', 'link17', '2023-07-17'),
    (8, 'Second post by Frank', 'link18', '2023-08-18'),
    (9, 'Second post by Grace', 'link19', '2023-09-19'),
    (10, 'Second post by Henry', 'link20', '2023-10-20');
INSERT INTO Comment (PostID, UserID, Text, Date) VALUES
    (1, 1, 'Comment 1 on post 1', '2023-01-01'),
    (2, 2, 'Comment 1 on post 2', '2023-02-02'),
    (3, 3, 'Comment 1 on post 3', '2023-03-03'),
    (4, 4, 'Comment 1 on post 4', '2023-04-04'),
    (5, 5, 'Comment 1 on post 5', '2023-05-05'),
    (6, 6, 'Comment 1 on post 6', '2023-06-06'),
    (7, 7, 'Comment 1 on post 7', '2023-07-07'),
    (8, 8, 'Comment 1 on post 8', '2023-08-08'),
    (9, 9, 'Comment 1 on post 9', '2023-09-09'),
    (10, 10, 'Comment 1 on post 10', '2023-10-10'),
    (1, 2, 'Comment 2 on post 1', '2023-01-11'),
    (2, 3, 'Comment 2 on post 2', '2023-02-12'),
    (3, 4, 'Comment 2 on post 3', '2023-03-13'),
    (4, 5, 'Comment 2 on post 4', '2023-04-14'),
    (5, 6, 'Comment 2 on post 5', '2023-05-15'),
    (6, 7, 'Comment 2 on post 6', '2023-06-16'),
    (7, 8, 'Comment 2 on post 7', '2023-07-17'),
    (8, 9, 'Comment 2 on post 8', '2023-08-18'),
    (9, 10, 'Comment 2 on post 9', '2023-09-19'),
    (10, 1, 'Comment 2 on post 10', '2023-10-20');

INSERT INTO ConnectionReq (UserID1, UserID2, Status, ConnectionDate) VALUES
    (1, 2, TRUE, '2023-01-01'),
    (2, 3, TRUE, '2023-02-01'),
    (3, 4, TRUE, '2023-03-01'),
    (4, 5, TRUE, '2023-04-01'),
    (5, 6, TRUE, '2023-05-01'),
    (6, 7, TRUE, '2023-06-01'),
    (7, 8, TRUE, '2023-07-01'),
    (8, 9, TRUE, '2023-08-01'),
    (9, 10, TRUE, '2023-09-01'),
    (10, 1, TRUE, '2023-10-01'),
    (2, 4, TRUE, '2023-01-02'),
    (3, 5, TRUE, '2023-02-03'),
    (4, 6, TRUE, '2023-03-04'),
    (5, 7, TRUE, '2023-04-05'),
    (6, 8, TRUE, '2023-05-06'),
    (7, 9, TRUE, '2023-06-07'),
    (8, 10, TRUE, '2023-07-08'),
    (9, 1, TRUE, '2023-08-09'),
    (10, 2, TRUE, '2023-09-10'),
    (1, 3, TRUE, '2023-10-11');
INSERT INTO Education (UserID, UniversityName, Degree, Major, StartDate, EndDate) VALUES
    (1, 'Harvard University', 'Bachelor', 'Computer Science', '2003-09-01', '2007-06-01'),
    (2, 'Stanford University', 'Master', 'Medicine', '2005-09-01', '2009-06-01'),
    (3, 'Yale University', 'Doctorate', 'Law', '2007-09-01', '2011-06-01'),
    (4, 'MIT', 'Bachelor', 'Education', '2001-09-01', '2005-06-01'),
    (5, 'Art Institute of Chicago', 'Bachelor', 'Fine Arts', '2009-09-01', '2013-06-01'),
    (6, 'Princeton University', 'Master', 'Physics', '2004-09-01', '2008-06-01'),
    (7, 'Columbia University', 'Bachelor', 'Architecture', '2006-09-01', '2010-06-01'),
    (8, 'Cornell University', 'Bachelor', 'Culinary Arts', '2002-09-01', '2006-06-01'),
    (9, 'University of Pennsylvania', 'Master', 'Nursing', '2007-09-01', '2011-06-01'),
    (10, 'Duke University', 'Bachelor', 'Journalism', '2001-09-01', '2005-06-01'),
    (1, 'MIT', 'Master', 'Data Science', '2008-09-01', '2010-06-01'),
    (2, 'Harvard University', 'Bachelor', 'Biology', '2001-09-01', '2005-06-01'),
    (3, 'Stanford University', 'Master', 'Business Administration', '2009-09-01', '2011-06-01'),
    (4, 'Yale University', 'Bachelor', 'Psychology', '2000-09-01', '2004-06-01'),
    (5, 'Princeton University', 'Master', 'Fine Arts', '2014-09-01', '2016-06-01'),
    (6, 'Columbia University', 'Doctorate', 'Engineering', '2008-09-01', '2012-06-01'),
    (7, 'Cornell University', 'Bachelor', 'Civil Engineering', '2003-09-01', '2007-06-01'),
    (8, 'University of Pennsylvania', 'Bachelor', 'Hospitality Management', '2005-09-01', '2009-06-01'),
    (9, 'Duke University', 'Master', 'Healthcare Management', '2010-09-01', '2012-06-01'),
    (10, 'MIT', 'Doctorate', 'Artificial Intelligence', '2012-09-01', '2016-06-01');

INSERT INTO Notification (UserID, Content, Date, Type) VALUES
    (1, 'Notification for John', '2023-01-01', 'Message'),
    (2, 'Notification for Jane', '2023-02-01', 'Connection'),
    (3, 'Notification for Alice', '2023-03-01', 'Endorsement'),
    (4, 'Notification for Bob', '2023-04-01', 'Post'),
    (5, 'Notification for Charlie', '2023-05-01', 'Like'),
    (6, 'Notification for David', '2023-06-01', 'Comment'),
    (7, 'Notification for Eva', '2023-07-01', 'Share'),
    (8, 'Notification for Frank', '2023-08-01', 'Mention'),
    (9, 'Notification for Grace', '2023-09-01', 'Tag'),
    (10, 'Notification for Henry', '2023-10-01', 'Follow'),
    (1, 'New endorsement for John', '2023-01-02', 'Endorsement'),
    (2, 'New comment on Janes post', '2023-02-02', 'Comment'),
    (3, 'Jane liked Alices post', '2023-03-02', 'Like'),
    (4, 'Bob shared your post', '2023-04-02', 'Share'),
    (5, 'Charlie mentioned you in a comment', '2023-05-02', 'Mention'),
    (6, 'David tagged you in a post', '2023-06-02', 'Tag'),
    (7, 'Eva followed you', '2023-07-02', 'Follow'),
    (8, 'New connection request from Frank', '2023-08-02', 'Connection'),
    (9, 'Grace accepted your connection request', '2023-09-02', 'Connection'),
    (10, 'Henry sent you a message', '2023-10-02', 'Message');
