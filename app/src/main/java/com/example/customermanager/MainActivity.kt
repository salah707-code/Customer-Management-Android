package com.example.customermanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.customermanager.data.Customer
import com.example.customermanager.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // فرض اتجاه الواجهة من اليمين إلى اليسار RTL كاملاً
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // حالة قفل التطبيق
    var isUnlocked by remember { mutableStateOf(false) }

    // الشاشة الحالية
    var currentScreen by remember { mutableStateOf("customers_list") }
    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }

    // بيانات وهمية للتجربة داخل التطبيق
    val sampleCustomers = remember {
        mutableStateListOf(
            Customer(
                id = 1,
                name = "أحمد محمود",
                phone = "0599123456",
                address = "شارع الملك خالد",
                itemType = "ملابس",
                totalAmount = 1500.0,
                paidAmount = 1350.0,
                remainingAmount = 150.0,
                registrationDate = "2026-01-10",
                lastInteractionDate = "2026-05-15",
                notes = "عميل مميز وسريع السداد"
            ),
            Customer(
                id = 2,
                name = "سارة علي",
                phone = "0501122334",
                address = "حي الأمل",
                itemType = "إلكترونيات",
                totalAmount = 3000.0,
                paidAmount = 3000.0,
                remainingAmount = 0.0,
                registrationDate = "2026-02-01",
                lastInteractionDate = "2026-05-14",
                notes = "تم سداد كامل المبلغ"
            )
        )
    }

    if (!isUnlocked) {
        LockScreen(onUnlockSuccess = { isUnlocked = true })
    } else {
        when (currentScreen) {
            "customers_list" -> CustomersScreen(
                customers = sampleCustomers,
                totalCustomersCount = sampleCustomers.size,
                onAddCustomerClick = { 
                    selectedCustomer = null
                    currentScreen = "add_edit_customer" 
                },
                onEditCustomerClick = { customer ->
                    selectedCustomer = customer
                    currentScreen = "add_edit_customer"
                },
                onDeleteCustomerClick = { customer ->
                    sampleCustomers.remove(customer)
                },
                onCustomerClick = { customer ->
                    selectedCustomer = customer
                    currentScreen = "customer_detail"
                }
            )

            "add_edit_customer" -> AddEditCustomerScreen(
                customerToEdit = selectedCustomer,
                onSaveClick = { customer ->
                    if (selectedCustomer == null) {
                        sampleCustomers.add(customer.copy(id = sampleCustomers.size + 1))
                    } else {
                        val index = sampleCustomers.indexOfFirst { it.id == customer.id }
                        if (index != -1) sampleCustomers[index] = customer
                    }
                    currentScreen = "customers_list"
                },
                onCancelClick = { currentScreen = "customers_list" }
            )

            "customer_detail" -> selectedCustomer?.let { customer ->
                CustomerDetailScreen(
                    customer = customer,
                    onBackClick = { currentScreen = "customers_list" },
                    onEditClick = { currentScreen = "add_edit_customer" },
                    onDeleteClick = {
                        sampleCustomers.remove(customer)
                        currentScreen = "customers_list"
                    },
                    onShareClick = { /* مشاركة التقرير */ }
                )
            }
        }
    }
}
