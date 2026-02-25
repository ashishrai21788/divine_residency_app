package com.app.core.views

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.app.core.utils.HSLocaleAwareTextView

//just to make expandable text view locale aware.
class ExpandableTextView : HSLocaleAwareTextView {

    var isCollapse = true

    private var showingLine = 1
    private var showingChar: Int = 0
    private var isCharEnable: Boolean = false

    private var showMore = "Show more"
    private var showLess = "Show less"
    private var dotdot = " "

    private val MAGIC_NUMBER = 10

    private var showMoreTextColor = Color.RED
    private var showLessTextColor = Color.RED

    private var mainText: String? = null

    private var isAlreadySet: Boolean = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainText = text.toString()
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
    }

    private fun addShowMore() {
        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                try {
                    val text = text.toString()
                    //mainText = text
                    if (!isAlreadySet) {
                        mainText = getText().toString()
                        isAlreadySet = true
                    }
                    var showingText = ""
                    if (isCharEnable) {
                        if (showingChar >= text.length) {
                            try {
                                throw Exception("Character count cannot be exceed total line count")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                        var newText = text.substring(0, showingChar)
                        newText += dotdot + showMore

                        isCollapse = true

                        setText(newText)

                    } else {

                        if (showingLine >= lineCount) {
                            try {
                                throw Exception("Line Number cannot be exceed total line count")
                            } catch (e: Exception) {
                                e.printStackTrace()

                            }

                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                            return
                        }
                        var start = 0
                        var end: Int
                        for (i in 0 until showingLine) {
                            end = layout.getLineEnd(i)
                            showingText += text.substring(start, end)
                            start = end
                        }

                        try {
                            var newText = showingText.substring(
                                0,
                                showingText.length - (dotdot.length + showMore.length + MAGIC_NUMBER)
                            )

                            newText += dotdot + showMore

                            isCollapse = true

                            setText(newText)
                        }catch (se:StringIndexOutOfBoundsException)
                        {
                            se.printStackTrace()
                        }
                    }

                    if(isCollapse) {
                        setShowMoreColoringAndClickable()
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }catch (e:java.lang.Exception){

                }

            }
        })
    }

    private fun setShowMoreColoringAndClickable() {
        val spannableString = SpannableString(text)


        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    maxLines = Integer.MAX_VALUE
                    text = mainText
                    isCollapse = false
                    showLessButton()
                }
            },
            text.length - (dotdot.length + showMore.length),
            text.length, 0
        )

        spannableString.setSpan(
            ForegroundColorSpan(showMoreTextColor),
            text.length - (dotdot.length + showMore.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun showLessButton() {

        val text = text.toString() + dotdot + showLess
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {

                    maxLines = showingLine

                    addShowMore()


                }
            },
            text.length - (dotdot.length + showLess.length),
            text.length, 0
        )

        spannableString.setSpan(
            ForegroundColorSpan(showLessTextColor),
            text.length - (dotdot.length + showLess.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, TextView.BufferType.SPANNABLE)
    }


    /*
     * User added field
     * */

    /**
     * User can add minimum line number to show collapse text
     *
     * @param lineNumber int
     */
    fun setShowingLine(lineNumber: Int) {
        if (lineNumber == 0) {
            try {
                throw Exception("Line Number cannot be 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }

        isCharEnable = false

        showingLine = lineNumber

        maxLines = showingLine

        if (isCollapse) {
            addShowMore()
        } else {
            maxLines = Integer.MAX_VALUE
            showLessButton()
        }

    }

    /**
     * User can limit character limit of text
     *
     * @param character int
     */
    fun setShowingChar(character: Int) {
        if (character == 0) {
            try {
                throw Exception("Character length cannot be 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }

        isCharEnable = true
        this.showingChar = character

        if (isCollapse) {
            addShowMore()
        } else {
            maxLines = Integer.MAX_VALUE
            showLessButton()
        }
    }

    /**
     * User can add their own  show more text
     *
     * @param text String
     */
    fun addShowMoreText(text: String) {
        showMore = text
    }

    /**
     * User can add their own show less text
     *
     * @param text String
     */
    fun addShowLessText(text: String) {
        showLess = text
    }

    /**
     * User Can add show more text color
     *
     * @param color Integer
     */
    fun setShowMoreColor(color: Int) {
        showMoreTextColor = color
    }

    /**
     * User can add show less text color
     *
     * @param color Integer
     */
    fun setShowLessTextColor(color: Int) {
        showLessTextColor = color
    }

    /**
     * User can add show dotdotdot text
     */
    fun setShowMoreTextDot(dotdotStr: String?=" ") {
        dotdot = dotdotStr?:" "
    }


}