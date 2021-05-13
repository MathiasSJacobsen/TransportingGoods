package algorithms

import Solution
import algorithms.operations.BruteForce
import algorithms.operations.K_Reinsert
import algorithms.operations.OneReinsert
import algorithms.operations.RemoveMostExpensiveFromDummy
import kotlin.math.exp
import kotlin.random.Random
import utils.isFeasible
import utils.costFunction as costFunction1

class SimulatedAnnealingBetter : IAlgorithm {
    override val name: String
        get() = "Simulated annealing (new)"

    override fun search(initSolution: Solution, timeConstraint: Double): Solution {
        val p1 = 0.001
        val p2 = 0.001
        val p3 = 0.5
        val instance = initSolution.instance
        var temperature = 200.0
        val coolingFactor = 0.998
        var incumbent = initSolution
        var bestSolution = initSolution
        for (i in 0 until 10000) {
            val rand = Random.nextFloat()
            val newSolution = when {
                rand < p1 -> {
                    BruteForce().operation(incumbent)
                } rand < p1 + p2 -> {
                    K_Reinsert().operation(incumbent)
                } rand < p1 + p2 + p3 ->{
                    OneReinsert().operation(incumbent)
                } else -> {
                    RemoveMostExpensiveFromDummy().operation(incumbent)
                }
            }
            val deltaE = costFunction1(instance, newSolution.arr) - costFunction1(instance, incumbent.arr)
            val randII = Random.nextFloat()
            if (deltaE < 0 && isFeasible(instance, newSolution.arr).first){
                incumbent = newSolution
                if (costFunction1(instance, newSolution.arr) < costFunction1(instance, bestSolution.arr)){
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
