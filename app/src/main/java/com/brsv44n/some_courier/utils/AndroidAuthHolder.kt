package com.brsv44n.some_courier.utils

import android.content.Context
import androidx.core.content.edit
import com.brsv44n.some_courier.domain.session.SessionClearable
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.AuthHolder.Tokens
import com.brsv44n.some_courier.core.utils.Optional
import com.brsv44n.some_courier.core.utils.asOptional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class AndroidAuthHolder(
    context: Context,
) : AuthHolder, SessionClearable {

    private companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }

    private val prefs = context.getSharedPreferences("auth_data", Context.MODE_PRIVATE)

    override val tokenFlow: Flow<Optional<Tokens>>
        get() = _tokenFlow.asStateFlow()

    override val isLoggedIn: Boolean
        get() = getTokens() != null

    private val _tokenFlow = MutableStateFlow(getTokens().asOptional())

    override fun setTokens(tokens: Tokens) {
        prefs.edit {
            putString(ACCESS_TOKEN, tokens.accessToken)
            putString(REFRESH_TOKEN, tokens.refreshToken)
        }
        _tokenFlow.value = tokens.asOptional()
    }

    override fun getTokens(): Tokens? {
        val accessToken = prefs.getString(ACCESS_TOKEN, null)
        val refreshToken = prefs.getString(REFRESH_TOKEN, null)
        if (accessToken == null) return null
        return Tokens(refreshToken, accessToken)
    }

    override fun clear() {
        prefs.edit { clear() }
        _tokenFlow.value = Optional.empty()
    }

    override suspend fun clearSessionInfo() {
        clear()
    }
}
