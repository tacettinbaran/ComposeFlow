package com.baran.composeflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.StringJoiner

class MyViewModel : ViewModel() {
    /*
    liveDatalar yerine kullanılabilir.
    liveDatalar kullanılmaya devam ediyor ama flow ve emit sayesinde
    daha efektif çalışma sağlar.
     */
    val counDownTimerFlow = flow<Int> {
        //emit(10)
        val countDownFrom = 10
        var counter = countDownFrom

        emit(countDownFrom)
        while (counter > 0) {
            delay(1000)
            counter--
            emit(counter)
        }
    }

   /*
    init {
        collectInViewModel()
    }
    */

    private fun collectInViewModel() {

        viewModelScope.launch {
            counDownTimerFlow
                .filter {
                    it % 2 == 0
                }
                .map {
                    it * it
                }
                .collect {
                    println("collect counter is :  $it")
                }

           /*
            counDownTimerFlow.collectLatest {
                delay(2000)
                println("collectLatest counter is :  $it")
            }
            */
        }

        /*
    //collect ile aynı
        counDownTimerFlow.onEach {
            println(it)
        }.launchIn(viewModelScope)
     */
    }

    private val  _liveData = MutableLiveData<String>("KotlinLiveData")
    val  liveData : LiveData<String> = _liveData

    //boş brakılamıyor. mutlaka ilk değer verilmesi gerekir.
    private val _stateFlow = MutableStateFlow("KotlinStateFlow")
    val stateFlow = _stateFlow.asStateFlow()

    //ilk değeri boş olması lazım yoksa hata verir
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow =_sharedFlow.asSharedFlow()

     
    fun changeLiveDataValue(){
        _liveData.value = "Live Data"
    }

    fun changeStateFlowValue(){
        _stateFlow.value = "State Flow"
    }

    fun changeSharedFlowValue(){
        viewModelScope.launch {
            _sharedFlow.emit("Shared Flow")
        }
    }
}

