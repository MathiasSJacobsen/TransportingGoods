import utils.Instance
import utils.costFunction
import utils.isFeasible


data class Solution( val instance: Instance, val arr: MutableList<Int>) {

    val solution = arr.map { it+1 }
    val cost = costFunction(instance, arr)
    val feasibility = isFeasible(instance, arr)

}