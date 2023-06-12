package com.hrv.mart.backendauth.config

import io.appwrite.Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppWriteConfig (
    @Value("\${hrv.mart.appwrite.endPoint}")
    private val appwriteEndpoint: String,
    @Value("\${hrv.mart.appwrite.projectId}")
    private val appwriteProjectId: String,
    @Value("\${hrv.mart.appwrite.apikey}")
    private val appwriteAPIKey: String
)
{
    @Bean
    fun getClient(): Client {
        val client = Client(
            endPoint = appwriteEndpoint
        )
        client.setProject(appwriteProjectId)
        client.setKey(appwriteAPIKey)
        return client
    }
}