package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.toDomain
import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.session.SessionClearable
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.domain.entities.Data
import com.brsv44n.some_courier.core.utils.ResourceManager
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class ProfileDataRepository(
    private val api: ApiService,
    private val resourceManager: ResourceManager
) : ProfileRepository, SessionClearable {

    companion object {
        private const val USER_KEY = "user_key"
    }

    private val userCacheSource =
        com.brsv44n.some_courier.core.data.caching.SharedCachingSource<String, User> {
            api.getUser().toDomain(resourceManager)
        }

    override fun getUser(): Flow<Data<User>> =
        userCacheSource.getAsFlow(USER_KEY)

    override suspend fun fetchUser(): Result<User> = runCatching {
        userCacheSource.get(USER_KEY)
    }

    override suspend fun logout(): Result<Unit> {
        val result = runCatching { api.logout() }
        return result
    }

    override suspend fun clearSessionInfo() {
        userCacheSource.clear()
    }
}
