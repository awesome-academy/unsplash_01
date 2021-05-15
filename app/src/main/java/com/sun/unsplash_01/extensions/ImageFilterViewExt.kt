package com.sun.unsplash_01.extensions

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.constraintlayout.utils.widget.ImageFilterView

fun ImageFilterView.toBitmap(): Bitmap? {
    return (this.drawable as? BitmapDrawable)?.bitmap
}
