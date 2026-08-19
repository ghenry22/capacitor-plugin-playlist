package org.dwbn.plugins.playlist

import android.app.Application

// Kept `open` (fork patch) so host apps that still subclass this (e.g. OptimalWork's
// MainApplication) keep compiling until they migrate to a plain Application class.
@Deprecated("No longer required; use your app's existing Application class")
open class App : Application()
