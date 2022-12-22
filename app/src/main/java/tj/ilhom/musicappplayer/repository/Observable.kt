package tj.ilhom.musicappplayer.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

interface Observable<T> {

    suspend fun observe(call: (suspend (value: T) -> Unit)? = null)

    fun observe(owner: LifecycleOwner, call: ((value: T) -> Unit)? = null)

    suspend fun value(value: T)

    abstract class Abstract<T> : Observable<T> {

        private var call: (suspend (value: T) -> Unit)? = null
        private var value: T? = null

        override suspend fun observe(call: (suspend (value: T) -> Unit)?) {
            this.call = call
            value?.let {
                this.call?.invoke(it)
            }
        }

        override fun observe(owner: LifecycleOwner, call: ((value: T) -> Unit)?) {
            owner.lifecycleScope.launch {
                owner.lifecycleScope.launchWhenStarted {
                    observe {
                        owner.lifecycleScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                            call?.invoke(it)
                        }
                    }
                }
            }
        }

        override suspend fun value(value: T) {
            call?.invoke(value)
            if (call == null) {
                this.value = value
            }
        }
    }

    class ObserveBoolean : Abstract<Boolean>()

    class ObserveString : Abstract<String>()

}
