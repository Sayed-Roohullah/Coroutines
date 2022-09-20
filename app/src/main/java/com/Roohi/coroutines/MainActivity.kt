package com.Roohi.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var counterText: TextView
    lateinit var viewModel : MainViewModel
    private val tag :String = "Saqi"
    var counter = 0
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         counterText = findViewById(R.id.counter)
         ////main Thread Execution/////////////
         Log.d(tag,"${Thread.currentThread().name}")

         /////////////View Model Scope and LifeCycle Scope///////////
         viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//         lifecycle.launch{
//             delay(1000)
//             val intent  = Intent(this@MainActivity,AnotherActivity::class.java)
//             startActivity(intent)
//             finish()
//         }

         ////////////////////job heirarcy/////////////////
         CoroutineScope(Dispatchers.Main).launch {
            RunBlock()
             execute()
         }

         CoroutineScope(Dispatchers.IO).launch{
             printFollowers()
             Followers()
         }

         GlobalScope.launch{
             executeTask()
         }

         //////////coroutine executions////////////
         CoroutineScope(Dispatchers.Main).launch {
             task1()
          }
         CoroutineScope(Dispatchers.Main).launch {
              task2()
         }
    }


    /////////////withContext function//////////
    private suspend fun executeTask(){
        Log.d(tag,"before")
        withContext(Dispatchers.IO){
            delay(1000)
            Log.d(tag,"inside")
        }
        Log.d(tag,"After")

    }
    /////////////runBlocking/////////////
    fun RunBlock(){
        runBlocking {
            launch {
                delay(1000)
                Log.d(tag,"World")
            }
            Log.d(tag,"Hello")

        }
    }
    /////////update counter//////////
    fun CounterUpdate(view: View) {
        Log.d(tag,"${Thread.currentThread().name}")

        counter++
       // counterText.text =  "${counterText.text.toString().toInt() + 1}"
        counterText.text =   "$counter"
    }
    //////////////Suspended function Coroutine heirarcy////////////
    ////////////////Coroutine Context//////////////
    private suspend fun execute(){
        val parJob = CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000){
                if (isActive){
                    ExecuteLongRunningTask()
                    Log.d(tag,i.toString())
                }
            }

        }
        delay(1000)
        Log.d(tag,"Canceling Job")
        ////////Cancelation Exception/////////////
        parJob.cancel()
        parJob.join()
        Log.d(tag,"Parent job Completed")
        val parentjob = GlobalScope.launch(Dispatchers.Main){
            //Log.d(tag,"Parent - $coroutineContext")
            Log.d(tag,"Parent Started")
            val childjob = launch(Dispatchers.IO) {
                Log.d(tag,"Child Started")
                delay(5000)
                Log.d(tag,"Child JobS Ended")
            }
            delay(3000)
            Log.d(tag,"Child Job Cancel")
            ////////Cancelation Exception////////////
            childjob.cancel()
            Log.d(tag,"Parent job Ended")
        }
         //parentjob.cancel()
        parentjob.join()
        Log.d(tag,"Parent Completed")

    }
    ///////////suspended functions/////////
    private  suspend fun printFollowers(){
        var fbFollowers = 0
        var InstaFollowers =0
        val job = CoroutineScope(Dispatchers.IO).launch {
            fbFollowers = getFbFollowers()
        }
        val job2 = CoroutineScope(Dispatchers.IO).launch {
            InstaFollowers = getInstaFollowers()
        }
        job.join()
        job2.join()
        Log.d(tag,"FB - $fbFollowers Insta - $InstaFollowers")
    }
    private suspend fun Followers(){
//        val fb = CoroutineScope(Dispatchers.IO).async {
//            getFbFollowers()
//            "Hello"
//        }
        ///////simple and fast way of async to load data/////////
//        CoroutineScope(Dispatchers.IO).launch {
//            var fb = async { getFbFollowers() }
//            var inst =async {getInstaFollowers()  }
//            Log.d(tag,"FB - ${fb.await()} Insta - ${inst.await()}")
//        }

        //////////////using async////////////
        val fb = CoroutineScope(Dispatchers.IO).async {
            getFbFollowers()
         }
        val insta = CoroutineScope(Dispatchers.IO).async {
            getInstaFollowers()
         }
        Log.d(tag,"FB - ${fb.await()} Insta - ${insta.await()}")

    }
    private suspend fun getFbFollowers():Int{
        //////suspended point////////
        delay(1000)
        return 54
    }
    private suspend fun getInstaFollowers():Int{
        //////suspended point////////
        delay(1000)
        return 114
    }

    suspend fun task1(){
        Log.d(tag,"Starting task1")
        /////////suspended point/////////////
        yield()
        Log.d(tag,"Ending task1")
    }
    suspend fun task2(){
        Log.d(tag,"Starting task2")
        /////////suspended point/////////////
        yield()
        Log.d(tag,"Ending task2")
    }

    ////////////long time execution func//////////////
    private fun ExecuteLongRunningTask(){
        for(i in 1..1000000000){

         }
    }
    //////////different way execution way of coroutine//////////
    fun DoAction(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(tag,"1-${Thread.currentThread().name}")

        }
        GlobalScope.launch(Dispatchers.Main){
            Log.d(tag,"2-${Thread.currentThread().name}")

        }
        MainScope().launch(Dispatchers.Default){
            Log.d(tag,"3-${Thread.currentThread().name}")

        }

    }
}