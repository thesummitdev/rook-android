package dev.thesummit.rook.data.settings

import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.data.Result
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

  suspend fun addSetting(setting:Setting): Unit
  suspend fun getSettingByKey(key:String): Flow<Result<Setting>>
  suspend fun getSettingByKeyOnce(key:String): Result<Setting>
  suspend fun dropSettings(keys:List<String>): Unit
  suspend fun dropAllSettings(): Unit

}
