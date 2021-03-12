import utils.Instance
import utils.costFunction
import utils.isFeasible


data class Solution( val instance: Instance, val arr: IntArray) {

    public val solution = arr.map { it+1 }
    public val cost = costFunction(instance, arr)
    public val feasibility = isFeasible(instance, arr)

}