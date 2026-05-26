package com.rebook.view;

import com.rebook.domain.AnswerType;

import java.util.Scanner;

public class InputView {
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
}
