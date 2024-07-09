## 안드로이드에서 소리를 재생하는 방법

MediaPlayer, SoundPool

### MediaPlayer 클래스

* 일반적인 소리 파일 연주에는 MediaPlayer 클래스를 사용한다. 

* 음악 파일과 비디오 파일 모두 재생할 수 있다.

* MediaPlayer로 raw 디렉터리 파일을 재생하는 코드는 다음과 같다.

* 사용이 끝나면 반드시 release() 메서드를 호출하여 자원을 해체해야한다. 

```kotlin
val mediaPlayer = MediaPlayer.create(this, R.raw.do1)
button.setOnClickListener{ mediaPlayer.start() }

...
mediaPlayer.release()
```

* MediaPlayer 클래스는 일반적으로 소리를 한 번만 재생하는 경우 또는 노래나 배경음과 같은 경우에 유용하게 사용된다.

* 그러나 실로폰과 같이 연타를 해서 연속으로 소리를 재생하는 경우에는 SoundPool 클래스가 더 유용하다.

### SoundPool 클래스

* Builder().build() 메서드로 SoundPool 객체를 생성하고 load() 메서드로 소리 파일을 로드하여 그 아이디를 반환한다.

```kotlin
val soundPool = SoundPool.Builder().build()

val soundId = soundPool.load(this, R.raw.do1, 1)
button.setOnClickListener{ soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f) }
```

* load() 메서드와 play() 메서드의 원형은 다음과 같다.

```kotlin
load(context: Context, resId: Int, priority: Int) :
```

+ context : 컨텍스트와 액티비티 지정

+ resId : 재생할 raw 디렉터리의 소리 파일 리소스 지정

+ priority : 우선순위 지정, 숫자가 높으면 우선순위 높음

```kotlin
play(soundId: Int, leftVolume: Float, rightVolume: Float, priority: Int, loop: Int, rate: Float) :
```

+ soundId : load() 메서드에서 반환된 음원의 id 지정

+ leftVolume : 왼쪽 볼륨을 0.0 ~ 1.0 사이에서 지정

+ rightVolume : 오른쪽 볼륨을 0.0 ~ 1.0 사이에서 지정

+ priority : 우선순위 지정, 0이 가장 낮은 순위

+ loop : 반복 지정, 0이면 반복 X, -1이면 반복

+ rate : 재생 속도 지정, 1.0이면 보통, 0.5이면 0.5 배속, 2.0이면 2배속
