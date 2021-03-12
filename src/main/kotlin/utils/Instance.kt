package utils

data class Instance(
    val numberOfNodes: Int,
    val numberOfVehicles: Int,
    val numberOfCalls: Int,
    val vehicles: Array<Vehicle?>,
    val calls: Array<Call?>,
    val travelTimeAndCost: Array<Array<Array<TravelTimeAndCost?>?>?>,
    val nodeTimeAndCost: Array<Array<NodeTimeAndCost?>?>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Instance

        if (numberOfNodes != other.numberOfNodes) return false
        if (numberOfVehicles != other.numberOfVehicles) return false
        if (numberOfCalls != other.numberOfCalls) return false
        if (!vehicles.contentEquals(other.vehicles)) return false
        if (!calls.contentEquals(other.calls)) return false
        if (!travelTimeAndCost.contentEquals(other.travelTimeAndCost)) return false
        if (!nodeTimeAndCost.contentEquals(other.nodeTimeAndCost)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numberOfNodes
        result = 31 * result + numberOfVehicles
        result = 31 * result + numberOfCalls
        result = 31 * result + vehicles.contentHashCode()
        result = 31 * result + calls.contentHashCode()
        result = 31 * result + travelTimeAndCost.hashCode()
        result = 31 * result + nodeTimeAndCost.hashCode()
        return result
    }

}