package com.example.E_Learning_CRUD.enums;

public enum Grade {
    A_PLUS("A+", 80, 100),
    A("A", 75, 79),
    A_MINUS("A-", 70, 74),
    B_PLUS("B+", 65, 69),
    B("B", 60, 64),
    B_MINUS("B-", 55, 59),
    C_PLUS("C+", 50, 54),
    C("C", 45, 49),
    C_MINUS("C-", 40, 44),
    D("D", 33, 39),
    F("F", 0, 59);

    private final String symbol;
    private final int minScore;
    private final int maxScore;

    Grade(String symbol, int minScore, int maxScore) {
        this.symbol = symbol;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public static Grade fromScore(double score) {
        for (Grade grade : values()) {
            if (score >= grade.minScore && score <= grade.maxScore) {
                return grade;
            }
        }
        return F;
    }

    // Getters
    public String getSymbol() { return symbol; }
    public int getMinScore() { return minScore; }
    public int getMaxScore() { return maxScore; }
}
