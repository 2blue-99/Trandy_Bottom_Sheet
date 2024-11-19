package com.blue.trendy_bottom_sheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blue.trendy_bottom_sheet.ui.theme.Trendy_Bottom_SheetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Trendy_Bottom_SheetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(false) }
    if(visible){
        BaseBottomSheet(
            onDismiss = { visible = !visible }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { visible = !visible }
        ) {
            Text("Push")
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
    // 상단 위치 조정
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // 반투명 배경 추가
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.Transparent,
            dragHandle = null,
            modifier = Modifier.align(Alignment.Center) // 화면 중앙에 위치
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .height(300.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
            ) {
                Text("Test")
                Spacer(modifier = Modifier.height(10.dp))
                Text("TEST")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Trendy_Bottom_SheetTheme {
        Greeting("Android")
    }
}