# PHASE 1 — PROJECT ANALYSIS PROMPT

**Use first. No coding yet.**

## PROMPT — PHASE 1

Act as a Senior Android Architect inside an existing Android project.

**Do NOT generate features yet.**

First analyze the full project and document architecture decisions.

### Scan and report

1. Activity structure
2. Fragment vs Compose usage
3. Navigation method (NavGraph / Router / Manual)
4. MVVM implementation
5. Base classes (BaseActivity, BaseFragment, BaseViewModel)
6. Repository + UseCase pattern
7. Retrofit setup
8. Interceptors + token refresh logic
9. DI framework (Hilt/Dagger/Koin)
10. Result wrappers / API response models
11. Existing push notification setup
12. Existing socket/realtime infra

### Define "Project Skill Rules"

- When to use Activity vs Fragment
- How ViewModels are scoped
- How APIs are added
- How navigation is extended

**Output analysis only. No feature code yet.**

---

# PHASE 2 — API INTEGRATION PROMPT

**Now integrate backend.**

## PROMPT — PHASE 2

Using the analyzed architecture, integrate Villa Society APIs from the attached Postman collection `Villa-Society-API.postman_collection`.

**Do NOT create new Retrofit instances.** Extend existing network layer.

**Base URL:** `http://localhost:3000/api`

### Implement

**Auth:**

- `POST /auth/login`
- `POST /auth/refresh-token`
- `GET /auth/profile`

**Visitors:**

- `POST /visitors`
- `POST /visitors/{id}/approve`
- `POST /visitors/{id}/reject`
- `POST /visitors/{id}/exit`
- `GET /visitors/logs`

**Billing:**

- `GET /my/bills`
- `GET /my/bills/pending`
- `GET /my/payments`

**Complaints:**

- `POST /complaints`
- `GET /complaints/my`

**Deliveries:**

- `POST /deliveries`
- `GET /deliveries`

**SOS:**

- `POST /sos/trigger`

**Attendance:**

- `POST /guard/attendance/check-in`
- `POST /guard/attendance/check-out`

### Generate

- DTOs
- Retrofit interfaces
- Repository implementations
- ViewModels
- UI state wrappers

Follow existing coding standards strictly.

---

# PHASE 3 — NAVIGATION INTEGRATION PROMPT

**Now wire flows.**

## PROMPT — PHASE 3

Extend the existing navigation system. **Do NOT create a new one.**

Implement role-based routing after login:

- **If RESIDENT** → ResidentGraph
- **If GUARD** → GuardGraph

**ResidentGraph:**

- Dashboard
- Visitors
- Billing
- Complaints
- Profile  
  *(Bottom navigation if project supports.)*

**GuardGraph:**

- Dashboard
- Add Visitor
- Visitor Logs
- Deliveries
- SOS
- Attendance

Use existing navigation patterns only. Add deep link placeholders for push notifications.

---

# PHASE 4 — UI IMPLEMENTATION PROMPT

**Now build screens.**

## PROMPT — PHASE 4

Generate UI screens using existing project UI technology (Compose or XML).

Follow design style inspired by:

- **MyGate** → security workflows
- **NoBrokerHood** → billing clarity

### UI rules

- White background
- Card layouts
- Status badges
- Minimal gradients
- Functional hierarchy

**Resident screens:**

- Dashboard (visitors + bills + notices)
- Visitor approvals
- Billing summary + history
- Complaints
- Profile

**Guard screens:**

- Dashboard grid
- Add visitor
- Visitor exit
- Deliveries
- SOS alerts
- Attendance

Reuse existing components, themes, typography. **Do NOT introduce new design systems.**

---

# PHASE 5 — PUSH NOTIFICATION PROMPT

**Now FCM layer.**

## PROMPT — PHASE 5

Integrate Firebase Cloud Messaging into the existing project.

### Token lifecycle

- Fetch on login
- Send to backend
- Update on refresh
- Remove on logout

### Payload format (backend contract)

```json
{
  "notification": {
    "title": "Visitor at Gate",
    "body": "John is waiting"
  },
  "data": {
    "type": "VISITOR_REQUEST",
    "visitor_id": "123",
    "villa_id": "V45",
    "floor_id": "GF"
  }
}
```

### Supported types

- `VISITOR_REQUEST`
- `VISITOR_STATUS`
- `DELIVERY_ARRIVED`
- `SOS_TRIGGERED`
- `BILL_GENERATED`
- `COMPLAINT_UPDATED`
- `NOTICE_PUBLISHED`

**Foreground:**

- Show in-app banner
- Refresh screen

**Background:**

- Show system notification
- Route on click

### Notification Channels

| Channel             | Priority  | Notes        |
|---------------------|-----------|--------------|
| visitor_alerts      | High      | —            |
| sos_alerts          | Max       | + alarm sound|
| delivery_alerts     | Default   | —            |
| billing_alerts      | Default   | —            |
| notice_alerts       | Low       | —            |

Create in Application class.

---

# PHASE 6 — SOCKET INTEGRATION PROMPT

**Realtime sync.**

## PROMPT — PHASE 6

Integrate realtime updates using existing socket infra if available. Else create lightweight SocketManager.

### Listen

**Resident:**

- `visitor_request`
- `visitor_status_update`
- `sos_triggered`

**Guard:**

- `sos_triggered`
- `visitor_status_update`

Join rooms using JWT identity. Prevent duplicate push + socket alerts. Update ViewModels in realtime.

---

# EXECUTION ORDER

Run prompts in this exact order:

1. **Phase 1** — Analysis  
2. **Phase 2** — APIs  
3. **Phase 3** — Navigation  
4. **Phase 4** — UI  
5. **Phase 5** — Push  
6. **Phase 6** — Socket  

**Do NOT merge phases.**

---

# Result After All Phases

You'll have:

- Fully integrated APIs
- Role navigation
- MyGate + NoBrokerHood UI
- Push notifications
- Realtime sockets
- Token refresh
- Deep linking
- Project-aligned architecture
