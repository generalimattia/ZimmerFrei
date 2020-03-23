package com.generals.network.model

import arrow.core.Option
import arrow.core.toOption
import okhttp3.Request
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

sealed class APIResult<out T> {
    data class Success<T>(val body: Option<T>) : APIResult<T>()
    data class Failure(val code: Int) : APIResult<Nothing>()
    data class Error(val throwable: Throwable?) : APIResult<Nothing>()

    fun <R> fold(
            ifSuccess: (Option<T>) -> R,
            ifFailure: (Int) -> R,
            ifError: (Throwable?) -> R): R =
            when (this) {
                is Success -> ifSuccess(body)
                is Failure -> ifFailure(code)
                is Error -> ifError(throwable)
            }
}

abstract class CallDelegate<TIn, TOut>(
        protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, APIResult<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<APIResult<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code: Int = response.code()
            val result: APIResult<T> = if (code in 200 until 300) {
                val body: T? = response.body()
                APIResult.Success(body.toOption())
            } else {
                APIResult.Failure(code)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result: APIResult<Nothing> = if (t is Exception) {
                APIResult.Error(t)
            } else {
                APIResult.Failure(400)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())
}

class ResultAdapter(
        private val type: Type
) : CallAdapter<Type, Call<APIResult<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<APIResult<Type>> = ResultCall(call)
}

class APIResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType: Type = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                APIResult::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}