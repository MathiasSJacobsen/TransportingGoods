package utils

data class RelationDataForCalls(
    val vehicles: MutableList<Int>,
    val call : Call,
    var travelTime: Array<Array<TravelTimeAndCost?>?>?,
    var minSize: Int,
    var maxSize: Int,

)
