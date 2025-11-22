# StudentPageGUI Student ID Integration Plan

## Motivation
- Tests will run multiple GUI clients simultaneously against a single server, so relying on the socket session (`ClientHandler` keeping `currentUser`) is risky: without an explicit student identifier in each request, any network hiccup or server restart could mix up who is acting.  
- The current `StudentPageGUI.sendRequest(Type, String)` only forwards `text` (typically `courseId`), so every call to `sendRequest(Type.ENROLL_COURSE, courseId)` leaves the server guessing which student made the request.  
- Passing the student identity from the GUI removes ambiguity and aligns with a design where each `Message` is self-describing (`studentId` + `courseId`), which also makes debugging and future stateless protocols easier.

## Where to Plug In the Student ID (code reference)
- `LoginPage.handleLogin()` already receives the successful response from the server, which contains `UserType` and implicitly confirms the username. When `openStudentMenu(username)` is invoked, `StudentPageGUI` is created with the login name and `Client`.  
- `StudentPageGUI` currently stores only `welcomeMessage` and `Client`. The constructor should be extended to accept `studentId` (could be the same as `username` or a separate ID returned by the server).  
- All four button handlers (`addCourse()`, `dropCourse()`, `viewSchedule()`, `viewCourses()`) build `Message` objects via `sendRequest()`. That helper should accept the studentId and include it in the payload instead of sending just the course ID or empty text.

## Proposed Solution
1. **Expand the constructor**  
   ```java
   public StudentPageGUI(String welcomeMessage, String studentId, Client client) { ... }
   ```
   store `studentId` in a final field so every request can include it.

2. **Propagate from LoginPage**  
   - When `LoginPage` receives a `Status.SUCCESS`, the response can either expose a dedicated student ID (extend `Message` if necessary) or simply use the logged-in username.
   - Call `new StudentPageGUI("Welcome " + username + "!", username, client);` so the GUI knows who it is.

3. **Send the studentId with each request**  
   - Update `sendRequest(Type type, String text)` to:
     ```java
     private Message sendRequest(Type type, String text) {
         ArrayList<String> payload = new ArrayList<>();
         payload.add(studentId);
         payload.add(text == null ? "" : text);
         Message request = new Message(type, Status.NULL, "", payload);
         return client.send(request);
     }
     ```
   - `addCourse()` and `dropCourse()` continue to pass the `courseId`; `viewSchedule()` / `viewCourses()` can pass an empty string.

4. **Server expectations**  
   - The server loop (`ClientHandler`) already calls `manager.processEnrollment(currentUser.getUsername(), message.getText())`. After this change, `message.getText()` will contain `courseId`, but we can also switch to `message.getList()` where the first entry is `studentId` and second entry `courseId`.
   - `SystemManager` methods like `processEnrollment`/`processDrop` can keep using the username parameter as today; no immediate change is required on the server if the username is already accurate. But explicit studentId in the message makes future refactors easier.

## Next Steps
1. Extend `Message` usage in `StudentPageGUI.sendRequest()` to carry both `studentId` and the action-specific payload (courseId or blank).
2. Update `LoginPage.openStudentMenu(...)` to pass the derived studentId to the student GUI along with the welcome message.
3. Optionally adjust server-side request handling to read `message.getList()` for the studentId/courseId pair, so responses can double-check the sender.
4. Add tests or instrumentation logs showing that each out-going `Message` includes the correct studentId when multiple clients are running.

With these changes, each client request is self-contained, so even when several students hit the server simultaneously the backend can unambiguously attribute every action to the right user.

