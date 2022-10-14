# 카카오톡 채팅 프로그램
# Java Socket 통신 기반 채팅 프로그램
#### 프로젝트 개발 기간 : 2021.10 ~ 2021.12
#### 프로젝트 개발 인원 : 1인
-----------------------
## Description
### PART 1. 프로그램 제공 기능
 - 실시간 채팅 기능
 - 로그인 및 로그아웃 기능
 - 친구 추가 기능
 - 채팅방 추가 기능

-----------------------
 
### PART 2. 프로그램 구성
 #### 시스템 구성도
 
 ![시스템구성도](https://user-images.githubusercontent.com/77719450/195785934-6cbd1f4b-15dd-40cc-8e9b-01bcfe928e8e.PNG)
 -----------------------
 #### 시스템 흐름도
 
 ![시스템흐름도](https://user-images.githubusercontent.com/77719450/195786722-8f5c0984-51bf-45ae-904b-309886564454.PNG)
 -----------------------
 #### 프로토콜 세부 정보


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
 #### JavaObjServer.java
 ![JavaObjServer](https://user-images.githubusercontent.com/77719450/195806504-e471f9b9-4a3b-4b48-8024-8addc277bfe9.png)

 - 서버 화면
 - 서버를 실행해야만 실시간 채팅 가능
 - 아이디, 채팅 내용, 프로토콜 등 클라이언트에서 받아오는 데이터들을 실시간으로 표시
 -----------------------
 #### JavaObjClientMain.java
 ![JavaObjClientMain](https://user-images.githubusercontent.com/77719450/195789360-c8e05c2d-4fbe-4eba-b80e-158f6d373243.png)

 - 로그인 화면
 - IP Address와 Port Number 표시 및 수정 가능
 - 이름과 아이디를 입력해야만 '로그인' 버튼 활성화
 - 이름과 아이디를 입력 후 '로그인' 버튼 클릭 시, 로비 화면(SecondFrame.java)으로 이동
 -----------------------
 #### SecondFrame.java
 
 ![SecondFrame](https://user-images.githubusercontent.com/77719450/195789383-27b72374-1971-46c7-8273-e9da394873f6.png)
 
 - 로비 화면
 - 친구 목록 화면과 채팅 목록 화면을 좌측 상단에 각각 사람 모양, 말풍선 모양의 버튼으로 표시
 - 친구 목록 화면에서 사용자 이름과 친구 목록 표시
 - 친구 목록 화면의 우측 상단에 친구 추가 버튼 클릭 시, 친구 추가 화면(AddFriendFrame.java)으로 이동
 - 채팅 목록 화면에서 채팅방 목록 표시
 - 채팅 목록 화면의 우측 상단에 채팅방 추가 버튼 클릭 시, 방 추가 화면(AddRoomFrame.java)으로 이동
 - 채팅 목록 화면에서 원하는 채팅방 클릭 시, 채팅 화면(JavaObjClientView.java)으로 이동
 - 좌측 하단에 톱니바퀴 버튼 클릭 시, '로그아웃', '회원초기화' 팝업 표시
 - '로그아웃' 클릭 시, 로그인 화면(JavaObjClientMain.java)으로 이동
 - '회원초기화' 클릭 시 이름, 아이디, 친구 목록, 채팅방 목록 삭제 후 로그인 화면(JavaObjClientMain.java)으로 이동
 - 다른 회원의 친구 목록, 채팅방 목록에 '회원초기화' 대상이 포함되어 있다면 해당 친구 목록, 채팅방 목록에서 '회원초기화' 대상 삭제
 -----------------------
 #### AddFriendFrame.java
 
 ![AddFriendFrame](https://user-images.githubusercontent.com/77719450/195791082-30fc16f6-0e5e-4797-bcb1-597f56ceaf3c.png)
 
 - 친구 추가 화면
 - 아이디를 입력해야만 '친구 추가' 버튼 활성화
 - 친구 추가할 아이디 입력 후 '친구 추가' 버튼 클릭 시, 친구 추가된 상태에서 로비 화면(SecondFrame.java)으로 이동
 -----------------------
 #### AddRoomFrame.java

 ![AddRoomFrame](https://user-images.githubusercontent.com/77719450/195791406-16519d21-3d1c-4185-bded-a31a2bc2df28.png)

 - 방 추가 화면
 - 친구 목록 표시
 - 친구 조회 가능
 - 조회 입력란에 친구의 키워드(아이디) 입력 즉시, 관련된 친구 테이블만 표시
 - 최소 1인 이상의 대화상대를 선택해야만 '확인' 버튼 활성화
 - 대화상대 중복 선택 가능
 - 대화상대 클릭 시, 하단에 선택한 대화상대 표시
 - 대화상대 재클릭 시, 하단에서 대화상대 제외 가능
 - 대화상대 선택(중복 선택 가능) 후 '확인' 버튼 클릭 시, 채팅방 추가된 상태에서 로비 화면(SecondFrame.java)으로 이동
 -----------------------
 #### JavaObjClientView.java

 ![JavaObjClientView](https://user-images.githubusercontent.com/77719450/195791791-02a56a2c-76dd-462a-a19c-1b896f2681f6.png)
 
 - 채팅 화면
 - 좌측 상단에 사용자 이름, 아이디 표시
 - 대화상대 입장 시, 입장 메시지 표시
 - 실시간 채팅 가능
 - 하단 대화 칸에 글이 존재해야만 '전송' 버튼 활성화
 - 하단 대화 칸에 글 입력 후 '전송' 버튼 클릭 시, 채팅방에 글 전송
 - 우측 하단에 클립 버튼 클릭 시, 파일첨부 창 표시
 - 파일첨부는 이미지만 가능
 - 클립 버튼 클릭 후 원하는 이미지 선택 시, 채팅방에 이미지 전송
 - 채팅이 채팅 화면을 벗어날 시, 스크롤바 생성
 -----------------------

## Environment
 > Java version 17.0.1
 > 
 > MySQL version 8.0.26
 >
 > Eclipse IDE version 2020-06 (4.16.0)
