package de.mm20.launcher2.icons.providers

import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import de.mm20.launcher2.database.AppDatabase
import de.mm20.launcher2.icons.*
import de.mm20.launcher2.ktx.randomElementOrNull
import de.mm20.launcher2.preferences.Settings
import de.mm20.launcher2.search.data.LauncherApp
import de.mm20.launcher2.search.data.Searchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class IconPackIconProvider(
    private val context: Context,
    private val iconPack: String,
    private val iconPackManager: IconPackManager,
): IconProvider {
    override suspend fun getIcon(searchable: Searchable, size: Int): LauncherIcon? {
        if (searchable !is LauncherApp) return null

        val component = ComponentName(searchable.`package`, searchable.activity)
        return iconPackManager.getIcon(iconPack, component, size)
            ?: iconPackManager.generateIcon(
                context,
                iconPack,
                baseIcon = withContext(Dispatchers.IO) {
                    searchable.launcherActivityInfo.getIcon(context.resources.displayMetrics.densityDpi)
                },
                size = size,
            )
    }
}