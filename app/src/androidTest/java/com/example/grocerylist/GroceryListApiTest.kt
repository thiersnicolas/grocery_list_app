package com.example.grocerylist

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.grocerylist.config.MockServerDispatcher
import com.example.grocerylist.network.GroceryListApiService
import com.example.grocerylist.network.rest.resources.GroceryListsResponse
import com.example.grocerylist.screens.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(BaseUrlModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class GroceryListApiTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var groceryListApiService: GroceryListApiService

    private val mockWebServer: MockWebServer = MockWebServer()

    @Module
    @InstallIn(SingletonComponent::class)
    class FakeBaseUrlModule {

        @Provides
        @Singleton
        fun getBaseUrl(): String = "http://localhost:8080/"
    }

    @Before
    fun setup() {
        hiltRule.inject()

        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun reset() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldGetReportsFromApi() {
        val reportsCall = groceryListApiService.getGroceryListsForUser("userId")

        reportsCall.enqueue(object : Callback<GroceryListsResponse?> {
            override fun onResponse(
                call: Call<GroceryListsResponse?>,
                response: Response<GroceryListsResponse?>
            ) {
                val groceryLists = response.body()?.groceryLists
                assertThat(groceryLists?.size, CoreMatchers.equalTo(5))

                val mappedToDomain = response.body()?.mapToGroceryLists()
                assertThat(
                    mappedToDomain?.get(0)?.id,
                    CoreMatchers.equalTo(UUID.fromString("9c2d393a-52c6-41a8-a2aa-950e0d4003a1"))
                )
                assertThat(mappedToDomain?.get(0)?.name, CoreMatchers.equalTo("AnotherOne"))
                assertThat(
                    mappedToDomain?.get(0)?.createdBy,
                    CoreMatchers.equalTo(UUID.fromString("37b2ff27-3a8d-4c0f-8f3d-3cc42a36ec1c"))
                )
                assertThat(
                    mappedToDomain?.get(0)?.createdDate,
                    CoreMatchers.equalTo(
                        LocalDateTime.parse(
                            "2023-09-06T12:13:45.664Z",
                            DateTimeFormatter.ISO_DATE_TIME
                        )
                    )
                )
                assertThat(mappedToDomain?.get(0)?.status, CoreMatchers.equalTo("open"))
            }

            override fun onFailure(call: Call<GroceryListsResponse?>, t: Throwable) {

            }
        })
    }
}
