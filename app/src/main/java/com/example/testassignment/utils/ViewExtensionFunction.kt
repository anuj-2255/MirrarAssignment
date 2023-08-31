package com.example.testassignment.utils

import android.app.Activity
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart


fun containsIgnoreCase(str: String?, searchStr: String?): Boolean {
    if (str == null || searchStr == null) return false

    val length = searchStr.length
    if (length == 0)
        return true

    for (i in str.length - length downTo 0) {
        if (str.regionMatches(i, searchStr, 0, length, ignoreCase = true))
            return true
    }
    return false
}

fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        checkMainThread()
        val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}


internal fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

fun Activity.showToast(text: String) {
    Toast.makeText(this.applicationContext, text, Toast.LENGTH_SHORT).show()
}

//var mPopupMenu: ListPopupWindow? = null
/*
fun Context.showMenu(
    array: ArrayList<String>,
    view: View,
    listener: CommonListener
) {


    mPopupMenu?.let {
        it.dismiss()
        return
    }



    mPopupMenu = ListPopupWindow(this)
    mPopupMenu?.isModal = true

    mPopupMenu?.let { popupMenu ->

        popupMenu.setAdapter(
            ArrayAdapter(
                this,
                R.layout.spinner_dropdown,
                array
            )
        )
        popupMenu.anchorView = view
        popupMenu.height = ListPopupWindow.WRAP_CONTENT
        //popupMenu.horizontalOffset = -360
        // popupMenu.width = 100
        // popupMenu.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.stroke_round_corner_gray))

        //popupMenu.isModal = true

        popupMenu.setOnDismissListener {
            mPopupMenu = null

        }

        popupMenu.setOnItemClickListener { adapterView, view, i, l ->
            listener.onItemSelect(array[i], i)
            popupMenu.dismiss()
            mPopupMenu = null

        }

        popupMenu.show()

    }


}
*/
