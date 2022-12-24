package tj.ilhom.musicappplayer.core.di

import android.content.Context
import android.content.SharedPreferences
import com.ilhom.core.json.JsonParserRepository
import com.ilhom.core.music.MusicManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.ilhom.core.music.MusicPlayerRepository
import com.ilhom.core.timer.TimerRepository
import tj.ilhom.musicappplayer.service.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModul {

    @Provides
    fun provideNotificationUtil(
        @ApplicationContext context: Context,
        musicPlayerRepository: MusicPlayerRepository
    ): NotificationUtil {
        return NotificationUtil(context, musicPlayerRepository)
    }

    @Provides
    fun providePlayMusicConfigStorage(
        @ApplicationContext context: Context, jsonParserRepository: JsonParserRepository
    ): com.ilhom.core.localStorage.PlayMusicConfigStorage {

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("music_play_config", Context.MODE_PRIVATE)

        return com.ilhom.core.localStorage.PlayMusicConfigStorage(
            sharedPreferences,
            jsonParserRepository
        )
    }

    @Provides
    @Singleton
    fun provideMediaSessionUtil(
        @ApplicationContext context: Context,
        musicPlayerRepository: MusicPlayerRepository
    ): MediaSessionUtil {
        return MediaSessionUtil(context, musicPlayerRepository)
    }

    @Provides
    @Singleton
    fun provideMusicPlayerUtil(@ApplicationContext context: Context): MusicPlayerRepository {
        return MusicPlayerRepository(context)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        val coroutineContext = SupervisorJob() + Dispatchers.IO
        return CoroutineScope(coroutineContext)
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface AppModules {

    @Binds
    fun bindReadAllMusicRepository(readlAllMusicRepository: com.ilhom.core.musicrepository.ReadAllMusicRepository.Base): com.ilhom.core.musicrepository.ReadAllMusicRepository

    @Binds
    fun bindReadMediaIcon(readMediaIcon: com.ilhom.core.musicrepository.ReadMedia.Base): com.ilhom.core.musicrepository.ReadMedia

    @Binds
    fun bindTimerRepository(timerRepository: TimerRepository.Base): TimerRepository

    @Binds
    fun bindMutableMusicConfig(mutableMusicConfig: com.ilhom.core.localStorage.MutableMusicConfig.Base): com.ilhom.core.localStorage.MutableMusicConfig

    @Binds
    fun bindMusicManager(manager: MusicManager.Base): MusicManager

    @Binds
    fun bindMutableLatestMusicRepository(latestMusic: com.ilhom.core.localStorage.latestMusic.MutableLatestMusic.Base): com.ilhom.core.localStorage.latestMusic.MutableLatestMusic

    @Binds
    fun bindReadLatestMusicRepository(mutableLatestMusic: com.ilhom.core.localStorage.latestMusic.MutableLatestMusic): com.ilhom.core.localStorage.latestMusic.ReadLatestMusic

    @Binds
    fun bindSavelatestMusic(mutableLatestMusic: com.ilhom.core.localStorage.latestMusic.MutableLatestMusic): com.ilhom.core.localStorage.latestMusic.SaveLatestMusic

    @Binds
    fun bindJsonParserr(jsonParserRepository: JsonParserRepository.Base): JsonParserRepository

}

@Module
@InstallIn(ActivityComponent::class)
interface ResModule {
    @Binds
    fun bindResManager(resManager: com.ilhom.core.musicrepository.ResManager.Base): com.ilhom.core.musicrepository.ResManager
}


