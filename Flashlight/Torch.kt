package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

class Torch(context: Context) {
    private var cameraId: String? = null
    private val cameraManager =
        //as 연산자를 사용하는 이유 : CAMEARA_SERVICE 메서드는 Object형을 반환하기 때문. CameraService 형으로 형변환을 함.
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager //flashOn()과 flashOff() 메서드의 기능을 켜기 위해 필요

    init {
        cameraId = getcameraId()
    }

    fun flashOn() {
        cameraId?.let {
            cameraManager.setTorchMode(it, true) //CameraManger는 기기가 가지고 있는 모든 카메라에 대한 정보 목록 제공
        }
    }

    fun flashOff() {
        cameraId?.let {
            cameraManager.setTorchMode(it, false)
        }
    }

    private fun getcameraId(): String? {
        val cameraIds = cameraManager.cameraIdList
        for (id in cameraIds) {
            val info = cameraManager.getCameraCharacteristics(id)
            val flashAvailable =
                info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            if (flashAvailable != null
                && flashAvailable
                && lensFacing != null
                && lensFacing == CameraCharacteristics.LENS_FACING_BACK
            ) {
                return id
            }
        }
        return null //해당하는 카메라 ID를 찾지 못했다면 null을 반환
    }
}
