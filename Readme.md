# Hunsu


## 🎈프로젝트 정보

---

- **진행 기간** : 2021. 01.06 ~ 2021.02.19
- **Daily Scrum :** 매일 오전 수업 직후 30분
- **프로젝트 설명**

    '패션' 이라는 주제로 소통의 장을 열 수 있는

    데일리 착장을 공유할 수 있는

    중요한 자리에 입고갈 스타일을 조언받을 수 있는  

    👉***대한민국 1등 '패션 모바일 웹'👈***

## 🚩 개발 목표

---

> 빠르게 변화하는 패션 트렌드에  사람들과 소통할 수 있는 패션 모바일 웹사이트

![Hunsu_7289ec50d5c844b186f3790918aae505_1](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/29c293d25b16ea803a46cea022fb30ce/Hunsu_7289ec50d5c844b186f3790918aae505_1.png)

- **소통을 위한 공간 ( 실 채 훈 )**
- **공유를 위한 공간 ( Ootd )**
- **투표 및 평가를 위한 공간 ( 뭘 입을까? )**


## ⚙ 기술 stack

---

![기술스택](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/68c410023a6f6306ac3a10ac637cc8a0/%EA%B8%B0%EC%88%A0%EC%8A%A4%ED%83%9D.png)

**프론트엔드**

- Vue
- Vuetify
- Vuex

**백엔드**

- Spring Boot
- Spring Security
- Swagger
- Stomp
- redis

**협업툴**

- Webex
- Jira
- GitLab

**Server**

- NGINX
- S3
- EC2

## 🖇 ERD

---

![erd](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/4a96405b7a97b2628476d19c9732d7e4/erd.png)

**[ ERD 파일 첨부 ]**

[hunsu.mwb](Hunsu%207289ec50d5c844b186f3790918aae505/hunsu.mwb)

## 📜프로젝트 구성도

---

![2](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/46af90a12368d77f6db063f595a159fe/2.png)

## 🌟 주요기능

---

### 1. #Ootd

> *해시태그 기능과 이미지 업로드 기능으로 나의 착장을 사람들과 공유하는 공간*

- 기능
    - 최신순 / 인기순 정렬
    - 해시태그 검색
    - 글 작성 가능
    - 무한 스크롤 기능
    - 유저 프로필 표시
    - 이미지 업로드 가능
    - 좋아요 기능
    - 댓글, 대댓글 작성 가능

### 2. 뭘입을까?

> *투표 기능을 제공하여 여러 스타일에 대한 사용자들의 의견을 들어보는 공간*

- 기능
    - 이미지 업로드 가능
    - 업로드 된 사진 좌우 슬라이드로 제공
    - 투표기능 ( 중복체크 가능, 중복 투표 불가 )
    - 투표결과 차트 반영
    - 댓글, 대댓글 작성 가능
    - 댓글 수정 삭제 가능
    - 좋아요 기능

### 3. 실채훈 ( 실시간 채팅 훈수 )

> *채팅 서비스를 제공하여 실시간으로 다른 사용자들에게 조언을 구하는 공간*

- 기능
    - 실시간 라이브 채팅방 개설 가능
    - 이미지 업로드 가능
    - 동시 접속자 수 표시
    - 채팅 개설자 프로필 확인 가능

## 📍기본기능

---

### 1. 마이 페이지

> *팔로우, 팔로워를 확인할 수 있고, 나의 게시물을 확인할 수 있는 공간*

- 기능
    - 팔로워 리스트 확인
    - 팔로잉 리스트 확인
    - 프로필 수정
    - 로그아웃 가능
    - 좋아요 누른 게시물 확인, 디테일로 이동 가능
    - 작성한 Ootd 게시물 확인, 디테일로 이동 가능

### 2. 인증

> 사용자의 이전 접속여부를 통해 자동 로그인, 로그인 유지 기능 및 기능의 제한

- 기능
    - JWT토큰 발급
    - JWT토큰 유효 시 자동 로그인
    - 서비스 접근 시 유효성 검사
    - 토큰 만료 시, 재발급 및 재 로그인 유도


## 🎬 페이지 정보

---

### 1. 메인 화면

![메인1](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/9c92c91cd2b5dc68ad357372ecd3ebbd/%EB%A9%94%EC%9D%B81.PNG)

- OOtd에서 가장 많은 좋아요를 받은 게시물을 홈화면에 배치

### 2. OOTD

![ootd1](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/57a508d78445f7b769b09cf18de52467/ootd1.PNG)

- 이미지 업로드 기능

![ootd2](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/378556da5cc8f632afc7f9f3b914b330/ootd2.PNG)

- 댓글, 대댓글 및 좋아요 기능

### 3. 뭘입을까?

![뭘입을까1](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/70693982a7c1b3de98791625e5ef7cd6/%EB%AD%98%EC%9E%85%EC%9D%84%EA%B9%8C1.png)

- 이미지 업로드 기능, 투표기능을 활용한

        게시물 작성

![뭘입을까2](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/6c16727f19b717aeebb7a5c66a3810a4/%EB%AD%98%EC%9E%85%EC%9D%84%EA%B9%8C2.png)

- 댓글, 대댓글 및 좋아요 기능

### 4. 실채훈 ( 실시간 채팅 훈수 )

![실채훈](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/0925c89273721f45a379479e6c06670f/%EC%8B%A4%EC%B1%84%ED%9B%88.PNG)

- 이미지 업로드 기능
- 실시간 채팅기능
- 좋아요 기능

### 5. 마이 페이지

![마이페이지](https://lab.ssafy.com/s04-webmobile2-sub3/s04p13c102/uploads/83ce65062f38e7d58a4c741b787b152b/%EB%A7%88%EC%9D%B4%ED%8E%98%EC%9D%B4%EC%A7%80.PNG)

- 팔로워, 팔로잉 확인가능
- 내가 올린 OOTD게시물 전체 확인 가능
- 좋아요한 게시물 확인가능
- 프로필 수정가능
