package algorithms

import Solution
import algorithms.opeartions.OneReinsert
import algorithms.opeartions.ThreeExchange
import algorithms.opeartions.TwoExchange
import utils.Instance
import utils.costFunction
import utils.isFeasible
import kotlin.random.Random

class LocalSearch:IAlgorithm {
    override fun search(initSolution: Solution): Solution {
        val p1 = 0.5
        val p2 = 0.01
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