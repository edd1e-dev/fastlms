# FastLMS

온라인 강좌 등록, 관리 웹입니다.

### 셋팅
* application.yml
    * spring.datasource의 url, username, password 수정
    * spring.mail의 host, username, password 수정
* 프로젝트 루트 폴더에 files 폴더를 생성해야 합니다.
* src의 baseLocalPath를 모두 로컬 환경에 맞게 수정해야 합니다.

### 의존성
Tomcat 8.5.84 (10 버전 사용  시 Deploy 무반응이 발생하니 주의해야 합니다.)

### 구현된 기능

✅ 회원가입 및 가입 인증메일 전송  
✅ 로그인 및 로그아웃  
✅ 비밀번호 찾기(비밀번호 초기화 기능)  
✅ 관리자(백오피스) 회원 관리  
✅ 관리자(백오피스) 카테고리 관리  
✅ 관리자(백오피스) 강좌 관리  
✅ 회원 로그인시 로그인 히스토리(로그) 기능  
✅ 관리자 회원 상세 정보에 로그인 목록 보기 기능  
✅ 배너관리(백오피스 기능)  
✅ 프론트 배너 노출 기능  