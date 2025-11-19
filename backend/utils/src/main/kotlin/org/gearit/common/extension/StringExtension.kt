package org.gearit.common.extension

/**
 * Утилитарный файл, для реализации универсальных функций со строкой.
 */

fun isNullOrBlank(value: String?): Boolean {
    return value.isNullOrEmpty()
}
