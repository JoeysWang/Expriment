package com.joeys.expriment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joeys.expriment.utils.log
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()


    val usersLiveData = MutableLiveData<List<UserEntity>>()


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getAllData() {
        "getAllData start ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()


        launch(Dispatchers.Main) {
            val users: List<UserEntity> = async(Dispatchers.IO) {
                "start 1 ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()
                queryUsers()
            }.await()
            "await 1 ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()


            val dowork=dowork().await()
            "dowork= $dowork ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()

            usersLiveData.value = users
        }


        "getAllData end 1   ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()

    }

    suspend fun dowork(): Deferred<String> = coroutineScope {
        async(Dispatchers.Default) {
            "dowork    ${System.currentTimeMillis()}  ${Thread.currentThread().name}".log()
            delay(2000)
            return@async " 拉阿拉" }
    }


    private suspend fun queryUsers(): List<UserEntity> {
        delay(2000)
        return listOf()
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}