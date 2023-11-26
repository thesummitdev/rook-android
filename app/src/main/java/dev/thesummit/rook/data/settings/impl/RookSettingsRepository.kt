package dev.thesummit.rook.data.settings.impl

import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingDao
import kotlin.requireNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class RookSettingsRepository(private val settingDao: SettingDao) : SettingsRepository {

  override suspend fun addSetting(setting: Setting) {
    settingDao.insert(setting)
  }

  override suspend fun getSettingByKey(key: String): Flow<Result<Setting>> {
    return withContext(Dispatchers.IO) {
        settingDao.get(key).map { setting ->
          try {
            requireNotNull(setting) { """No setting found with key ${key}""" }
            Result.Success(setting)
          } catch (exception:Exception){
            Result.Error(exception)
          }
        }
    }
  }

  override suspend fun getSettingByKeyOnce(key:String): Result<Setting> {
      try {
        val setting = settingDao.getOnce(key)
        requireNotNull(setting) { """No setting found with key ${key}""" }
          return Result.Success(setting)
      } catch (exception: Exception) {
          return Result.Error(exception)
      }
  }

  override suspend fun dropSettings(keys: List<String>) {
    settingDao.dropSettings(keys)
  }

  override suspend fun dropAllSettings() {
    settingDao.dropAllSettings()
  }
}
