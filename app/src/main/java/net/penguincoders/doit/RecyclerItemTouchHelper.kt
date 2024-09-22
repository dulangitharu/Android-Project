package net.penguincoders.doit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.penguincoders.doit.Adapters.ToDoAdapter

class RecyclerItemTouchHelper(private val adapter: ToDoAdapter, private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete this task?")
            builder.setPositiveButton("Confirm") { _, _ ->
                adapter.deleteItem(position)
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        val icon: Drawable?
        val background: ColorDrawable

        if (dX > 0) { // Swiping to the right
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_edit)
            background = ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        } else { // Swiping to the left
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete)
            background = ColorDrawable(Color.RED)
        }

        icon?.let {
            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
            val iconBottom = iconTop + it.intrinsicHeight

            if (dX > 0) { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + it.intrinsicWidth
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
            } else if (dX < 0) { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
            } else { // No swipe
                background.setBounds(0, 0, 0, 0)
            }

            background.draw(c)
            it.draw(c)
        }
    }
}
