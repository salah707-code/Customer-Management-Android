package com.example.customermanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    // إضافة عميل جديد
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    // تعديل بيانات عميل
    @Update
    suspend fun updateCustomer(customer: Customer)

    // حذف عميل
    @Delete
    suspend fun deleteCustomer(customer: Customer)

    // جلب جميع العملاء مرتبين أحدث تعامل أولاً
    @Query("SELECT * FROM customers ORDER BY lastInteractionDate DESC")
    fun getAllCustomers(): Flow<List<Customer>>

    // البحث عن عميل بالاسم أو رقم الهاتف
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :searchQuery || '%' OR phone LIKE '%' || :searchQuery || '%'")
    fun searchCustomers(searchQuery: String): Flow<List<Customer>>

    // جلب العملاء الذين لديهم مبالغ مستحقة (المتبقي أكبر من صفر)
    @Query("SELECT * FROM customers WHERE remainingAmount > 0 ORDER BY remainingAmount DESC")
    fun getCustomersWithPendingPayments(): Flow<List<Customer>>

    // جلب عدد إجمالي العملاء
    @Query("SELECT COUNT(*) FROM customers")
    fun getCustomerCount(): Flow<Int>
}
