package com.examples.geoquiz

import androidx.annotation.StringRes

data class Questions (@StringRes val textResID: Int, val answer: Boolean, var answered: Boolean = false) {


}