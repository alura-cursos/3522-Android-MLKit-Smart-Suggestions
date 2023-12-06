package com.alura.mail

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.alura.mail.ui.navigation.HomeNavHost
import com.alura.mail.ui.theme.MAILTheme
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAILTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    HomeNavHost(navController = navController)

                    val conversation = mutableListOf<TextMessage>()
                    conversation.add(
                        TextMessage.createForLocalUser(
                        "Hello, Good Morning", System.currentTimeMillis()))

                    val smartReply = SmartReply.getClient()
                    smartReply.suggestReplies(conversation)
                        .addOnSuccessListener { result ->
                            if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                                // The conversation's language isn't supported, so
                                // the result doesn't contain any suggestions.
                            } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                                for (suggestion in result.suggestions) {
                                    val replyText = suggestion.text
                                    Log.i("SmartReply", replyText)
                                }
                            }
                        }
                }
            }
        }
    }
}

