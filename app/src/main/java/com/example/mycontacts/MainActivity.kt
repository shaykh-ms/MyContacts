package com.example.mycontacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mycontacts.presentation.contact_list_screen.ContactSyncScreen
import com.example.mycontacts.ui.theme.MyContactsTheme
import com.example.mycontacts.presentation.contact_list_screen.ContactListViewModel
import com.example.mycontacts.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var hasPermission by mutableStateOf(false)
        val permissions = arrayOf(
           android.Manifest.permission.READ_CONTACTS,
           android.Manifest.permission.WRITE_CONTACTS
        )

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            hasPermission = result.all { it.value }
        }

        if (!hasPermissions(permissions)) {
            permissionLauncher.launch(permissions)
        } else {
            hasPermission = true
        }

        setContent {
            MyContactsTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp))
                ) { innerPadding ->
                    val viewModel: ContactListViewModel = hiltViewModel()
                    var selectedItemIndex by remember {
                        mutableIntStateOf(2)
                    }

                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            Screen.entries.forEachIndexed { index, screen ->
                                item(
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        selectedItemIndex = index
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title,
                                            tint =  colorResource(R.color.black)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = screen.title,
                                            color =   colorResource(R.color.black),
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Normal,
                                                lineHeight = 24.sp
                                            ),
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                        )

                                    },
                                )

                            }

                        },

                        layoutType =
                        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                            currentWindowAdaptiveInfo()
                        )





                    ) {

                        val state by viewModel.state.collectAsStateWithLifecycle()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(id = R.color.white)),
                            contentAlignment = Alignment.Center
                        ) {
                            when (selectedItemIndex) {
                                0 -> {
                                    Text(text = "Favourites")


                                }

                                1 -> {
                                    Text(text = "Recent")
                                }

                                2 -> {
                                    ContactSyncScreen(
                                        state = state,
                                        event = viewModel.uiEvent,
                                        onEvent = { event ->
                                            viewModel.onEvent(event)

                                        },
                                        hasPermission =  hasPermission,
                                        onRequestPermissions = {
                                            permissionLauncher.launch(permissions)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

















    }
    private fun hasPermissions(perms: Array<String>): Boolean {
        return perms.all {
            ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyContactsTheme {
        Greeting("Android")
    }
}