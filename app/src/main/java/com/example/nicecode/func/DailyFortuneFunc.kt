package com.example.nicecode

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

private val Context.dailyFortuneDataStore by preferencesDataStore(name = "daily_fortune")

private val fortuneDateKey = stringPreferencesKey("fortune_date")
private val fortuneLevelKey = intPreferencesKey("fortune_level")
private val fortuneLuckyNumberKey = intPreferencesKey("fortune_lucky_number")

private val favorablePool = listOf(
    "\u5199\u4ee3\u7801",
    "\u559d\u5496\u5561",
    "\u8bfb\u4e66",
    "\u6563\u6b65",
    "\u65e9\u7761",
    "\u6574\u7406\u684c\u9762",
    "\u5b66\u65b0\u6280\u80fd",
    "\u8054\u7cfb\u8001\u670b\u53cb",
    "\u8fd0\u52a8",
    "\u9ebb\u8fa3\u70eb",
    "\u4e0a\u5206",
    "\u6478\u9c7c\u4e0d\u88ab\u53d1\u73b0"
)

private val unfavorablePool = listOf(
    "\u71ac\u591c",
    "\u5237\u77ed\u89c6\u9891",
    "\u62d6\u5ef6",
    "\u66b4\u996e\u66b4\u98df",
    "\u548c\u4eba\u4e89\u5435",
    "\u51b2\u52a8\u6d88\u8d39",
    "\u4e45\u5750\u4e0d\u52a8",
    "\u94bb\u725b\u89d2\u5c16",
    "\u73a9\u624b\u673a\u8fc7\u4e45",
    "\u9a7e\u9a76\u8fc7\u5feb",
    "\u538b\u6291\u60c5\u7eea"
)

private val fortuneLevels = listOf(
    FortuneLevelMeta(
        label = "\u51f6",
        quote = "\u585e\u7fc1\u5931\u9a6c\uff0c\u7109\u77e5\u975e\u798f"
    ),
    FortuneLevelMeta(
        label = "\u7a33",
        quote = "\u9759\u4ee5\u4fee\u8eab\uff0c\u4fed\u4ee5\u517b\u5fb7"
    ),
    FortuneLevelMeta(
        label = "\u987a",
        quote = "\u884c\u767e\u91cc\u8005\u534a\u4e5d\u5341"
    ),
    FortuneLevelMeta(
        label = "\u5927\u987a",
        quote = "\u6c34\u5230\u6e20\u6210\uff0c\u4e0d\u5fc5\u5f3a\u6c42"
    ),
    FortuneLevelMeta(
        label = "\u5c0f\u5409",
        quote = "\u4e0d\u79ef\u8dcc\u6b65\uff0c\u65e0\u4ee5\u81f3\u5343\u91cc"
    ),
    FortuneLevelMeta(
        label = "\u5409",
        quote = "\u65bd\u6069\u4e0d\u56fe\u62a5\uff0c\u671b\u4eba\u83ab\u6028"
    ),
    FortuneLevelMeta(
        label = "\u5927\u5409",
        quote = "\u5f97\u4e4b\u5766\u7136\uff0c\u5931\u4e4b\u6de1\u7136"
    )
)

internal data class FortuneLevelMeta(
    val label: String,
    val quote: String,
)

internal data class DailyFortune(
    val date: LocalDate,
    val levelIndex: Int,
    val luckyNumber: Int,
    val favorable: List<String>,
    val unfavorable: List<String>,
) {
    val levelMeta: FortuneLevelMeta
        get() = fortuneLevels[levelIndex]
}

internal suspend fun loadTodayFortune(
    context: Context,
    currentDate: LocalDate = LocalDate.now(),
    currentTime: LocalTime = LocalTime.now(),
): DailyFortune {
    val preferences: androidx.datastore.preferences.core.Preferences = context.dailyFortuneDataStore.data.first()
    val storedDate: String? = preferences[fortuneDateKey]
    val storedLevelIndex: Int? = preferences[fortuneLevelKey]
    val storedLuckyNumber: Int? = preferences[fortuneLuckyNumberKey]

    val todayString = currentDate.toString()
    val hasValidStoredValues = storedLevelIndex != null &&
        storedLuckyNumber != null &&
        storedLevelIndex in fortuneLevels.indices &&
        storedLuckyNumber in 0..9
    val hasTodayRecord = storedDate == todayString &&
        hasValidStoredValues

    if (hasTodayRecord) {
        return buildDailyFortune(
            date = currentDate,
            levelIndex = storedLevelIndex!!,
            luckyNumber = storedLuckyNumber!!
        )
    }

    val luckyNumber = generateLuckyNumber(currentDate)
    val levelIndex = generateLevelIndex(
        firstOpenTime = currentTime,
        luckyNumber = luckyNumber
    )

    context.dailyFortuneDataStore.edit { preferencesMap ->
        preferencesMap[fortuneDateKey] = todayString
        preferencesMap[fortuneLevelKey] = levelIndex
        preferencesMap[fortuneLuckyNumberKey] = luckyNumber
    }

    return buildDailyFortune(
        date = currentDate,
        levelIndex = levelIndex,
        luckyNumber = luckyNumber
    )
}

private fun buildDailyFortune(
    date: LocalDate,
    levelIndex: Int,
    luckyNumber: Int,
): DailyFortune {
    val dateSeed = date.toSeedValue()
    return DailyFortune(
        date = date,
        levelIndex = levelIndex,
        luckyNumber = luckyNumber,
        favorable = takeSeededItems(
            source = favorablePool,
            seed = dateSeed xor 0x13579BDFL,
            count = 3
        ),
        unfavorable = takeSeededItems(
            source = unfavorablePool,
            seed = dateSeed xor 0x2468ACE0L,
            count = 3
        )
    )
}

private fun generateLuckyNumber(date: LocalDate): Int {
    val seededLuckyNumber = Random(date.toSeedValue().toInt()).nextInt(10)
    val runtimeLuckyNumber = Random.nextInt(100)
    val remainderLuckyNumber =
        (runtimeLuckyNumber + seededLuckyNumber) % (seededLuckyNumber + 10)
    return remainderLuckyNumber % 10
}

private fun generateLevelIndex(
    firstOpenTime: LocalTime,
    luckyNumber: Int,
): Int {
    val minuteSecondProduct = firstOpenTime.minute * firstOpenTime.second
    val hashedValue = hashMinuteSecondProduct(minuteSecondProduct)
    val moduloResult = hashedValue % (luckyNumber + 1)

    return moduloResult % fortuneLevels.size
}

private fun hashMinuteSecondProduct(value: Int): Int {
    var hash = value
    hash = hash xor (hash shl 13)
    hash = hash xor (hash ushr 17)
    hash = hash xor (hash shl 5)
    return hash and Int.MAX_VALUE
}

private fun takeSeededItems(
    source: List<String>,
    seed: Long,
    count: Int,
): List<String> {
    return source.shuffled(Random(seed.toInt())).take(count)
}

private fun LocalDate.toSeedValue(): Long {
    return "%04d%02d%02d".format(year, monthValue, dayOfMonth).toLong()
}
