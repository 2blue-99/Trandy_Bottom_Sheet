package com.blue.trendy_bottom_sheet

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.lifecycleScope
import com.blue.trendy_bottom_sheet.ui.theme.Trendy_Bottom_SheetTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var geocoder: Geocoder

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(this, Locale.KOREAN)

        setContent {
            Trendy_Bottom_SheetTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                ) {
                    Greeting(
                        "Android",
                        geocoder
                    )
//                    AnimatedVisibilityWithCustomAnimation()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Greeting(name: String, geocoder: Geocoder, modifier: Modifier = Modifier) {

    val startTime = System.currentTimeMillis()
    val list = mutableListOf<Address>()

    val gap = CoroutineScope(Dispatchers.IO).async {
        for(i in 1..100){
            geocoder.getFromLocationName(
                "경기도 김포시 고촌읍 고송로 7",
                1,
            ){
                Log.e("TAG", "결과값 : $it", )
                list.add(it[0])
            }
        }
    }
    Log.e("TAG", "list: $list", )

    var visible by remember { mutableStateOf(false) }
    if(visible){
        BaseBottomSheet(
            onDismiss = { visible = !visible }
        )
//        BaseAlertDialog(
//            onDismiss = { visible = !visible }
//        )
//        CustomAlertDialog(
//            onDismiss = { visible = !visible }
//        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { visible = !visible }
        ) {
            Text("Push")
        }

    }
}

@Composable
fun AnimatedVisibilityWithCustomAnimation() {
    var isVisible by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically(initialOffsetY = {
                it
            }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = {
                it
            })
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 50.dp)
                .height(50.dp),
            onClick = {
                isVisible = isVisible.not()
            }
        ) {
            Text(text = "Visibility 변경하기")
        }
    }
}

@Composable
fun BaseAlertDialog(
    onDismiss: () -> Unit
){
    AlertDialog(
        title = { Text(text = "알림") },
        onDismissRequest = onDismiss ,
        confirmButton = {
            TextButton(onClick = { /*TODO*/ }) { Text(text = "확인") }
                        },
        dismissButton = {
            TextButton(onClick = { /*TODO*/ }) { Text(text = "취소") }
        }
    )
}

@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false

        )
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "This is a minimal dialog",
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    onDismiss: () -> Unit
){
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var alphaState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        alphaState = true
    }
    val value by animateFloatAsState(
        targetValue = if(alphaState) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
        ),
        label = "",
        finishedListener = {if(!alphaState) onDismiss() }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.Transparent,
        dragHandle = null,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .background(Color.Transparent)
                .alpha(value)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(10.dp)
                    .height(300.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(6.dp)
                    .width(30.dp)
                    .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(100.dp))

                Text("Test")
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).async {
                        alphaState = false
//                        onDismiss()
                    }

                }) {
                    Text(text = "Push")
                }
            }
        }
    }
}

