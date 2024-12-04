import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:DataBase2.db";

        String[] queries = {
                "CREATE TABLE IF NOT EXISTS MAIN_USER (" +
                        "UserName INT PRIMARY KEY NOT NULL, " +
                        "FirstName VARCHAR(60), " +
                        "LastName VARCHAR(60), " +
                        "Email VARCHAR(150), " +
                        "Password VARCHAR(100), " +
                        "Job VARCHAR(300), " +
                        "DateOfBirth VARCHAR(200), " +
                        "Gender VARCHAR(6), " +
                        "Country VARCHAR(300), " +
                        "City VARCHAR(400))",

                "CREATE TABLE IF NOT EXISTS Profile (" +
                        "P_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID INT, " +
                        "Intro TEXT, " +
                        "More_Info TEXT, " +
                        "Accomplishment TEXT, " +
                        "ContactInfo VARCHAR(255), " +
                        "Background VARCHAR(255), " +
                        "Language VARCHAR(255), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Company (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID INT, " +
                        "Name VARCHAR(200) NOT NULL, " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Company_User (" +
                        "UserID INT NOT NULL, " +
                        "CompanyID INT NOT NULL, " +
                        "Start_Date VARCHAR(200), " +
                        "End_Date VARCHAR(200), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName), " +
                        "FOREIGN KEY (CompanyID) REFERENCES Company(ID))",

                "CREATE TABLE IF NOT EXISTS Skill (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "Name VARCHAR(100), " +
                        "UserID INT NOT NULL, " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Endorsement (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "SkillID INT, " +
                        "UserID INT, " +
                        "EndorsedBy INT, " +
                        "FOREIGN KEY (SkillID) REFERENCES Skill(ID), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName), " +
                        "FOREIGN KEY (EndorsedBy) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Post (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID INT, " +
                        "Content TEXT, " +
                        "UniqueLink VARCHAR(255) NOT NULL, " +
                        "PostDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Comment (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "PostID INT, " +
                        "UserID INT, " +
                        "SecondId INT," +
                        "Text TEXT, " +
                        "Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (PostID) REFERENCES Post(ID), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Likes (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "PostID INT, " +
                        "UserID INT, " +
                        "CommentID INT, " +
                        "FOREIGN KEY (PostID) REFERENCES Post(ID), " +
                        "FOREIGN KEY (CommentID) REFERENCES Comment(ID), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS ConnectionReq (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID1 INT, " +
                        "UserID2 INT, " +
                        "Status BOOLEAN, " +
                        "ConnectionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (UserID1) REFERENCES MAIN_USER(UserName), " +
                        "FOREIGN KEY (UserID2) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Message (" +
                        "MessageID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "SenderID INT, " +
                        "ReceiverID INT, " +
                        "Content TEXT, " +
                        "SentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "IsRead BOOLEAN DEFAULT 0, " +
                        "IsArchived BOOLEAN DEFAULT 0, " +
                        "FOREIGN KEY (SenderID) REFERENCES MAIN_USER(UserName), " +
                        "FOREIGN KEY (ReceiverID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Education (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID INT, " +
                        "UniversityName VARCHAR(300), " +
                        "Degree VARCHAR(100), " +
                        "Major VARCHAR(100), " +
                        "StartDate VARCHAR(200), " +
                        "EndDate VARCHAR(200), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))",

                "CREATE TABLE IF NOT EXISTS Notification (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "UserID INT, " +
                        "Content TEXT, " +
                        "Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "Type VARCHAR(50), " +
                        "FOREIGN KEY (UserID) REFERENCES MAIN_USER(UserName))"
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            for (String query : queries) {
                stmt.execute(query);
            }
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
