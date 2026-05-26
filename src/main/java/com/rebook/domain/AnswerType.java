package com.rebook.domain;

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
