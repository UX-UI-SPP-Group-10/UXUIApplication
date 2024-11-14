package com.example.uxuiapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.group10.uxuiapp.R

@Composable
fun ChangeButton(onClose: () -> Unit) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFF000000))
        .padding(8.dp)
    ) {
        Row {
            // Close Button: Closes the change button
            Button(
                onClick = onClose,
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    modifier = Modifier.size(30.dp).background(Color(0xFF000000)), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // GIF button: Add a GIF to the list - NOT WORKING
            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000) // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gif),
                    contentDescription = "GIF",
                    modifier = Modifier.size(60.dp).background(Color(0xFF000000)), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Edit button: Edit name of the list (maybe color?) - NOT WORKING
            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000) // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit text",
                    modifier = Modifier.size(30.dp).background(Color(0xFF000000)), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button: Delete a list - NOT WORKING
            Button(
                onClick = {},
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000) // Set button background color
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete",
                    modifier = Modifier.size(30.dp).background(Color(0xFF000000)), // Set icon size as desired
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}
