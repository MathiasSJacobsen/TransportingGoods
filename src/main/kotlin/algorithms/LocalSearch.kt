package algorithms

import Solution
import algorithms.operations.OneReinsert
import algorithms.operations.ThreeExchange
import algorithms.operations.TwoExchange
import utils.costFunction
import utils.isFeasible
import kotlin.random.Random

class LocalSearch:IAlgorithm {
    override val name: String
        get() = "Local search"

    override fun search(initSolution: Solution, timeConstraint: Int): Solution {
        val p1 = 0.3
        val p2 = 0.2
        val instance = initSolution.instance
        var bestSolution = initSolution

        for (i in 0 until 10000){
            val rand = Random.nextFloat()
            val currentSolution = when {
                rand < p1 -> {
                    TwoExchange().operation(bestSolution)
                }
                rand < p1+p2 -> {
                    ThreeExchange().operation(bestSolution)
                }
                else -> {
                    OneReinsert().operation(bestSolution)
                }
            }
            if (isFeasible(instance,currentSolution.arr).first &&
                costFunction(instance, currentSolution.arr) < costFunction(instance, bestSolution.arr)){ bestSolution = currentSolution
            }
        }
        return bestSolution
    }
}