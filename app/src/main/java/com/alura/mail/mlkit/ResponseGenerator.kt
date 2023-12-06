package com.alura.mail.mlkit

import android.util.Log
import com.alura.mail.model.Message
import com.alura.mail.model.Suggestion
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class ResponseGenerator {

    fun generateResponse(
        listMessages: List<Message>,
        onSuccess: (List<String>) -> Unit
    ) {
        val conversation = listMessages.map { message ->
            if(message.isLocalUser) {
                TextMessage.createForLocalUser(message.content, System.currentTimeMillis())
            } else {
                TextMessage.createForRemoteUser(message.content, System.currentTimeMillis(), "userId")
            }

        }
        val smartReply = SmartReply.getClient()
        smartReply.suggestReplies(conversation)
            .addOnSuccessListener { result ->
                if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                    // The conversation's language isn't supported, so
                    // the result doesn't contain any suggestions.
                } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                    val responses: List<String> = result.suggestions.map { it.text }
                    onSuccess(responses)
                }
            }
    }

    fun messageToSuggestionAction(messages: List<String>): List<Suggestion> {
        return messages.map { message ->
            Suggestion(message)
        }
    }
}
