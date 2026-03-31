package com.example.smartsalesvisit.common

object AiPrompts {

    fun visitSummaryPrompt(notes: String): String {
        return """
        You are an AI Sales Assistant.

        Analyze the following sales meeting notes and generate structured output.

        Notes:
        $notes

        Give response strictly in this format:

        Meeting Summary:
        Pain Points:
        Action Items:
        Recommended Next Step:
        Customer Emotion: (Interested / Neutral / Hesitant / Negative)
        Deal Probability: (Give percentage like 70%)
        Suggested Strategy:

        Keep answers short and clear.
    """.trimIndent()
    }

}