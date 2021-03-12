package algorithms

import Solution
import algorithms.opeartions.OneReinsert
import algorithms.opeartions.ThreeExchange
import algorithms.opeartions.TwoExchange
import utils.costFunction
import utils.isFeasible
import kotlin.math.exp
import kotlin.random.Random

class SimulatedAnnealing : IAlgorithm{
    override fun search(initSolution: Solution): Solution {
        val p1 = 0.3
        val p2 = 0.2
        val instance = initSolution.instance
        var temperature = 40.0
        val coolingFactor = 0.998
        var incumbent = initSolution
        var bestSolution = initSolution
        for (i in 0 until 10000) {
            val rand = Random.nextFloat()
            val newSolution = when {
                rand < p1 -> {
                    TwoExchange().operation(incumbent)
                }rand < p1 + p2 -> {
                    ThreeExchange().operation(incumbent)
                } else -> {
                    OneReinsert().operation(incumbent)
                }
            }
            val deltaE = costFunction(instance, newSolution.arr) - costFunction(instance, incumbent.arr)
            val randII = Random.nextFloat()
            if (deltaE < 0 && isFeasible(instance, newSolution.arr).first){
                incumbent = newSolution
                if (costFunction(instance, newSolution.arr) < costFunction(instance, bestSolution.arr)){
                    bestSolution = incumbent
                }
            } else if (isFeasible(instance, newSolution.arr).first && randII < exp(-(deltaE).toFloat()/temperature)){
                incumbent = newSolution
            }
            temperature *= coolingFactor
        }
        return bestSolution
    }
}