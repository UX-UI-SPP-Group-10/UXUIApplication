package com.example.uxuiapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.group10.uxuiapp.R

@Composable
fun ChangeButton(onClose: () -> Unit,
                 onDelete: () -> Unit,
                 onOpdate: () -> Unit,
                 onGifSelect: ()-> Unit,
                 onColorChange: () -> Unit)
{

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiary)
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
                    containerColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(30.dp), // Set icon size
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // GIF button: Add a GIF to the list
            Button(
                onClick = onGifSelect,
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary // Match first button
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gif),
                    contentDescription = "GIF",
                    modifier = Modifier.size(30.dp), // Set icon size
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Edit button: Edit name of the list
            Button(
                onClick = onOpdate,
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary // Match first button
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit text",
                    modifier = Modifier.size(30.dp), // Set icon size
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onColorChange,
                modifier = Modifier
                    .width(45.dp)
                    .height(38.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.palette),
                    contentDescription = "Change Color",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Delete button: Delete a list
            Button(
                onClick = onDelete,
                modifier = Modifier
                    .width(45.dp) // Set button width
                    .height(38.dp), // Set button height
                contentPadding = PaddingValues(0.dp), // Remove default padding
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary // Match first button
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete",
                    modifier = Modifier.size(30.dp), // Set icon size
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
