package com.rebook;

import com.rebook.domain.AnswerType;
import com.rebook.domain.BookConditionResponse;
import com.rebook.domain.BuyDecisionResult;
import com.rebook.domain.ResponseEvaluationResult;
import com.rebook.provider.QuestionProvider;
import com.rebook.service.BuyDecisionService;
import com.rebook.view.InputView;
import com.rebook.view.OutputView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("ReBook 콘솔 MVP 시작");

        //1. 책 제목 입력
        InputView inputView = new InputView();
        System.out.println("책 제목을 입력하세요.");
        String title = inputView.inputBookTitle();
        System.out.println("책 제목: " + title);
        //2. 책 질문 목록 가져오기
        QuestionProvider provider = new QuestionProvider();
        List<String> questions = provider.getQuestions();
        OutputView outputView = new OutputView();
        BuyDecisionService service = new BuyDecisionService();

        BuyDecisionResult finalResult = null;
        //3. 질문 반복
        for (String question : questions) {
            //4. 질문 출력
            outputView.printQuestion(question);
            //5. 답변 입력
            AnswerType answerType = inputView.inputAnswer();
            //6. BookConditionResponse 생성
            BookConditionResponse bookConditionResponse = new BookConditionResponse(question, answerType);
            //7. BuyDecisionService로 응답 평가
            ResponseEvaluationResult evaluationResult = service.evaluateResponse(bookConditionResponse);

            //8. rejected == true이면 BuyDecisionResult 생성 후 break
            if (evaluationResult.isRejected()) {
                finalResult = new BuyDecisionResult(
                        false,
                        evaluationResult.getRejectReason(),
                        "매입할 수 없습니다."
                );
                break;
            }

        }
        //9. 모든 질문 통과 시 매입 가능 BuyDecisionResult 생성
        if (finalResult == null) {
            finalResult = new BuyDecisionResult(
                    true,
                    "",
                    "매입 가능합니다."
            );
        }

        //10. 결과 출력
        outputView.printResult(finalResult);
    }
}
