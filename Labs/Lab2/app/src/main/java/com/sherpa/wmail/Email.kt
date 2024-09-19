package com.sherpa.wmail

class Email (
    val sender: String,
    val title: String,
    val summary: String,
    val date: String,
    val picId: Int,
    var unRead: Boolean) {
}