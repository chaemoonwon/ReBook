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