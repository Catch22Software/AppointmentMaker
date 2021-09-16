Title: Appointment Maker

Purpose: To add/delete/alter appointments and the corresponding customer records that are attached to the appointments.

Author: James Mills

Contact info: jmil698@student.wgu.edu

Application version: 1.0

Application date : 08/01/2021

IDE:  IntelliJ IDEA 2021.2.1 (Community Edition)
Build #IC-212.5080.55, built on August 23, 2021
Runtime version: 11.0.11+9-b1504.16 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Windows 10 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 1776M
Cores: 8
Non-Bundled Plugins: Key Promoter X (2021.2), ski.chrzanow.foldableprojectview (1.1.1), mobi.hsz.idea.gitignore (4.2.0), org.jetbrains.kotlin (212-1.5.30-release-409-IJ4638.7)
Kotlin: 212-1.5.30-release-409-IJ4638.7

JDK version: 11.0.11

JavaFX version: JavaFX-SDK 11.0.2

Directions: 
	User must enter username and password to log into system. Log in screen displays the system default Zone ID. Program is presented in the current system default timezone and will also change to french for the login prompts if the system default language is set to French. Any login attempts, either successful or not, are recorded in a text log named "login_activity.txt" . If user has any appointments within 15 minutes of login time, a custom prompt is displayed showing those appointments, otherwise it will just be a blank greeting. After login process. User is presented with a screen showing all appointments in the system. User is greeted at the top of the screen with their appropriate username. There are filters to show appointments via Year, Month and Year, Week or All appointments. From the main screen, user can choose to go to Customer Screen, Appointment Setting Screen, Generate reports, or User Maintenance ( if user is admin ). 

	User has a menu access that allows going between screens at any time during the application usage and also has a logout option that brings back to the login screen as well. User can also choose the "About this program" for more info about the program. User must log in in order to access any other screens of the application.

	If the user is admin, the user maintenance screen can be accessed to add users, change passwords for existing users or delete users. Users cannot be deleted if they have any appointments assigned to them. All new users must have unique ( case insensitive ) usernames. Also the database uses a password hashing system to store the plain text password securely.

	In the Customer screen, User can add, edit or delete Customer records in the database. A customer cannot be deleted if they have appointments assigned to them. New customers also have a country and division assigned to them, user must select country first which will populate the choices for division. For example, in the US, US would be country and the state itself would be the division. The customer address field will contain the city or area name. When the user adds a new customer or changes an existing customer, they will be not allowed to make duplicate customers, which would have every field be equal ( case insensitive ) excluding the Customer ID field which is auto populated and disabled.

	In the Appointment setting screen, user can add, edit or delete Appointment records in the database. When an appointment is added or changed, the appointment times are first checked against the database appointments and then against the business hours of the business ( Monday - Sunday 8AM-10PM ) . All times displayed are relative to the user's default timezone and adjusted accordingly. When appointments are created, or updated, the user can assign them to another user. 

	When the reports screen is opened, the user is presented with three different choices. User can click cancel to go back to the screen before choosing the report option.

#1   Appointment Types

	User is presented with a pop up showing total number of appointments and also has them separated by type as well. User can filter the amount of appointments that are contributing to the counts by year, month and year, week or all appointments. Once window is closed, user is back to the report choice option screen to choose another if needed.

#2 Appointments by Contact

	User is presented with an option to choose which Contact to run the report for, then is presented with a table of all of the appointments assigned to the selected contact. Once window is closed, user is back to the report choice option screen to choose another if needed.

#3 Appointment Alterations  **ADDITIONAL REPORT OPTION**

	User is presented with a table of all of the appointments either created by or updated by another user that was assigned to them. For example the "test" username can see all appointments that were either created by and or updated by "admin" username or any other and that were assigned to "test" for the user ID field. Once window is closed, user is back to the report choice option screen to choose another if needed.

MySQL connector used: mysql-connector-java-8.0.26

EXTRA: login_activity.txt is located in appointmentmaker.Utility/Logs

TO switch to local defined mysql database, utiliize the SQL script within the backup folder and change the DataSource properties to the other properties file.


