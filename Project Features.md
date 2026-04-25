# Introduction:
- The Clear Day Application is an Android-based system designed to organize and manage projects and tasks efficiently. The application allows users to create projects, add tasks, and track progress in a structured and user-friendly manner.

- The system provides different user roles, where the administrator (Admin) can manage and oversee projects, while users can create, update, and manage tasks, as well as upload files related to each task or project. The application also supports workflow tracking and enhances collaboration among team members.

- This application aims to simplify task management processes, reduce complexity in organizing work, and improve overall productivity and efficiency.
---

## Techniques Used:

- Java
- XML
- Firebase Store
- SharedPreferences
- Cloudinary 
- Firebase Authentication
- Material Design Components

---

## Main Features:
1. Profile Features:
   
The application provides a profile feature that allows users to edit their personal information, including their name and email address, as well as update their profile picture. After making changes, users can save the updated information by clicking the "Save" button, which stores the new data successfully.

    - View Profile:
    Allows users to view their personal information, 
    including name, email, and profile picture.
    Data is retrieved from Firebase Firestore.
---
    - Edit Profile Information:
    Enables users to update their personal details such
    as name and email. Input validation is handled before
    saving the changes to Firebase Firestore.
---
    - Update Profile Picture:
    Users can upload or change their profile image. 
    The image is stored using Cloudinary , and its reference 
    is saved in Firestore.
---
    - Save Profile Changes:
    After editing, users can save their updated information
    by clicking the "Save" button. All changes are stored and 
    synchronized using Firebase.
---
    - Local Storage (Session Management): 
    Basic user data (e.g., login session) can be stored 
    locally using SharedPreferences to improve performance 
    and user experience.



2. Project Features :
   
This feature involves implementing the project management functionality within the application, allowing users to create, update, view, and delete projects. The logic is responsible for handling project-related data, including project title, description, deadlines, and associated tasks. It also ensures proper input validation and organizes projects in a structured and user-friendly way.
Additionally, the system allows the administrator (Admin) to send project invitations to users. Once an invitation is sent, the user receives it in a dedicated interface where they can review and accept the invitation. After accepting, the user gains full access to the project and its associated tasks, enabling effective collaboration.
The feature communicates with Firebase (Firestore) to store and retrieve project data, providing real-time synchronization across devices. In addition, it manages user interactions and state changes efficiently to ensure a smooth project management experience within the application

    - Create Project:
    Allows users to create a new project by entering details 
    such as title, description, and deadline. The system validates
    the input before storing the project data in Firebase Firestore.
---
    - Update Project:
    Enables users to edit project information. 
    All updates are saved and synchronized in real-time using
    Firebase Firestore.
---
    - Delete Project:
    Allows users to delete projects that are no longer needed.
    The project and its related data are removed from Firebase.
---
    - View Projects:
    Users can view all available projects. 
    Project data is retrieved dynamically from 
    Firebase Firestore.
---
    - Manage Project Tasks:
    Each project contains associated tasks, 
    which are linked and organized within the project 
    structure using Firebase Firestore.
---
    - Send Project Invitations:
    The administrator can send invitations to users to join a project. 
    Invitation data is stored and managed using Firebase.
---
    - Receive and Accept Invitations:
    Invited users can view invitations in a dedicated interface and accept them.
    Once accepted, access permissions are updated in Firebase, granting the user
    full access to the project.
---
    - User Access Control:
    The system manages user roles and permissions (Admin/User) 
    using Firebase, ensuring secure and controlled access to project data.
---
    - Real-time Synchronization:
    All project-related data is synchronized across devices instantly 
    using Firebase Firestore, ensuring consistency and up-to-date information.
---
    - User Interaction Management:
    The system handles user actions and state changes efficiently to provide 
    a smooth and responsive project management experience.




4. Task Features :
   
This feature is responsible for implementing the task management functionality within the application, allowing users to create, update, track, and organize tasks efficiently. Each task includes essential attributes such as title, description, priority level, and deadlines, enabling better organization and time management.
The system handles input validation and supports assigning tasks to specific projects or users. It also allows users to update task statuses (Urgent,Running ,On going) to clearly track progress.
In addition, users can attach files or images to tasks, making it easier to share relevant resources and information. The feature also supports sending invitations to users to join projects directly within the task workflow, enhancing collaboration among team members.
The application integrates with Firebase (Firestore) to store and retrieve task data, ensuring real-time synchronization across devices. It also manages user interactions, notifications, and proper state handling to provide a smooth and seamless task management experience.

    - Create Task:
    Allows users to create a new task by entering required details such as title,
    description, priority level, and deadline. 
    The system validates the input before saving the task using Firebase Firestore.
---
    - Update Task:
    Enables users to modify existing task details. All updates are stored and
    synchronized in real-time through Firebase Firestore,
    ensuring consistency across devices.
---
    - Delete Task:
    Allows users to delete tasks that are no longer needed. The task is removed
    directly from Firebase, keeping the database clean and updated.
---
    - View Tasks:
    Users can view all tasks related to a specific project. 
    Task data is retrieved dynamically from Firebase Firestore.

