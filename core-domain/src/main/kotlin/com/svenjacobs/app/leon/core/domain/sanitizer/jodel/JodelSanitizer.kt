package com.svenjacobs.app.leon.core.domain.sanitizer.jodel

import android.content.Context
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import com.svenjacobs.app.leon.core.common.url.decodeUrl
import com.svenjacobs.app.leon.core.domain.R
import com.svenjacobs.app.leon.core.domain.sanitizer.Sanitizer
import com.svenjacobs.app.leon.core.domain.sanitizer.SanitizerId
import org.json.JSONObject

class JodelSanitizer : Sanitizer {
	override val id = SanitizerId("jodel")

	override fun getMetadata(context: Context) = Sanitizer.Metadata(
		name = context.getString(R.string.sanitizer_jodel_name),
	)

	@OptIn(ExperimentalEncodingApi::class)
	override fun invoke(input: String): String {
		val encoded = URL_REGEX.find(input)?.groupValues?.getOrNull(1) ?: return input
		val base64Data = decodeUrl(encoded)
		val jsonString = Base64.Default.decode(base64Data)
		val jsonObject = JSONObject(jsonString.decodeToString())
		val url = jsonObject.getString("\$android_url")
		return url
	}

	override fun matchesDomain(input: String) = URL_REGEX.containsMatchIn(input)

	private companion object {
		private val URL_REGEX = Regex("^https?://shared\\.jodel\\.com/a/key_live_.*&data=([^?&]*)")
	}
}
