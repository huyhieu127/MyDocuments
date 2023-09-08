package com.huyhieu.mydocuments.ui.activities.media_pipe

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark
import com.google.mediapipe.framework.TextureFrame
import com.google.mediapipe.solutioncore.CameraInput
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView
import com.google.mediapipe.solutions.facemesh.FaceMesh
import com.google.mediapipe.solutions.facemesh.FaceMeshOptions
import com.google.mediapipe.solutions.facemesh.FaceMeshResult
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMediaPipeBinding
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.libraries.utils.logError
import com.huyhieu.mydocuments.utils.tryCatch


class MediaPipeActivity : BaseActivity<ActivityMediaPipeBinding>() {
    override fun binding() = ActivityMediaPipeBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        cameraInput()
    }

    private lateinit var faceMesh: FaceMesh
    private var cameraInput: CameraInput? = null
    private lateinit var glSurfaceView: SolutionGlSurfaceView<FaceMeshResult>
    override fun onClick(v: View?) {
    }

    private fun cameraInput() {
        initFaceMesh()
        initCameraInput()
        initGlSurfaceView()
    }

    private fun initFaceMesh() {
        val faceMeshOptions =
            FaceMeshOptions.builder().setStaticImageMode(false).setRefineLandmarks(true)
                .setMaxNumFaces(1).setRunOnGpu(true).build()
        faceMesh = FaceMesh(this, faceMeshOptions).apply {
            setErrorListener { message: String, e: RuntimeException? ->
                logError("MediaPipe Face Mesh error:$message")
            }
        }
    }

    private fun initCameraInput() {
        // Initializes a new CameraInput instance and connects it to MediaPipe Face Mesh Solution.
        cameraInput = CameraInput(this).apply {
            setNewFrameListener { textureFrame: TextureFrame? -> faceMesh.send(textureFrame) }
        }
    }

    private fun initGlSurfaceView() {
        // Initializes a new GlSurfaceView with a ResultGlRenderer<FaceMeshResult> instance
        // that provides the interfaces to run user-defined OpenGL rendering code.
        glSurfaceView = SolutionGlSurfaceView<FaceMeshResult>(
            this, faceMesh.glContext, faceMesh.glMajorVersion
        ).apply {
            setSolutionResultRenderer(FaceMeshResultGlRenderer())
            setRenderInputImage(true)
        }

        faceMesh.setResultListener { faceMeshResult: FaceMeshResult ->
            tryCatch {
                val noseLandmark: NormalizedLandmark =
                    faceMeshResult.multiFaceLandmarks()[0].landmarkList[1]
                logDebug(
                    String.format(
                        "MediaPipe Face Mesh nose normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                        noseLandmark.x,
                        noseLandmark.y
                    )
                )
            }
            // Request GL rendering.
            glSurfaceView.setRenderData(faceMeshResult)
            glSurfaceView.requestRender()
        }

        // The runnable to start camera after the GLSurfaceView is attached.
        with(vb) {
            glSurfaceView.post(::startCamera)
            framePreview.addView(glSurfaceView)
            glSurfaceView.isVisible = true
            framePreview.requestLayout()
        }
    }

    private fun startCamera() {
        cameraInput?.start(
            this@MediaPipeActivity,
            faceMesh.glContext,
            CameraInput.CameraFacing.FRONT,
            glSurfaceView.width,
            glSurfaceView.height
        )
    }

    private fun resumeCamera() {
        //Available when has been initialized
        if (cameraInput != null) {
            initCameraInput()
            glSurfaceView.post(::startCamera)
            glSurfaceView.isVisible = true
        }
    }

    private fun stopCamera() {
        glSurfaceView.isVisible = false
        cameraInput?.close()
    }

    override fun onPause() {
        super.onPause()
        stopCamera()
    }

    override fun onResume() {
        super.onResume()
        resumeCamera()
    }
}