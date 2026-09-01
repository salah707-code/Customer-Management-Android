package com.example.customermanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.customermanager.data.Customer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCustomerScreen(
    customerToEdit: Customer? = null,
    onSaveClick: (Customer) -> Unit,
    onCancelClick: () -> Unit
) {
    var name by remember { mutableStateOf(customerToEdit?.name ?: "") }
    var phone by remember { mutableStateOf(customerToEdit?.phone ?: "") }
    var address by remember { mutableStateOf(customerToEdit?.address ?: "") }
    var itemType by remember { mutableStateOf(customerToEdit?.itemType ?: "") }
    var totalAmountText by remember { mutableStateOf(customerToEdit?.totalAmount?.toString() ?: "") }
    var paidAmountText by remember { mutableStateOf(customerToEdit?.paidAmount?.toString() ?: "") }
    var notes by remember { mutableStateOf(customerToEdit?.notes ?: "") }

    // حساب المبلغ المتبقي تلقائياً
    val totalAmount = totalAmountText.toDoubleOrNull() ?: 0.0
    val paidAmount = paidAmountText.toDoubleOrNull() ?: 0.0
    val remainingAmount = totalAmount - paidAmount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (customerToEdit == null) "إضافة عميل جديد" else "تعديل بيانات العميل",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("اسم العميل") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("رقم الهاتف") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("العنوان") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = itemType,
                onValueChange = { itemType = it },
                label = { Text("نوع البضاعة") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = totalAmountText,
                    onValueChange = { totalAmountText = it },
                    label = { Text("إجمالي المبلغ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = paidAmountText,
                    onValueChange = { paidAmountText = it },
                    label = { Text("المدفوع") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            // عرض المبلغ المتبقي المحسوب تلقائياً
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("المبلغ المتبقي (تلقائي):", fontWeight = FontWeight.Bold)
                    Text("$remainingAmount ر.س", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("ملاحظات") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("إلغاء")
                }

                Button(
                    onClick = {
                        val newCustomer = Customer(
                            id = customerToEdit?.id ?: 0,
                            name = name,
                            phone = phone,
                            address = address,
                            itemType = itemType,
                            totalAmount = totalAmount,
                            paidAmount = paidAmount,
                            remainingAmount = remainingAmount,
                            registrationDate = customerToEdit?.registrationDate ?: "2026-09-01",
                            lastInteractionDate = "2026-09-01",
                            notes = notes
                        )
                        onSaveClick(newCustomer)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    enabled = name.isNotBlank() && phone.isNotBlank()
                ) {
                    Text("حفظ")
                }
            }
        }
    }
}
