package com.hope.tubitak_bitki.presentation.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hope.tubitak_bitki.presentation.ui.theme.ErrorRed
import com.hope.tubitak_bitki.presentation.ui.theme.NeonGreen
import java.io.File

@Composable
fun CameraScreen(
    onCloseClick: () -> Unit = {},
    onAnalyzeClick: (Any) -> Unit = {},
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsState()

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    var recordingState: Recording? by remember { mutableStateOf(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                viewModel.onEvent(CameraEvent.OnPermissionResult(isGranted))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) Toast.makeText(context, "Lütfen kamera izni veriniz", Toast.LENGTH_SHORT).show()
            viewModel.onEvent(CameraEvent.OnPermissionResult(isGranted))
        }
    )

    LaunchedEffect(Unit) {
        if (!state.hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val mimeType = context.contentResolver.getType(uri)
            if (mimeType?.startsWith("video") == true) {
                viewModel.onEvent(CameraEvent.OnVideoTaken(uri))
            } else {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.onEvent(CameraEvent.OnPhotoTaken(bitmap))
            }
        }
    }

    Scaffold(containerColor = Color.Black) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {


            if (state.capturedBitmap != null || state.capturedVideoUri != null) {
                CapturedPreviewUI(
                    bitmap = state.capturedBitmap,
                    videoUri = state.capturedVideoUri,
                    onRetake = { viewModel.onEvent(CameraEvent.OnRetake) },
                    onAnalyze = {
                        val data = state.capturedBitmap ?: state.capturedVideoUri
                        if (data != null) onAnalyzeClick(data)
                    }
                )
            }
            else if (state.hasPermission) {
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            this.controller = cameraController
                            cameraController.bindToLifecycle(lifecycleOwner)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart).background(Color.Black.copy(0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Kapat", tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.9f))))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = { viewModel.onEvent(CameraEvent.OnModeChange(isVideo = false)) }) {
                            Text("FOTOĞRAF", color = if(!state.isVideoMode) NeonGreen else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(onClick = { viewModel.onEvent(CameraEvent.OnModeChange(isVideo = true)) }) {
                            Text("VİDEO", color = if(state.isVideoMode) ErrorRed else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        }) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = "Galeri", tint = Color.White, modifier = Modifier.size(32.dp))
                        }

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(4.dp, Color.White, CircleShape)
                                .padding(6.dp)
                                .background(if (state.isVideoMode) ErrorRed else NeonGreen, if (state.isRecording) RoundedCornerShape(16.dp) else CircleShape)
                                .clickable {
                                    if (state.isVideoMode) {
                                        if (state.isRecording) {
                                            recordingState?.stop()
                                        } else {
                                            viewModel.onEvent(CameraEvent.OnRecordStart)
                                            recordingState = startVideoRecording(
                                                context = context,
                                                controller = cameraController,
                                                onVideoSaved = { uri ->
                                                    viewModel.onEvent(CameraEvent.OnVideoTaken(uri))
                                                    recordingState = null
                                                },
                                                onError = { msg ->
                                                    viewModel.onEvent(CameraEvent.OnRecordStop)
                                                    recordingState = null
                                                    Toast.makeText(context, "Hata: $msg", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }
                                    } else {
                                        takePhoto(context, cameraController) { bitmap ->
                                            viewModel.onEvent(CameraEvent.OnPhotoTaken(bitmap))
                                        }
                                    }
                                }
                        )

                        IconButton(onClick = {
                            cameraController.cameraSelector = if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                        }) {
                            Icon(Icons.Default.Cameraswitch, contentDescription = "Çevir", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Lütfen kamera izni veriniz", color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                            Text("İzin Ver", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CapturedPreviewUI(
    bitmap: Bitmap?,
    videoUri: Uri?,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (videoUri != null) {
            Box(Modifier.fillMaxSize().background(Color.DarkGray), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Video Hazır (Sessiz)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(Color.Black.copy(0.6f)).padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onRetake,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Tekrar")
            }

            Button(
                onClick = onAnalyze,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
            ) {
                Text("Sisteme Yolla")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(image.toBitmap(), 0, 0, image.width, image.height, matrix, true)
                onPhotoTaken(rotatedBitmap)
                image.close()
            }
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun startVideoRecording(
    context: Context,
    controller: LifecycleCameraController,
    onVideoSaved: (Uri) -> Unit,
    onError: (String) -> Unit
): Recording {
    val outputFile = File(context.cacheDir, "temp_video_${System.currentTimeMillis()}.mp4")

    return controller.startRecording(
        FileOutputOptions.Builder(outputFile).build(),
        AudioConfig.create(false),
        ContextCompat.getMainExecutor(context)
    ) { event ->
        when(event) {
            is VideoRecordEvent.Finalize -> {
                if (event.hasError()) {
                    onError(event.cause?.message ?: "Hata")
                } else {
                    onVideoSaved(Uri.fromFile(outputFile))
                }
            }
        }
    }
}