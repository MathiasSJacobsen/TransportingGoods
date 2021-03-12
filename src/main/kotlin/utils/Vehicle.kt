package utils

data class Vehicle(
    val index: Int,
    val homeNode: Int,
    val startingTime: Int,
    val capacity: Int,
    var possibleCalls: List<Int> = arrayListOf()


) {

}