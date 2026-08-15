package com.example.nicecode

private const val countResultLength = 4
private val mainGroupParticipationRange = 1..3

internal sealed interface CountPermutationResult {
    data class Success(val results: List<String>) : CountPermutationResult
    data class Error(val message: String) : CountPermutationResult
}

internal fun validateCountPermutationInputs(
    groupValues: List<String>,
    mainGroupIndex: Int,
    mainGroupParticipationCountText: String,
): String? {
    val groups = groupValues.map { it.toList() }
    val nonEmptyGroups = groups.filter { it.isNotEmpty() }
    if (nonEmptyGroups.isEmpty()) {
        return "\u8bf7\u81f3\u5c11\u586b\u5199\u4e00\u7ec4\u6570\u5b57\u7ec4\u5408"
    }

    val mainGroup = groups.getOrNull(mainGroupIndex).orEmpty()
    if (mainGroup.isEmpty()) {
        return "\u4e3b\u8981\u7ec4\u5408\u4e0d\u80fd\u4e3a\u7a7a"
    }

    if (mainGroupParticipationCountText.isBlank()) {
        return "\u8bf7\u586b\u5199\u4e3b\u8981\u7ec4\u5408\u53c2\u4e0e\u6570"
    }

    val participationCount = mainGroupParticipationCountText.toIntOrNull()
        ?: return "\u4e3b\u8981\u7ec4\u5408\u53c2\u4e0e\u6570\u5fc5\u987b\u4e3a 1-3"
    if (participationCount !in mainGroupParticipationRange) {
        return "\u4e3b\u8981\u7ec4\u5408\u53ea\u80fd\u53c2\u4e0e 1-3 \u4f4d"
    }

    return null
}

internal fun generateCountPermutationResults(
    groupValues: List<String>,
    mainGroupIndex: Int,
    mainGroupParticipationCountText: String,
): CountPermutationResult {
    val validationMessage = validateCountPermutationInputs(
        groupValues = groupValues,
        mainGroupIndex = mainGroupIndex,
        mainGroupParticipationCountText = mainGroupParticipationCountText
    )
    if (validationMessage != null) {
        return CountPermutationResult.Error(validationMessage)
    }

    val groups = groupValues.map { it.toList() }
    val nonEmptyGroups = groups.filter { it.isNotEmpty() }
    val mainGroup = groups[mainGroupIndex]
    val allNonEmptyPool = nonEmptyGroups.flatten()
    val participationCount = mainGroupParticipationCountText.toInt()
    val uniqueResults = linkedSetOf<String>()

    generatePositionSelections(participationCount).forEach { mainPositions ->
        val pools = List(countResultLength) { index ->
            if (index in mainPositions) {
                mainGroup
            } else {
                allNonEmptyPool
            }
        }
        appendCartesianProducts(
            pools = pools,
            currentIndex = 0,
            builder = StringBuilder(),
            resultCollector = uniqueResults
        )
    }

    return CountPermutationResult.Success(uniqueResults.toList().sorted())
}

private fun generatePositionSelections(participationCount: Int): List<Set<Int>> {
    val positions = (0 until countResultLength).toList()
    val result = mutableListOf<Set<Int>>()

    fun dfs(startIndex: Int, current: MutableList<Int>) {
        if (current.size == participationCount) {
            result += current.toSet()
            return
        }

        for (index in startIndex until positions.size) {
            current += positions[index]
            dfs(index + 1, current)
            current.removeAt(current.lastIndex)
        }
    }

    dfs(startIndex = 0, current = mutableListOf())
    return result
}

private fun appendCartesianProducts(
    pools: List<List<Char>>,
    currentIndex: Int,
    builder: StringBuilder,
    resultCollector: MutableSet<String>,
) {
    if (currentIndex == pools.size) {
        resultCollector += builder.toString()
        return
    }

    pools[currentIndex].forEach { digit ->
        builder.append(digit)
        appendCartesianProducts(
            pools = pools,
            currentIndex = currentIndex + 1,
            builder = builder,
            resultCollector = resultCollector
        )
        builder.deleteCharAt(builder.lastIndex)
    }
}
