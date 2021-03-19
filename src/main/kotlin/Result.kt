import utils.Instance
// TODO: find a way to only take 3 decimals from improvement
data class Result(
    val instanceName: String,
    val search: String,
    val averageCost: Long,
    val bestObjective: Int,
    val improvement: Double,
    val runningTime: String,
    val instance: Instance,
    val solution: List<Int>,
)
