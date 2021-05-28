package me.jamilalrasyidis.notetakingapp

data class Note(
    var title: String,
    var content: String,
    var timestamp: String
) {
    override fun toString(): String {
        return "Note(title=$title, content=$content, timestamp=$timestamp)"
    }
}