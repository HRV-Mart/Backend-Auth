package com.hrv.mart.backendauth.repository

import com.hrv.mart.backendauth.config.ReactiveKafkaProducerConfig
import com.hrv.mart.userlibrary.model.User
import com.hrv.mart.userlibrary.service.UserProducer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Repository

@Repository
class KafkaRepository(
    private val property: KafkaProperties
) {
    private val kafkaTemplate = ReactiveKafkaProducerConfig()
        .reactiveKafkaProducerTemplate(property)
    private val userProducer = UserProducer(kafkaTemplate)
    fun createUser(user: User) =
        userProducer.createUser(user)
    fun deleteUser(userId: String) =
        userProducer.deleteUser(userId)
}