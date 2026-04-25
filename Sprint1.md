# Clear Day — Sprint 1

| **Sprint Number** | 1 |
| --- | --- |
| **Start Date** | 20-02-2026 |
| **Team Capacity** | ~20 hours |
| **Sprint 1 Goal** | Users can register, log in, and go through onboarding screens to access the Clear Day application with Firebase Authentication. |
| **Duration** | 10 days  |
| **End Date** | 01-03-2026 |
| **Story Points** | 13Points |

---

### **Story #US-01: Project Setup & Firebase Configuration**

| **Story Points: 5** | Priority: Must-Have | Assigned to: all members |
| --- | --- | --- |

**Description:**
As a development team, we want to set up the Android project with MVC architecture, Firebase, and all required dependencies so that all team members can start development.

**Tasks:**
☐     Create Android project in Android Studio - Whole Team
☐     Set up MVC architecture (ViewModel,View,Controller) - Hala
☐     Add Firebase dependencies to build.gradle -Ragad
☐     Configure Firebase Authentication in Firebase Console - Ragade
☐     Configure Firebase Firestore in Firebase Console -Ragade
☐     Create base package structure (ui, data) - Hala
☐     Set up Git repository and branching strategy - Alia

**Acceptance Criteria:**
☐     All team members can run the project locally
☐     Firebase project is connected and configured
☐     MVC structure is in place
☐     Git repository is set up with initial commit

************************************************************************************************************

### **Story #US-02: User Registration**

| **Story Points: 2** | Priority: Must-Have | Assigned to: Hala M. Dalloul and Alia Khazendar |
| --- | --- | --- |

As a new user (Team Leader / Student), I want to register with my email and password so that I can create an account and access Clear Day.

**Tasks:**

- [ ]  Design Register Fragment XML layout (user name, email, password, register button) - Hala
- [ ]  Create Register Fragment with Firebase Auth createUserWithEmailAndPassword - Alia
- [ ]  Create User model class - Hala
- [ ]  Save user profile to Firestore after registration - Alia
    
    **Description:**
    
- [ ]  Show error messages for invalid inputs - Alia
- [ ]  Navigate to Home screen after successful registration - Alia

**Acceptance Criteria:**

- [ ]  User can enter email and password
- [ ]  Password confirmation validation works
- [ ]  Firebase account is created successfully
- [ ]  User document is saved in Firestore
- [ ]  User is navigated to Home screen after registration
- [ ]  Error messages are shown for invalid inputs

****

### Story #US-03: User Login

| **Story Points: 3** | Priority: Must-Have | Assigned to: Alia and Hala |
| --- | --- | --- |

**Description:**

As a registered user, I want to log in with my email and password so that I can access my projects and tasks.

**Tasks:**

- [ ]  Design Login Fragment XML layout (email, password, login button, register link) - Hala
- [ ]  Create Login  Fragment with input validation - Alia
- [ ]  Implement Firebase Auth signInWithEmailAndPassword -Alia
- [ ]  Handle authentication errors (wrong password, user not found) -Alia
- [ ]  Navigate to Home screen on successful login -Alia

**Acceptance Criteria:**

- [ ]  User can enter email and password
- [ ]  Successful login navigates to Home screen
- [ ]  Error messages shown for wrong credentials
- [ ]  Firebase Auth session is persisted

*****

### **Story #US-04: Onboarding Screens**

| **Story Points: 3** | Priority: Must-Have | Assigned to: Hala |
| --- | --- | --- |

**Description:**

As a new user, I want to see onboarding screens explaining the app's features so that I can understand how to use Clear Day.

**Tasks:**

- [ ]  Design 3 onboarding XML layouts (Welcome, Projects Overview, Tasks Overview) - Hala
- [ ]  Create OnboardingActivity with ViewPager2 - Hala
- [ ]  Add Dots Indicator and next buttons - Hala
- [ ]  Store onboarding completion flag in SharedPreferences - Hala

**Acceptance Criteria:**
☐     New users see 3 onboarding screens on first launch
☐     Returning users are taken directly to Login/Home
☐     Onboarding completion is persisted in SharedPreferences

************************************************************************************************************

### **Sprint 1 - Definition of Done**

- All tasks for the story are completed
- Code follows MVC architecture pattern
- All acceptance criteria are met
- Code reviewed by at least one team member
- Pull request approved and merged to main branch
- Manually tested on Android Emulator
- No critical bugs
- Firebase Authentication is working end-to-end

************************************************************************************************************

**Sprint 1 - Schedule**

| **Day** | **Activities** |
| --- | --- |
| **Mon (Day 1)** | Sprint Planning (1h) | Setup Android project | Configure Firebase | Set up Git repository |
| Wed (Day 2) | Daily Standup (15min) | Continue US-01 Setup | Begin US-02 Registration UI |
| Fri (Day 4) | Daily Standup (15min) | Complete US-01 | Progress on US-02 Registration logic |
| Mon (Day 6) | Daily Standup (15min) | Complete US-02 | Start US-03 Login |
| Wed (Day 8) | Daily Standup (15min) | Complete US-03 | Begin US-04 Onboarding | Start US-05 |
| Fri (Day 10) | Final testing | Sprint Review (1h) | Sprint Retrospective (45min) |

************************************************************************************************************

**Sprint 1 - Review & Retrospective Plan**

**Sprint Review Agenda**

- Review Sprint 1 goal - Scrum Master (5 min)
- Demo US-01: Project Setup and Firebase connection
- Demo US-02: User Registration flow end-to-end
- Demo US-03: User Login with Firebase
- Demo US-04: Onboarding Screens (ViewPager2)
- Review incomplete work (5 min)
- Get feedback from instructor/Product Owner (15 min)
- Discuss Sprint 2 priorities (5 min)

**Sprint Retrospective Format**

- What went well?
- What didn't go well?
- What will we improve in Sprint 2?
- Action items for next sprint

************************************************************************************************************

**Sprint 1 - Risks & Mitigation**

| **Risk** | **Likelihood** | **Mitigation** |
| --- | --- | --- |
| Firebase configuration issues | Medium | Follow official Firebase docs, test connection early |
| Team member unavailable | Low | Pair programming, documented code |
| MVC complexity for new team members | Medium | Shared knowledge session, reference architecture |

************************************************************************************************************

**Sprint 1 - Success Metrics**

- Sprint goal achieved (users can register, login, and onboard)
- All 5 stories completed (15 story points)
- All acceptance criteria met
- Firebase Auth working end-to-end
- No critical bugs
- Code reviewed and merged
- Team learned MVC + Firebase basics

************************************************************************************************************

**Sprint 1 - Team Commitment**

We commit to working together, communicating openly, supporting each other, and delivering our best work this sprint.

| **Name** | **Role** | **Date** |
| --- | --- | --- |
| Hala | Scrum Master and Developer | 01-03-2026 |
| Alia | Developer | 01-03-2026 |
| Rgade | Design Arti. | 01-03-2026 |
| Eman | Design Arti. | 01-03-2026 |
