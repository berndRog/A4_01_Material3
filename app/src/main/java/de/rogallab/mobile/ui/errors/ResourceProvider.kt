package de.rogallab.mobile.ui.errors

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources

class ResourceProvider(private val context: Context) {
   fun getString(@StringRes resId: Int): String =
      context.getString(resId)
   fun getDrawable(@DrawableRes resId: Int): Drawable? =
      AppCompatResources.getDrawable(context, resId)
}