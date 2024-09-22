package net.penguincoders.doit

import android.content.DialogInterface

interface DialogCloseListener {
    fun handleDialogClose(dialog: DialogInterface)
}
