package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        val chatsTmp = chats.partition { it.isArchived }
        val archiveItem = composeArchiveItem(chatsTmp.first.map { it.toChatItem() })
        val newChats = mutableListOf<ChatItem>()
        if(archiveItem!=null){
            newChats.add(archiveItem)
        }
        newChats.addAll(
            chatsTmp.second
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
        )
        return@map newChats
    }

    private val query = mutableLiveData("")

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatItems = chats.value!!

            result.value = if (queryStr.isEmpty()) chatItems
            else chatItems.filter { it.title.contains(queryStr, true) }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    private fun composeArchiveItem(archive: List<ChatItem>) : ChatItem?{
        val msgCount = archive.sumBy { it.messageCount }
        return archive.lastOrNull().let {
            it?.copy(messageCount = msgCount)
        }
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    private fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun onCancelArchiveClick(chatId: String) {
        restoreFromArchive(chatId)
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }
}