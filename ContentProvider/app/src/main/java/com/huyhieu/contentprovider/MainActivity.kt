package com.huyhieu.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huyhieu.contentprovider.content_provider.UserContentProvider
import com.huyhieu.contentprovider.room.entity.User
import com.huyhieu.contentprovider.ui.theme.ContentProviderTheme
import com.huyhieu.contentprovider.viewmodel.UserVM

const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentProviderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val vm: UserVM = viewModel<UserVM>()
    val users = vm.users?.collectAsState(initial = listOf())
    val userSelected = remember {
        mutableStateOf<User?>(null)
    }
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (toolbar, content, deleteButton, addButton) = createRefs()

        Text(
            text = "Content Provider",
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(Color.Blue)
                .padding(vertical = 12.dp),
            color = Color.White,
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            ),
        )

        LazyUsers(
            users = users,
            userSelected = userSelected,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(toolbar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        AnimatedVisibility(visible = userSelected.value != null, enter = slideInVertically(
            initialOffsetY = {
                it
            },
        ) + fadeIn(), exit = slideOutVertically(
            targetOffsetY = {
                it
            },
        ) + fadeOut(), modifier = Modifier.constrainAs(deleteButton) {
            bottom.linkTo(parent.bottom, 16.dp)
            end.linkTo(addButton.start, 16.dp)
        }) {
            Button(
                onClick = {
                    //vm.deleteUser(userSelected.value)
                    userSelected.value?.let { user ->
                        val uri = ContentUris.withAppendedId(UserContentProvider.CONTENT_URI, user.id)
                        context.contentResolver.delete(uri, null, null)
                    }
                    userSelected.value = null
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Delete User")
            }
        }

        Button(onClick = {
            val age = (18..100).random()
            val user = User(name = "AutoName ${System.currentTimeMillis()}", age = age)
            Log.d(TAG, "Insert: $user")
            //vm.insertUser(user)

            val contentValues = ContentValues()
            contentValues.put("name", "Hero")
            contentValues.put("age", 20)
            val uri = context.contentResolver.insert(UserContentProvider.CONTENT_URI, contentValues)
            Log.d(TAG, "URI: $uri")
        }, modifier = Modifier.constrainAs(addButton) {
            bottom.linkTo(parent.bottom, 16.dp)
            end.linkTo(parent.end, 16.dp)
        }) {
            Text(text = "Add User")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentProviderTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            HomePage(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyUsers(
    users: State<List<User>>?,
    userSelected: MutableState<User?>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        if (users != null) {
            items(users.value, key = { it.id }) { user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                        .clickable {
                            userSelected.value = if (userSelected.value == user) {
                                null
                            } else {
                                user
                            }
                        }
                        .background(
                            if (userSelected.value == user) Color.Cyan
                            else Color.White
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                ) {
                    Text(text = user.id.toString())
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = user.name, style = TextStyle(fontWeight = FontWeight.Bold))
                        Text(text = "Age: ${user.age}")
                    }
                }
            }
        }

    }
}