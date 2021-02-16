package ru.skillbranch.devintensive.models

import java.util.*

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        when {
            question.name == Question.IDLE.name -> return question.question to status.color
            question.validateAnswer(answer.trim()) != "" -> return question.validateAnswer(answer.trim())+"\n"+question.question to status.color
            question.answers.contains(answer.toLowerCase(Locale.ROOT)) -> {
                if (question.name != Question.IDLE.name) {
                    question = question.nextQuestion()
                }
                return "Отлично - ты справился\n${question.question}" to status.color
            }
            status.name != Status.CRITICAL.name -> {
                status = status.nextStatus()
                return "Это неправильный ответ\n${question.question}" to status.color
            }
            else -> {
                status = Status.NORMAL
                question = Question.NAME
                return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values().first()
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateAnswer(answer: String): String = if (answer.first().isLowerCase())
                "Имя должно начинаться с заглавной буквы"
            else ""
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateAnswer(answer: String): String = if (answer.first().isUpperCase())
                "Профессия должна начинаться со строчной буквы"
            else ""
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validateAnswer(answer: String): String =
                if (answer.contains("[0-9]".toRegex()))
                    "Материал не должен содержать цифр"
                else ""
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validateAnswer(answer: String): String =
                if (answer.contains("[a-zA-Zа-яА-Я.]".toRegex()))
                    "Год моего рождения должен содержать только цифры"
                else ""
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): String {
                var answerTmp = answer
                answerTmp = answerTmp.replace("[\\s]+".toRegex(), "")
                return if (answerTmp.contains("[a-zA-Zа-яА-Я]".toRegex()) || answerTmp.length != 7)
                    "Серийный номер содержит только цифры, и их 7"
                else ""
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): String = ""
        };

        abstract fun nextQuestion(): Question

        abstract fun validateAnswer(answer: String): String
    }
}