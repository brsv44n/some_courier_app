package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.domain.models.Restaurant
import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.core.domain.entities.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class FakeProfileRepository : ProfileRepository {
    override fun getUser(): Flow<Data<User>> = flow {
        emit(Data(isLoading = true))
        delay(1000)
        emit(
            Data(
                value =
                User(
                    id = 1,
                    name = "Unnamed",
                    phone = "",
                    restaurant = Restaurant(
                        id = 1,
                        address = "Some address that shows",
                        managerPhone = "+79999999999"
                    )
                )
            )
        )
    }

    override suspend fun fetchUser(): Result<User> = Result.success(
        User(
            id = 1,
            name = "Unnamed",
            phone = "",
            restaurant = Restaurant(
                id = 1,
                address = "Some address that shows",
                managerPhone = "+79999999999"
            )
        )
    )

    override suspend fun logout(): Result<Unit> = Result.success(Unit)
}
