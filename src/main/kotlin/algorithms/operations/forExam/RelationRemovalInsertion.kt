package algorithms.operations.forExam

import Solution
import algorithms.operations.Operation
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import utils.Instance
import utils.RelationDataForCalls

class RelationRemovalInsertion : Operation() {

    override fun operation(solution: Solution): Solution {
        return solution
    }




    fun calculateRelation(instance: Instance){
        val out = mutableListOf<MutableList<Double>>(mutableListOf())
        for (i in 0 until instance.numberOfCalls-1){
            for (j in (i+1) until instance.numberOfCalls){

            }
        }



    }

    fun getData(instance: Instance): HashMap<Int, RelationDataForCalls?> {
        val t = hashMapOf<Int, RelationDataForCalls?>()

        for (call in instance.calls) {
            t[call!!.index] = RelationDataForCalls(mutableListOf(), call,null,Int.MAX_VALUE,Int.MIN_VALUE)
            for (vehicle in instance.vehicles) {
                if (vehicle!!.possibleCalls.contains(call.index)) {
                    val temp = t[call.index]
                    temp?.vehicles?.add(vehicle.index)
                    t[call.index] = temp
                }
            }
        }

        return t
    }

    fun getVehicleCallRelation(i:Int, j:Int, data: HashMap<Int, RelationDataForCalls?>): Double {
        val vehicleForCallI = data[i]?.vehicles!!
        val vehicleForCallJ = data[j]?.vehicles!!
        val minSize = min(vehicleForCallI.size, vehicleForCallJ.size)
        vehicleForCallI.retainAll(vehicleForCallJ)

        return 1.0 - (vehicleForCallI.size.toDouble()/minSize)
    }

    fun getSizeRelation(i: Int, j: Int, data: HashMap<Int, RelationDataForCalls?>): Int {
        val sizeOfCallI = data[i]?.call?.size!!
        val sizeOfCallJ = data[j]?.call?.size!!

        return abs(sizeOfCallI-sizeOfCallJ)
    }

    fun getTB(i: Int, j: Int, data: HashMap<Int, RelationDataForCalls?>): Int {
        val lowerDeliveryI = data[i]?.call?.lowerTimeDelivery!!
        val lowerDeliveryJ = data[j]?.call?.lowerTimeDelivery!!

        val upperDeliveryI = data[i]?.call?.upperTimeDelivery!!
        val upperDeliveryJ = data[j]?.call?.upperTimeDelivery!!

        return getTA(i, j, data) + min(upperDeliveryI, upperDeliveryJ) - max(lowerDeliveryI, lowerDeliveryJ)
    }

    fun getTA(i: Int, j: Int, data: HashMap<Int, RelationDataForCalls?>): Int {
        val lowerPickupI = data[i]?.call?.lowerTimePickup!!
        val lowerPickupJ = data[j]?.call?.lowerTimePickup!!

        val upperPickupI = data[i]?.call?.upperTimePickup!!
        val upperPickupJ = data[j]?.call?.upperTimePickup!!

        return min(upperPickupI, upperPickupJ) - max(lowerPickupI, lowerPickupJ)
    }
    fun normalize(maxX:Double, minX:Double, x:Double): Double {
        return (x-minX)/(maxX-minX)
    }

}