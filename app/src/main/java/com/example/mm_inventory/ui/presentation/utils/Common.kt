package com.example.mm_inventory.ui.presentation.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mm_inventory.R
import com.example.mm_inventory.ui.theme.Shapes

//textfile menu----------------------------------------------

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyTextFieldMenu(
    label: String,
    items: List<String>,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(value = false) }
    //var selectedOptionText by remember { mutableStateOf(value = items[0]) }
// We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier.heightIn(min = 56.dp)//at least 56dp of height
    ) {
        OutlinedTextField(
            readOnly = true,
            value = text,//selectedOptionText,
            onValueChange = { },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant.copy(alpha = ContentAlpha.high),
                cursorColor = MaterialTheme.colors.secondary,
                focusedLabelColor = MaterialTheme.colors.secondary
            ),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { selectedItem ->
                DropdownMenuItem(
                    onClick = {
                        //selectedOptionText = selectedItem
                        onValueChange(selectedItem)
                        expanded = false
                    }
                ) {
                    Text(text = selectedItem)
                }
            }
        }
    }
}

//simple textfile-----------------------------------------------
@Composable
fun MyTextFieldForm(
    label: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    onClearText: () -> Unit = {},
    onSendClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    isReadOnly: Boolean = false
) {
    //var text by remember { mutableStateOf("") }
    val isPassword = (keyboardType == KeyboardType.NumberPassword)
    var isVisibility by remember { mutableStateOf(value = true) }
//    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = { onValueChange(it) },
            label = {
                Text(
                    text = label,
//                    style = TextStyle(color = Color.Gray.copy(alpha = 0.5f))
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onSend = { onSendClicked() },
                onSearch = { onSearchClicked() }
            ),
            visualTransformation = if (isPassword && isVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(
                    onClick = { if (isPassword) isVisibility = !isVisibility else onClearText() }
                ) {
                    Icon(
                        painter =
                        if (isPassword) {
                            if (isVisibility) painterResource(id = R.drawable.ic_baseline_visibility_off_24)//on
                            else painterResource(id = R.drawable.ic_baseline_visibility_24)//off
                        } else {
                            painterResource(id = R.drawable.ic_baseline_close_24)
                        },
                        contentDescription = null
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant.copy(alpha = ContentAlpha.high),
                cursorColor = MaterialTheme.colors.secondary,
                focusedLabelColor = MaterialTheme.colors.secondary
            ),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
            readOnly = isReadOnly,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)//at least 56dp of height
        )
//    }
}

//simple button--------------------------------------------------------
@Composable
fun MyButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
//    Column(modifier = modifier) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            ),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
            enabled = enabled,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
            //.padding(horizontal = 0.dp)
        ) {
            Text(text = text, style = MaterialTheme.typography.h6)
        }
//    }
}

//top app bar------------------------------------------------
@Composable
fun MyTopApBar(
    title: String? = null,
    navIcon: ImageVector? = ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_back_24),
    actionIconBookMark: ImageVector? = null,
    actionIcon: ImageVector? = null,
    onActionIcon: () -> Unit = {},
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isFav by remember {//change color
        mutableStateOf(isFavorite)
    }
    TopAppBar(
        title = { title?.let { Text(text = it) } },
        navigationIcon = {
            IconButton(onClick = {}) {
                navIcon?.let { Icon(
                    imageVector = it,
                    contentDescription = "go back") }
            }
        },
        actions = {
            actionIcon?.let {
                IconButton(onClick = onActionIcon) {
                    Icon(
                        imageVector = it,
                        contentDescription = "info"
                    )

                }
            }
            actionIconBookMark?.let {
                IconButton(onClick = { isFav = !isFav }) {
                    Icon(
                        imageVector = it,
                        contentDescription = "bookmark",
                        tint = if (isFav) MaterialTheme.colors.secondaryVariant else LocalContentColor.current.copy(
                            alpha = LocalContentAlpha.current
                        )
                    )
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = modifier
    )
}

//fab button-----------------------------------
@Composable
fun MyFab(
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

//my normal card -----------------------------------
@ExperimentalMaterialApi
@Composable
fun MyNormalCard(
    title: String,
    subTitle: String,
    stock: Int,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    onOperateExistenceClicked: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Card(
            onClick = onClick,//IR A LA ACCION O PAGINA
            elevation = 5.dp,
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(0.dp)),
            border = BorderStroke(width = 1.dp, color = if (stock < 50) MaterialTheme.colors.error else Color.Unspecified),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    //  .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 15.dp
                    )//INTERNAL CHILDREN ELEMENTS PADDING
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(weight = 2f)
                ) {
                    Row(
                        modifier = Modifier//THIS ROW DRAW A CIRCLE
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.secondary,
                                shape = CircleShape
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            //fontWeight = FontWeight.Medium,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = subTitle,
                            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_24),
                    contentDescription = "",
                )

            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_equalizer_24),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onOperateExistenceClicked() },
                tint = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDeleteClicked() },
                tint = MaterialTheme.colors.error
            )
        }
    }
}

//show logo---------------------------------------------
@Composable
fun MyLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.mipmap.logo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(170.dp, 130.dp)
    )
}

//show text password-----------------------------------------
@Composable
fun MyText(
    text: String,
    isTitle: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = if (!isTitle) MaterialTheme.typography.subtitle1.copy(
//show login text
            fontWeight = FontWeight.Light,
            //color = Color.Gray
        )
        else MaterialTheme.typography.h6.copy(
//show article text
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        ),
        maxLines = 3,
        modifier = modifier
    )
}

//CLick text-----------------------
@Composable
fun MyForgivenPassword(
    modifier: Modifier = Modifier,
    onRegisterClicked: (Int) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        ClickableText(
            text = AnnotatedString(
                text = "No tengo cuenta. Registrarme",
                spanStyle = SpanStyle(
                    color = Color.DarkGray,
                    textDecoration = TextDecoration.Underline
                )
            ),
            onClick = onRegisterClicked
        )
    }
}

@Composable
fun MyProgressBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondaryVariant)
    }
}