package org.hydev.mcpm.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

fun err(msg: String) { System.err.println(msg) }

/**
 * Parallel map https://jivimberg.io/blog/2018/05/04/parallel-map-in-kotlin/
 */
suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}
