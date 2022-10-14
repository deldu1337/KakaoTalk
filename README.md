# 카카오톡 채팅 프로그램
# Java Socket 통신 기반 채팅 프로그램
#### 프로젝트 개발 기간 : 2021.10 ~ 2021.12
-----------------------
## Description
### PART 1. 프로그램 제공 기능
 - 실시간 채팅 기능
 - 로그인 및 로그아웃 기능
 - 친구 추가 기능
 - 채팅방 추가 기능
 
### PART 2. 프로그램 구성
 - 시스템 구성도
 ![시스템구성도](https://user-images.githubusercontent.com/77719450/195785934-6cbd1f4b-15dd-40cc-8e9b-01bcfe928e8e.PNG)
 -----------------------
 - 시스템 흐름도
 ![시스템흐름도](https://user-images.githubusercontent.com/77719450/195786722-8f5c0984-51bf-45ae-904b-309886564454.PNG)
 -----------------------
 |Protocol|용도/내용|방향|
|------|---|---|
|100|Login|Client -> Server|
|150|ID 저장|Client -> Server|
|200|채팅 Message|Client -> Server -> Client|
|300|Image|Client -> Server -> Client|
|400|Logout|Client -> Server|
|500|Mouse Event|Client -> Server -> Client|
 -----------------------
### PART 3. 화면 구성 및 세부 기능
 - FirstFrame.java
 ![FirstFrame](https://user-images.githubusercontent.com/77719450/195063363-e1eab384-91fe-494a-835f-3a5c5d23b1ce.PNG)
 
 - 'ENTRANCE' 버튼 클릭 시, 초기 방 선택 화면(SecondFrame.java)으로 이동
 -----------------------
 - SecondFrame.java
 ![SecondFrame](https://user-images.githubusercontent.com/77719450/195063484-9b1ca027-3a1c-4b9e-a8af-8e92214cd541.PNG)
 
 - 사용 중인 방은 하늘 색으로 표시
 - 빈 방 버튼 클릭 시, 방 화면(ChooseRoom.java)으로 이동함과 동시에 로그인 화면(LoginFrame.java) 표시
 - 사용 중인 방 버튼 클릭 시, 방 화면(ChooseRoom.java)으로 이동
 - 비상구 버튼 클릭 시, 관리자 로그인 화면(AdminLoginFrame.java)으로 이동
 
 -----------------------
## Environment
 - 