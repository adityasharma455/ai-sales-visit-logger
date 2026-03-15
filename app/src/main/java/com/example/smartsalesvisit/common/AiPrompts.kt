package com.example.smartsalesvisit.common

object AiPrompts {

    fun visitSummaryPrompt(notes: String): String {
        return """
        You are a CRM assistant.

        Extract information from the meeting notes.

        Return ONLY in this format:

        Meeting Summary:
        Pain Points:
        Action Items:
        Recommended Next Step:

        Meeting Notes:
        $notes
        """.trimIndent()
    }

}