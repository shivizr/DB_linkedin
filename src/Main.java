import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:DataBase2.db";
        Scanner scanner = new Scanner(System.in);
        boolean Login = false;
        int currentID = -1;
        // Establish database connection
        try (Connection connection = DriverManager.getConnection(url)) {

            while (!Login) {
                System.out.println("Welcome!\n1-login\n2-sign up");
                int enterNum = scanner.nextInt();
                scanner.nextLine();
                if (enterNum == 1) {
                    System.out.println("Enter your ID:");
                    int userID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    if (login(connection, userID, password)) {
                        System.out.println("Login successful.");
                        currentID = userID;
                        Login = true;
                    } else {
                        System.out.println("Wrong ID or password.");
                    }
                } else if (enterNum == 2) {
                    System.out.println("Enter ID:");
                    int ID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter firstname:");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter lastname:");
                    String lastname = scanner.nextLine();
                    System.out.println("Enter email:");
                    String email = scanner.nextLine();
                    System.out.println("Enter Password:");
                    String password = scanner.nextLine();
                    System.out.println("Enter you Job:");
                    String Job = scanner.nextLine();
                    System.out.print("Enter the year of birth: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter the month of birth: ");
                    int month = scanner.nextInt();
                    System.out.print("Enter the day of birth: ");
                    int day = scanner.nextInt();
                    scanner.nextLine();
                    String dateOfBirth = String.format("%04d-%02d-%02d", year, month, day); // Format as YYYY-MM-DD
                    System.out.println("Enter gender (male/female/other):");
                    String gender = scanner.nextLine();
                    System.out.println("Enter Country:");
                    String country = scanner.nextLine();
                    System.out.println("Enter City:");
                    String city = scanner.nextLine();
                    signUp(connection, ID, firstName, lastname, email, password, Job, dateOfBirth, gender, country, city);
                    currentID = ID;
                    Login = true;
                }
                if (Login && !userHasProfile(connection, currentID)) {
                    System.out.println("You don't have a profile. Do you want to create your profile now? (yes/no)");
                    String createProfileChoice = scanner.nextLine();
                    if (createProfileChoice.equalsIgnoreCase("yes")) {
                        createProfile(connection, currentID, scanner);
                    }
                }
            }

            while (true) {
                System.out.println("Home");
                showNotifications(connection, currentID);
                checkAndNotifyBirthdays(connection, currentID);
                System.out.println("1.View posts by network\n2.View liked posts by mynetwork\n3.View commented posts by mynetwork\n4.SearchUsers\n5.Settings\n6.MyDirect");
                System.out.println("7.My network\n8.Add posts\n9.Profile\n10.Manage connection requests\n11.Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewpostsby_mynetwork(connection, currentID, scanner);
                        break;
                    case 2:
                        view_liked_posts_by_network(connection, currentID, scanner);
                        break;
                    case 3:
                        view_commented_posts_by_mynetwork(connection, currentID, scanner);
                        break;
                    case 4:
                        System.out.println("Search by:1.location 2.language 3.company 4.firstName/LastName");
                        int searchChoice = scanner.nextInt();
                        scanner.nextLine();
                        String keyword = "";
                        switch (searchChoice) {
                            case 1:
                                System.out.println("Enter location:");
                                keyword = scanner.nextLine();
                                searchUsers(connection, "location", keyword, currentID, scanner);
                                break;
                            case 2:
                                System.out.println("Enter language:");
                                keyword = scanner.nextLine();
                                searchUsers(connection, "language", keyword, currentID, scanner);
                                break;
                            case 3:
                                System.out.println("Enter company:");
                                keyword = scanner.nextLine();
                                searchUsers(connection, "company", keyword, currentID, scanner);
                                break;
                            case 4:
                                System.out.println("Enter first name or last name:");
                                keyword = scanner.nextLine();
                                searchUsers(connection, "name", keyword, currentID, scanner);
                                break;
                            default:
                                System.out.println("Option is not available.");
                                break;
                        }
                        break;
                    case 5:
                        setting_menu(connection, currentID, scanner);
                        break;
                    case 6:
                        myDirect(connection, currentID, scanner);
                        break;
                    case 7:
                        myNetwork(connection, currentID, scanner);
                        break;
                    case 8:
                        addPost(connection, currentID, scanner);
                        break;
                    case 9:
                        viewprofile(connection, currentID);
                        break;
                    case 10:
                        manage_connection_requests(connection, currentID, scanner);
                        break;
                    case 11:
                        System.out.println("Exit:)");
                        return;
                    default:
                        System.out.println("Option is not available.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void manage_connection_requests(Connection connection, int currentUserID, Scanner scanner) throws SQLException {
        String sql = "SELECT UserID1, UserID2 FROM ConnectionReq WHERE UserID2 = ? AND Status = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Connection requests list:");
            while (rs.next()) {
                int requesterID = rs.getInt("UserID1");
                String requesterName = getUserName(connection, requesterID);
                System.out.println("Connection request from: " + requesterName + " (UserID: " + requesterID + ")");
                System.out.println("1.Accept");
                System.out.println("2.Decline");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    acceptConnectionRequest(connection, currentUserID, requesterID);
                } else if (choice == 2) {
                    declineConnectionRequest(connection, currentUserID, requesterID);
                }
            }
        }
    }

    public static void createProfile(Connection connection, int userID, Scanner scanner) throws SQLException {
        System.out.println("Enter your intro:");
        String intro = scanner.nextLine();
        System.out.println("Enter more_info:");
        String moreInfo = scanner.nextLine();
        System.out.println("Enter accomplishments:");
        String accomplishment = scanner.nextLine();
        System.out.println("Enter contact info:");
        String contactInfo = scanner.nextLine();
        System.out.println("Enter background:");
        String background = scanner.nextLine();
        System.out.println("Enter languages:");
        String language = scanner.nextLine();

        String sql = "INSERT INTO Profile (UserID, Intro, More_Info, Accomplishment, ContactInfo, Background, Language) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, intro);
            stmt.setString(3, moreInfo);
            stmt.setString(4, accomplishment);
            stmt.setString(5, contactInfo);
            stmt.setString(6, background);
            stmt.setString(7, language);
            stmt.executeUpdate();
            System.out.println("Your profile created successfully:)go check it.");
        }
    }

    public static boolean userHasProfile(Connection connection, int userID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Profile WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public static void viewprofile(Connection connection, int currentUserID) throws SQLException {
        String sql = """
                    SELECT u.FirstName, u.LastName, u.Email, u.Country, u.City, u.DateOfBirth, u.Job, p.Intro, p.More_Info, p.Accomplishment, p.ContactInfo, p.Background, p.Language
                    FROM MAIN_USER u
                    LEFT JOIN Profile p ON u.UserName = p.UserID
                    WHERE u.UserName = ?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String country = rs.getString("Country");
                String city = rs.getString("City");
                String dateOfBirth = rs.getString("DateOfBirth");
                String job = rs.getString("Job");
                String intro = rs.getString("Intro");
                String moreInfo = rs.getString("More_Info");
                String accomplishment = rs.getString("Accomplishment");
                String contactInfo = rs.getString("ContactInfo");
                String background = rs.getString("Background");
                String language = rs.getString("Language");
                //print details of build porfile
                System.out.println(firstName + " " + lastName);
                System.out.println("Email:" + email);
                System.out.println("Country:" + country);
                System.out.println("City:" + city);
                System.out.println("Date of Birth:" + dateOfBirth);
                System.out.println("Job Title:" + job);
                System.out.println("Intro:" + intro);
                System.out.println("More_Info:" + moreInfo);
                System.out.println("Accomplishment:" + accomplishment);
                System.out.println("Contact Info:" + contactInfo);
                System.out.println("Background:" + background);
                System.out.println("Language:" + language);
                viewUserPosts(connection, currentUserID);
            }
        }
    }

    public static void viewUserPosts(Connection connection, int userID) throws SQLException {
        String sql = "SELECT * FROM Post WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("User's Posts:");
            while (rs.next()) {
                int postID = rs.getInt("ID");
                String content = rs.getString("Content");
                String postDate = rs.getString("PostDate");
                System.out.println("post with Id:" + postID + "And content:" + content + "has posted on date: " + postDate);
            }
        }
    }

    public static void addPost(Connection connection, int userID, Scanner scanner) throws SQLException {
        System.out.println("Enter post content:");
        String content = scanner.nextLine();
        String uniqueLink = UUID.randomUUID().toString();
        String sql = "INSERT INTO Post (UserID, Content, UniqueLink) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, content);
            stmt.setString(3, uniqueLink);
            stmt.executeUpdate();
            System.out.println("Your post added to database:).");
        }
    }

    public static void setting_menu(Connection connection, int userID, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Settings\n1.Change Job Status:\n2.Previous");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter your new job title:");
                    String newJob = scanner.nextLine();
                    changeJobStatus(connection, userID, newJob);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Option is not available.");
                    break;
            }
        }
    }

    public static void myNetwork(Connection connection, int currentUserID, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("My Network \n1.View Connection Requests\n2.MyNetwork\n3.Previous page");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewConnectionRequests(connection, currentUserID, scanner);
                    break;
                case 2:
                    showNetworkUsers(connection, currentUserID);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Option is not available.");
                    break;
            }
        }
    }

    public static void viewConnectionRequests(Connection connection, int userID, Scanner scanner) throws SQLException {
        String sql = "SELECT UserID1 FROM ConnectionReq WHERE UserID2 = ? AND Status = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Connection requests lists :");
            while (rs.next()) {
                int requesterID = rs.getInt("UserID1");
                String requesterName = getUserName(connection, requesterID);
                System.out.println("connection request from: " + requesterName);
                System.out.println("1.Accept");
                System.out.println("2.Decline");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    acceptConnectionRequest(connection, userID, requesterID);
                } else if (choice == 2) {
                    declineConnectionRequest(connection, userID, requesterID);
                }
            }
        }
    }

    public static void acceptConnectionRequest(Connection connection, int userID, int requesterID) throws SQLException {
        String sql = "UPDATE ConnectionReq SET Status = 1 WHERE UserID1 = ? AND UserID2 = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requesterID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            System.out.println("connection request accepted.");
        }
        String notificationSql = "DELETE FROM Notification WHERE UserID = ? AND Content LIKE ?";
        try (PreparedStatement notificationStmt = connection.prepareStatement(notificationSql)) {
            notificationStmt.setInt(1, userID);
            notificationStmt.setString(2, "%connection request from userID: " + requesterID + "%");
            notificationStmt.executeUpdate();
//            System.out.println("Notification of request accepted.");
        }
    }

    public static void declineConnectionRequest(Connection connection, int userID, int requesterID) throws SQLException {
        String sql = "DELETE FROM ConnectionReq WHERE UserID1 = ? AND UserID2 = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, requesterID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            System.out.println("Connection request declined.");
        }
        String notificationSql = "DELETE FROM Notification WHERE UserID = ? AND Content LIKE ?";
        try (PreparedStatement notificationStmt = connection.prepareStatement(notificationSql)) {
            notificationStmt.setInt(1, userID);
            notificationStmt.setString(2, "%connection request from userID: " + requesterID + "%");
            notificationStmt.executeUpdate();
//            System.out.println("Notification of declining connection request");
        }
    }

    public static void changeJobStatus(Connection connection, int userID, String newJob) throws SQLException {
        String sql = "UPDATE MAIN_USER SET Job = ? WHERE UserName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newJob);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            notifyUserConnectionsAboutJobChange(connection, userID, getUserName(connection, userID), newJob);
            System.out.println("job title changes to:" + newJob);
        }
    }

    public static boolean login(Connection connection, int userID, String password) throws SQLException {
        String sql = "SELECT * FROM MAIN_USER WHERE UserName = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public static void signUp(Connection connection, int userID, String firstName, String lastName, String email, String password, String job, String dateOfBirth, String gender, String country, String city) throws SQLException {
        String sql = "INSERT INTO MAIN_USER (UserName, FirstName, LastName, Email, Password, Job, DateOfBirth, Gender, Country, City) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, job);
            stmt.setString(7, dateOfBirth.toString());
            stmt.setString(8, gender);
            stmt.setString(9, country);
            stmt.setString(10, city);
            stmt.executeUpdate();
            System.out.println("user signed up with id: " + userID);
        }
    }

    public static void searchUsers(Connection connection, String searchBy, String keyword, int currentUserID, Scanner scanner) throws SQLException {
        String sql = "";
        switch (searchBy) {
            case "location":
                sql = """
                            SELECT u.UserName, u.FirstName, u.LastName, u.Country, u.City, p.Language, c.Name AS CompanyName
                            FROM MAIN_USER u
                            JOIN Profile p ON u.UserName = p.UserID
                            JOIN Company_User cu ON u.UserName = cu.UserID
                            JOIN Company c ON cu.CompanyID = c.ID
                            WHERE u.City = ? OR u.Country = ?
                        """;
                break;
            case "language":
                sql = """
                            SELECT u.UserName, u.FirstName, u.LastName, u.Country, u.City, p.Language, c.Name AS CompanyName
                            FROM MAIN_USER u
                            JOIN Profile p ON u.UserName = p.UserID
                            JOIN Company_User cu ON u.UserName = cu.UserID
                            JOIN Company c ON cu.CompanyID = c.ID
                            WHERE p.Language = ?
                        """;
                break;
            case "company":
                sql = """
                            SELECT u.UserName, u.FirstName, u.LastName, u.Country, u.City, p.Language, c.Name AS CompanyName
                            FROM MAIN_USER u
                            JOIN Profile p ON u.UserName = p.UserID
                            JOIN Company_User cu ON u.UserName = cu.UserID
                            JOIN Company c ON cu.CompanyID = c.ID
                            WHERE c.Name = ?
                        """;
                break;
            case "name":
                sql = """
                            SELECT u.UserName, u.FirstName, u.LastName, u.Country, u.City, p.Language, c.Name AS CompanyName
                            FROM MAIN_USER u
                            JOIN Profile p ON u.UserName = p.UserID
                            JOIN Company_User cu ON u.UserName = cu.UserID
                            JOIN Company c ON cu.CompanyID = c.ID
                            WHERE u.FirstName = ? OR u.LastName = ?;
                        """;
                break;
            default:
                System.out.println("Option is not available.");
                return;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (searchBy.equals("location")) {
                stmt.setString(1, keyword);
                stmt.setString(2, keyword);
            } else {
                stmt.setString(1, keyword);
            }
            ResultSet rs = stmt.executeQuery();
            System.out.println("Search result:");
            while (rs.next()) {
                int userName = rs.getInt("UserName");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String country = rs.getString("Country");
                String city = rs.getString("City");
                String userLanguage = rs.getString("Language");
                String companyName = rs.getString("CompanyName");
                System.out.println("UserName: " + userName + ", Name: " + firstName + " " + lastName + ", Country: " + country + ", City: " + city + ", Language: " + userLanguage + ", Company: " + companyName);
            }

            System.out.println("enter id you want to view their profile:");
            int selectedUserID = scanner.nextInt();
            scanner.nextLine();
            viewProfile(connection, selectedUserID, currentUserID, scanner);
        }
    }


    public static void viewProfile(Connection connection, int userID, int currentUserID, Scanner scanner) throws SQLException {
        String sql = "SELECT * FROM MAIN_USER WHERE UserName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String country = rs.getString("Country");
                String city = rs.getString("City");
                String dateOfBirth = rs.getString("DateOfBirth");
                String gender = rs.getString("Gender");
                System.out.println("Profile of " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Country: " + country);
                System.out.println("City: " + city);
                System.out.println("Date of Birth: " + dateOfBirth);
                System.out.println("Gender: " + gender);
                System.out.println("Skills:");
                listSkills(connection, userID);
                String viewerName = getUserName(connection, currentUserID);
                notifyProfileView(connection, userID, viewerName);

                System.out.println("1.send connection request\n2.endorse skill\n3.previous page");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        sendConnectionRequest(connection, currentUserID, userID);
                        break;
                    case 2:
                        endorseSkill(connection, userID, currentUserID, scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Option not available");
                        break;
                }
            } else {
                System.out.println("user not found");
            }
        }
    }

    public static void listSkills(Connection connection, int userID) throws SQLException {
        String sql = "SELECT ID, Name FROM Skill WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int skillID = rs.getInt("ID");
                String skillName = rs.getString("Name");
                System.out.println("ID of skill: " + skillID + ", Skill: " + skillName);
            }
        }
    }

    public static void endorseSkill(Connection connection, int userID, int endorsedBy, Scanner scanner) throws SQLException {
        System.out.println("enter skill ID you want to endorse:");
        int skillID = scanner.nextInt();
        scanner.nextLine();
        String sql = "INSERT INTO Endorsement (SkillID, UserID, EndorsedBy) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, skillID);
            stmt.setInt(2, userID);
            stmt.setInt(3, endorsedBy);
            stmt.executeUpdate();
            System.out.println("Skill endorsed successfully.");

            String endorserName = getUserName(connection, endorsedBy);
            String skillName = getSkillName(connection, skillID);
            notifySkillEndorsement(connection, userID, endorserName, skillName);
//            System.out.println("Notification sends.");
        }
    }

    public static void notifySkillEndorsement(Connection connection, int userID, String endorserName, String skillName) throws SQLException {
        String content = endorserName + " endorsed your skill: " + skillName;
        createNotification(connection, userID, content, "SkillEndorsement");
    }

    public static String getSkillName(Connection connection, int skillID) throws SQLException {
        String sql = "SELECT Name FROM Skill WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, skillID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Name");
            }
        }
        return "";
    }

    public static void sendConnectionRequest(Connection connection, int currentUserID, int targetUserID) throws SQLException {
        String sql = "INSERT INTO ConnectionReq (UserID1, UserID2, Status) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            stmt.setInt(2, targetUserID);
            stmt.executeUpdate();
            notifyConnectionRequest(connection, targetUserID, getUserName(connection, currentUserID));
            System.out.println("Connection request sent.");
        }
    }

    public static void notifyConnectionRequest(Connection connection, int userID, String requesterName) throws SQLException {
        String content = "you have connection request from" + requesterName;
        createNotification(connection, userID, content, "ConnectionRequest");
    }

    public static String getUserName(Connection connection, int userID) throws SQLException {
        String sql = "SELECT FirstName, LastName FROM MAIN_USER WHERE UserName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("FirstName") + " " + rs.getString("LastName");
            }
        }
        return "";
    }

    public static void createNotification(Connection connection, int userID, String content, String type) throws SQLException {
        String sql = "INSERT INTO Notification (UserID, Content, Type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setString(2, content);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }

    public static void showNotifications(Connection connection, int userID) throws SQLException {
        String sql = "SELECT Content, Date, Type FROM Notification WHERE UserID = ? ORDER BY Date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Notifications:");
            while (rs.next()) {
                String content = rs.getString("Content");
                String dateString = rs.getString("Date");
                String type = rs.getString("Type");
                System.out.println("[" + dateString + "] (" + type + ") " + content);
            }
        }
    }

    public static void checkAndNotifyBirthdays(Connection connection, int currentUserID) throws SQLException {
        String sql = """
                    SELECT u.UserName, u.FirstName, u.LastName, u.DateOfBirth
                    FROM MAIN_USER u
                    JOIN ConnectionReq c ON (u.UserName = c.UserID1 OR u.UserName = c.UserID2)
                    WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.Status = 1
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            stmt.setInt(2, currentUserID);
            ResultSet rs = stmt.executeQuery();
            LocalDate today = LocalDate.now();
            while (rs.next()) {
                int friendID = rs.getInt("UserName");
                String friendName = rs.getString("FirstName") + " " + rs.getString("LastName");
                LocalDate birthDate = LocalDate.parse(rs.getString("DateOfBirth"));
                if (birthDate.getMonth() == today.getMonth() && birthDate.getDayOfMonth() == today.getDayOfMonth()) {
                    notifyBirthday(connection, friendID, friendName);
                }
            }
        }
    }

    public static void myDirect(Connection connection, int currentUserID, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Direct \n1.view conversations\n2.send message\n3.mark message as unread\n4.archive message\n5.filter conversations");
            System.out.println("6.search conversation\n7.unarchive message\n8.delete message\n9.previous page");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewConversations(connection, currentUserID);
                    break;
                case 2:
                    sendMessage(connection, currentUserID, scanner);
                    break;
                case 3:
                    System.out.println("Enter id of message you want to mark as unread:");
                    int messageID = scanner.nextInt();
                    markMessageAsUnread(connection, messageID);
                    break;
                case 4:
                    System.out.println("Enter id of message you want to to archive:");
                    int archiveMessageID = scanner.nextInt();
                    archiveMessage(connection, archiveMessageID);
                    break;
                case 5:
                    System.out.println("Filter by:1.Unread 2.Archived");
                    int filterChoice = scanner.nextInt();
                    filterConversations(connection, currentUserID, filterChoice);
                    break;
                case 6:
                    System.out.println("Enter word/part of word to search in conversations:");
                    String keyword = scanner.nextLine();
                    searchMessages(connection, keyword);
                    break;
                case 7:
                    System.out.println("Enter id of message you want to unarchive:");
                    int unarchiveMessageID = scanner.nextInt();
                    unarchiveMessage(connection, unarchiveMessageID);
                    break;
                case 8:
                    System.out.println("Enter id of message you want to delete:");
                    int deleteMessageID = scanner.nextInt();
                    deleteMessage(connection, deleteMessageID);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Option not vailabel");
                    break;
            }
        }
    }

    public static void viewConversations(Connection connection, int currentUserID) throws SQLException {
        String sql = "SELECT * FROM Message WHERE SenderID = ? OR ReceiverID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            stmt.setInt(2, currentUserID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Conversations:");
            while (rs.next()) {
                int messageID = rs.getInt("MessageID");
                int senderID = rs.getInt("SenderID");
                int receiverID = rs.getInt("ReceiverID");
                String content = rs.getString("Content");
                String sentDate = rs.getString("SentDate");
                boolean isRead = rs.getBoolean("IsRead");
                boolean isArchived = rs.getBoolean("IsArchived");
                System.out.println("MessageID: " + messageID + ", From: " + senderID + ", To: " + receiverID + ", Content: " + content + ", SentDate: " + sentDate + ", IsRead: " + isRead + ", IsArchived: " + isArchived);
            }
            markMessagesAsRead(connection, currentUserID);
        }
    }

    public static void markMessagesAsRead(Connection connection, int currentUserID) throws SQLException {
        String sql = "UPDATE Message SET IsRead = 1 WHERE ReceiverID = ? AND IsRead = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated + " messages marked as read.");
        }
    }

    public static void filterConversations(Connection connection, int currentUserID, int filterChoice) throws SQLException {
        String sql = "";
        switch (filterChoice) {
            case 1:
                sql = "SELECT * FROM Message WHERE (SenderID = ? OR ReceiverID = ?) AND IsRead = 0";
                break;
            case 2:
                sql = "SELECT * FROM Message WHERE (SenderID = ? OR ReceiverID = ?) AND IsArchived = 1";
                break;
            default:
                System.out.println("Option not available.");
                return;
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            stmt.setInt(2, currentUserID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Filtered Conversations:");
            while (rs.next()) {
                int messageID = rs.getInt("MessageID");
                int senderID = rs.getInt("SenderID");
                int receiverID = rs.getInt("ReceiverID");
                String content = rs.getString("Content");
                String sentDate = rs.getString("SentDate");  // Read as string
                boolean isRead = rs.getBoolean("IsRead");
                boolean isArchived = rs.getBoolean("IsArchived");
                System.out.println("MessageID: " + messageID + ", From: " + senderID + ", To: " + receiverID + ", Content: " + content + ", SentDate: " + sentDate + ", IsRead: " + isRead + ", IsArchived: " + isArchived);
            }
        }
    }


    public static void unarchiveMessage(Connection connection, int messageID) throws SQLException {
        String sql = "UPDATE Message SET IsArchived = 0 WHERE MessageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageID);
            stmt.executeUpdate();
            System.out.println("Message with ID: " + messageID + " unarchived.");
        }
    }

    public static void searchMessages(Connection connection, String keyword) throws SQLException {
        String sql = "SELECT * FROM Message WHERE Content LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int messageID = rs.getInt("MessageID");
                int senderID = rs.getInt("SenderID");
                int receiverID = rs.getInt("ReceiverID");
                String content = rs.getString("Content");
                String sentDate = rs.getString("SentDate");
                System.out.println("messageID: " + messageID + "from user: " + senderID + ",to user: " + receiverID + ",with content: " + content + "in the date: " + sentDate);
            }
        }
    }

    public static void viewpostsby_mynetwork(Connection connection, int userID, Scanner scanner) throws SQLException {
        String sql = """
                    SELECT p.ID, p.Content 
                    FROM Post p
                    JOIN ConnectionReq c ON (p.UserID = c.UserID1 OR p.UserID = c.UserID2)
                    WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.Status = 1
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int postID = rs.getInt("ID");
                String content = rs.getString("Content");
                System.out.println("Post ID: " + postID + ", Content: " + content);
                System.out.println("Options:\n1.like post\n2.view comments\n3.add comment\n4.share post\n5.view likes\n6-previous page");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        likePost(connection, postID, userID);
                        break;
                    case 2:
                        viewComments(connection, postID, userID, scanner);
                        break;
                    case 3:
                        System.out.println("Enter your comment:");
                        String comment = scanner.nextLine();
                        addComment(connection, postID, userID, comment);
                        break;
                    case 4:
                        sharePost(connection, postID, userID, scanner);
                        break;
                    case 5:
                        viewLikes(connection, postID);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Option not available");
                        break;
                }
            }
        }
    }

    public static void view_liked_posts_by_network(Connection connection, int userID, Scanner scanner) throws SQLException {
        String sql = """
                    SELECT p.ID, p.Content 
                    FROM Post p
                    JOIN Likes l ON p.ID = l.PostID
                    JOIN ConnectionReq c ON (l.UserID = c.UserID1 OR l.UserID = c.UserID2)
                    WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.Status = 1
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int postID = rs.getInt("ID");
                String content = rs.getString("Content");
                System.out.println("Liked Post ID: " + postID + ", Content: " + content);
                System.out.println("Options:\n1.like post\n2.view comments\n3.add comment\n4.share post\n5.view likes\n6.previous page");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        likePost(connection, postID, userID);
                        break;
                    case 2:
                        viewComments(connection, postID, userID, scanner);
                        break;
                    case 3:
                        System.out.println("Enter your comment:");
                        String comment = scanner.nextLine();
                        addComment(connection, postID, userID, comment);
                        break;
                    case 4:
                        sharePost(connection, postID, userID, scanner);
                        break;
                    case 5:
                        viewLikes(connection, postID);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Option not available");
                        break;
                }
            }
        }
    }


    public static void sharePost(Connection connection, int postID, int senderID, Scanner scanner) throws SQLException {
        showNetworkUsers(connection, senderID);
        System.out.println("enter the ID of the user you want to share the post with:");
        int receiverID = scanner.nextInt();
        scanner.nextLine();
        if (isUserInNetwork(connection, senderID, receiverID)) {
            String sql = "SELECT Content FROM Post WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, postID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String content = rs.getString("Content");
                    String message = "shared post: " + content;
                    String messageSql = "INSERT INTO Message (SenderID, ReceiverID, Content) VALUES (?, ?, ?)";
                    try (PreparedStatement messageStmt = connection.prepareStatement(messageSql)) {
                        messageStmt.setInt(1, senderID);
                        messageStmt.setInt(2, receiverID);
                        messageStmt.setString(3, message);
                        messageStmt.executeUpdate();
                        System.out.println("post shared with user: " + receiverID);
                    }
                }
            }
        } else {
            System.out.println("The receiver is not in your network. post not shared.");
        }
    }

    public static boolean isUserInNetwork(Connection connection, int userID1, int userID2) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ConnectionReq WHERE " +
                "((UserID1 = ? AND UserID2 = ? AND Status = 1) OR " +
                "(UserID1 = ? AND UserID2 = ? AND Status = 1)) OR " +
                "(UserID2 IN (SELECT UserID2 FROM ConnectionReq WHERE UserID1 = ? AND Status = 1) AND UserID1 = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID1);
            stmt.setInt(2, userID2);
            stmt.setInt(3, userID2);
            stmt.setInt(4, userID1);
            stmt.setInt(5, userID1);
            stmt.setInt(6, userID2);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public static void viewComments(Connection connection, int postID, int userID, Scanner scanner) throws SQLException {
        String sql = "SELECT * FROM Comment WHERE (PostID = ? AND SecondId IS NULL) ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Comments:");
            while (rs.next()) {
                int commentID = rs.getInt("ID");
                String text = rs.getString("Text");
                int commentUserID = rs.getInt("UserID");
                int likeCount = getCommentLikeCount(connection, commentID);
                int commentCount = getCommentCount(connection, postID);
                String commentUserName = getUserName(connection, commentUserID);
                System.out.println("Comment ID: " + commentID + ", User: " + commentUserName + ", Comment: " + text);
                System.out.println("Options:\n1.like comment\n2.reply to comment\n3.view Comment Likes\n4.Num of Likes and Comments\n5.back to post");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        likeComment(connection, commentID, userID);
                        break;
                    case 2:
                        System.out.println("Enter your reply:");
                        String reply = scanner.nextLine();
                        addCommentToComment(connection, postID, userID, reply, commentID);
                        break;
                    case 3:
                        viewCommentLikes(connection, commentID);
                        break;
                    case 4:
                        System.out.println("num of likes" + likeCount);
                        System.out.println("num of comments:" + commentCount);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        }
    }

    public static int getCommentLikeCount(Connection connection, int commentID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Likes WHERE CommentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static int getCommentCount(Connection connection, int postID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comment WHERE PostID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static void viewLikes(Connection connection, int postID) throws SQLException {
        String sql = "SELECT UserID FROM Likes WHERE PostID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("likes from:");
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = getUserName(connection, userID);
                System.out.println("user: " + userName);
            }
        }
    }

    public static void viewCommentLikes(Connection connection, int commentID) throws SQLException {
        String sql = "SELECT UserID FROM Likes WHERE CommentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("likes from:");
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = getUserName(connection, userID);
                System.out.println("user: " + userName);
            }
        }
    }

    public static void likePost(Connection connection, int postID, int userID) throws SQLException {
        String sql = "INSERT INTO Likes (PostID, UserID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            System.out.println("post liked.");

            String getPostAuthorSQL = "SELECT UserID FROM Post WHERE ID = ?";
            try (PreparedStatement getAuthorStmt = connection.prepareStatement(getPostAuthorSQL)) {
                getAuthorStmt.setInt(1, postID);
                ResultSet rs = getAuthorStmt.executeQuery();
                if (rs.next()) {
                    int postAuthorID = rs.getInt("UserID");
                    if (postAuthorID != userID) {
                        String likerName = getUserName(connection, userID);
                        notifyPostLiked(connection, postAuthorID, likerName);
                    }
                }
            }
        }
    }

    public static void likeComment(Connection connection, int commentID, int userID) throws SQLException {
        String sql = "INSERT INTO Likes (CommentID, UserID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            System.out.println("comment liked.");

            String getCommentAuthor = "SELECT UserID, Text FROM Comment WHERE ID = ?";
            try (PreparedStatement getAuthorStmt = connection.prepareStatement(getCommentAuthor)) {
                getAuthorStmt.setInt(1, commentID);
                ResultSet rs = getAuthorStmt.executeQuery();
                if (rs.next()) {
                    int commentAuthorID = rs.getInt("UserID");
                    String commentContent = rs.getString("Text");
                    if (commentAuthorID != userID) {
                        String likerName = getUserName(connection, userID);
                        notifyReplyOrLikeComment(connection, commentAuthorID, likerName, commentContent);
                    }
                }
            }
        }
    }

    public static void addComment(Connection connection, int postID, int userID, String text) throws SQLException {
        String sql = "INSERT INTO Comment (PostID, UserID, Text) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            stmt.setInt(2, userID);
            stmt.setString(3, text);
            stmt.executeUpdate();
            System.out.println("comment added to postID: " + postID + " by userID: " + userID);
            String getPostAuthorSQL = "SELECT UserID, Content FROM Post WHERE ID = ?";
            try (PreparedStatement getAuthorStmt = connection.prepareStatement(getPostAuthorSQL)) {
                getAuthorStmt.setInt(1, postID);
                ResultSet rs = getAuthorStmt.executeQuery();
                if (rs.next()) {
                    int postAuthorID = rs.getInt("UserID");
                    String postContent = rs.getString("Content");
                    if (postAuthorID != userID) {
                        String commenterName = getUserName(connection, userID);
                        notifyCommentOnPost(connection, postAuthorID, commenterName, postContent);
                    }
                }
            }
        }
    }

    public static void addCommentToComment(Connection connection, int postID, int userID, String text, int parentCommentID) throws SQLException {
        String sql = "INSERT INTO Comment (PostID, UserID, SecondId, Text) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postID);
            stmt.setInt(2, userID);
            stmt.setInt(3, parentCommentID); //which is the second int in the database
            stmt.setString(4, text);
            stmt.executeUpdate();
            System.out.println("Reply added to commentID: " + parentCommentID + " on postID: " + postID + " by userID: " + userID);
            String getCommentAuthor = "SELECT UserID, Text FROM Comment WHERE ID = ?";
            try (PreparedStatement getAuthorStmt = connection.prepareStatement(getCommentAuthor)) {
                getAuthorStmt.setInt(1, parentCommentID);
                ResultSet rs = getAuthorStmt.executeQuery();
                if (rs.next()) {
                    int commentAuthorID = rs.getInt("UserID");
                    String commentContent = rs.getString("Text");
                    if (commentAuthorID != userID) {
                        String replierName = getUserName(connection, userID);
                        notifyReplyOrLikeComment(connection, commentAuthorID, replierName, commentContent);
                    }
                }
            }
        }
    }

    public static void notifyUserConnectionsAboutJobChange(Connection connection, int userID, String userName, String newJob) throws SQLException {
        String sql = "SELECT UserID1, UserID2 FROM ConnectionReq WHERE (UserID1 = ? OR UserID2 = ?) AND Status = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int connectionUserID1 = rs.getInt("UserID1");
                int connectionUserID2 = rs.getInt("UserID2");
                int notifyUserID = (connectionUserID1 == userID) ? connectionUserID2 : connectionUserID1;
                notifyJobChange(connection, notifyUserID, userName, newJob);
            }
        }
    }

    public static void notifyJobChange(Connection connection, int userID, String userName, String newJob) throws SQLException {
        String content = userName + " changed their job status to " + newJob;
        createNotification(connection, userID, content, "JobChange");
    }

    public static void notifyBirthday(Connection connection, int userID, String friendName) throws SQLException {
        String content = "It's " + friendName + "'s birthday.";
        createNotification(connection, userID, content, "Birthday");
    }

    public static void notifyProfileView(Connection connection, int userID, String viewerName) throws SQLException {
        String content = viewerName + " viewed your profile.";
        createNotification(connection, userID, content, "ProfileView");
    }

    public static void notifyPostLiked(Connection connection, int userID, String likerName) throws SQLException {
        String content = likerName + " liked your post.";
        createNotification(connection, userID, content, "PostLiked");
    }

    public static void notifyCommentOnPost(Connection connection, int userID, String commenterName, String postContent) throws SQLException {
        String content = commenterName + " commented on your post: " + postContent;
        createNotification(connection, userID, content, "CommentOnPost");
    }

    public static void notifyReplyOrLikeComment(Connection connection, int userID, String responderName, String commentContent) throws SQLException {
        String content = responderName + " replied to or liked your comment: " + commentContent;
        createNotification(connection, userID, content, "ReplyOrLikeComment");
    }

    public static void view_commented_posts_by_mynetwork(Connection connection, int userID, Scanner scanner) throws SQLException {
        String sql = """
                    SELECT DISTINCT p.ID, p.Content 
                    FROM Post p
                    JOIN Comment cmt ON p.ID = cmt.PostID
                    JOIN ConnectionReq c ON (cmt.UserID = c.UserID1 OR cmt.UserID = c.UserID2)
                    WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.Status = 1
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int postID = rs.getInt("ID");
                String content = rs.getString("Content");
                System.out.println("commented post ID: " + postID + "with content: " + content);

                System.out.println("Options:\n1.like post\n2.view comment\n3.Add comment\n4.share post\n5.view likes\n6.previous page");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        likePost(connection, postID, userID);
                        break;
                    case 2:
                        viewComments(connection, postID, userID, scanner);
                        break;
                    case 3:
                        System.out.println("enter comment you want to write:");
                        String comment = scanner.nextLine();
                        addComment(connection, postID, userID, comment);
                        break;
                    case 4:
                        sharePost(connection, postID, userID, scanner);
                        break;
                    case 5:
                        viewLikes(connection, postID);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Not available option");
                        break;
                }
            }
        }
    }

    public static void markMessageAsUnread(Connection connection, int messageID) throws SQLException {
        String sql = "UPDATE Message SET IsRead = 0 WHERE MessageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageID);
            stmt.executeUpdate();
            System.out.println("message with ID: " + messageID + " marked as unread.");
        }
    }

    public static void archiveMessage(Connection connection, int messageID) throws SQLException {
        String sql = "UPDATE Message SET IsArchived = 1 WHERE MessageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageID);
            stmt.executeUpdate();
            System.out.println("message with ID: " + messageID + " archived.");
        }
    }

    public static void deleteMessage(Connection connection, int messageID) throws SQLException {
        String sql = "DELETE FROM Message WHERE MessageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageID);
            stmt.executeUpdate();
            System.out.println("deleted message with ID: " + messageID);
        }
    }

    public static void showNetworkUsers(Connection connection, int currentUserID) throws SQLException {
        String sql = """
                    SELECT u.UserName, u.FirstName, u.LastName
                    FROM MAIN_USER u
                    JOIN ConnectionReq c ON (u.UserName = c.UserID1 OR u.UserName = c.UserID2)
                    WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.Status = 1
                    UNION
                    SELECT u.UserName, u.FirstName, u.LastName
                    FROM MAIN_USER u
                    JOIN ConnectionReq c1 ON (u.UserName = c1.UserID1 OR u.UserName = c1.UserID2)
                    JOIN ConnectionReq c2 ON (c1.UserID1 = c2.UserID1 OR c1.UserID1 = c2.UserID2 OR c1.UserID2 = c2.UserID1 OR c1.UserID2 = c2.UserID2)
                    WHERE (c2.UserID1 = ? OR c2.UserID2 = ?) AND u.UserName != ? AND u.UserName NOT IN (
                        SELECT UserID1 FROM ConnectionReq WHERE UserID2 = ? AND Status = 1
                        UNION
                        SELECT UserID2 FROM ConnectionReq WHERE UserID1 = ? AND Status = 1
                    )
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, currentUserID);
            stmt.setInt(2, currentUserID);
            stmt.setInt(3, currentUserID);
            stmt.setInt(4, currentUserID);
            stmt.setInt(5, currentUserID);
            stmt.setInt(6, currentUserID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Users in your network:");
            while (rs.next()) {
                int userID = rs.getInt("UserName");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                System.out.println("ID: " + userID + ", Name: " + firstName + " " + lastName);
            }
        }
    }

    public static void sendMessage(Connection connection, int senderID, Scanner scanner) throws SQLException {
        showNetworkUsers(connection, senderID);
        System.out.println("Enter the ID of the user you want to send a message to:");
        int receiverID = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.println("Enter your message:");
        String content = scanner.nextLine();

        if (isUserInNetwork(connection, senderID, receiverID)) {
            String sql = "INSERT INTO Message (SenderID, ReceiverID, Content) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, senderID);
                stmt.setInt(2, receiverID);
                stmt.setString(3, content);
                stmt.executeUpdate();
                System.out.println("Message sent from userID: " + senderID + " to userID: " + receiverID);
            }
        } else {
            System.out.println("The receiver is not in your network. Message not sent.");
        }
    }

}