package com.example.localhomecinema.server

object ChatManager {


    private val messages =
        mutableListOf<String>()


    fun addMessage(message:String){

        messages.add(message)

        if(messages.size > 100){

            messages.removeAt(0)

        }

    }


    fun getMessages():List<String>{

        return messages

    }

}