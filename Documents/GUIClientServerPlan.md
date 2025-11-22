# GUI 客户端实现 client-server 的审核说明

## 目标
只在 `app.Client.GUImanager` 下做修改，让 GUI 和已在 `app.Server` 里的后端走 `Message`/socket 通信，保持前端职责单一、避免改动其他人负责的文件。

## 主要构件（你负责）
1. **`app.Client.Client`（或类似）**  
   - 在 GUI 目录下创建一个轻量的 socket 客户端。  
   - 封装 `Socket`、`ObjectOutputStream`/`ObjectInputStream`，提供 `Message send(Message request)` 同步接口。  
   - 连接目标地址 `localhost:9090`（与服务器一致），可在出现异常时向 GUI 抛 `IOException` 由界面提示“服务器不可用”。

2. **`LoginPage` 变更**  
   - 构造函数接收 `Client` 实例；`handleLogin()` 不再直接创建页面，而是先发 `Message(Type.LOGIN, ...)`。  
   - 在收到 `Status.SUCCESS` 后，依据 `UserType` 决定 `AdminPageGUI`/`StudentPageGUI`，并把 `Client` 传入，保持同一连接。  
   - 登录失败时使用 `JOptionPane` 显示服务器返回的 `text`。

3. **`AdminPageGUI` / `StudentPageGUI` 按钮**  
   - 每个业务按钮组装 `Message`（填 `Type`、`text` 或 `list`）并调用 `client.send(...)`。  
   - 根据响应 `Status` 显示成功/错误提示；需要刷新列表时 (如查看课程) 用 `response.getList()` 填充表格（先保持简单弹窗）。  
   - 例外情况 `IOException` 直接显示“无法连接服务器，请稍后再试”。  
   - **所有网络调用必须放在后台线程里执行**，不要在 Swing 的 EDT 里直接 `client.send`，否则 UI 会卡死。可以在 `SwingWorker` 或 `ExecutorService` 中调用 `client.send`，然后用 `SwingUtilities.invokeLater` 更新提示。

4. **共享 Message 模式**  
   - `Type.CREATE_COURSE`, `.EDIT_COURSE`, `.DROP_COURSE` 等保持一致，发送的字符串顺序应与服务端 `ClientHandler` 所期望的一致。  
   - `response.getList()` 可用于展示课程/课表/holds；若 `Message` 还没包含 list，可以让服务器先返回 `text` 字段。

## 不动的内容
- 不修改 `app.Server` 的文件；如果后端需要调整接口，把改动留给负责的同事。  
- 不直接操作 `data/*.csv`；所有课程/学生变化都通过 `SystemManager`（server）完成。  
- 不搬动目录结构，仍维护现有的 package `app.Client.GUImanager`。  

## 审核要点
1. `Client` 类必须在 GUI 包里，暴露一个同步 `Message send(Message request)`。  
2. `LoginPage` 成功登录后把 `Client` 传给具体页面，而不是每次 new `Client`。  
3. 所有按钮事件都调用 `client.send` 并处理服务器响应（成功/失败提示、列表/文本）。  
4. 若需要 JSON/DTO，可以在前端构造 `Message` `list` 字符串（不改服务器）。  

## 需要你确认
- 客户端长连接还是每次调用重新 connect（当前建议先每个 `send` 打开一次 socket，后续可优化）。  
- 每种操作要发送哪些字段？建议先列出 `Type`→`payload` 对照表（我可以帮你草拟）。  
- 是否需要把响应列表展示成一个弹窗？当前可以用 `JOptionPane` 列出 `response.getList()` 内容。

以上内容你看有没有遗漏，或者需要我把某个按钮演示成“send -> TextArea” 的具体调用？*** End Patchacket Let’s respond next.	

