package com.brsv44n.some_courier.core.android_utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import androidx.core.text.getSpans
import com.brsv44n.some_courier.core.utils.Text

fun Text.asString(context: Context? = null): String = when (this) {
    Text.Empty -> ""
    is Text.Resource -> context?.getString(resId, *args).orEmpty()
    is Text.Simple -> value
}

@Suppress("Deprecated")
fun String.toHtml(flags: Int = Html.FROM_HTML_MODE_COMPACT): CharSequence =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, flags)
    } else {
        Html.fromHtml(this)
    }

@Suppress("MagicNumber")
fun String.formatTextWithBulletedList(): CharSequence {
    val spannable = SpannableString(this.toHtml().trim())
    spannable.getSpans<BulletSpan>(0, spannable.length - 1).forEach {
        val start = spannable.getSpanStart(it)
        val end = spannable.getSpanEnd(it)
        spannable.removeSpan(it)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            spannable.setSpan(
                BulletSpan(12.dpToPx.toInt(), 4),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannable.setSpan(
                BulletSpan(12.dpToPx.toInt()),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return spannable
}
