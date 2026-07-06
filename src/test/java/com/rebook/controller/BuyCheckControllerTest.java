package com.rebook.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebook.domain.AnswerType;
import com.rebook.domain.BookConditionQuestion;
import com.rebook.domain.ResponseEvaluationResult;
import com.rebook.dto.request.BuyCheckAnswerRequest;
import com.rebook.dto.request.BuyCheckRequest;
import com.rebook.dto.response.BuyCheckResponse;
import com.rebook.dto.response.ErrorResponse;
import com.rebook.provider.QuestionProvider;
import com.rebook.service.BuyDecisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.rebook.domain.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyCheckController.class)
class BuyCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionProvider questionProvider;

    @MockBean
    private BuyDecisionService buyDecisionService;


    @Test
    public void 답변_중_YES가_있으면_매입불가_응답을_반환한다() throws Exception {

        BuyCheckRequest request = new BuyCheckRequest("자바의 정석", List.of(
                new BuyCheckAnswerRequest(1L, AnswerType.NO),
                new BuyCheckAnswerRequest(2L, AnswerType.YES)
        ));

        given(questionProvider.findById(1L)).willReturn(Optional.of(mock(BookConditionQuestion.class)));
        given(questionProvider.findById(2L)).willReturn(Optional.of(mock(BookConditionQuestion.class)));

        BuyCheckResponse buyCheckResponse = new BuyCheckResponse(false, "매입 불가능 합니다.", "곰팡이가 있어 매입할 수 없습니다.");

        given(buyDecisionService.evaluateResponse(any())).willReturn(
                new ResponseEvaluationResult(false, ""),
                new ResponseEvaluationResult(true,"곰팡이가 있어 매입할 수 없습니다.")
        );

        mockMvc.perform(post("/api/buy-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyable").value(buyCheckResponse.isBuyable()))
                .andExpect(jsonPath("$.message").value(buyCheckResponse.getMessage()))
                .andExpect(jsonPath("$.rejectReason").value(buyCheckResponse.getRejectReason()))
                .andReturn();
    }

    @Test
    public void 모든_답변이_NO이면_매입가능_응답을_반환한다() throws Exception {
        BuyCheckRequest request = new BuyCheckRequest("자바의 정석", List.of(
                new BuyCheckAnswerRequest(1L, AnswerType.NO),
                new BuyCheckAnswerRequest(2L, AnswerType.NO)
        ));

        given(questionProvider.findById(1L)).willReturn(Optional.of(mock(BookConditionQuestion.class)));
        given(questionProvider.findById(2L)).willReturn(Optional.of(mock(BookConditionQuestion.class)));

        BuyCheckResponse buyCheckResponse = new BuyCheckResponse(true, "매입 가능합니다.", "");


        given(buyDecisionService.evaluateResponse(any())).willReturn(
                new ResponseEvaluationResult(false,""),
                new ResponseEvaluationResult(false,"")
        );

        mockMvc.perform(post("/api/buy-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyable").value(buyCheckResponse.isBuyable()))
                .andExpect(jsonPath("$.message").value(buyCheckResponse.getMessage()))
                .andExpect(jsonPath("$.rejectReason").value(buyCheckResponse.getRejectReason()))
                .andReturn();
    }



    @Test
    public void 존재하지_않는_questionId이면_400응답을_반환한다() throws Exception {

        //given
        ErrorResponse errorResponse = new ErrorResponse(INVALID_QUESTION_ID);

        BuyCheckRequest request = new BuyCheckRequest("자바의 정석", List.of(
                new BuyCheckAnswerRequest(999L, AnswerType.NO)
        ));


        given(questionProvider.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/buy-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
                .andExpect(jsonPath("$.message").value(errorResponse.getMessage()))
                .andReturn();
    }

    @Test
    public void 잘못된_요청_request이면_400응답을_반환한다() throws Exception {

        ErrorResponse errorResponse = new ErrorResponse(INVALID_REQUEST);

        BuyCheckRequest buyCheckRequest = new BuyCheckRequest(
                "책 제목",
                List.of()
        );


        mockMvc.perform(post("/api/buy-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyCheckRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
                .andExpect(jsonPath("$.message").value(errorResponse.getMessage()))
                .andReturn();


    }
}
