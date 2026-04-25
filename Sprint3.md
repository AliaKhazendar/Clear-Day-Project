# Clear Day — Sprint 3

| **Sprint Number** | 3 |
| --- | --- |
| **Start Date** | 14-03-2026 |
| **Team Capacity** | ~30 hours |
| **Sprint 3 Goal**
 | Users can view a Kanban-style task board inside a project with columns (To Do, In Progress, Done), create tasks, update task details, change task status, and delete tasks — all synced with Firebase Firestore. AND add Profile Activity  |
| **Duration** | 16 days |
| **End Date** | 30-03-2026 |
| **Story Points** | 23 Points |

### **Story #US-11: View Task Board (Kanban Columns)**

| **Story Points: 5** | Priority: Must-Have | Assigned to:Hala |
| --- | --- | --- |

**Description:**

As a user, I want to see tasks inside a project organized in Kanban columns (To Do, In Progress, Done) so that I can quickly understand task status.

**Tasks:**

- [ ]  Design TaskBoardActivity XML layout with ViewPager2 + TabLayout (3 columns) - Hala
- [ ]  Create TaskFragment for each column (To Do, In Progress, Done) - Hala
- [ ]  Create TaskAdapter for RecyclerView in each fragment - Hala
- [ ]  Create Task model class (id, title, description, status, projectId, dueDate, createdAt) - Hala

**Acceptance Criteria:**

- [ ]  Task board opens from Project Detail screen
- [ ]  3 tabs shown: To Do, In Progress, Done
- [ ]  Each column shows tasks from Firestore filtered by status
- [ ]  Task count shown in each tab header
- [ ]  Empty state shown in empty columns

*****

### **Story #US-12: Create New Task**

| **Story Points: 5** | Priority: Must-Have | Assigned to: Alia , Hala |
| --- | --- | --- |

**Description:**

As a user, I want to create a new task inside a project so that I can track work items .

**Tasks:**

- [ ]  FAB button on Task Board opens AddTaskActivity - Alia
- [ ]  Design AddTaskActivity XML layout (title, description, status dropdown, due date picker) - Alia
- [ ]  Save new task to Firestore with projectId, status (default: To Do), timestamp -Alia
- [ ]  Validate required fields (title required) - Alia
- [ ]  Navigate back to Task Board after save - Alia
- [ ]  New task appears in the correct column via real-time listener -Hala

**Acceptance Criteria:**

- [ ]  FAB opens Add Task screen
- [ ]  User can enter title, description, status, and due date
- [ ]  Title is required (validation)
- [ ]  Task is saved to Firestore with correct projectId
- [ ]  Task appears in correct Kanban column in real-time

*****

### **Story #US-13: Update Task Details**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Ragade |
| --- | --- | --- |

**Description:**

As a user, I want to tap on a task to open its details and edit the title, description, or due date so that I can keep task information accurate.

**Tasks:**

- [ ]  Handle task card click to open TaskDetailActivity -  Ragade
- [ ]  Design TaskDetailActivity XML layout (title, description, status, due date, edit/delete buttons) - Ragade
- [ ]  Pre-fill all task data in edit form - Ragade
- [ ]  Update task fields in Firestore on save - Ragade
- [ ]  Validate updated inputs - Ragade
- [ ]  Navigate back to Task Board after update -Ragade

**Acceptance Criteria:**

- [ ]  Tapping a task opens Task Detail screen
- [ ]  All task details are displayed
- [ ]  User can edit title, description, and due date
- [ ]  Updated data is saved to Firestore
- [ ]  Task board reflects updated information

****

### **Story #US-14: Delete Task**

| **Story Points: 2** | Priority: Must-Have | Assigned to: Hala |
| --- | --- | --- |

**Description:**

As a user, I want to delete a task I no longer need so that the project board stays clean and relevant.

**Tasks:**

- [ ]  Delete button in TaskDetailActivity shows confirmation dialog - Hala
- [ ]  On confirmation, delete task document from Firestore -Hala
- [ ]  Navigate back to Task Board after deletion - Hala
- [ ]  Deleted task removed from RecyclerView in real-time - Hala

**Acceptance Criteria:**

- [ ]  Delete button shows confirmation dialog
- [ ]  Task is deleted from Firestore on confirm
- [ ]  Task is removed from Kanban column in real-time
- [ ]  User is returned to Task Board

****

### **Story #US-15: Update Task Status (Drag or Button)**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Raghad |
| --- | --- | --- |

**Description:**

As a user, I want to change a task's status (To Do, In Progress, Done) so that the project board accurately reflects task progress.

**Tasks:**

- [ ]  Add status change button/dropdown in TaskDetailActivity - Raghad
- [ ]  Update task status field in Firestore - Raghad
- [ ]  Task moves to correct column in real-time via Firestore listener -Raghad
- [ ]  Show success message after status change - Raghad

**Acceptance Criteria:**

- [ ]  User can change task status from Task Detail screen
- [ ]  Status options: To Do, In Progress, Done
- [ ]  Firestore document is updated with new status
- [ ]  Task moves to correct Kanban column in real-time
- [ ]  Success message shown after update

*******

**Sprint 3 - Team Commitment**

We commit to working together, communicating openly, supporting each other, and delivering our best work this sprint.

| **Name** | **Role** | **Date** |
| --- | --- | --- |
| Hala | Scrum Master and Developer | 30-03 |
| Alia | Product manager and Developer | 30-03 |
| Rgade | Developer | 30-03 |
| Eman | Developer | 30-03 |