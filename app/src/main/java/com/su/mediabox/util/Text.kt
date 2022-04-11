package com.su.mediabox.util

object Text {
    /**
     * 屏蔽带有某些关键字的弹幕
     *
     * @return 若屏蔽此字符串，则返回true，否则false
     */
    fun String.shield(): Boolean {
        return false
    }

    /**
     * 格式化合成字符串，生成类似"1 - 2 - 3"这样的字符串，空白或者null不会加上多余分隔符
     */
    fun formatMergedStr(delimiter: String, vararg strs: String?) = StringBuilder().apply {
        for (str in strs)
            if (!str.isNullOrBlank())
                append(str).append(delimiter)
    }.removeSuffix(delimiter).toString()
}