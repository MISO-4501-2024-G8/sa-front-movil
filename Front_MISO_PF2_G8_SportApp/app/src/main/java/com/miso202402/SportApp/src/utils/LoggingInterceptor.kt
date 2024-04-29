import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method()
        val url = request.url().toString()
        val headers = request.headers()
        val requestBody = request.body()

        // Loguear la información
        println("Request URL: $url")
        println("Request Method: $method")
        println("Request Headers: $headers")
        println("Request Body: $requestBody")

        val response = chain.proceed(request)

        // Aquí también puedes registrar la respuesta si lo deseas
        val responseCode = response.code().toString()
        val responseHeaders = response.headers().toString()
        val responseBody = response.body()

        println("Response Code: $responseCode")
        println("Response Headers: $responseHeaders")
        println("Response Body: ${responseBody?.string()}")

        return response
    }
}

