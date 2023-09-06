package com.example.grocerylist.config

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

class MockServerDispatcher {
    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/api/grocery-lists?userId=userId" -> MockResponse().setResponseCode(200).setBody(
                    getJsonContent("login_success.json")
                )
                "/api/session?IsDone=False" -> MockResponse().setResponseCode(200).setBody(
                    getJsonContent("sessions_response_list.json")
                )
                "/api/report/foruser/11" -> MockResponse().setResponseCode(200).setBody(
                    getJsonContent("reports_response_list.json")
                )
                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName))
            .use { it.readText() }
    }
}
