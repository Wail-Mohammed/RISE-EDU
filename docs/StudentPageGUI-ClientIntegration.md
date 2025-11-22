# StudentPageGUI Client-Server Integration Plan

## 1 Current Structure
- `app.Client.GUImanager.StudentPageGUI` already serves as a pure Swing presentation layer: a header, a 2×2 grid of buttons, and a shared `styleMainButton()` helper. The constructor accepts a `Client` reference (unused so far), and the GUI deliberately never imports `app.Server` or instantiates sockets. This matches the principle that “the GUI only talks to the backend through the client.”
- `LoginPage` already uses `Client.send(Message)` to authenticate and then opens either the student or admin panel depending on the server response. The `Client` class remains focused on `send()` while hiding the networking details, following the intended `Client implements AutoCloseable` separation.

## 2 Pain Points (code reference)
- The title label was hardcoded as `new JLabel("Welcome student!")`, so the `welcomeMessage` passed into the constructor never appeared.
- `addCourse()`, `dropCourse()`, `viewSchedule()`, and `viewCourses()` currently only show local `JOptionPane` dialogs; they never build a `Message` or call `client.send(...)`, so no request ever reaches the server.
- The UI stores only `String welcomeMessage` and lacks a `studentId` (or other user context), preventing it from constructing complete request payloads.

## 3 Improvement Directions (based on the current code)
1. **Use the welcome message**  
   - Build the title label with the passed `welcomeMessage`: `new JLabel(welcomeMessage, SwingConstants.CENTER)`.  
   - Have `LoginPage` call `new StudentPageGUI("Welcome " + username + "!", client)` so the greeting reflects the logged-in student.
2. **Hook each button into `Client`**  
   - Each handler should collect any necessary input (e.g., `courseId`). The GUI now keeps `studentId` (passed from `LoginPage`) so each message can include who is acting.  
   - Build a `Message` with a payload such as:
     ```java
     ArrayList<String> payload = new ArrayList<>();
     payload.add(studentId);
     payload.add(courseId.trim());
     Message req = new Message(Type.ENROLL_COURSE, Status.NULL, "", payload);
     ```
   - Call `client.send(req)` and inspect `resp.getStatus()` to decide whether to show a success dialog or display `resp.getText()` on failure.  
   - Catch `IOException` and `ClassNotFoundException` and show “Unable to reach the server; please try again later.”
3. **Streamline response handling**  
   - For `viewSchedule()` and `viewCourses()`, the payload can simply contain the `studentId`. The server can return a list (or text), which the client can format (e.g., via `StringBuilder`) before showing in a dialog.  
   - If richer UI is desired later, feed the response data into a dedicated Swing panel instead of `showMessageDialog`.
4. **Keep the UI responsive**  
   - Disable the buttons or show a “please wait” indicator while the network call is running to prevent duplicate clicks, and restore the buttons once a response arrives.

## 4 Next Steps
1. Decide whether `LoginPage` should pass the `studentId` (or other user context) into `StudentPageGUI`.
2. Implement one handler (for example, `addCourse()`) that calls `client.send()` and reacts to `resp.getStatus()`.
3. Repeat the pattern for `dropCourse()`, `viewSchedule()`, and `viewCourses()`.
4. If the backend does not yet support certain `Type` values, document them (e.g., in `Documents/GUIClientServerPlan.md`) and align with the server team before wiring the requests.

After these steps, `StudentPageGUI` will evolve from a placeholder shell into a view that truly interacts with the server via the client.

