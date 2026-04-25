# Clear Day — Sprint 4

| **Sprint Number** | 4 |
| --- | --- |
| **Start Date** | 31-03-2026 |
| **Team Capacity** | ~30 hours |
| **Sprint 4 Goal**
 | Users can view and edit their personal Profile, invite new team members to a project via email or link, and attach files or images to individual tasks — all synced with Firebase Firestore and Firebase Storage. |
| **Duration** | 16 days |
| **End Date** | 13-04-2026 |
| **Story Points** | 21 Points |

### Story #US-14: User Profile

| Story Point: 8 | Story Priority :Must-Hava | Assigned :Eman |
| --- | --- | --- |

**Description:**

*As a user, I want to view and edit my personal profile (name, profile photo, email, and bio) so that my identity is visible to my team members.*

---

**Tasks:**

☐ Design ProfileActivity XML layout with CircleImageView, TextViews, and Edit button  *— UI Dev*

☐ Add navigation to ProfileActivity from DrawerMenu or BottomNav  *— UI Dev*

☐ Create User model class (id, name, email, bio, photoUrl)  *— Lead Dev*

☐ Implement photo upload using Firebase Storage + update Firestore  *— Lead Dev*

☐ Add Edit Profile form (dialog or separate activity) for name & bio  *— UI Dev*

☐ Show loading indicator while data is being fetched or saved  *— UI Dev*

☐ Display user initials as fallback avatar when no photo is set  *— Eman*

---

**Acceptance Criteria:**

☐ Profile screen shows current user name, email, bio, and photo

☐ User can tap Edit and update name and bio

☐ User can upload a new profile photo from gallery or camera

☐ Updated profile is saved to Firestore and reflected immediately

☐ Initials avatar shown when no photo is uploaded

☐ Loading state shown during data fetch and save operations

---

### Story #US-15: Invite Member

| Story Point: 7 | Story Priority :Must-Hava | Assigned : Ragade |
| --- | --- | --- |

**Description:**

As a project owner or admin, I want to invite a new team member to a project by entering their email or sharing an invite link so that they can join and collaborate.

---

**Tasks:**

☐ Design InviteMemberBottomSheet or Dialog with email input field and Share Link button  *— UI Dev*

☐ Add invite option to Project Detail screen (menu or floating action button)  *— UI Dev*

☐ Create Invitation model class (id, projectId, email, status, invitedAt)  *— Lead Dev*

☐ Create InvitationRepository with Firestore write for storing invitations  *— Lead Dev*

☐ Generate unique invite link using Firebase Dynamic Links or deep link with token  *— Lead Dev*

☐ Implement email invite: validate email format, write invitation to Firestore  *— Lead Dev*

☐ Handle invite acceptance: update project members list in Firestore when user clicks link  *— Lead Dev*

☐ Show success/error feedback after sending invite  *— UI Dev*

☐ Display list of pending invitations on project settings screen  *— UI Dev*

---

**Acceptance Criteria:**

☐ Invite option visible on Project Detail screen

☐ User can enter a valid email and send invite

☐ Invitation is stored in Firestore with status 'pending'

☐ Unique invite link can be copied and shared externally

☐ Invited user who taps the link is added to project members

☐ Invalid or duplicate emails are rejected with error message

☐ Pending invitations list shown in project settings

---

### Story #US-16: Task Attachments

| Story Point: 6 | Story Priority : Must-Have | Assigned : Alia |
| --- | --- | --- |

**Description:**

As a user, I want to attach files or images to a task so that I can share relevant documents or screenshots with my team directly on the task.

---

**Tasks:**

☐ Add Attachments section to TaskDetailActivity with a RecyclerView  *— UI Dev*

☐ Create Add Attachment button that opens file/image picker (gallery + file manager)  *— UI Dev*

☐ Create Attachment model class (id, taskId, name, url, type, uploadedAt, uploadedBy)  *— Lead Dev*

☐ Create AttachmentRepository with Firebase Storage upload + Firestore metadata save  *— Lead Dev*

☐ Display thumbnails for image attachments and file icon for non-image files  *— UI Dev*

☐ Allow user to tap attachment to open/preview (use Intent for external viewer)  *— UI Dev*

---

**Acceptance Criteria:**

☐ Attachments section visible on Task Detail screen

☐ User can tap Add Attachment to pick image or file from device

☐ File uploads to Firebase Storage and metadata saved to Firestore

☐ Images show thumbnail, other files show a file icon with name

☐ Tapping an attachment opens or downloads it

---

| **Topic** | **Details** |
| --- | --- |
| **Firebase Storage** | Used for profile photo upload (US-14) and task file attachments (US-16). Add storage rules to allow authenticated users only. |
| **Firebase Dynamic Links** | Required for invite link generation (US-15). Configure in Firebase Console with deep link prefix. |
| **CircleImageView Library** | Add de.hdodenhof:circleimageview to Gradle for rounded profile photo display. |
| **File/Image Picker** | Use Android Activity Result API with GetContent / OpenDocument intents for picking files and images. |
| **Firestore Security Rules** | Update rules to restrict attachments and invitations to project members only. |
| **Offline Handling** | Use Firestore offline persistence; show a retry option if upload fails due to network issues. |

## Definition of Done

- ☐ All tasks marked complete and code merged to main branch.
- ☐ Firebase Storage and Firestore rules updated and tested.
- ☐ Profile, Invite, and Attachment screens reviewed on emulator and physical device.
- ☐ No crashes or data loss on happy-path flows.
- ☐ PR description written and approved before merging.
- ☐ Sprint retrospective notes documented.

**Sprint 3 - Team Commitment**

We commit to working together, communicating openly, supporting each other, and delivering our best work this sprint.

| **Name** | **Role** | **Date** |
| --- | --- | --- |
| Hala | Scrum Master and Developer | 30-03 |
| Alia | Product manager and Developer | 30-03 |
| Rgade | Developer | 30-03 |
| Eman | Developer | 30-03 |
