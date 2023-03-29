package com.hrv.mart.backendauth.config

import com.hrv.mart.userlibrary.model.User
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions

@Configuration
class ReactiveKafkaProducerConfig {
    @Bean
    fun reactiveKafkaProducerTemplate(
        properties: KafkaProperties
    ): ReactiveKafkaProducerTemplate<String, User> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate<String, User>(SenderOptions.create(props))
    }
}