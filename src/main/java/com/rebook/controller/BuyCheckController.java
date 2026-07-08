package com.rebook.controller;

import com.rebook.domain.*;
import com.rebook.dto.request.BuyCheckAnswerRequest;
import com.rebook.dto.request.BuyCheckRequest;
import com.rebook.dto.response.BuyCheckResponse;
import com.rebook.dto.response.ErrorResponse;
import com.rebook.provider.QuestionProvider;
import com.rebook.service.BuyDecisionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.rebook.domain.ErrorCode.*;

@RestController
@RequestMapping("/api/buy-check")
public class BuyCheckController {

    private final QuestionProvider provider;
    private final BuyDecisionService service;

    @Autowired
    public BuyCheckController(QuestionProvider provider, BuyDecisionService service) {
        this.provider = provider;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> checkBuyable(@Valid @RequestBody BuyCheckRequest request) {
        List<BuyCheckAnswerRequest> answers = request.getAnswers();
        for (BuyCheckAnswerRequest answer : answers) {
            Optional<BookConditionQuestion> optionalQuestion = provider.findById(answer.getQuestionId());
            if (optionalQuestion.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(INVALID_QUESTION_ID);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            BookConditionQuestion question = optionalQuestion.get();
            BookConditionResponse response = new BookConditionResponse(question, answer.getAnswerType());
            ResponseEvaluationResult evaluationResult = service.evaluateResponse(response);
            if (evaluationResult.isRejected()) {
                BuyDecisionResult result = new BuyDecisionResult(false, evaluationResult.getRejectReason(), "매입 불가능 합니다.");
                return ResponseEntity.ok(BuyCheckResponse.from(result));
            }
        }
        BuyDecisionResult result = new BuyDecisionResult(true, "", "매입 가능합니다.");
        return ResponseEntity.ok(BuyCheckResponse.from(result));
    }
}
