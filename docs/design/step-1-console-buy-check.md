# 1단계 1일차 - 자바 콘솔 기반 매입 가능 여부 판단 기능 설계

## 1. 사용자가 콘솔에서 입력해야 하는 값?

- 책 제목
- 책 상태 질문에 대한 답변

## 2. 프로그램이 사용자에게 보여줘야 하는 책 상태 질문은 무엇인가?

- 책에 2cm를 초과하는 심한 찢어짐이 있나요?
- 책에 곰팡이가 있나요?
- 책에 심한 오염 또는 심한 낙서가 있나요?
- 페이지 누락 또는 제본 불량으로 책이 분리되어 있나요?
- 책에 물에 젖은 흔적이 있나요?

## 3. 어떤 답변이면 매입불가로 판단해야 하는가?

- 2cm 초과의 심한 찢어짐 있음
- 곰팡이 있음
- 심한 오염 또는 심한 낙서가 있음
- 물에 젖은 흔적이 있음
- 페이지 누락 또는 제본 불량으로 책이 분리되어 있음.

## 4. 입력, 판단, 출력 책임은 각각 어떤 객체가 맡아야 하는가?

- InputView : 사용자 입력
- OutputView : 질문과 결과 출력
- BuyDecisionService : 매입 가능 여부 판단
- BuyDecisionResult : 매입 결과와 매입 불가 사유 저장

## 5. 나중에 상태 등급, 예상가 계산, AI 판매글 생성으로 확장하려면 지금 무엇을 미리 분리해야 하는가?

- 질문 목록을 Main에서 분리해야 함.
- InputView는 사용자 답변을 입력받는 역할만 담당하고, 해당 답변이 매입불가 사유에 해당하는지 판단하는 로직은 BuyDecisionService로 분리해야 함.
- 판단 결과를 BuyDecisionResult 객체로 분리해야 함.
- 결과 출력은 OutputView에서 담당하도록 분리해야 함.

# 1단계 2일차 - 콘솔 프로그램 기본 클래스 구조 설계

## 1. InputView

### 역할

- 사용자 입력을 받는다.
- 책 제목을 입력받는다.
- 책 상태 질문에 대한 답변을 입력받는다.

### 예상 메서드

- inputTitle()
- inputAnswer()

### 설계 기준

- 입력값을 다른 객체가 사용할 수 있어야 하므로 입력 메서드는 값을 반환해야 한다.
- InputView는 입력만 담당하고, 매입 가능 여부 판단은 하지 않는다.

---

## 2. OutputView

### 역할

- 질문을 출력한다.
- 매입 가능 여부 결과를 출력한다.
- 매입불가 사유를 출력한다.

### 설계 기준

- OutputView는 출력만 담당한다.
- 질문 목록을 만들거나 매입 가능 여부를 판단하지 않는다.
- 출력 메서드는 값을 반환하기보다 화면에 출력하는 책임에 집중한다.

---

## 3. BuyDecisionService

### 역할

- 사용자의 책 상태 응답을 바탕으로 매입 가능 여부를 판단한다.
- 매입불가 사유를 만든다.
- 판단 결과를 BuyDecisionResult로 반환한다.

### 받을 값

- List<BookConditionResponse>

### 반환할 값

- BuyDecisionResult

---

## 4. BuyDecisionResult

### 역할

- 매입 가능 여부를 저장한다.
- 매입불가 사유 목록을 저장한다.
- 결과 안내 메시지를 저장한다.

### 필요한 값

- buyable
- rejectReasons
- message

---

## 5. BookConditionResponse

### 역할

- 질문 1개와 그 질문에 대한 답변 1개를 연결한다.

### 필요한 값

- question
- answerType

### 추후 확장 후보

- etcText

---

## 6. AnswerType

### 역할

- 사용자의 답변 종류를 안전하게 표현한다.

### 값

- YES
- NO
- UNKNOWN
- OTHER

---

## 7. Main

### 역할

- 객체들을 생성한다.
- 입력 → 판단 → 출력 흐름을 연결한다.

### 하지 말아야 할 일

- 직접 입력 로직을 작성하지 않는다.
- 직접 매입 가능 여부를 판단하지 않는다.
- 직접 결과 출력 로직을 복잡하게 작성하지 않는다.


# 1단계 3일차 - 콘솔 기본 클래스 구조 및 메서드 시그니처 설계

## 1. 패키지 구조는 어떻게 나누는가?

```text
view
- InputView
- OutputView

service
- BuyDecisionService

domain
- BuyDecisionResult
- BookConditionResponse
- AnswerType

main
- Main
```

### 설계 기준

- `view`는 사용자 입력과 화면 출력 책임을 가진다.
- `service`는 매입 가능 여부 판단 로직을 가진다.
- `domain`은 판단에 필요한 데이터와 결과 객체를 가진다.
- `main`은 전체 실행 흐름을 연결한다.
- `BuyDecisionResult`와 `BookConditionResponse`는 결과와 데이터를 담는 객체이므로 `domain` 패키지에 둔다.

---

## 2. InputView

### 역할

- 사용자로부터 책 제목을 입력받는다.
- 책 상태 질문에 대한 답변을 입력받는다.
- 입력받은 값을 다른 객체가 사용할 수 있도록 반환한다.

### 예상 메서드

```java
String inputBookTitle()
AnswerType inputAnswer()
```

### 설계 기준

- `InputView`는 입력만 담당한다.
- `InputView`는 매입 가능 여부를 판단하지 않는다.
- `InputView`는 결과를 출력하지 않는다.
- `InputView`는 `BookConditionResponse`를 직접 만들지 않는다.
- `BookConditionResponse`를 만들려면 `question` 정보가 필요하므로, `InputView`가 질문까지 알면 책임이 커진다.
- 따라서 `InputView`는 사용자의 답변을 `AnswerType`으로 반환하는 것이 적절하다.

---

## 3. OutputView

### 역할

- 질문을 출력한다.
- 매입 가능 여부 결과를 출력한다.
- 매입불가 사유를 출력한다.
- 결과 안내 메시지를 출력한다.

### 예상 메서드

```java
void printQuestion(String question)
void printResult(BuyDecisionResult result)
void printRejectReasons(List<String> rejectReasons)
```

### 설계 기준

- `OutputView`는 출력만 담당한다.
- `OutputView`는 질문 목록을 만들지 않는다.
- `OutputView`는 매입 가능 여부를 판단하지 않는다.
- 질문을 출력하는 시점에는 아직 사용자의 답변이 없으므로, `BookConditionResponse`가 아니라 `String question`을 받는 것이 자연스럽다.
- 출력 메서드는 값을 반환하지 않고 화면에 출력하는 책임에 집중한다.

---

## 4. BuyDecisionService

### 역할

- 사용자의 책 상태 응답 목록을 바탕으로 매입 가능 여부를 판단한다.
- 매입불가 사유를 만든다.
- 판단 결과를 `BuyDecisionResult`로 반환한다.

### 예상 메서드

```java
BuyDecisionResult evaluate(List<BookConditionResponse> responses)
```

또는

```java
BuyDecisionResult evaluateBuyDecision(List<BookConditionResponse> responses)
```

### 설계 기준

- `BuyDecisionService`는 판단 로직만 담당한다.
- `BuyDecisionService`는 사용자 입력을 직접 받지 않는다.
- `BuyDecisionService`는 결과를 직접 출력하지 않는다.
- 여러 질문에 대한 응답을 판단해야 하므로 `List<BookConditionResponse>`를 입력받는다.
- 판단 결과는 `BuyDecisionResult` 객체로 반환한다.
- 클래스 이름이 이미 `BuyDecisionService`이므로, 메서드명은 `evaluate()`처럼 간결하게 가져가는 것도 가능하다.

---

## 5. BuyDecisionResult

### 역할

- 매입 가능 여부 판단 결과를 저장한다.
- 매입불가 사유 목록을 저장한다.
- 결과 안내 메시지를 저장한다.

### 필요한 값

```java
boolean buyable
List<String> rejectReasons
String message
```

### 설계 기준

- `boolean` 하나만 반환하면 매입불가 사유와 안내 메시지를 표현하기 어렵다.
- 매입불가 사유는 여러 개일 수 있으므로 `List<String>`으로 관리한다.
- 결과 안내 메시지는 매입 가능 여부에 따라 달라질 수 있으므로 결과 객체에 포함한다.

---

## 6. BookConditionResponse

### 역할

- 질문 1개와 그 질문에 대한 답변 1개를 연결한다.

### 필요한 값

```java
String question
AnswerType answerType
```

### 추후 확장 후보

```java
String etcText
```

### 설계 기준

- `BookConditionResponse` 하나는 질문 1개와 답변 1개를 의미한다.
- 여러 질문에 대한 응답은 `List<BookConditionResponse>`로 관리한다.
- `OTHER` 답변이 들어올 경우, 기타 내용을 저장하기 위해 `etcText`가 필요할 수 있다.

---

## 7. AnswerType

### 역할

- 사용자의 답변을 프로그램 내부에서 안전하게 표현한다.
- 사용자 입력 문자열을 정해진 enum 값으로 변환한다.

### 값

```java
YES
NO
UNKNOWN
OTHER
```

### 예상 메서드

```java
AnswerType fromInput(String input)
```

### 설계 기준

- 사용자 입력 문자열을 그대로 판단 로직에 사용하지 않는다.
- 내부에서는 `AnswerType`으로 변환해서 사용한다.
- `"예"`, `"아니오"`, `"모름"`, `"기타"` 같은 입력을 enum 값으로 변환한다.
- `changeAnswer(String answer)`보다 `fromInput(String input)`이 더 명확하다.
- 잘못된 입력이 들어왔을 때 어떻게 처리할지는 다음 구현 단계에서 결정한다.

---

## 8. Main

### 역할

- 객체들을 생성한다.
- 입력 → 판단 → 출력 흐름을 연결한다.

### 실행 흐름

```text
1. InputView, OutputView, BuyDecisionService 생성
2. 책 제목 입력
3. 질문 목록 준비
4. 질문 출력
5. 사용자 답변 입력
6. question + answerType을 묶어 BookConditionResponse 생성
7. List<BookConditionResponse> 생성
8. BuyDecisionService에 전달
9. BuyDecisionResult 반환
10. 결과 출력
```

### 하지 말아야 할 일

- 직접 입력 로직을 작성하지 않는다.
- 직접 매입 가능 여부를 판단하지 않는다.
- 직접 결과 출력 문구를 복잡하게 작성하지 않는다.
- 바코드 흐름을 현재 단계에 포함하지 않는다.

### 설계 기준

- `Main`은 전체 흐름을 조립하는 역할만 한다.
- 판단은 `BuyDecisionService`가 담당한다.
- 입력은 `InputView`가 담당한다.
- 출력은 `OutputView`가 담당한다.
- 1차 MVP에서는 바코드 흐름을 제외하고 질문 기반 매입 가능 여부 판단에 집중한다.

---

## 9. 오늘 결정한 핵심 설계 기준

- `BuyDecisionResult`와 `BookConditionResponse`는 `domain` 패키지에 둔다.
- `InputView`는 `BookConditionResponse`를 직접 만들지 않고, 사용자의 답변을 `AnswerType`으로 반환한다.
- 질문 출력 시점에는 아직 답변이 없으므로 `OutputView`의 질문 출력 메서드는 `String question`을 받는다.
- `AnswerType`은 사용자 입력 문자열을 enum으로 변환하는 메서드를 가진다.
- `BuyDecisionService`는 `List<BookConditionResponse>`를 받아 `BuyDecisionResult`를 반환한다.
- 매입불가 사유는 여러 개일 수 있으므로 `List<String>`으로 관리한다.
- 1차 MVP에서는 바코드 흐름을 `Main`에 포함하지 않고, 질문 기반 매입 가능 여부 판단에 집중한다.

---

## 10. 다음 구현 단계에서 고민할 점

- `BuyDecisionResult` 생성자는 어떤 값을 받아야 하는가?
- `BookConditionResponse` 생성자는 어떤 값을 받아야 하는가?
- `AnswerType.fromInput()`은 잘못된 입력을 어떻게 처리해야 하는가?
- `Main`에서 `question`과 `answerType`을 묶는 코드는 어디에 두는 것이 좋은가?
- `OutputView`의 결과 출력 메서드는 `BuyDecisionResult` 하나만 받아도 충분한가?
- 질문 목록은 `Main`에 둘 것인가, 별도 객체로 분리할 것인가?