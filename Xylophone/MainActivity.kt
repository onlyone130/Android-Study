package com.example.xylophone

import android.content.pm.ActivityInfo
import android.media.SoundPool
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xylophone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //soundPool 객체 초기화 코드, setMaxStreams() 메서드 - 한꺼번에 재생하는 음원 개수 지정 가능
    private val soundPool = SoundPool.Builder().setMaxStreams(8).build()

    private val sounds by lazy {
        //listOf() 함수를 사용해서 텍스트 뷰와 음원 파일 리소스 ID를 연관 지은 Pair 객체 8개를 리스트 객체 sounds로 만듦
        listOf(
            Pair(binding.do1, R.raw.do1),
            Pair(binding.re, R.raw.re),
            Pair(binding.mi, R.raw.mi),
            Pair(binding.fa, R.raw.fa),
            Pair(binding.sol, R.raw.sol),
            Pair(binding.la, R.raw.la),
            Pair(binding.si, R.raw.si),
            Pair(binding.do2, R.raw.do2),
        )
    }
override fun onCreate(savedInstanceState: Bundle?) {
    //화면이 가로모드로 고정되게 하기
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    //sounds 리스트를 forEach() 함수를 사용하여 요소를 하나씩 꺼내서 tune() 메서드에 전달
    sounds.forEach { tune(it) }
}

private fun tune(pitch: Pair<TextView, Int>) {      // tune() 메서드는 Pair 객체를 받음
    val soundId = soundPool.load(this, pitch.second, 1)     //load() 메서드로 음원의 ID를 얻음
    pitch.first.setOnClickListener {                   //전달받은 Pair 객체의 첫번째 프로퍼티인텍스트 뷰를 얻음
        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f) //텍스트 뷰를 클릭했을 때 음원 재생
    }
}

override fun onDestroy() {
    super.onDestroy()
    soundPool.release()
}
}
