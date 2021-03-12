package utils


data class Call(
    val index: Int,
    val originNode: Int,
    val destinationNode: Int,
    val size: Int,
    val costOfNotTransporting: Int,
    val lowerTimePickup: Int,
    val upperTimePickup: Int,
    val lowerTimeDelivery: Int,
    val upperTimeDelivery: Int
)