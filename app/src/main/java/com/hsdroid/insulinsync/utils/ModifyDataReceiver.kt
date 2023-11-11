package com.hsdroid.insulinsync.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModifyDataReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dao: StoreDao
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let {
            var list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("listData", Insulin::class.java)
            } else {
                intent.getParcelableExtra("listData")
            }
            list?.apply {

                val calc = quantityTotal.toInt() - dosageUnit.toInt()
                val calc2 = calc.toFloat()

                Log.d("harish", "result after modification is $calc")

                Thread {
                    dao.insertData(
                        Insulin(
                            id,
                            uname,
                            insulinName,
                            quantityTotal,
                            calc2,
                            dosageUnit,
                            dosageTime
                        )
                    )
                }.start()
            }
        }
    }
}