# To-Do List Application

## Description

A comprehensive task management application built with JavaFX that allows users to create, manage, and track their tasks with features like categorization, prioritization, notifications, and collaborative sharing. The application provides both a graphical user interface and a console-based interface for maximum flexibility.

## Features

### Core Functionality
- **User Authentication**: Secure registration and login system
- **Task Management**: Complete CRUD operations (Create, Read, Update, Delete) for tasks
- **Advanced Filtering**: Filter tasks by priority, category, date, and status
- **Notes System**: Create and manage personal notes
- **Notification System**: Automatic reminders and task-related notifications
- **Task Sharing**: Invite other users to view your tasks
- **Reward System**: Earn coins for completing tasks
- **Dashboard**: Visual statistics and charts for task overview
- **Profile Management**: Update user information and settings

### Task Features
- Priority levels: Low, Medium, High
- Categories: Routine, Work, Study, Sport, Other
- Status tracking: To Do, In Progress, Completed
- Due date management
- Task completion rewards

### Collaboration
- Invite users by email to share tasks
- View tasks shared by others
- Manage sharing permissions

## Technologies Used

- **Java**: Core programming language
- **JavaFX**: GUI framework for rich desktop application
- **MySQL**: Relational database for data persistence
- **JDBC**: Database connectivity
- **CSS**: Styling for JavaFX components

## Prerequisites

- Java JDK 8 or higher
- MySQL Server 5.7 or higher
- JavaFX SDK 11 or higher
- MySQL Connector/J (JDBC driver)

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd to-do-list
```

### 2. Database Setup
1. Install and start MySQL Server
2. Create a database named `project`:
   ```sql
   CREATE DATABASE project;
   ```
3. The application will automatically create required tables on first run

### 3. JavaFX Setup
1. Download JavaFX SDK from [official website](https://openjfx.io/)
2. Extract to a directory (e.g., `C:\javafx\javafx-sdk-23.0.2\`)
3. Update the path in `compile.bat` if necessary

### 4. Database Configuration
Update database credentials in `src/Model/DatabaseManager.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/project?characterEncoding=UTF-8";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

## Build and Run

### Using Batch File (Windows)
1. Compile the application:
   ```bash
   compile.bat
   ```

2. Run the GUI application:
   ```bash
   java -cp "bin;C:\path\to\javafx-sdk\lib\*" main.Main
   ```

### Manual Compilation
```bash
# Set JavaFX path
export JAVAFX_LIB="path/to/javafx-sdk/lib/*"

# Compile
javac -d bin -cp $JAVAFX_LIB src/util/*.java src/Controller/*.java src/view/*.java src/Model/*.java

# Run
java -cp "bin:$JAVAFX_LIB" main.Main
```

## Usage

### Getting Started
1. **Registration**: Create a new account with your personal information
2. **Login**: Access your account using email and password

### Task Management
1. **Create Tasks**: Use the "Add Task" button to create new tasks with:
   - Title and description
   - Priority level
   - Category
   - Due date

2. **View Tasks**: Dashboard displays active tasks with filtering options
3. **Complete Tasks**: Mark tasks as completed to earn coins
4. **Edit/Delete**: Modify or remove existing tasks

### Advanced Features
- **Filter Tasks**: Sort by priority, category, date, or status
- **Notes**: Create personal notes for additional organization
- **Notifications**: View system notifications and reminders
- **Sharing**: Invite other users to view your tasks
- **Statistics**: Monitor task completion with visual charts

### Console Version
The application also includes a console-based version accessible through `src/main/Main.java` for users who prefer command-line interface.

## Database Schema

The application uses the following database tables:

- **users**: User account information
- **tasks**: Task data with relationships to users
- **notes**: User-created notes
- **notification**: System and reminder notifications
- **task_shares**: Task sharing relationships between users

## Project Structure

```
to-do-list/
├── src/
│   ├── main/
│   │   └── Main.java              # Console application entry point
│   ├── Controller/                # JavaFX controllers
│   ├── Model/                     # Data models and database logic
│   ├── util/                      # Utility classes
│   └── view/                      # FXML files and UI resources
├── compile.bat                    # Build script
└── README.md                      # This file
```

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add comments for complex logic
- Test database operations thoroughly
- Maintain MVC architecture pattern

## Troubleshooting

### Common Issues
- **JavaFX Path Error**: Ensure JavaFX SDK path is correctly set in compile.bat
- **Database Connection**: Verify MySQL server is running and credentials are correct
- **Missing Dependencies**: Download required JDBC driver for MySQL

### Build Errors
- Clear the `bin` directory and recompile
- Check Java version compatibility
- Ensure all source files are present

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Future Enhancements

- [ ] Mobile application version
- [ ] Cloud synchronization
- [ ] Advanced reporting and analytics
- [ ] Integration with calendar applications
- [ ] Multi-language support

## Contact

For questions, bug reports, or feature requests:
- Create an issue on GitHub
- Email: [younesmessekher792@gmail.com]

---

**Note**: This application is designed for personal and small team task management. For enterprise use, consider additional security and scalability measures.
