# BudgetJourney 

# 프로젝트 명 : 거지나기

## **1. Intro**



### **배경**

'졸업을 부탁해' 프로젝트에서 영감을 받은 ‘거지나기’ 프로젝트입니다.

기존의 졸업 학점 계산 애플리케이션을 통해 학점을 쉽게 계산할 수 있었듯이, 이번 프로젝트’에서는 재정 관리와 예산 계획을 간편하게 할 수 있는 프로그램을 개발하고자 합니다.

대부분의 자취를 하는 학생 및 직장인들은 예상치 못한 지출로 인해 과소비하는 경향이 있습니다. 이를 해결하기 위해, 이 앱은 사용자에게 소득과 지출을 기록하고 월별 예산을 설정할 수 있는 기능을 제공합니다. 또한, 영수증이나 계좌 내역 등의 공식적인 소비 인증 자료를 첨부할 수 있어 재정 기록의 신뢰성을 높입니다.

소비 패턴을 분석하여 효율적인 예산 관리 서비스를 제공하며, 월별 사용자 간의 소비 습관 경쟁을 통해 참여율을 높이고 경제적 목표 달성을 위한 보상 시스템도 도입할 계획입니다.

이 앱을 통해 학생들과 직장인들이 예산을 효과적으로 관리하고 지출을 추적하여 경제적인 자립과 효율적인 재정 관리를 이루는 것을 목표로 합니다.

## **2. 기능**



### **1) 목표 금액 추가**

- 매달 사용할 **예산 목표 금액**을 설정하여 예산 관리 효율성 증대.

### **2) 개인 달력 기능**

- 월간 달력에서 **지출 내역 및 중요한 이벤트**를 확인할 수 있도록 제공.
- 날짜별로 지출 기록, 목표 금액 대비 진행 상황 표시.

### **3) 영수증 인증 사진 저장**

- 영수증 인증 사진을 개인 폴더에 저장.
- 사용자가 사진을 첨부하여 **지출 내역을 기록하는 습관** 형성 유도.
- 사진 업로드를 통한 가계부 작성의 귀찮음이 습관으로 전환되는 효과 기대.

### **4) 영수증이 없는 구매 관리**

- 사용자가 영수증이 없는 구매를 빠르게 기록할 수 있도록 **UI 제공**.
- 필수 입력 필드:
  - 구매 항목명
  - 지출 금액
  - 구매 날짜

### **5) 회원가입 및 닉네임 설정**

- 간편한 회원가입 기능 제공.
- **닉네임 설정**을 통해 개인화된 사용자 경험 제공.

### **6) 지출에 맞는 칭호 부여**

- 사용자의 **지난 달 지출 관리 수준**에 따라 칭호 부여.
- 예: “절약왕”, “가성비 고수”, “탕진잼 마스터” 등 재미있는 타이틀로 동기 부여.지출 패턴에 따라 **칭호 업데이트** 기능 추가로 지속적인 흥미 유발.

### 7) 친구 추가 기능

- 사용자간의 친구 추가 기능을 부여하여, 서로의 소비 패턴을 파악 가능.
- 경쟁 시스템을 만들어 서로 간의 경쟁심을 자극, 효과적인 절약을 도모

### **8) 지출 경고 한도 설정 및 알림 및 리마인딩**

- 매달 첫 시작날에 지출 한도를 사용자가 설정.
- 해당 지출 한도에 근접하거나, 넘어갈 경우 아래와 같은 메세지 표시.

  **재미있는 경고 메시지**

- “알림: 지갑이 탈진 상태입니다! 예산을 확인하고 쉬게 해주세요. 😅”
- “경고: ‘탕진잼’ 모드가 발동 중입니다! 절약 히어로로 돌아와 주세요!”
- “앗! 예산이 ‘빨간불’입니다. 절약 챌린지에 도전해보세요!”

**메시지 스타일**

- **긴급성 강조**: 경고색(빨강 또는 주황)과 주의를 끄는 아이콘(❗, ⚠️) 활용.
- **긍정적인 제안 포함**: 해결 방안 제시로 경고가 부정적으로 느껴지지 않도록.
- **개인화된 메시지**: “OO님, 이번 주 예산 관리가 조금 필요해 보여요!”

### **9) 종합적인 활동에 따른 지갑 방어력 지수 부여**

**1. 지갑 방어력 계산 로직**

- **기록 성실도**:
  월간/주간 기록 빈도 기준 점수 부여.
  예: 매일 기록 시 +10점, 매주 1회 기록 시 +5점.
- **예산 준수 여부**:
  목표 금액 대비 실제 지출을 기준으로 점수 부여.
  예산 초과 시 -10점, 예산 내 관리 시 +15점.
- **균형 잡힌 지출**:
  특정 카테고리(식비, 쇼핑, 교통비 등)에서 편중되지 않은 소비를 점수화.
  균형 잡힌 소비 패턴 발견 시 +10점.

**2. 지갑 방어력 등급**

- 점수 구간에 따라 등급 부여.
  - 0~30점: “초보 방어자”
  - 31~60점: “지출 관리자”
  - 61~90점: “절약 마스터”
  - 91점 이상: “재정 수호자”

## **3. 개발 중점**



### **User: User Friendly**

1. UI / UX 철학은 무조건 사용자 편의를 고려하고자 합니다.
2. 우선은 웹 앱을 고려하지만 향후 모바일 앱으로도 발전 가능성을 열어두고자 합니다.

### **Develop: Learn Each Other**

> 서로가 서로한테 배우고자 합니다. TIL 공유 플랫폼이 추구하는 가치는 서로의 지식을 공유하고 확장하고자 하는 욕구로 서로가 발전하는 것이라 생각하기 때문입니다. 이 프로젝트를 통해 서로 다른 지식과 기술 스택에 대해 서로 이해하고 공유하고 결합함으로써 하나의 서비스가 남겠지만, 우리한테는 모두가 노력한 지식이 남을 수 있다고 생각합니다.

### **개인의 성장과 팀의 목표는 하나**



> **개인은 팀의 성장으로, 팀은 개인의 성장으로 이어진다**는 철학을 중심으로 합니다.

> 프로젝트를 통해 쌓인 경험은 **미래의 발판**이 되며, 팀원 모두에게 **새로운 가능성**을 열어줍니다.



### **더 나은 서비스를 위해 배움을 멈추지 않는다**


> 서비스를 발전시키는 과정에서 **끊임없이 배우고 개선**하며, 성장과 혁신을 지속합니다.

> 사용자와 개발자 모두가 이 과정을 통해 **더 나은 가치와 경험**을 얻을 수 있습니다.


## **4. 기술 스택**



### **<Backend>**

**_Spring Framework_**

- Java 기반의 Spring 프레임워크를 활용하여 안정적이고 확장 가능한 백엔드 애플리케이션을 설계 및 구현.
- Spring Boot를 사용한 빠른 개발 환경 설정과 REST API 설계.
- Spring Data JPA를 활용하여 데이터베이스와의 효율적인 통신 및 관리.
- 확장 가능하고 유지보수 쉬운 모듈형 아키텍처 설계 경험.

**_Postman_**

- API 요청 모음 관리 및 테스트.
- API 공유 및 응답 결과 확인에 용이.

**_Docker_**

- 네트워크 설정 등 운영 환경 관리.
- Docker Compose로 서비스(백엔드, DB, 프론트엔드) 간 연동 관리.

**_AWS S3_**

- AWS S3를 활용한 이미지 및 파일 저장소 구성 경험.
- 비용 효율적인 스토리지와 데이터 보안 관리.
- S3 버킷과의 통합 및 Spring Framework 연동 경험.

**_AWS EC2_**

- EC2를 사용하여 가상 서버를 구축하고 보안 및 네트워킹을 구성하며 스토리지를 관리.

### <Frontend>

**_Vue.js & React_**

- Vue.js와 React를 활용하여 **모던 웹 애플리케이션 개발** 경험.
- **컴포넌트 기반 설계**와 **상태 관리 (Vuex, Redux)**를 통해 유지보수성과 확장성이 높은 프론트엔드 아키텍처 구현.
- React에서는 **Hooks**, **Context API**, **React Router**를 활용한 동적 라우팅 및 상태 관리.
- Vue.js에서는 **Composition API**를 통해 더욱 간결하고 재사용 가능한 코드 작성.

**_JavaScript (ES6+)_**

- ES6+ 문법(Arrow Functions, Destructuring, Template Literals 등)을 활용한 **모던 자바스크립트 코드 작성**.
- **Fetch API**와 **Axios**를 사용하여 RESTful API와 효율적으로 통신.
- **Promise**, **async/await** 등을 사용하여 비동기 로직을 간단하고 가독성 있게 구현.
- 코드의 일관성과 가독성을 위해 **모듈화**와 **함수형 프로그래밍 패턴**을 적용.

**_Redux_**

- Redux를 이용한 상태 관리로 대규모 애플리케이션의 데이터 흐름 최적화.
- Redux Toolkit, Middleware 등을 활용하여 코드 복잡도를 줄이고 효율적인 상태 관리 구현.

**_CSS (Sass & Styled-components)_**

- 사용자 친화적인 UI/UX 설계와 다양한 CSS 라이브러리 활용 경험.
- Sass 및 Styled-components를 이용한 동적 스타일링 구현.
- 반응형 디자인: CSS Grid, Flexbox 활용으로 다양한 디바이스 환경에 최적화된 레이아웃 제공.
- 다크 모드 및 테마 설정과 같은 고급 CSS 기능 활용 경험.
- 테일윈드와 달리 CSS와 로직이 하나의 컴포넌트로 통합되어 있기 때문에 코드의 가독성이 높고 유지보수에 능함.
- 테일윈드 프리셋에 제한되지 않고 고도로 커스터마이징된 스타일을 적용.

### **<Version Control>**

**_Git & GitHub_**

- Git을 활용한 체계적인 버전 관리 경험.
- 효율적인 소스 코드 관리와 히스토리 추적을 통해 프로젝트의 안정성 유지.
- GitHub를 통해 협업 환경에서의 효율적인 코드 리뷰 및 협업 프로세스 경험.
- 브랜치 전략: Feature 브랜치, Hotfix 브랜치, Release 브랜치 등 체계적인 브랜치 관리.
- Merge 관리: Pull Request 기반 워크플로우를 활용하여 코드 충돌 방지 및 원활한 통합.
- Issue Tracking: GitHub Issues를 사용하여 작업 분배 및 진행 상황을 투명하게 관리.
- 프로젝트 관리: GitHub Projects를 통해 전체적인 프로젝트 진행 상황을 시각적으로 추적.
