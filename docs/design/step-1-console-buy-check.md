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
- 최종 결과에 포함된 매입불가 사유 또는 안내 메시지를 출력한다.

### 설계 기준

- OutputView는 출력만 담당한다.
- 질문 목록을 만들거나 매입 가능 여부를 판단하지 않는다.
- 출력 메서드는 값을 반환하기보다 화면에 출력하는 책임에 집중한다.

---

## 3. BuyDecisionService

### 역할

- 사용자의 책 상태 응답 1개를 평가한다.
- 해당 응답이 매입불가 조건에 해당하는지 판단한다.
- 매입불가 조건이면 매입불가 사유를 담은 `ResponseEvaluationResult`를 반환한다.
- 매입불가 조건이 아니면 다음 질문으로 진행 가능한 `ResponseEvaluationResult`를 반환한다.

### 받을 값

- BookConditionResponse

### 반환할 값

- ResponseEvaluationResult

### 설계 기준

- 모든 응답을 한 번에 판단하지 않는다.
- 매입 불가 사유를 여러 개 누적하지 않는다.
- 답변 하나가 입력될 때마다 `BookConditionResponse` 하나를 평가한다.
- 매입불가 조건이 발견되면 즉시 판단을 종료할 수 있도록 평가 결과를 반환한다.
- 사용자 입력은 `InputView`가 담당한다.
- 결과 출력은 `OutputView`가 담당한다.
- 반복 흐름 제어는 `Main`이 담당한다.

---

## 4. BuyDecisionResult

### 역할

- 최종 매입 가능 여부를 저장한다.
- 매입불가일 경우 단일 매입불가 사유를 저장한다.
- 결과 안내 메시지를 저장한다.

### 필요한 값

- buyable
- rejectReason
- message

### 설계 기준

- 1차 MVP에서는 매입불가 사유를 여러 개 누적하지 않는다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 따라서 `List<String> rejectReasons`가 아니라 `String rejectReason`을 사용한다.
- 모든 질문을 통과하면 매입 가능 결과를 생성한다.

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
- 입력 → 응답 평가 → 즉시 종료 또는 다음 질문 진행 → 최종 출력 흐름을 연결한다.

### 하지 말아야 할 일

- 직접 입력 로직을 작성하지 않는다.
- 직접 매입 가능 여부를 판단하지 않는다.
- 직접 결과 출력 로직을 복잡하게 작성하지 않는다.
- 매입불가 사유를 여러 개 누적하지 않는다.


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
- ResponseEvaluationResult
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
- `ResponseEvaluationResult`는 응답 1개를 평가한 중간 결과 객체이므로 `domain` 패키지에 둔다.

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
- 결과 안내 메시지를 출력한다.

### 예상 메서드

```java
void printQuestion(String question)
void printResult(BuyDecisionResult result)
```

### 설계 기준

- `OutputView`는 출력만 담당한다.
- `OutputView`는 질문 목록을 만들지 않는다.
- `OutputView`는 매입 가능 여부를 판단하지 않는다.
- 질문을 출력하는 시점에는 아직 사용자의 답변이 없으므로, `BookConditionResponse`가 아니라 `String question`을 받는 것이 자연스럽다.
- 출력 메서드는 값을 반환하지 않고 화면에 출력하는 책임에 집중한다.
- 최종 결과에 포함된 매입불가 사유 또는 안내 메시지를 출력한다.

---

## 4. BuyDecisionService

### 역할

- 사용자의 책 상태 응답 1개를 평가한다.
- 해당 응답이 매입불가 조건에 해당하는지 판단한다.
- 매입불가 조건이면 매입불가 사유를 담은 `ResponseEvaluationResult`를 반환한다.
- 매입불가 조건이 아니면 다음 질문으로 진행 가능한 `ResponseEvaluationResult`를 반환한다.

### 예상 메서드

```java
ResponseEvaluationResult evaluateResponse(BookConditionResponse response)
```

### 설계 기준

- BuyDecisionService는 전체 응답 목록을 한 번에 판단하지 않는다.
- BuyDecisionService는 매입불가 사유 목록을 누적하지 않는다.
- BuyDecisionService는 응답 1개에 대한 판단 책임을 가진다.
- 사용자 입력을 직접 받지 않는다.
- 결과를 직접 출력하지 않는다.
- 반복 흐름 제어는 Main이 담당한다.

---

## 5. BuyDecisionResult

### 역할

- 최종 매입 가능 여부 판단 결과를 저장한다.
- 매입불가일 경우 단일 매입 불가 사유를 저장한다.
- 결과 안내 메시지를 저장한다.

### 필요한 값

```java
boolean buyable
String rejectReason
String message
```

### 설계 기준

- `boolean` 하나만 반환하면 매입불가 사유와 안내 메시지를 표현하기 어렵다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 상태 등급 판정 사유는 1차 MVP 이후 단계에서 추가한다.

---

## 6. ResponseEvaluationResult

### 역할

- `BookConditionResponse` 하나를 평가한 중간 결과를 저장한다.
- 현재 응답이 매입불가 조건에 해당하는지 표현한다.
- 매입불가일 경우 매입불가 사유를 저장한다.

### 필요한 값

```java
boolean rejected
String rejectReason
```

### 설계 기준
- String 하나만으로는 응답 평가 결과를 표현하기 어렵다.
- boolean 하나만으로는 매입불가 사유를 함께 표현하기 어렵다. 따라서 응답 1개 평가 결과를 별도 객체로 분리한다.
- rejected = true이면 현재 응답이 매입불가 조건에 해당한다.
- rejected = false이면 다음 질문으로 진행할 수 있다.
- 1차 MVP에서는 상태 등급 판정 사유를 포함하지 않는다.
- 상태 등급 판정은 다음 단계에서 확장한다.

## 7. BookConditionResponse

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

- `BookConditionResponse`는 질문 하나를 평가하기 위한 단위로 사용한다.
- `Main`은 질문마다 `BookConditionResponse`를 생성하고, 즉시 `BuyDecisionService`에 전달한다.
- `OTHER` 답변이 들어올 경우, 기타 내용을 저장하기 위해 `etcText`가 필요할 수 있다.

---

## 8. AnswerType

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

## 9. Main

### 역할

- 객체들을 생성한다.
- 입력 → 응답 평가 → 즉시 종료 또는 다음 질문 진행 → 최종 출력 흐름을 연결한다.

### 실행 흐름

```text
1. InputView, OutputView, BuyDecisionService, QuestionProvider를 생성
2. 책 제목 입력
3. QuestionProvider에서 질문 목록을 가져옴.
4. 질문을 하니씩 반복한다.
5. OutputView에서 질문을 출력한다.
6. InputView에서 사용자 답변을 입력받는다. 
7. question + answerType을 묶어 BookConditionResponse 생성한다.
8. BuyDecisionService.evaluateResponse(response)를 호출한다.
9. ResponseEvaluationResult가 rejected 상태면 매입불가 BuyDecisionResult를 생성한다.
10. 매입 불가 결과를 출력하고 프로그램 흐름을 종료한다.
11. rejected 상태가 아니면 다음 질문으로 넘어간다.
12. 모든 질문을 통과하면 매입 가능 BuyDecisionResult를 생성한다.
13. OutputView가 최종 결과를 출력한다.
```

### 하지 말아야 할 일

- 직접 입력 로직을 작성하지 않는다.
- 직접 매입 가능 여부를 판단하지 않는다.
- 직접 결과 출력 문구를 복잡하게 작성하지 않는다.
- 바코드 흐름을 현재 단계에 포함하지 않는다.
- 매입불가 사유를 여러 개 누적하지 않는다.

### 설계 기준

- `Main`은 전체 흐름을 조립하는 역할만 한다.
- 판단은 `BuyDecisionService`가 담당한다.
- 입력은 `InputView`가 담당한다.
- 출력은 `OutputView`가 담당한다.
- 질문 목록 제공은 QuestionProvider가 담당한다.
- Main은 ResponseEvaluationResult를 보고 반복을 계속할지 종료할지만 결정한다.
- `ResponseEvaluationResult`는 `BuyDecisionService`가 생성한다.
- `BuyDecisionResult`는 전체 흐름을 제어하는 `Main`에서 생성한다.
- 추후 결과 생성 로직이 복잡해지면 `BuyDecisionResult`의 정적 팩토리 메서드나 별도 Factory로 분리할 수 있다.
- 모든 질문을 통과하면 매입 가능 BuyDecisionResult를 생성한다.
- 1차 MVP에서는 바코드 흐름을 제외하고 질문 기반 매입 가능 여부 판단에 집중한다.

---

## 10. 오늘 결정한 핵심 설계 기준

- `BuyDecisionResult`, `ResponseEvaluationResult`, `BookConditionResponse`는 `domain` 패키지에 둔다.
- `InputView`는 `BookConditionResponse`를 직접 만들지 않고, 사용자의 답변을 `AnswerType`으로 반환한다.
- 질문 출력 시점에는 아직 답변이 없으므로 `OutputView`의 질문 출력 메서드는 `String question`을 받는다.
- `AnswerType`은 사용자 입력 문자열을 enum으로 변환하는 메서드를 가진다.
- `BuyDecisionService`는 `BookConditionResponse` 하나를 받아 현재 응답이 매입불가 조건에 해당하는지 평가한다.
- 응답 1개의 평가 결과는 `ResponseEvaluationResult`로 표현한다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 매입불가 사유는 1차 MVP에서 단일 사유만 사용한다.
- 최종 `BuyDecisionResult`는 매입불가 발견 시 또는 모든 질문 통과 시 생성한다.
- 1차 MVP에서는 상태 등급 판정 사유를 포함하지 않는다.
- 상태 등급 판정은 다음 단계에서 확장한다.
- 1차 MVP에서는 바코드 흐름을 `Main`에 포함하지 않고, 질문 기반 매입 가능 여부 판단에 집중한다.
---

## 11. 다음 구현 단계에서 고민할 점

- `BuyDecisionResult` 생성자는 어떤 값을 받아야 하는가?
- `BookConditionResponse` 생성자는 어떤 값을 받아야 하는가?
- `AnswerType.fromInput()`은 잘못된 입력을 어떻게 처리해야 하는가?
- `Main`에서 `question`과 `answerType`을 묶는 코드는 어디에 두는 것이 좋은가?
- `OutputView`의 결과 출력 메서드는 `BuyDecisionResult` 하나만 받아도 충분한가?
- 질문 목록은 `Main`에 둘 것인가, 별도 객체로 분리할 것인가?
- `ResponseEvaluationResult` 생성자는 어떤 값을 받아야 하는가?
- `ResponseEvaluationResult`는 매입불가가 아닐 때 `rejectReason`을 어떻게 둘 것인가?
- `BuyDecisionResult`는 매입 가능 결과와 매입불가 결과를 어떻게 구분할 것인가?
- 매입불가 즉시 종료 흐름에서 `break`를 사용할 것인가, `return`을 사용할 것인가?
- `QuestionProvider`는 어느 패키지에 둘 것인가?
- 최종 `BuyDecisionResult`는 어느 시점에 생성하는 것이 자연스러운가?

---

# 1단계 4일차 - 클래스 생성 방향 및 생성자 설계

## 1. 오늘 과제 목적

1단계 4일차 과제에서는 실제 Java 클래스 파일을 생성하기 전에,
각 객체가 어떤 생성자와 필드를 가져야 하는지 최종적으로 설계했다.

이번 단계의 핵심은 전체 기능 구현이 아니라,
`BookConditionResponse`, `BuyDecisionResult`, `AnswerType`, `QuestionProvider`, `Main`의 역할과 생성 방향을 명확히 정리하는 것이다.

---

## 2. BookConditionResponse 생성자 설계

### 역할

`BookConditionResponse`는 질문 1개와 그 질문에 대한 답변 1개를 연결하는 객체다.

### 생성자 방향

```java
BookConditionResponse(String question, AnswerType answerType)
```

### 받을 값

- `question`
- `answerType`

### 이유

- `BookConditionResponse` 하나는 질문 1개와 답변 1개를 묶어야 한다.
- 따라서 생성 시점에 `question`과 `answerType`을 함께 받는 것이 자연스럽다.
- 사용자가 어떤 질문에 어떤 답변을 했는지 연결할 수 있어야 `BuyDecisionService`가 판단할 수 있다.

---

## 3. etcText 위치 설계

### 결정

`etcText`는 `AnswerType`이 아니라 `BookConditionResponse`에 둔다.

### 이유

- `AnswerType.OTHER`는 “기타를 선택했다”는 답변 종류만 의미한다.
- 사용자가 입력한 기타 설명은 특정 질문에 대한 응답에 속한다.
- 따라서 실제 기타 설명은 `BookConditionResponse`의 필드로 두는 것이 자연스럽다.

### 추후 확장 방향

```java
BookConditionResponse(String question, AnswerType answerType, String etcText)
```

### 설계 기준

- 현재 1차 MVP에서는 `question`, `answerType`만 사용한다.
- `OTHER` 답변에 대한 구체적인 설명이 필요해지는 시점에 `etcText`를 추가한다.

---

## 4. BuyDecisionResult 생성 방식 설계

### 역할

`BuyDecisionResult`는 최종 매입 가능 여부 판단 결과를 담는 객체다.

### 필요한 값

```java
boolean buyable
String rejectReason
String message
```

### 생성자 방향

```java
BuyDecisionResult(boolean buyable, String rejectReason, String message)
```

### 이유

- 판단 결과 객체는 생성된 뒤 값이 자주 바뀌기보다, 생성 시점에 필요한 값을 모두 받는 것이 안전하다.
- 1차 MVP에서는 매입불가 사유를 여러 개 누적하지 않는다.
- 매입불가 조건이 발견되면 즉시 판단을 종료하므로 단일 rejectReason만 있으면 충분하다.
- setter로 값을 하나씩 변경하는 방식은 결과 객체의 일관성을 해칠 수 있다.

### 설계 기준

- 1차 MVP에서는 생성자에서 `buyable`, `rejectReason`, `message`를 모두 받는 방향으로 설계한다.
- 매입 가능 결과용 메서드와 매입 불가 결과용 메서드는 추후 리팩터링 후보로 둔다.
- 상태 등급 판정 사유는 다음 단계에서 확장한다.

---

## 5. AnswerType.fromInput() 입력 처리 방향

### 역할

`AnswerType.fromInput(String input)`은 사용자 입력 문자열을 `AnswerType` enum 값으로 변환한다.

### 변환 대상

```text
예     → YES
아니오 → NO
모름   → UNKNOWN
기타   → OTHER
```

### 잘못된 입력 처리 방향

잘못된 입력은 `UNKNOWN`으로 처리하지 않고, 다시 입력받도록 한다.

### 이유

- 잘못된 입력은 사용자의 실수일 수 있다.
- 잘못된 입력을 `UNKNOWN`으로 처리하면 사용자의 실제 의도와 다른 결과가 나올 수 있다.
- 매입 가능 여부를 정확하게 판단하려면 올바른 답변을 다시 입력받는 것이 좋다.

### 설계 기준

- `AnswerType.fromInput()`은 입력값을 enum으로 변환하는 책임을 가진다.
- 다시 입력받는 반복 흐름은 `InputView`에서 처리하는 방향을 고려한다.
- 잘못된 입력을 어떻게 알릴지는 구현 단계에서 결정한다.

---

## 6. InputView에서 fromInput 호출 여부

### 결정

`InputView.inputAnswer()` 내부에서 `AnswerType.fromInput()`을 호출할 수 있다.

### 이유

- `InputView`는 사용자 입력을 받는 역할을 가진다.
- 사용자가 입력한 문자열을 프로그램 내부에서 사용할 수 있는 `AnswerType`으로 변환해야 한다.
- 변환 자체의 기준은 `AnswerType`이 가지고, 입력 반복 흐름은 `InputView`가 담당하는 구조가 자연스럽다.

### 설계 기준

- `InputView`는 매입 가능 여부를 판단하지 않는다.
- `InputView`는 `BookConditionResponse`를 직접 만들지 않는다.
- `InputView`는 사용자 입력 문자열을 받아 `AnswerType`으로 변환된 값을 반환한다.

---

## 7. QuestionProvider 설계

### 역할

`QuestionProvider`는 책 상태 질문 목록을 제공하는 객체다.

### 반환 타입

```java
List<String>
```

### 이유

- 질문을 제공하는 시점에는 아직 사용자의 답변이 없다.
- 따라서 `List<BookConditionResponse>`가 아니라 `List<String>`을 반환하는 것이 자연스럽다.
- 1차 MVP에서는 질문 문장만 있으면 질문 출력과 응답 수집이 가능하다.

### 추후 확장 후보

나중에 질문별 매입불가 사유, 질문 ID, 상태 등급 계산 기준 등이 필요해지면 `Question` 객체로 확장할 수 있다.

```java
List<Question>
```

### 설계 기준

- 1차 MVP에서는 단순하게 `List<String>`으로 시작한다.
- 질문 목록은 `Main`에서 직접 관리하지 않고 별도 클래스로 분리한다.
- DB 저장이나 서버 조회는 이후 고도화 단계에서 고려한다.

---

## 8. Main에서 BookConditionResponse 생성 여부

### 결정

현재 단계에서는 `Main`에서 `question + answerType`을 묶어 `BookConditionResponse`를 생성하는 것을 허용한다.

### 이유

- 1차 MVP 범위에서는 흐름이 단순하다.
- 지금 `ResponseFactory` 같은 별도 객체를 만들면 오히려 구조가 복잡해질 수 있다.
- 현재 목표는 최소 클래스 구조를 이해하고 작성하는 것이다.

### 설계 기준

- `Main`은 전체 흐름을 조립한다.
- 현재 단계에서는 `BookConditionResponse` 생성까지 허용한다.
- 나중에 질문/응답 조립 로직이 복잡해지면 별도 객체로 분리한다.

---

## 9. 매입불가 즉시 종료 흐름

### 결정

ReBook 콘솔 MVP에서는 매입불가 사유를 여러 개 누적하지 않는다.
각 질문에 대한 답변을 평가했을 때 매입불가 조건이 발견되면 즉시 판단을 종료한다.

### 처리 흐름

1. 질문을 하나 출력한다.
2. 사용자가 답변을 입력한다.
3. 질문과 답변을 묶어 `BookConditionResponse`를 생성한다.
4. `BuyDecisionService`가 현재 응답 1개를 평가한다.
5. 평가 결과를 `ResponseEvaluationResult`로 반환한다.
6. `ResponseEvaluationResult`가 매입불가 상태라면 즉시 `BuyDecisionResult`를 생성한다.
7. 매입불가 결과를 출력하고 질문 반복을 종료한다.
8. 매입불가가 아니라면 다음 질문으로 진행한다.
9. 모든 질문을 통과하면 매입 가능 `BuyDecisionResult`를 생성한다.

### 설계 기준

- `BookConditionResponse` 하나는 질문 1개와 답변 1개를 의미한다.
- 매입불가 여부는 답변 하나가 끝날 때마다 평가한다.
- 매입불가 사유는 여러 개 누적하지 않는다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 응답 1개의 평가 결과는 `ResponseEvaluationResult`로 표현한다.
- 최종 `BuyDecisionResult`는 매입불가 발견 시 또는 모든 질문 통과 시 생성한다.

---

## 10. 오늘 결정한 핵심 설계 기준

- `BookConditionResponse`는 `question`과 `answerType`을 생성자로 받는다.
- `etcText`는 `AnswerType`이 아니라 `BookConditionResponse`에 둔다.
- 잘못된 입력은 `UNKNOWN`으로 처리하지 않고 다시 입력받도록 한다.
- `InputView`는 `AnswerType.fromInput()`을 호출할 수 있다.
- `QuestionProvider`는 1차 MVP에서 `List<String>`을 반환한다.
- `Main`에서 `BookConditionResponse`를 생성하는 것은 현재 단계에서 허용한다.
- `ResponseFactory`는 아직 만들지 않는다.
- 1차 MVP에서는 단순한 구조를 우선하고, 복잡해지는 시점에 분리한다.
- `BuyDecisionResult`는 `buyable`, `rejectReason`, `message`를 생성자에서 모두 받는다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 매입불가 사유는 1차 MVP에서 단일 사유만 사용한다.
- 응답 1개의 평가 결과는 `ResponseEvaluationResult`로 표현한다.
- 최종 `BuyDecisionResult`는 매입불가 발견 시 또는 모든 질문 통과 시 생성한다.
- `BuyDecisionService`는 응답 1개를 평가하는 책임을 가진다.

---

## 11. 다음 과제에서 할 일

다음 과제에서는 오늘 정리한 설계를 바탕으로 실제 Java 클래스 파일을 생성한다.

### 다음 과제 범위

- `domain` 패키지 생성
- `AnswerType` enum 생성
- `BookConditionResponse` 클래스 생성
- `ResponseEvaluationResult` 클래스 생성
- `BuyDecisionResult` 클래스 생성
- `QuestionProvider` 클래스 생성
- `InputView`, `OutputView`, `BuyDecisionService`, `Main` 클래스 생성
- 필드, 생성자, getter, 메서드 시그니처 작성

### 다음 과제 전 생각할 질문

- `ResponseEvaluationResult`의 필드는 모두 `final`로 둘 수 있을까?
- `ResponseEvaluationResult`는 매입불가가 아닐 때 `rejectReason`을 어떻게 표현할까?
- `BuyDecisionResult`는 매입 가능 결과와 매입불가 결과를 어떻게 구분할까?
- `BuyDecisionResult`에서 `rejectReason`은 언제 사용될까?
- `QuestionProvider`는 어느 패키지에 두는 것이 좋을까?
- `Main`은 매입불가 즉시 종료 흐름을 어떻게 표현할까?

---

# 설계 보완 - 매입불가 즉시 종료 흐름 반영 (5일차)

## 1. 변경 배경

기존 설계에서는 각 답변을 평가하면서 매입불가 사유 목록을 누적하고,
모든 질문이 끝난 뒤 최종 `BuyDecisionResult`를 생성하는 흐름을 고려했다.

하지만 1차 MVP의 목표는 사용자가 빠르게 매입 가능 여부를 확인하는 것이다.
따라서 매입불가 조건이 하나라도 발견되면 더 이상 질문을 진행하지 않고,
즉시 매입불가 결과를 반환하는 방식이 더 적절하다고 판단했다.

---

## 2. 변경된 판단 흐름

```text
질문 목록 준비
→ 질문 1개 출력
→ 사용자 답변 입력
→ BookConditionResponse 생성
→ BuyDecisionService가 응답 1개 평가
→ 매입불가 조건이면 즉시 BuyDecisionResult 생성 후 종료
→ 매입불가 조건이 아니면 다음 질문 진행
→ 모든 질문을 통과하면 매입 가능 BuyDecisionResult 생성
→ 결과 출력
```

---

## 3. 매입불가 즉시 종료 기준

1차 MVP에서는 매입불가 사유를 여러 개 누적하지 않는다.

```text
매입불가 사유 발견
→ 즉시 판단 종료
→ 매입불가 결과 출력
```

이 방식은 사용자가 불필요한 질문에 계속 답하지 않아도 된다는 장점이 있다.

단, 추후 고도화 단계에서는 책 상태 전체 진단을 위해 모든 매입불가 사유를 누적해서 보여주는 방식으로 확장할 수 있다.

---

## 4. BuyDecisionService 책임 수정

### 기존에 고려했던 책임

- 모든 응답을 한 번에 판단한다.
- 또는 매입불가 사유를 누적한다.

### 수정된 책임

- `BookConditionResponse` 하나를 평가한다.
- 해당 응답이 매입불가 조건인지 판단한다.
- 매입불가라면 평가 결과에 매입불가 사유를 담는다.
- 매입불가가 아니라면 다음 질문으로 진행 가능한 결과를 반환한다.

### 메서드 후보

```java
ResponseEvaluationResult evaluateResponse(BookConditionResponse response)
```

### 설계 기준

- `BuyDecisionService`는 사용자 입력을 직접 받지 않는다.
- `BuyDecisionService`는 화면에 결과를 출력하지 않는다.
- `BuyDecisionService`는 응답 1개에 대한 판단 책임을 가진다.
- `BuyDecisionService`는 매입불가 사유 목록을 직접 누적하지 않는다.
- 최종 흐름 제어는 `Main`이 담당한다.

---

## 5. ResponseEvaluationResult 도입

`String` 하나만으로는 응답 1개의 평가 결과를 표현하기 어렵다.

필요한 정보는 다음과 같다.

```text
- 매입불가 여부
- 매입불가 사유
- 다음 질문으로 진행 가능한지 여부
```

따라서 응답 1개 평가 결과를 담는 객체를 도입한다.

### 역할

```text
ResponseEvaluationResult
= BookConditionResponse 하나를 평가한 결과
```

### 1차 MVP 필드 후보

```java
boolean rejected
String rejectReason
```

### 추후 확장 후보

```java
BookConditionResponse response
String gradeReason
```

### 설계 기준

- 1차 MVP에서는 상태 등급 판정 사유를 구현하지 않는다.
- 상태 등급 판정은 다음 단계에서 확장한다.
- 현재는 매입불가 여부와 매입불가 사유 표현에 집중한다.
- 매입불가가 아닌 경우에는 `rejected = false`로 표현한다.

---

## 6. BuyDecisionResult 구조 수정

기존 고려했던 구조:

```java
boolean buyable
List<String> rejectReasons
String message
```

하지만 1차 MVP에서는 매입불가 사유가 발견되면 즉시 종료하므로,
여러 개의 매입불가 사유 목록이 필요하지 않다.

### 1차 MVP 추천 구조

```java
boolean buyable
String rejectReason
String message
```

### 추후 확장 후보

상태 등급 판정까지 포함하는 단계에서는 아래 구조로 확장할 수 있다.

```java
boolean buyable
String rejectReason
List<String> gradeReasons
String message
```

### 설계 기준

- 매입불가일 경우 `rejectReason`에 단일 사유를 담는다.
- 매입가능일 경우 `rejectReason`은 비어 있거나 사용하지 않는다.
- 상태 등급 판정 사유는 1차 MVP 이후 단계에서 추가한다.
- 1차 MVP에서는 `List<String> rejectReasons`를 사용하지 않는다.

---

## 7. Main 실행 흐름 수정

`Main`은 전체 흐름을 조립하고 반복을 제어한다.

```text
1. InputView, OutputView, BuyDecisionService, QuestionProvider를 생성한다.
2. 책 제목을 입력받는다.
3. QuestionProvider에서 질문 목록을 가져온다.
4. 질문을 하나씩 반복한다.
5. OutputView가 질문을 출력한다.
6. InputView가 사용자의 답변을 입력받는다.
7. question + answerType을 묶어 BookConditionResponse를 생성한다.
8. BuyDecisionService.evaluateResponse(response)를 호출한다.
9. 평가 결과가 매입불가이면 BuyDecisionResult를 생성한다.
10. OutputView가 매입불가 결과를 출력한다.
11. 프로그램 흐름을 종료한다.
12. 매입불가가 아니면 다음 질문으로 넘어간다.
13. 모든 질문을 통과하면 매입 가능 BuyDecisionResult를 생성한다.
14. OutputView가 매입 가능 결과를 출력한다.
```

### 설계 기준

- `Main`은 직접 입력 로직을 작성하지 않는다.
- `Main`은 직접 출력 문구를 복잡하게 작성하지 않는다.
- `Main`은 판단 조건을 직접 가지지 않는다.
- `Main`은 반복 흐름과 객체 연결만 담당한다.
- `Main`은 `ResponseEvaluationResult`를 보고 반복을 종료할지 계속할지만 결정한다.

---

## 8. QuestionProvider 도입

질문 목록은 `Main`에서 직접 관리하지 않고 별도 객체로 분리한다.

### 역할

```text
QuestionProvider
= 책 상태 질문 목록을 제공하는 객체
```

### 메서드 후보

```java
List<String> getQuestions()
```

### 설계 기준

- 1차 MVP에서는 `List<String>`으로 질문 목록을 반환한다.
- 질문별 ID, 등급 기준, 매입불가 사유 연결은 추후 확장한다.
- 나중에 DB 또는 서버에서 질문을 가져오는 구조로 변경할 수 있다.

---

## 9. 오늘 이후 과제 반영 기준

앞으로 ReBook 콘솔 MVP 과제에서는 아래 기준을 따른다.

- 매입불가 사유를 여러 개 누적하지 않는다.
- 매입불가 조건이 발견되면 즉시 판단을 종료한다.
- 매입불가가 아닌 경우 다음 질문으로 진행한다.
- 모든 질문을 통과하면 매입 가능 결과를 생성한다.
- `BuyDecisionService`는 응답 1개를 평가한다.
- `ResponseEvaluationResult`를 도입해 응답 1개의 평가 결과를 표현한다.
- `BuyDecisionResult`는 1차 MVP에서 `buyable`, `rejectReason`, `message`를 가진다.
- 1차 MVP에서는 상태 등급 판정 사유를 구현하지 않는다.
- 상태 등급 판정은 다음 단계에서 확장한다.
- `QuestionProvider`는 질문 목록 제공 책임을 가진다.

# 1단계 6일차 - Java 클래스 파일 생성 및 메서드 시그니처 작성

## 1. 오늘 과제 목적

1단계 6일차 과제에서는 지금까지 설계한 내용을 바탕으로 실제 Java 콘솔 프로젝트 환경을 만들고,
ReBook 콘솔 MVP에 필요한 기본 클래스 파일과 메서드 시그니처를 작성했다.

이번 단계의 핵심은 전체 로직 구현이 아니라,
앞으로 매입 가능 여부 판단 흐름을 연결할 수 있도록 **패키지 구조, 클래스 구조, 필드, 생성자, getter, 메서드 시그니처**를 준비하는 것이다.

추가로 사용자 입력 문자열을 `AnswerType` enum으로 변환하는 기초 입력 처리 흐름까지 구현했다.

---

## 2. 오늘 생성한 패키지 구조

```text
src/main/java/com/rebook
├─ Main.java
├─ domain
│  ├─ AnswerType.java
│  ├─ BookConditionResponse.java
│  ├─ ResponseEvaluationResult.java
│  └─ BuyDecisionResult.java
├─ provider
│  └─ QuestionProvider.java
├─ service
│  └─ BuyDecisionService.java
└─ view
   ├─ InputView.java
   └─ OutputView.java
```

### 패키지별 책임

- `domain`: 판단에 필요한 데이터와 결과 객체를 둔다.
- `provider`: 질문 목록을 제공한다.
- `service`: 매입 가능 여부 판단 로직을 담당한다.
- `view`: 사용자 입력과 화면 출력을 담당한다.
- `Main`: 전체 객체를 연결하고 실행 흐름을 제어한다.

---

## 3. AnswerType 구현

### 역할

`AnswerType`은 사용자의 답변을 프로그램 내부에서 안전하게 표현하는 enum이다.

### 현재 값

```java
YES, NO, OTHER
```

### 현재 구현 기준

```java
public enum AnswerType {
    YES, NO, OTHER;

    public static AnswerType fromInput(String input) {
        return switch (input) {
            case "1" -> YES;
            case "2" -> NO;
            case "3" -> OTHER;
            default -> null;
        };
    }
}
```

### 입력 변환 규칙

```text
1 → YES
2 → NO
3 → OTHER
그 외 입력 → null
```

### 설계 기준

- 사용자 입력 문자열을 그대로 판단 로직에 넘기지 않는다.
- 내부에서는 `AnswerType`으로 변환해서 사용한다.
- 잘못된 입력은 `UNKNOWN`으로 처리하지 않는다.
- 잘못된 입력은 `InputView`에서 다시 입력받는다.
- `OTHER`는 사용자가 의도적으로 기타를 선택한 경우에만 사용한다.

---

## 4. InputView 구현

### 역할

`InputView`는 사용자 입력을 담당한다.

### 현재 구현 내용

- `Scanner`를 필드로 한 번 생성해서 재사용한다.
- `inputBookTitle()`로 책 제목을 입력받는다.
- `inputAnswer()`로 답변을 입력받는다.
- 입력 문자열을 `AnswerType.fromInput()`으로 변환한다.
- 잘못된 입력이면 올바른 입력이 들어올 때까지 다시 입력받는다.

### 현재 구현 기준

```java
private final Scanner input = new Scanner(System.in);

public String inputBookTitle() {
    return input.nextLine();
}

public AnswerType inputAnswer() {
    AnswerType answerType = AnswerType.fromInput(input.nextLine());

    while (answerType == null) {
        System.out.println("1,2,3 중 하나를 입력해주세요.");
        answerType = AnswerType.fromInput(input.nextLine());
    }

    return answerType;
}
```

### 설계 기준

- `InputView`는 입력만 담당한다.
- `InputView`는 매입 가능 여부를 판단하지 않는다.
- `InputView`는 `BookConditionResponse`를 직접 만들지 않는다.
- 잘못된 입력을 `OTHER`로 처리하지 않는다.
- 현재 콘솔 MVP에서는 잘못된 입력 안내 메시지를 `InputView`에 두지만, 추후 역할 분리를 강화하면 `OutputView`로 옮길 수 있다.

---

## 5. BookConditionResponse 구현

### 역할

`BookConditionResponse`는 질문 1개와 답변 1개를 연결하는 객체다.

### 현재 필드

```java
private final String question;
private final AnswerType answerType;
```

### 설계 기준

- 필드는 `final`로 둔다.
- 생성 시점에 질문과 답변을 함께 받는다.
- `BuyDecisionService`가 질문과 답변을 확인할 수 있도록 getter를 제공한다.
- 1차 MVP에서는 `etcText`를 사용하지 않는다.

---

## 6. ResponseEvaluationResult 구현

### 역할

`ResponseEvaluationResult`는 응답 1개를 평가한 중간 결과 객체다.

### 현재 필드

```java
private final boolean rejected;
private final String rejectReason;
```

### 설계 기준

- 필드는 `final`로 둔다.
- `rejected = true`이면 현재 응답이 매입불가 조건에 해당한다.
- `rejected = false`이면 다음 질문으로 진행할 수 있다.
- 매입불가가 아닐 때 `rejectReason`은 빈 문자열 `""`로 표현한다.
- 응답 평가 결과는 매입불가 여부와 관계없이 항상 객체로 반환한다.

---

## 7. BuyDecisionResult 구현

### 역할

`BuyDecisionResult`는 최종 매입 가능 여부 판단 결과를 담는 객체다.

### 현재 필드

```java
private final boolean buyable;
private final String rejectReason;
private final String message;
```

### 설계 기준

- 필드는 `final`로 둔다.
- 매입 가능 여부는 `buyable`로 구분한다.
- 매입불가일 경우 `rejectReason`에 단일 사유를 담는다.
- 매입가능일 경우 `rejectReason`은 빈 문자열 `""`로 둔다.
- 출력에 필요한 메시지는 `message`에 담는다.

---

## 8. QuestionProvider 구현

### 역할

`QuestionProvider`는 책 상태 질문 목록을 제공한다.

### 현재 질문 목록

```text
- 2cm 초과의 심한 찢어짐이 있나요?
- 곰팡이가 있나요?
- 심한 오염 또는 심한 낙서가 있나요?
- 물에 젖은 흔적이 있나요?
- 페이지 누락 또는 제본 불량으로 책이 분리되어 있나요?
```

### 설계 기준

- 질문 목록은 `Main`에서 직접 관리하지 않는다.
- 1차 MVP에서는 `List<String>`으로 질문 목록을 제공한다.
- 질문 목록은 `List.of()`를 사용해 고정 목록으로 관리한다.
- 추후 질문 ID, 매입불가 사유, 등급 기준 등이 필요해지면 `Question` 객체로 확장할 수 있다.

---

## 9. BuyDecisionService 구현 상태

### 역할

`BuyDecisionService`는 `BookConditionResponse` 하나를 평가한다.

### 현재 구현 상태

현재는 메서드 시그니처와 임시 반환값만 작성했다.

```java
public ResponseEvaluationResult evaluateResponse(BookConditionResponse bookConditionResponse) {
    return new ResponseEvaluationResult(false, "");
}
```

### 설계 기준

- 사용자 입력을 직접 받지 않는다.
- 결과를 직접 출력하지 않는다.
- 반복 흐름을 제어하지 않는다.
- 응답 1개에 대한 평가만 담당한다.
- 실제 매입불가 판단 로직은 다음 단계 이후 구현한다.

---

## 10. OutputView 구현 상태

### 역할

`OutputView`는 질문과 최종 결과를 출력한다.

### 현재 메서드 시그니처

```java
void printQuestion(String question)
void printResult(BuyDecisionResult result)
```

### 설계 기준

- 출력만 담당한다.
- 질문 목록을 직접 만들지 않는다.
- 매입 가능 여부를 판단하지 않는다.
- `BuyDecisionResult`를 받아 최종 결과를 출력한다.
- 다음 과제에서 실제 출력 내용을 구현한다.

---

## 11. Main 구현 상태

### 현재 상태

`Main`은 아직 전체 흐름을 연결하지 않고, 실행 확인과 흐름 주석만 작성했다.

### 현재 흐름 주석

```text
1. 책 제목 입력
2. 책 질문 목록 가져오기
3. 질문 반복
4. 질문 출력
5. 답변 입력
6. BookConditionResponse 생성
7. BuyDecisionService로 응답 평가
8. rejected == true이면 BuyDecisionResult 생성 후 break
9. 모든 질문 통과 시 매입 가능 BuyDecisionResult 생성
10. 결과 출력
```

### 설계 기준

- `Main`은 전체 흐름을 조립한다.
- 직접 입력 로직을 작성하지 않는다.
- 직접 출력 문구를 복잡하게 작성하지 않는다.
- 직접 판단 조건을 가지지 않는다.
- 다음 단계에서 객체 생성과 흐름 연결을 구현한다.

---

## 12. 오늘 결정한 핵심 구현 기준

- `AnswerType`은 `YES`, `NO`, `OTHER`만 사용한다.
- 잘못된 입력은 `UNKNOWN`으로 처리하지 않는다.
- 사용자 입력 문자열은 `AnswerType.fromInput()`으로 변환한다.
- `fromInput()`은 변환만 담당한다.
- 다시 입력받는 반복 흐름은 `InputView`가 담당한다.
- `Scanner`는 `InputView`의 필드로 한 번 생성해서 재사용한다.
- `InputView`는 `BookConditionResponse`를 만들지 않는다.
- `BuyDecisionService`는 아직 실제 판단 로직을 구현하지 않는다.
- `OutputView`는 다음 과제에서 실제 출력 기능을 구현한다.
- `Main`은 다음 과제에서 전체 흐름 연결을 준비한다.

---

## 13. 다음 과제에서 할 일

### 다음 과제명

1단계 7일차 - `OutputView` 출력 기능 구현 및 `Main` 흐름 연결 준비

### 다음 과제 범위

- `OutputView.printQuestion(String question)` 구현
- `OutputView.printResult(BuyDecisionResult result)` 구현
- `InputView`의 잘못된 입력 안내 메시지를 계속 유지할지, `OutputView`로 분리할지 검토
- `Main`에서 객체 생성 흐름 준비
- `Main`에서 질문 목록 조회, 질문 반복, 답변 입력 흐름을 연결할 준비

### 다음 과제 전 설계 질문

- `OutputView`는 단순히 출력만 담당해야 할까?
- `OutputView`가 `BuyDecisionResult`의 상태를 보고 출력 문구를 선택해도 될까?
- 잘못된 입력 안내 메시지는 `InputView`에 남겨둘까, `OutputView`로 분리할까?
- `Main`은 객체 생성과 흐름 제어를 어디까지 담당해야 할까?
- `BuyDecisionResult`는 생성자로 직접 만들까, 정적 팩토리 메서드를 둘까?

---


# 1단계 7일차 - OutputView 출력 기능 구현 및 Main 흐름 연결 준비

## 1. 오늘 과제 목적

1단계 7일차 과제에서는 `OutputView`의 출력 기능을 구현하고, `Main`에서 전체 실행 흐름을 연결했다.

이번 단계의 핵심은 전체 판단 로직을 완성하는 것이 아니라, 지금까지 설계한 객체들을 실제 실행 흐름 안에서 연결하는 것이다.

특히 `Main`은 직접 매입불가 조건을 판단하지 않고, `BuyDecisionService`가 반환한 `ResponseEvaluationResult`를 보고 반복을 계속할지 종료할지만 결정하도록 설계했다.

---

## 2. OutputView 구현

### 역할

`OutputView`는 콘솔 화면에 질문과 최종 결과를 출력하는 객체다.

### 구현한 메서드

```java
void printQuestion(String question)
void printResult(BuyDecisionResult result)
```

---

## 3. printQuestion(String question) 구현

### 역할

`printQuestion(String question)`은 사용자에게 책 상태 질문을 출력한다.

### 출력 형태

```text
책에 곰팡이가 있나요?
1. 예
2. 아니오
3. 기타
```

### 설계 기준

- `OutputView`는 질문을 출력한다.
- 질문마다 답변 선택지를 함께 출력한다.
- 질문 목록을 직접 만들지 않는다.
- 사용자 입력을 받지 않는다.
- 매입 가능 여부를 판단하지 않는다.
- 선택지를 매번 출력해서 사용자가 입력 규칙을 잊지 않도록 한다.

---

## 4. printResult(BuyDecisionResult result) 구현

### 역할

`printResult(BuyDecisionResult result)`는 최종 매입 가능 여부 결과를 출력한다.

### 출력 기준

최종 결과 메시지는 항상 출력한다.

```java
System.out.println(result.getMessage());
```

매입불가 결과일 경우에는 매입불가 사유도 함께 출력한다.

```java
if (!result.isBuyable()) {
    System.out.println("사유: " + result.getRejectReason());
}
```

### 매입불가 출력 예시

```text
매입할 수 없습니다.
사유: 곰팡이가 있어 매입할 수 없습니다.
```

### 설계 기준

- `OutputView`는 `BuyDecisionResult`를 화면에 보여준다.
- `message`는 항상 출력한다.
- `buyable == false`인 경우에만 `rejectReason`을 출력한다.
- 이 조건문은 매입 판단 로직이 아니라 출력 형식 결정이다.
- 실제 매입 가능 여부 판단은 `BuyDecisionService`가 담당한다.

---

## 5. Main 흐름 연결

### 현재 연결된 흐름

```text
ReBook 콘솔 MVP 시작
→ 책 제목 입력
→ 질문 목록 조회
→ 질문 반복
→ 질문 출력
→ 답변 입력
→ BookConditionResponse 생성
→ BuyDecisionService.evaluateResponse() 호출
→ rejected이면 매입불가 BuyDecisionResult 생성 후 반복 종료
→ 모든 질문 통과 시 매입 가능 BuyDecisionResult 생성
→ OutputView가 최종 결과 출력
```

---

## 6. Main의 책임

`Main`은 전체 흐름을 조립한다.

### 담당하는 일

- `InputView` 생성
- `OutputView` 생성
- `QuestionProvider` 생성
- `BuyDecisionService` 생성
- 책 제목 입력 호출
- 질문 목록 조회
- 질문 반복
- `OutputView.printQuestion(question)` 호출
- `InputView.inputAnswer()` 호출
- `BookConditionResponse` 생성
- `BuyDecisionService.evaluateResponse(response)` 호출
- `ResponseEvaluationResult`를 보고 반복 종료 여부 결정
- 최종 `BuyDecisionResult` 생성
- `OutputView.printResult(finalResult)` 호출

### 담당하지 않는 일

- 직접 사용자 입력을 받지 않는다.
- 직접 질문 목록을 만들지 않는다.
- 직접 매입불가 조건을 판단하지 않는다.
- 직접 결과 출력 문구를 복잡하게 관리하지 않는다.
- 매입불가 사유를 여러 개 누적하지 않는다.

---

## 7. Main에서 BuyDecisionResult 생성 시점

### 매입불가 결과 생성

질문 반복 중 `BuyDecisionService`가 반환한 `ResponseEvaluationResult`가 매입불가 상태이면 즉시 최종 결과를 생성하고 반복을 종료한다.

```java
if (evaluationResult.isRejected()) {
    finalResult = new BuyDecisionResult(
            false,
            evaluationResult.getRejectReason(),
            "매입할 수 없습니다."
    );
    break;
}
```

### 매입 가능 결과 생성

매입 가능 결과는 질문 하나를 통과했다고 바로 생성하지 않는다.

모든 질문을 끝까지 통과한 뒤, 즉 `for` 반복문이 끝난 뒤에 생성한다.

```java
if (finalResult == null) {
    finalResult = new BuyDecisionResult(
            true,
            "",
            "매입 가능합니다."
    );
}
```

### 설계 기준

- 매입불가 결과는 매입불가 조건이 발견된 즉시 생성한다.
- 매입 가능 결과는 모든 질문을 통과한 뒤 생성한다.
- 최종 결과 출력은 질문 반복이 끝난 뒤 한 번만 수행한다.
- `Main`은 매입불가 조건 자체를 직접 판단하지 않는다.
- `Main`은 `ResponseEvaluationResult.isRejected()`만 보고 흐름을 제어한다.

---

## 8. 오늘 결정한 핵심 설계 기준

- 질문과 선택지는 `OutputView.printQuestion()`에서 함께 출력한다.
- 최종 결과는 `OutputView.printResult()`에서 출력한다.
- 매입불가 사유는 매입불가 결과일 때만 출력한다.
- `OutputView`의 조건문은 판단 로직이 아니라 출력 형식 결정으로 본다.
- `Main`은 `BuyDecisionService`의 평가 결과만 보고 흐름을 제어한다.
- `Main`은 직접 매입불가 조건을 판단하지 않는다.
- 매입 가능 결과는 모든 질문을 통과한 뒤 생성한다.
- 최종 결과 출력은 질문 반복이 끝난 뒤 한 번만 수행한다.
- `BuyDecisionService`는 반복문 안에서 매번 생성하지 않고 한 번만 생성해 재사용한다.
- 책 제목 입력값은 변수에 저장한다.
- 현재 단계에서는 `Main`에서 `BuyDecisionResult`를 직접 생성한다.
- 정적 팩토리 메서드는 결과 생성 로직이 복잡해지는 시점에 도입을 고려한다.

---

## 9. 현재까지의 객체 책임 정리

### InputView

- 책 제목을 입력받는다.
- 책 상태 질문에 대한 답변을 입력받는다.
- 사용자 입력 문자열을 `AnswerType`으로 변환한다.
- 잘못된 입력이면 다시 입력받는다.
- 매입 가능 여부를 판단하지 않는다.

### OutputView

- 질문을 출력한다.
- 답변 선택지를 출력한다.
- 최종 결과 메시지를 출력한다.
- 매입불가일 경우 매입불가 사유를 출력한다.
- 매입 가능 여부 자체를 판단하지 않는다.

### QuestionProvider

- 책 상태 질문 목록을 제공한다.
- 질문 목록을 `Main`에서 직접 관리하지 않도록 분리한다.

### BuyDecisionService

- `BookConditionResponse` 하나를 평가한다.
- 해당 응답이 매입불가 조건인지 판단한다.
- `ResponseEvaluationResult`를 반환한다.
- 입력과 출력은 담당하지 않는다.
- 반복 흐름 제어는 담당하지 않는다.

### Main

- 전체 실행 흐름을 연결한다.
- 질문 반복을 제어한다.
- 입력, 출력, 판단 객체를 연결한다.
- `ResponseEvaluationResult`를 보고 반복 종료 여부를 결정한다.
- 최종 `BuyDecisionResult`를 생성한다.
- 직접 매입불가 조건을 판단하지 않는다.

---

## 10. 현재 단계에서 남은 개선 후보

- `Main`에서 책 제목을 출력하는 부분은 추후 `OutputView`로 옮길 수 있다.
- `OutputView.printResult()` 앞뒤에 빈 줄을 넣어 콘솔 가독성을 높일 수 있다.
- `Main`의 흐름 주석은 학습 단계에서는 유지하고, 추후 코드가 안정되면 줄일 수 있다.
- `BuyDecisionResult` 생성 코드가 복잡해지면 정적 팩토리 메서드를 고려할 수 있다.
- `finalResult == null` 방식은 현재 단계에서는 적절하지만, 추후 결과 흐름이 복잡해지면 별도 결과 생성 객체로 분리할 수 있다.

---

## 11. 다음 과제에서 할 일

다음 과제에서는 `BuyDecisionService`의 실제 매입불가 판단 로직을 구현한다.

### 다음 과제명

1단계 8일차 - `BuyDecisionService` 실제 매입불가 판단 로직 구현

### 다음 과제 범위

- `BuyDecisionService.evaluateResponse(BookConditionResponse response)` 실제 구현
- `AnswerType.YES` 답변일 때 매입불가 조건으로 판단할지 검토
- 질문 문자열 기반 판단을 사용할지 검토
- 매입불가 사유 문자열 관리 위치 결정
- `ResponseEvaluationResult(true, reason)` 반환 흐름 구현
- 매입불가가 아닐 때 `ResponseEvaluationResult(false, "")` 반환 흐름 유지

### 다음 과제 전 생각할 질문

- `BuyDecisionService`는 질문 문자열을 보고 판단해야 할까?
- 질문마다 고유한 식별자나 조건 정보를 가진 객체가 필요할까?
- 1차 MVP에서는 문자열 기반 판단이 허용될까?
- `AnswerType.YES`일 때만 매입불가 조건으로 볼 수 있을까?
- 매입불가 사유 문자열은 어디에서 관리하는 것이 좋을까?

---

# 1단계 8일차 - BuyDecisionService 실제 매입불가 판단 로직 구현

## 1. 오늘 과제 목적

1단계 8일차 과제에서는 `BuyDecisionService`가 실제로 매입불가 여부를 판단하도록 구현했다.

이전 단계까지는 `Main`에서 질문 반복, 답변 입력, `BookConditionResponse` 생성, `BuyDecisionService.evaluateResponse()` 호출, 최종 결과 출력 흐름까지 연결했다.

이번 단계의 핵심은 `evaluateResponse()` 내부에서 응답 1개를 평가하고, 매입불가 여부와 사유를 `ResponseEvaluationResult`로 반환하는 것이다.

---

## 2. 오늘 구현한 메서드

```java
public ResponseEvaluationResult evaluateResponse(BookConditionResponse response)
```

이 메서드는 사용자의 응답 1개를 평가한다.

### 입력

```java
BookConditionResponse response
```

`BookConditionResponse`는 질문 1개와 답변 1개를 함께 가진다.

```text
question
answerType
```

### 출력

```java
ResponseEvaluationResult
```

`ResponseEvaluationResult`는 응답 1개에 대한 평가 결과를 나타낸다.

```text
rejected
rejectReason
```

---

## 3. 기본 판단 흐름

`evaluateResponse()`의 기본 판단 흐름은 다음과 같다.

```text
1. answerType이 YES가 아니면 rejected=false 반환
2. answerType이 YES이면 질문 문자열을 확인
3. 질문 내용에 맞는 매입불가 사유 결정
4. 매입불가 조건에 해당하면 rejected=true 반환
5. 어떤 조건에도 걸리지 않으면 rejected=false 반환
```

---

## 4. YES가 아닌 답변 처리

현재 1차 MVP에서는 `AnswerType.NO`와 `AnswerType.OTHER`를 매입불가로 보지 않는다.

따라서 `YES`가 아닌 답변은 즉시 통과 처리한다.

```java
if (response.getAnswerType() != AnswerType.YES) {
    return new ResponseEvaluationResult(false, "");
}
```

### 설계 기준

- `YES`는 매입불가 조건에 해당할 가능성이 있다.
- `NO`는 현재 질문에 해당 사항이 없다는 의미이므로 통과한다.
- `OTHER`는 1차 MVP에서는 별도 보류 처리하지 않고 통과한다.
- 추후에는 `OTHER`에 대해 추가 설명 입력 또는 보류 상태를 도입할 수 있다.

---

## 5. 질문 문자열 기반 매입불가 판단

현재 1차 MVP에서는 별도의 `Question` 객체나 `questionId`를 사용하지 않는다.

따라서 질문 문자열에 포함된 핵심 단어를 기준으로 매입불가 사유를 결정한다.

### 찢어짐 조건

```java
if (response.getQuestion().contains("찢어짐")) {
    return new ResponseEvaluationResult(true, "2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다.");
}
```

### 곰팡이 조건

```java
else if (response.getQuestion().contains("곰팡이")) {
    return new ResponseEvaluationResult(true, "곰팡이가 있어 매입할 수 없습니다.");
}
```

### 오염 또는 낙서 조건

```java
else if (response.getQuestion().contains("오염")
        || response.getQuestion().contains("낙서")) {
    return new ResponseEvaluationResult(true, "심한 오염 또는 심한 낙서가 있어 매입할 수 없습니다.");
}
```

### 물에 젖은 흔적 조건

```java
else if (response.getQuestion().contains("물")
        || response.getQuestion().contains("젖은")) {
    return new ResponseEvaluationResult(true, "물에 젖은 흔적이 있어 매입할 수 없습니다.");
}
```

### 페이지 누락 또는 제본 불량 조건

```java
else if (response.getQuestion().contains("페이지 누락")
        || response.getQuestion().contains("제본 불량")
        || response.getQuestion().contains("분리")) {
    return new ResponseEvaluationResult(true, "페이지 누락 또는 제본 불량으로 책이 분리되어 매입할 수 없습니다.");
}
```

---

## 6. 기본 통과 처리

`YES` 답변이 들어왔더라도 어떤 매입불가 조건에도 매칭되지 않으면 통과 처리한다.

```java
return new ResponseEvaluationResult(false, "");
```

현재 질문 목록이 모두 매입불가 조건 질문이기 때문에 일반적으로는 이 코드까지 도달하지 않는다.

다만 예외적인 질문이 추가되거나, 질문 문자열이 변경될 가능성을 대비해 기본 반환값을 둔다.

---

## 7. 오늘 결정한 핵심 설계 기준

- `BuyDecisionService`는 입력을 직접 받지 않는다.
- `BuyDecisionService`는 결과를 직접 출력하지 않는다.
- `BuyDecisionService`는 `BookConditionResponse` 하나를 평가한다.
- `BuyDecisionService`는 `ResponseEvaluationResult`를 반환한다.
- 현재 질문 목록은 모두 매입불가 조건 질문이다.
- `AnswerType.YES`이면 질문 문자열을 기준으로 매입불가 여부를 판단한다.
- `AnswerType.NO`와 `AnswerType.OTHER`는 현재 1차 MVP에서는 통과 처리한다.
- 매입불가 사유 문자열은 현재 단계에서는 `BuyDecisionService` 안에서 관리한다.
- 질문 문자열 판단은 1차 MVP에서는 허용하지만, 추후 개선 대상이다.

---

## 8. 현재 방식의 장점과 한계

### 장점

- 구현이 단순하다.
- 현재 MVP 범위에 적합하다.
- `Question` 객체를 추가하지 않아도 빠르게 동작 흐름을 완성할 수 있다.
- `Main`과 `View`의 책임을 침범하지 않는다.

### 한계

- 질문 문장이 바뀌면 `contains()` 조건이 깨질 수 있다.
- 매입불가 사유가 `BuyDecisionService` 안에 직접 들어 있어 조건이 많아질수록 코드가 길어진다.
- 질문과 사유의 관계가 한 곳에서 명확하게 관리되지 않는다.
- 추후 상태 등급, 예상 가격, 보류 상태 등이 추가되면 현재 구조만으로는 부족할 수 있다.

---

## 9. 추후 개선 후보

추후에는 아래 방식으로 개선할 수 있다.

### Question 객체 도입

```text
Question
- id
- content
- rejectReason
```

질문 문장과 매입불가 사유를 함께 관리할 수 있다.

### enum 기반 질문 타입 도입

```text
BookConditionQuestion
- TORN
- MOLD
- POLLUTION
- WET
- PAGE_MISSING
```

문자열 비교 대신 고정된 타입으로 판단할 수 있다.

### 정적 팩토리 메서드 도입

`ResponseEvaluationResult` 생성 코드를 더 명확하게 만들 수 있다.

```java
ResponseEvaluationResult.rejected("곰팡이가 있어 매입할 수 없습니다.");
ResponseEvaluationResult.passed();
```

---

## 10. 다음 과제에서 할 일

다음 과제에서는 전체 콘솔 실행 흐름을 테스트한다.

### 다음 과제명

1단계 9일차 - 전체 실행 흐름 테스트 및 콘솔 시나리오 점검

### 다음 과제 범위

- 모든 질문에 `2. 아니오` 입력 시 매입 가능 결과가 나오는지 확인
- 첫 번째 질문에 `1. 예` 입력 시 즉시 매입불가로 종료되는지 확인
- 곰팡이 질문에 `1. 예` 입력 시 곰팡이 사유가 출력되는지 확인
- 오염/낙서 질문에 `1. 예` 입력 시 해당 사유가 출력되는지 확인
- 물에 젖은 흔적 질문에 `1. 예` 입력 시 해당 사유가 출력되는지 확인
- 페이지 누락/제본 불량 질문에 `1. 예` 입력 시 해당 사유가 출력되는지 확인
- 잘못된 입력 시 다시 입력받는지 확인
- `3. 기타` 입력 시 현재 MVP 기준으로 통과 처리되는지 확인

---

# 1단계 9일차 - 전체 실행 흐름 테스트 및 콘솔 시나리오 점검

## 1. 오늘 과제 목적

1단계 9일차 과제에서는 ReBook 콘솔 MVP의 전체 실행 흐름을 실제 콘솔에서 테스트했다.

이번 단계의 핵심은 새 기능을 추가하는 것이 아니라, 지금까지 구현한 객체들이 함께 연결되어 의도한 흐름대로 동작하는지 확인하는 것이다.

---

## 2. 테스트 대상 흐름

오늘 테스트한 전체 흐름은 다음과 같다.

```text
책 제목 입력
→ 질문 출력
→ 답변 입력
→ BookConditionResponse 생성
→ BuyDecisionService.evaluateResponse() 호출
→ 매입불가이면 즉시 종료
→ 매입불가가 아니면 다음 질문 진행
→ 모든 질문 통과 시 매입 가능 결과 출력
```

---

## 3. 테스트 시나리오

| 번호 | 테스트 시나리오 | 입력값 | 기대 결과 | 실제 결과 | 통과 여부 | 수정 필요 여부 |
|---|---|---|---|---|---|---|
| 1 | 모든 질문 통과 | 2, 2, 2, 2, 2 | 매입 가능합니다. | 매입 가능합니다. | O | X |
| 2 | 첫 번째 질문에서 매입불가 | 1 | 찢어짐 사유와 함께 매입불가 출력 | 찢어짐 사유와 함께 매입불가 출력 | O | X |
| 3 | 곰팡이 질문에서 매입불가 | 2, 1 | 곰팡이 사유와 함께 매입불가 출력 | 곰팡이 사유와 함께 매입불가 출력 | O | X |
| 4 | 오염/낙서 질문에서 매입불가 | 2, 2, 1 | 오염/낙서 사유와 함께 매입불가 출력 | 오염/낙서 사유와 함께 매입불가 출력 | O | X |
| 5 | 물/젖은 흔적 질문에서 매입불가 | 2, 2, 2, 1 | 물에 젖은 흔적 사유와 함께 매입불가 출력 | 물에 젖은 흔적 사유와 함께 매입불가 출력 | O | X |
| 6 | 페이지 누락/제본 불량 질문에서 매입불가 | 2, 2, 2, 2, 1 | 페이지 누락/제본 불량 사유와 함께 매입불가 출력 | 페이지 누락/제본 불량 사유와 함께 매입불가 출력 | O | X |
| 7 | 기타 입력 처리 | 3, 3, 3, 3, 3 | 매입 가능합니다. | 매입 가능합니다. | O | X |
| 8 | 잘못된 입력 처리 | 5 → abc → 2 | 재입력 메시지 출력 후 정상 입력 처리 | 재입력 메시지 출력 후 매입 가능합니다. | O | X |

---

## 4. 테스트 결과 해석

모든 테스트 시나리오가 통과했다.

이를 통해 현재 콘솔 MVP의 핵심 흐름은 정상 동작한다고 판단한다.

확인된 내용은 다음과 같다.

- 모든 질문을 통과하면 매입 가능 결과가 출력된다.
- 매입불가 조건에서 `1. 예`를 입력하면 즉시 종료된다.
- 매입불가 사유가 질문별로 올바르게 출력된다.
- `3. 기타`는 현재 MVP 기준으로 통과 처리된다.
- 잘못된 입력은 재입력 메시지를 출력하고 다시 입력받는다.

---

## 5. 오늘 결정한 핵심 기준

- 현재 단계에서는 수동 콘솔 테스트로 전체 흐름을 검증한다.
- 테스트 결과는 표 형태로 기록한다.
- 정상 흐름과 예외 흐름을 모두 확인한다.
- 모든 시나리오가 통과했으므로 현재 단계에서 코드 수정은 필요하지 않다.
- 다음 단계에서는 구조 리팩터링 후보를 점검한다.

---

## 6. 현재 방식의 장점과 한계

### 장점

- 실제 사용자가 입력하는 흐름을 직접 확인했다.
- 정상 흐름과 매입불가 흐름을 모두 검증했다.
- 잘못된 입력 처리까지 확인했다.
- 현재 콘솔 MVP가 실제로 동작한다는 것을 확인했다.

### 한계

- 사람이 직접 입력하는 수동 테스트이므로 반복하기 어렵다.
- 입력 시나리오가 늘어나면 테스트 시간이 길어진다.
- 출력 문구 변경 여부를 자동으로 검증하지는 못한다.
- 추후에는 JUnit 기반 자동 테스트가 필요하다.

---

## 7. 추후 개선 후보

- `BuyDecisionService` 단위 테스트 도입
- `AnswerType.fromInput()` 단위 테스트 도입
- `InputView` 입력 검증 흐름 테스트 방법 고민
- `QuestionProvider` 질문 목록 검증
- 콘솔 출력 결과를 자동으로 검증하는 방식 검토
- 수동 테스트 시나리오를 자동 테스트로 전환

---

## 8. 다음 과제에서 할 일

다음 과제에서는 현재까지 구현한 1차 콘솔 MVP 구조를 점검하고, 리팩터링 후보를 구분한다.

### 다음 과제명

1단계 10일차 - 1차 콘솔 MVP 구조 리팩터링 후보 점검

### 다음 과제 범위

- 지금 바로 수정해야 할 부분과 나중에 미룰 부분 구분
- 문자열 `contains()` 기반 판단의 한계 점검
- `QuestionProvider` 구조 개선 필요성 검토
- `OTHER` 답변 처리 방식 재검토
- 수동 테스트를 자동 테스트로 바꿀 시점 검토

---

# 1단계 10일차 - 1차 콘솔 MVP 구조 리팩터링 후보 점검

## 1. 오늘 과제 목적

1단계 10일차 과제에서는 현재까지 구현한 1차 콘솔 MVP 구조를 점검하고, 리팩터링 후보를 분류했다.

이번 단계의 핵심은 코드를 바로 수정하는 것이 아니라, 지금 바로 고쳐야 할 부분과 나중에 미뤄도 되는 부분을 구분하는 것이다.

---

## 2. 현재 콘솔 MVP 상태

현재 콘솔 MVP는 아래 흐름까지 동작한다.

```text
책 제목 입력
→ 질문 출력
→ 답변 입력
→ BookConditionResponse 생성
→ BuyDecisionService가 응답 평가
→ 매입불가이면 즉시 종료
→ 모든 질문 통과 시 매입 가능 결과 출력
```

이전 테스트에서 정상 흐름, 매입불가 즉시 종료, 기타 입력, 잘못된 입력 재입력 처리가 모두 통과했다.

---

## 3. 리팩터링 후보 정리

| 리팩터링 후보 | 현재 문제 | 지금 수정 여부 | 이유 | 다음 액션 |
|---|---|---|---|---|
| `BuyDecisionService` 문자열 `contains()` 판단 | 질문 문장이 바뀌면 판단 로직이 깨질 수 있음 | 다음 과제에서 수정 | 질문과 사유가 이미 연결되어 있으므로 구조 개선 필요 | `BookConditionQuestion` 객체 도입 |
| `QuestionProvider`의 `List<String>` 구조 | 질문과 매입불가 사유가 분리되어 있음 | 다음 과제에서 수정 | 질문과 사유를 한 객체로 묶으면 Service가 단순해짐 | `List<BookConditionQuestion>` 반환 구조 검토 |
| `ResponseEvaluationResult(false, "")` | 빈 문자열이 의미를 명확히 드러내지는 않음 | 지금은 유지 | 1차 MVP에서는 매입불가 아님을 표현하기에 충분함 | 추후 `passed()`, `rejected(reason)` 정적 팩토리 검토 |
| `OTHER` 통과 처리 | 기타 답변이 실제로는 애매한 상태일 수 있음 | 지금은 유지 | 1차 MVP는 매입불가 여부만 판단하므로 범위 밖 | 추후 추가 설명 입력 또는 보류 상태 검토 |
| 수동 콘솔 테스트 | 사람이 직접 입력해야 해서 반복이 어려움 | 지금은 유지 | 구조 리팩터링 후 단위 테스트를 도입하는 게 효율적 | `BuyDecisionService`부터 JUnit 테스트 도입 |
| `Main`에서 `BuyDecisionResult` 직접 생성 | 결과 생성 책임이 Main에 일부 있음 | 지금은 유지 | 현재 흐름이 단순하고 학습 단계에서는 명확함 | 추후 정적 팩토리 또는 결과 생성 메서드 검토 |

---

## 4. 다음 과제에서 수정할 것

다음 과제에서는 아래 항목을 실제로 수정한다.

```text
1. BookConditionQuestion 객체 도입
2. QuestionProvider가 List<BookConditionQuestion>을 반환하도록 변경
3. BookConditionResponse가 String question 대신 BookConditionQuestion을 가지도록 검토
4. BuyDecisionService에서 contains() 판단 제거
5. YES이면 question.getRejectReason()으로 매입불가 결과 반환
```

---

## 5. 지금은 유지할 것

아래 항목은 현재 1차 MVP에서는 유지한다.

```text
1. ResponseEvaluationResult(false, "")
2. OTHER 통과 처리
3. Main에서 BuyDecisionResult 직접 생성
4. 수동 테스트 방식
```

---

## 6. 나중에 검토할 것

아래 항목은 다음 단계 이후에 검토한다.

```text
1. ResponseEvaluationResult 정적 팩토리 메서드
2. OTHER 추가 설명 입력
3. 상태 등급 판정
4. JUnit 자동 테스트
5. Question enum화
```

---

## 7. 오늘 결정한 핵심 설계 기준

- 질문과 매입불가 사유는 함께 변하는 데이터다.
- 함께 변하는 데이터는 하나의 객체로 묶는 것이 좋다.
- `BuyDecisionService`가 질문 문자열을 직접 해석하는 구조는 개선 대상이다.
- 하지만 모든 개선을 한 번에 적용하지 않는다.
- 다음 리팩터링의 중심은 `BookConditionQuestion` 도입이다.

---

## 8. 다음 과제에서 할 일

### 다음 과제명

1단계 11일차 - `BookConditionQuestion` 도입을 통한 질문/사유 구조 리팩터링

### 다음 과제 범위

- `BookConditionQuestion` 클래스 설계
- 질문 문장과 매입불가 사유를 하나의 객체로 묶기
- `QuestionProvider` 반환 타입 변경 검토
- `BookConditionResponse` 구조 변경 검토
- `BuyDecisionService`에서 문자열 `contains()` 판단 제거 검토

---

# 1단계 11일차 - BookConditionQuestion 도입을 통한 질문/사유 구조 리팩터링

## 1. 오늘 과제 목적

1단계 11일차 과제에서는 질문 문장과 매입불가 사유를 하나의 객체로 묶는 리팩터링을 진행했다.

기존 구조에서는 `QuestionProvider`가 질문 문장만 제공하고, `BuyDecisionService`가 질문 문자열을 분석해 매입불가 사유를 결정했다.

이 방식은 질문 문장이 바뀌면 Service의 조건문도 함께 바뀌어야 한다는 문제가 있었다.

---

## 2. 기존 구조의 문제

기존 구조는 다음과 같았다.

```text
QuestionProvider
→ 질문 문장만 제공

BuyDecisionService
→ 질문 문자열 contains()로 분석
→ 매입불가 사유 결정
```

이 구조의 문제는 다음과 같다.

```text
1. 질문 문장과 매입불가 사유가 분리되어 있다.
2. 질문 문장이 바뀌면 Service의 조건문도 같이 수정해야 한다.
3. BuyDecisionService가 판단뿐 아니라 질문 문장 해석까지 담당한다.
```

---

## 3. 개선 후 구조

개선 후 구조는 다음과 같다.

```text
BookConditionQuestion
→ 질문 문장
→ 매입불가 사유

QuestionProvider
→ List<BookConditionQuestion> 제공

BookConditionResponse
→ BookConditionQuestion + AnswerType

BuyDecisionService
→ YES인지 확인
→ YES이면 질문 객체의 매입불가 사유로 결과 반환
```

---

## 4. 변경된 객체 책임

### BookConditionQuestion

```text
책 상태 질문 하나를 표현한다.
질문 문장과, 해당 질문이 YES일 때의 매입불가 사유를 가진다.
```

### QuestionProvider

```text
BookConditionQuestion 목록을 제공한다.
질문 문장과 매입불가 사유의 관계를 한 곳에서 관리한다.
```

### BookConditionResponse

```text
사용자의 응답 하나를 표현한다.
어떤 질문에 대해 어떤 답변을 했는지 가진다.
```

### BuyDecisionService

```text
사용자 응답을 평가한다.
YES가 아니면 통과 결과를 반환한다.
YES이면 질문 객체의 매입불가 사유로 매입불가 결과를 반환한다.
질문 문자열을 직접 분석하지 않는다.
```

### Main

```text
QuestionProvider에서 질문 객체 목록을 가져온다.
질문 문장만 OutputView에 전달한다.
질문 객체와 답변을 BookConditionResponse로 묶어 Service에 전달한다.
```

---

## 5. 테스트 결과

| 번호 | 테스트 시나리오 | 입력값 | 기대 결과 | 실제 결과 | 통과 여부 | 수정 필요 여부 |
|---|---|---|---|---|---|---|
| 1 | 모든 질문 통과 | 2, 2, 2, 2, 2 | 매입 가능합니다. | 매입 가능합니다. | O | X |
| 2 | 첫 번째 질문에서 매입불가 | 1 | 찢어짐 사유와 함께 매입불가 출력 | 2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다. | O | X |
| 3 | 곰팡이 질문에서 매입불가 | 2, 1 | 곰팡이 사유와 함께 매입불가 출력 | 곰팡이가 있어 매입할 수 없습니다. | O | X |
| 4 | 오염/낙서 질문에서 매입불가 | 2, 2, 1 | 오염/낙서 사유와 함께 매입불가 출력 | 심한 오염 또는 심한 낙서가 있어 매입할 수 없습니다. | O | X |
| 5 | 물/젖은 흔적 질문에서 매입불가 | 2, 2, 2, 1 | 물에 젖은 흔적 사유와 함께 매입불가 출력 | 물에 젖은 흔적이 있어 매입할 수 없습니다. | O | X |
| 6 | 페이지 누락/제본 불량 질문에서 매입불가 | 2, 2, 2, 2, 1 | 페이지 누락/제본 불량 사유와 함께 매입불가 출력 | 페이지 누락 또는 제본 불량으로 책이 분리되어 매입할 수 없습니다. | O | X |
| 7 | 기타 입력 처리 | 3, 3, 3, 3, 3 | 매입 가능합니다. | 매입 가능합니다. | O | X |
| 8 | 잘못된 입력 처리 | 5 → abc → 2 | 재입력 메시지 출력 후 정상 입력 처리 | 매입 가능합니다. | O | X |

---

## 6. 오늘 결정한 핵심 기준

```text
질문 문장과 매입불가 사유는 함께 변한다.
함께 변하는 데이터는 BookConditionQuestion으로 묶는다.
BuyDecisionService는 질문 문자열을 분석하지 않는다.
BuyDecisionService는 사용자의 답변이 YES인지 확인하고 결과를 반환한다.
```

---

## 7. 현재 남은 개선 후보

```text
1. rejectedReason → rejectReason으로 용어 통일
2. BookConditionQuestion 생성자 파라미터 question → content로 이름 통일
3. ResponseEvaluationResult(false, "")를 정적 팩토리 메서드로 개선
4. BuyDecisionService 단위 테스트 도입
5. QuestionProvider의 질문 목록 관리 방식 개선 검토
```

---

## 8. 다음 과제에서 할 일

### 다음 과제명

1단계 12일차 - BuyDecisionService 단위 테스트 설계 준비

### 다음 과제 범위

- BuyDecisionService의 테스트 대상 흐름 정리
- YES / NO / OTHER 응답별 기대 결과 정리
- 질문 객체의 매입불가 사유가 결과에 담기는지 확인할 테스트 설계
- JUnit 도입 전 테스트 케이스 표 작성

---

# 1단계 12일차 - BuyDecisionService 단위 테스트 설계 준비

## 1. 오늘 과제 목적

1단계 12일차 과제에서는 `BuyDecisionService`의 단위 테스트를 작성하기 전에 테스트 설계를 먼저 진행했다.

이번 단계의 핵심은 JUnit 코드를 바로 작성하는 것이 아니라, 어떤 입력을 만들고 어떤 결과를 검증해야 하는지 정리하는 것이다.

---

## 2. 현재 BuyDecisionService의 책임

현재 `BuyDecisionService`는 다음 책임을 가진다.

```text
BookConditionResponse를 받는다.
AnswerType이 YES인지 확인한다.
YES가 아니면 통과 결과를 반환한다.
YES이면 질문 객체의 매입불가 사유를 담아 rejected 결과를 반환한다.
```

---

# 1단계 13일차 - BuyDecisionService JUnit 테스트 구조 설계 및 첫 테스트 구현

## 1. 오늘 과제 목적

이번 과제의 목적은 `BuyDecisionService`의 응답 평가 로직을 JUnit 단위 테스트로 검증하는 것이다.

기존에는 콘솔에서 직접 실행하며 수동 테스트로 확인했다. 이번 단계에서는 `BookConditionQuestion`, `BookConditionResponse` 객체를 직접 생성하고 `BuyDecisionService.evaluateResponse()`를 호출해 결과를 자동으로 검증한다.

---

## 2. 테스트 대상

```text
BuyDecisionService.evaluateResponse(BookConditionResponse response)
```

---

# 1단계 14일차 - 콘솔 MVP 테스트 범위 확장 및 마무리 점검

## 1. 오늘 과제 목적

이번 과제의 목적은 `AnswerType.fromInput()`에 대한 단위 테스트를 작성하고, 콘솔 MVP가 다음 단계로 넘어갈 준비가 되었는지 점검하는 것이다.

---

## 2. AnswerType 테스트 목적

`AnswerType.fromInput()`은 사용자 입력 문자열을 프로그램 내부에서 사용할 enum 값으로 변환하는 책임을 가진다.

```text
"1" → YES
"2" → NO
"3" → OTHER
잘못된 입력 → null
```

---

# 1단계 15일차 - 콘솔 MVP Spring Boot 전환 전 구조 점검

## 1. 오늘 과제 목적

오늘 과제의 목적은 콘솔 MVP에서 만든 객체 중 Spring Boot API 전환 후에도 재사용할 수 있는 객체와, 콘솔 전용이라 제거 또는 대체해야 하는 객체를 구분하는 것이다.

또한 API 요청/응답 구조로 전환하기 위해 DTO를 왜 분리해야 하는지, Controller가 기존 Main의 어떤 책임을 가져가는지 정리한다.

---

## 2. Spring Boot에서도 재사용 가능한 객체

Spring Boot에서도 재사용 가능성이 높은 객체는 다음과 같다.

- AnswerType
- BookConditionQuestion
- QuestionProvider
- BuyDecisionService

일부 수정 또는 역할 재검토가 필요한 객체는 다음과 같다.

- BookConditionResponse
- ResponseEvaluationResult
- BuyDecisionResult

BookConditionResponse와 BuyDecisionResult는 현재 내부 도메인 흐름에서 사용하는 객체이므로, API 요청/응답 객체로 그대로 사용하기보다는 별도의 DTO를 두는 것이 적절하다.

---

## 3. Spring Boot에서 제외 또는 대체될 객체

Spring Boot API 구조에서는 아래 객체를 그대로 사용하지 않는다.

- InputView
- OutputView
- Main

InputView는 콘솔 입력을 담당하고, OutputView는 콘솔 출력을 담당한다. Spring Boot에서는 HTTP Request Body가 입력 역할을 하고, JSON Response가 출력 역할을 한다.

Main은 콘솔 프로그램의 전체 실행 흐름을 조립했지만, Spring Boot에서는 Controller가 API 요청 흐름을 조립한다.

---

## 4. DTO를 분리하는 이유

DTO를 분리하는 이유는 외부 API 요청/응답 형식과 내부 도메인 객체를 분리하기 위해서다.

Request DTO는 외부에서 들어오는 데이터를 API 요청 형식에 맞게 받는다.

Response DTO는 내부 처리 결과를 외부 클라이언트에게 보여주기 좋은 응답 형식으로 변환한다.

도메인 객체를 그대로 외부에 노출하면 API 형식 변경이 내부 도메인 구조에 영향을 줄 수 있고, 내부 필드가 불필요하게 외부에 공개될 수 있다.

따라서 Spring Boot 전환 시에는 BuyCheckRequest, BuyCheckAnswerRequest, BuyCheckResponse 같은 DTO를 별도로 설계하는 것이 적절하다.

---

## 5. Controller가 가져갈 Main의 책임

콘솔 MVP에서 Main은 입력, 질문 목록 조회, 응답 객체 생성, Service 호출, 최종 결과 생성, 출력 연결을 담당했다.

Spring Boot 전환 후 Controller는 이 중 API 요청 흐름을 연결하는 책임을 가져간다.

Controller는 HTTP 요청을 받고, Request DTO를 읽고, Service를 호출하고, Response DTO를 반환한다.

즉, Main이 콘솔 흐름 조립자였다면 Controller는 API 요청 흐름 조립자다.

---

## 6. 테스트 유지 방식

기존 AnswerTypeTest와 BuyDecisionServiceTest는 SpringBootTest로 전환하지 않는다.

이 테스트들은 Spring 컨테이너가 필요 없는 순수 Java 객체 테스트이므로 JUnit 단위 테스트로 유지한다.

추후 Controller 테스트는 WebMvcTest 또는 SpringBootTest를 사용할 수 있다.

---

## 7. 오늘 결정한 기준

- InputView, OutputView, Main은 콘솔 전용 객체다.
- Spring Boot에서는 Controller가 API 요청 흐름을 조립한다.
- DTO는 API 요청/응답 형식과 내부 도메인 객체를 분리하기 위해 사용한다.
- BuyDecisionService는 콘솔이나 웹에 의존하지 않으므로 재사용 가능하다.
- 기존 Service/Domain 테스트는 JUnit 단위 테스트로 유지한다.

---

## 8. 다음 과제

다음 과제에서는 Spring Boot API 전환을 위한 패키지 구조와 DTO를 설계한다.

예상 설계 대상:

- controller/BuyCheckController
- dto/request/BuyCheckRequest
- dto/request/BuyCheckAnswerRequest
- dto/response/BuyCheckResponse

---

# 1단계 16일차 - Spring Boot API 전환을 위한 패키지 구조와 DTO 설계

## 1. 오늘 과제 목적

오늘 과제의 목적은 콘솔 MVP를 Spring Boot API 구조로 전환하기 위해 필요한 패키지 구조와 DTO를 설계하는 것이다.

콘솔 MVP에서는 InputView, OutputView, Main이 입력과 출력 흐름을 담당했다. Spring Boot API 구조에서는 Controller, Request DTO, Response DTO가 이 흐름을 대체한다.

---

## 2. 새로 필요한 패키지

Spring Boot 전환 후 새로 필요한 패키지는 다음과 같다.

```text
controller
dto/request
dto/response
```

controller 패키지는 API 요청을 받는 Controller를 둔다.

dto/request 패키지는 외부에서 들어오는 요청 데이터를 담는 DTO를 둔다.

dto/response 패키지는 외부로 반환할 응답 데이터를 담는 DTO를 둔다.

## 3. BuyCheckRequest 설계

BuyCheckRequest는 클라이언트가 서버로 보내는 매입 가능 여부 확인 요청 데이터를 담는 Request DTO다.

필드 후보는 다음과 같다.

private String bookTitle;
private List<BuyCheckAnswerRequest> answers;

bookTitle은 사용자가 어떤 책에 대해 매입 가능 여부를 확인하는지 나타낸다.

answers는 질문별 답변 목록을 담는다.

## 4. BuyCheckAnswerRequest 설계

BuyCheckAnswerRequest는 질문 하나에 대한 사용자의 답변을 담는 Request DTO다.

필드 후보는 다음과 같다.

private Long questionId;
private AnswerType answerType;

questionId는 어떤 질문에 대한 답변인지 식별하기 위해 사용한다.

answerType은 사용자의 답변을 YES, NO, OTHER 중 하나로 표현한다.

## 5. questionId를 요청값으로 받는 이유

API 요청에서는 questionContent보다 questionId를 받는 것이 적절하다.

이유는 다음과 같다.

질문 문장이 바뀌어도 API 요청 구조가 유지된다.
긴 질문 문장을 매번 요청으로 보내지 않아도 된다.
서버가 questionId를 기준으로 정확한 질문과 매입불가 사유를 찾을 수 있다.
클라이언트가 임의로 질문 문장을 조작하는 문제를 줄일 수 있다.

## 6. BuyCheckResponse 설계

BuyCheckResponse는 매입 가능 여부 판단 결과를 클라이언트에게 반환하기 위한 Response DTO다.

필드 후보는 다음과 같다.

```java
private boolean buyable;
private String message;
private String rejectReason;
```

buyable은 매입 가능 여부를 나타낸다.

message는 사용자에게 보여줄 결과 메시지를 나타낸다.

rejectReason은 매입불가일 경우 사유를 나타낸다.

## 7. Controller 처리 흐름

Controller는 Request DTO를 받은 뒤 다음 순서로 처리한다.

```text
1. BuyCheckRequest를 받는다.
2. Request 안의 answers를 하나씩 확인한다.
3. questionId로 QuestionProvider에서 BookConditionQuestion을 찾는다.
4. BookConditionQuestion과 AnswerType을 묶어 BookConditionResponse를 만든다.
5. BuyDecisionService.evaluateResponse(response)를 호출한다.
6. rejected = true이면 BuyDecisionResult를 만든다.
7. 모든 답변을 통과하면 매입 가능 BuyDecisionResult를 만든다.
8. BuyDecisionResult를 BuyCheckResponse로 변환한다.
9. BuyCheckResponse를 반환한다.
```
## 8. DTO를 Domain 객체로 변환하는 책임

현재 단계에서는 Controller가 DTO를 Domain 객체로 변환해도 된다.

이유는 아직 DTO와 Domain 객체의 변환 로직이 짧고 단순하기 때문이다.

하지만 추후 변환 코드가 길어지거나 여러 Controller에서 반복된다면 별도 Mapper 객체로 분리하는 것이 좋다.

Mapper는 처음부터 무조건 만드는 객체가 아니라, 변환 책임이 커졌을 때 분리하는 객체다.

## 9. 오늘 결정한 기준

- BuyCheckRequest는 외부에서 들어오는 요청 데이터를 담는다.
- BuyCheckResponse는 Service 판단 이후 외부로 반환할 응답 데이터를 담는다.
- questionContent보다 questionId를 요청값으로 받는다.
- Service는 API 요청 DTO를 직접 알지 않도록 한다.
- 현재 단계에서는 Controller에서 DTO를 Domain으로 변환한다.
- Mapper는 변환 책임이 커졌을 때 분리한다.

## 10. 다음 과제

다음 과제에서는 questionId 기반 질문 조회 구조를 설계한다.

다음 설계 질문:

- BookConditionQuestion에 questionId 필드를 추가해야 할까?
- QuestionProvider는 findById(Long questionId) 같은 메서드를 가져야 할까?
- 요청으로 들어온 questionId가 존재하지 않으면 어떻게 처리해야 할까?

---

# 1단계 17일차 - questionId 기반 질문 조회 구조 설계

## 1. 오늘 과제 목적

오늘 과제의 목적은 Spring Boot API 요청에서 들어오는 questionId를 기준으로 서버가 어떤 BookConditionQuestion을 찾아야 하는지 설계하는 것이다.

이전 과제에서 BuyCheckAnswerRequest는 questionId와 answerType을 가진다고 설계했다. 따라서 오늘은 questionId를 실제 서버 질문 목록과 연결하는 구조를 정리한다.

---

## 2. questionId가 필요한 이유

API 방식에서는 사용자가 질문에 순서대로 답하는 것이 아니라, 답변 목록을 한 번에 보낼 수 있다.

따라서 서버는 각 answer가 어떤 질문에 대한 답변인지 알아야 한다.

questionId는 클라이언트가 보낸 답변과 서버가 가진 BookConditionQuestion을 연결하는 식별자 역할을 한다.

questionId를 통해 서버는 해당 질문의 내용과 매입불가 사유를 찾을 수 있다.

---

## 3. BookConditionQuestion에 id 필드가 필요한 이유

API 요청에는 questionId가 포함된다.

서버가 이 questionId를 기준으로 질문을 찾으려면 BookConditionQuestion도 id를 가지고 있어야 한다.

따라서 BookConditionQuestion은 다음 정보를 함께 가지는 방향이 적절하다.

```java
private final Long id;
private final String content;
private final String rejectReason;
```

id는 질문 식별자, content는 질문 문장, rejectReason은 해당 질문에 YES로 답했을 때 사용할 매입불가 사유를 의미한다.

## 4. QuestionProvider.findById 설계

QuestionProvider는 질문 목록을 관리하고 제공하는 객체다.

따라서 특정 questionId에 해당하는 BookConditionQuestion을 찾는 책임도 QuestionProvider가 가지는 것이 자연스럽다.

Controller가 직접 질문 목록을 반복하면서 찾으면 Controller가 요청 흐름 연결뿐 아니라 질문 조회 세부 로직까지 알게 된다.

따라서 QuestionProvider에 다음 메서드를 추가하는 방향으로 설계한다.

```java
public Optional<BookConditionQuestion> findById(Long questionId)
```

## 5. findById 반환 타입

findById의 반환 타입은 Optional<BookConditionQuestion>을 사용한다.

이유는 요청으로 들어온 questionId에 해당하는 질문이 존재할 수도 있고, 존재하지 않을 수도 있기 때문이다.

null을 반환하면 Controller가 null 체크를 놓쳤을 때 NullPointerException이 발생할 수 있다.

Optional을 사용하면 질문이 없을 수 있다는 사실을 반환 타입에서 명확히 표현할 수 있다.

## 6. 잘못된 questionId 처리 기준

존재하지 않는 questionId가 들어온 경우에는 매입불가로 처리하지 않는다.

매입불가는 책 상태에 대한 판단 결과다.

반면 잘못된 questionId는 사용자가 보낸 API 요청값 문제다.

따라서 잘못된 questionId는 잘못된 요청으로 처리하는 것이 적절하다.

## 7. Controller 처리 흐름

Controller는 다음 흐름으로 처리한다.

```text
1. BuyCheckRequest를 받는다.
2. answers를 반복한다.
3. 각 answer의 questionId로 QuestionProvider.findById(questionId)를 호출한다.
4. 질문이 없으면 잘못된 요청으로 처리한다.
5. 질문이 있으면 BookConditionQuestion과 answerType을 묶어 BookConditionResponse를 생성한다.
6. BuyDecisionService.evaluateResponse(response)를 호출한다.
7. rejected = true이면 매입불가 BuyDecisionResult를 생성한다.
8. rejected = false이면 다음 answer로 넘어간다.
9. 모든 답변이 통과되면 매입 가능 BuyDecisionResult를 생성한다.
10. BuyDecisionResult를 BuyCheckResponse로 변환한다.
11. BuyCheckResponse를 반환한다.
```
## 8. 오늘 결정한 기준

- API 요청에서는 questionId로 질문을 식별한다.
- BookConditionQuestion에 id 필드를 추가한다.
- QuestionProvider는 findById(Long questionId)를 제공한다.
- findById는 Optional<BookConditionQuestion>을 반환한다.
- 잘못된 questionId는 매입불가가 아니라 잘못된 요청으로 처리한다.
- Controller는 questionId로 질문을 찾은 뒤 BookConditionResponse를 만들어 Service를 호출한다.

## 9. 다음 과제

다음 과제에서는 오늘 설계한 내용을 바탕으로 questionId 기반 질문 조회 구조를 구현한다.

다음 구현 대상:

- BookConditionQuestion id 필드 추가
- BookConditionQuestion 생성자 수정
- BookConditionQuestion getId() 추가
- QuestionProvider 질문 목록에 id 부여
- QuestionProvider.findById(Long questionId) 구현
- 필요 시 QuestionProvider 테스트 검토