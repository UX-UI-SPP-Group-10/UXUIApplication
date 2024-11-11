package com.example.uxuiapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChangeButton() {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.secondary)) {
        Row {
            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    modifier = Modifier.size(30.dp).background(MaterialTheme.colorScheme.secondary), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gif),
                    contentDescription = "GIF",
                    modifier = Modifier.size(60.dp).background(MaterialTheme.colorScheme.secondary), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit text",
                    modifier = Modifier.size(30.dp).background(MaterialTheme.colorScheme.secondary), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete",
                    modifier = Modifier.size(30.dp).background(MaterialTheme.colorScheme.secondary), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun SeeChangeButton(){
    ChangeButton()
}