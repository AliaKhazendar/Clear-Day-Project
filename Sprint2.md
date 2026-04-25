
# Clear Day —Sprint 2

| **Sprint Number** | 2 |
| --- | --- |
| **Start Date** | 01-03-2026 |
| **Team Capacity** | ~20 hours |
| **Sprint 2 Goal**
 | Users can view all their projects in a RecyclerView on the Home screen, create new projects, view project details, and update or delete projects — all synced with Firebase Firestore. |
| **Duration** | 2 Weeks |
| **End Date** | 12-03-2026 |
| **Story Points** | 16 Points |

---

### **Story #US-06: View All Projects (Home Screen)**

| **Story Points: 5** | Priority: Must-Have | Assigned to: Ragade |
| --- | --- | --- |

**Description:**

As a user (Team Leader / Student), I want to see all my projects listed on the Home screen in a RecyclerView so that I can quickly access any project.

**Task:**

- [ ]  Design Home Activity XML layout with Recycler View + FAB (Floating Action Button) + bottom navigation - Ragade
- [ ]  Create Project Adapter for Recycler View - Ragade
- [ ]  Create Project model class (id, name, description, ownerId, status, project member list, task list, createdAt) - Ragade
- [ ]  Observe LiveData in Home Activity and update Recycler View in home fragment - Ragade
- [ ]  Show loading indicator while fetching from Firestore - Ragade

Acceptance Criteria:

- [ ]  Home screen shows all user projects in RecyclerView
- [ ]  Projects load in real-time from Firestore
- [ ]  Empty state is shown when no projects exist
- [ ]  Loading indicator appears while fetching
- [ ]  Each project card shows name and description

******

### **Story #US-07: Create New Project**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Ragade |
| --- | --- | --- |

**Description:**

As a user, I want to press the Add button (FAB) to create a new project so that I can start organizing my work.

**Tasks:**

- [ ]  FAB button on Home screen navigates to Add Project Dialog- Ragade
- [ ]  Design Add Project Dialog XML layout (name field, description field, save button) - Ragade
- [ ]  Create Add project logic - Ragade
- [ ]  Save new project to Firestore with userId, timestamp - Ragade
- [ ]  Validate inputs (name required) - Ragade
- [ ]  Navigate back to Home screen after save -Ragade
- [ ]  Show confirmation/error messages - Ragade

**Acceptance Criteria:**

- [ ]  FAB button opens Add Project dialog
- [ ]  User can enter project name and description
- [ ]  Name field is required (validation)
- [ ]  New project is saved to Firestore
- [ ]  User returns to Home screen and new project appears in list

*****

### **Story #US-08: View Project Details**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Hala |
| --- | --- | --- |

**Description:**

As a user, I want to tap on a project in the list to open its details page so that I can see all project information and manage tasks within it.

**Tasks:**

- [ ]  Handle RecyclerView item click in Home fragment- Hala
- [ ]  Pass project object via new Instance function to Project Detail Fragment - Hala
- [ ]  Design Project Detail Fragment XML layout (project name, description, dropdown menu to (edit,  delete and delete al tasks )) - Hala
- [ ]  Fetch project data from Firestore by project ID - Hala
- [ ]  Display project name, description, and metadata - Hala

**Acceptance Criteria:**

- [ ]  Tapping a project navigates to Project Detail screen
- [ ]  Project name, description are displayed correctly
- [ ]  Edit and Delete buttons are visible
- [ ]  Project data is fetched from Firestore

*****

### **Story #US-09: Update Project**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Hala |
| --- | --- | --- |

**Description:**

As a user, I want to edit my project name and description so that I can keep project information up to date.

**Tasks:**

- [ ]  Edit button in Project Detail Fragment (in dropdown menu) opens Edit Project dialog -Hala
- [ ]  Pre-fill current project data in edit form -Hala
- [ ]  Update project fields in Firestore - Hala
- [ ]  Validate updated inputs -Hala
- [ ]  Show success/error messages - Hala
- [ ]  Refresh project details after update -Hala

**Acceptance Criteria:**

- [ ]  Edit button opens edit form with pre-filled data
- [ ]  User can modify project name and description
- [ ]  Updated data is saved to Firestore
- [ ]  Project Detail screen reflects updated data
- [ ]  Success message is shown

******

### **Story #US-10: Delete Project**

| **Story Points: 2** | Priority: Must-Have | Assigned to: Alia |
| --- | --- | --- |

**Description:**

As a user, I want to delete a project I no longer need so that my project list stays clean.

**Tasks:**

- [ ]  Delete button in Project Detail Fragment shows confirmation dialog(form dropdown menu ) -Alia
- [ ]  On confirmation, delete project document from Firestore -Alia
- [ ]  Navigate back to Home screen after deletion - Alia
- [ ]  Show success message after deletion - Alia

**Acceptance Criteria:**

- [ ]  Delete button shows a confirmation dialog
- [ ]  Project is deleted from Firestore on confirm
- [ ]  User is returned to Home screen
- [ ]  Deleted project is removed from RecyclerView list

---

**Sprint 2 - Definition of Done**

- All tasks for the story are completed
- MVC architecture followed View + Controller
- All acceptance criteria met
- Firebase Firestore CRUD operations working
- RecyclerView updates in real-time via LiveData
- Code reviewed and merged
- Manually tested on Android Emulator
- No critical bugs

**Sprint 2 - Schedule**

| **Day** | **Activities** |
| --- | --- |
| **Mon (Day 1)** | Sprint Planning (1h) | Begin US-06: Home screen layout + RecyclerView |
| Wed (Day 3) | Daily Standup | Complete RecyclerView + Firestore listener | Begin US-07 Add Project |
| Fri (Day 5) | Daily Standup | Complete US-06 & US-07 | Begin US-08 Project Details |
| Mon (Day 8) | Daily Standup | Complete US-08 | Begin US-09 Update + US-10 Delete |
| Wed (Day 10) | Daily Standup | Complete US-09 & US-10 | Integration testing |
| Fri (Day 12) | Final testing | Sprint Review (1h) | Sprint Retrospective (45min) |

**Sprint 2 - Success Metrics**

- Sprint goal achieved (full project CRUD working)
- All 5 stories completed (16 story points)
- Firestore data syncs in real-time
- RecyclerView displays projects correctly
- No critical bugs
- Code reviewed and merged

******

**Sprint 2 - Team Commitment**

We commit to working together, communicating openly, supporting each other, and delivering our best work this sprint.

| **Name** | **Role** | **Date** |
| --- | --- | --- |
| Hala | Scrum Master and Developer | 12-03-2026 |
| Alia | Product manager and Developer | 12-03-2026 |
| Rgade | Developer | 12-03-2026 |
| Eman | Developer | 12-03-2026 |
