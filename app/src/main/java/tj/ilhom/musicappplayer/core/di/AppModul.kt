package tj.ilhom.musicappplayer.core.di

import android.content.Context
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
import tj.ilhom.musicappplayer.repository.TimerRepository
import tj.ilhom.musicappplayer.repository.musicrepository.ReadAllMusicRepository
import tj.ilhom.musicappplayer.repository.musicrepository.ReadMedia
import tj.ilhom.musicappplayer.repository.musicrepository.ResManager
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao
import tj.ilhom.musicappplayer.repository.room.database.MusicDatabase
import tj.ilhom.musicappplayer.service.MediaSessionUtil
import tj.ilhom.musicappplayer.service.MusicManager
import tj.ilhom.musicappplayer.service.MusicPlayerUtil
import tj.ilhom.musicappplayer.service.NotificationUtil


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
    fun provideMusicManager(
        @ApplicationContext context: Context,
        musicDao: MusicDao,
        coroutineScope: CoroutineScope,
        musicPlayerUtil: MusicPlayerUtil,
        musicSessionUtil: MediaSessionUtil,
        notificationUtil: NotificationUtil
    ): MusicManager {
        return MusicManager(
            context,
            musicDao,
            coroutineScope,
            musicPlayerUtil,
            musicSessionUtil,
            notificationUtil
        )
    }

    @Provides
    fun provideNotificationUtil(@ApplicationContext context: Context): NotificationUtil {
        return NotificationUtil(context)
    }

    @Provides
    fun provideMediaSessionUtil(@ApplicationContext context: Context): MediaSessionUtil {
        return MediaSessionUtil(context)
    }

    @Provides
    fun provideMusicPlayerUtil(): MusicPlayerUtil {
        return MusicPlayerUtil()
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

}

@Module
@InstallIn(ActivityComponent::class)
interface ResModule {
    @Binds
    fun bindResManager(resManager: ResManager.Base): ResManager
}


