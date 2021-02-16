package ru.skillbranch.devintensive.extensions

import java.lang.StringBuilder

fun String.truncate(i: Int = 16): String{
    val stringBuilder = StringBuilder()
    val sourceStr = this.trim()
    if(sourceStr.length > i){
        stringBuilder.append(sourceStr.subSequence(0,i).trimEnd())
        stringBuilder.append("...")
    } else {
        stringBuilder.append(sourceStr)
    }
    return stringBuilder.toString()
}

fun String.stripHtml(): String{
    var resultStr = this
    resultStr = resultStr.replace("<.*?>".toRegex(), "")
    resultStr = resultStr.replace("[\\s]{2,}".toRegex(), " ")
    resultStr = resultStr.replace("&.[^\\sа-яА-Я]*?;".toRegex(),"")
    return resultStr
}