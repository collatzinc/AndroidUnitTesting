package com.collatzinc.androidunittesting.common

import java.util.regex.Pattern

object Utils {

    fun passwordWithoutSpaceAtStartAndEnd(): Pattern = Pattern.compile(
        "^\\S+(\\s+\\S+)*$"
    )
}