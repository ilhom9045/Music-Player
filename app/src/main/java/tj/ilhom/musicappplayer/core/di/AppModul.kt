package tj.ilhom.musicappplayer.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
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
import tj.ilhom.musicappplayer.repository.JsonParserRepository
import tj.ilhom.musicappplayer.repository.MusicManager
import tj.ilhom.musicappplayer.repository.TimerRepository
import tj.ilhom.musicappplayer.repository.localStorage.MutableMusicConfig
import tj.ilhom.musicappplayer.repository.localStorage.PlayMusicConfigStorage
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.MutableLatestMusic
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.ReadLatestMusic
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.SaveLatestMusic
import tj.ilhom.musicappplayer.repository.musicrepository.ReadAllMusicRepository
import tj.ilhom.musicappplayer.repository.musicrepository.ReadMedia
import tj.ilhom.musicappplayer.repository.musicrepository.ResManager
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao
import tj.ilhom.musicappplayer.repository.room.database.MusicDatabase
import tj.ilhom.musicappplayer.service.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModul {

    @Provides
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        val dbName = "musics"
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            dbName
        ).build()
    }

    @Provides
    fun provideMusicDao(musicDatabase: MusicDatabase): MusicDao {
        return musicDatabase.dao()
    }


    @Provides
    fun provideNotificationUtil(
        @ApplicationContext context: Context,
        musicPlayerUtil: MusicPlayerUtil
    ): NotificationUtil {
        return NotificationUtil(context, musicPlayerUtil)
    }

    @Provides
    fun providePlayMusicConfigStorage(
        @ApplicationContext context: Context, jsonParserRepository: JsonParserRepository
    ): PlayMusicConfigStorage {

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("music_play_config", Context.MODE_PRIVATE)

        return PlayMusicConfigStorage(sharedPreferences, jsonParserRepository)
    }

    @Provides
    @Singleton
    fun provideMediaSessionUtil(
        @ApplicationContext context: Context,
        musicPlayerUtil: MusicPlayerUtil
    ): MediaSessionUtil {
        return MediaSessionUtil(context, musicPlayerUtil)
    }

    @Provides
    @Singleton
    fun provideMusicPlayerUtil(@ApplicationContext context: Context): MusicPlayerUtil {
        return MusicPlayerUtil(context)
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
    fun bindReadAllMusicRepository(readlAllMusicRepository: ReadAllMusicRepository.Base): ReadAllMusicRepository

    @Binds
    fun bindReadMediaIcon(readMediaIcon: ReadMedia.Base): ReadMedia

    @Binds
    fun bindTimerRepository(timerRepository: TimerRepository.Base): TimerRepository

    @Binds
    fun bindMutableMusicConfig(mutableMusicConfig: MutableMusicConfig.Base): MutableMusicConfig

    @Binds
    fun bindMusicManager(manager: MusicManager.Base): MusicManager

    @Binds
    fun bindMutableLatestMusicRepository(latestMusic: MutableLatestMusic.Base): MutableLatestMusic

    @Binds
    fun bindReadLatestMusicRepository(mutableLatestMusic: MutableLatestMusic): ReadLatestMusic

    @Binds
    fun bindSavelatestMusic(mutableLatestMusic: MutableLatestMusic): SaveLatestMusic

    @Binds
    fun bindJsonParserr(jsonParserRepository: JsonParserRepository.Base):JsonParserRepository

}

@Module
@InstallIn(ActivityComponent::class)
interface ResModule {
    @Binds
    fun bindResManager(resManager: ResManager.Base): ResManager
}


