package algorithms.opeartions

import Solution
import java.util.Collections.swap
import kotlin.random.Random
import utils.costFunction
import utils.isFeasible

class BruteForce : Operation() {
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        var cost = solution.cost
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val random = (0 until solutionWithIndependentRoutes.size).random()
        val chosen = solutionWithIndependentRoutes[random]
        // Cant be bigger then then calls, cuz of heap overload
        if (chosen.size < 4 || chosen.size > 10){
            return solution
        }

        val copy = solutionWithIndependentRoutes
        val perms = chosen.permutations()
        var out = solution
        for (perm in perms) {
            copy[random] = perm
            val test = fromIndependentRoutesToSolution(copy)
            if (isFeasible(instance, test).first && costFunction(instance, test) < cost){
                cost = costFunction(instance, test)
                solutionWithIndependentRoutes[random] = perm
                out = Solution(instance, fromIndependentRoutesToSolution(solutionWithIndependentRoutes))
            }
        }
        return out

    }

    private fun <V> MutableList<V>.permutations(): HashSet<MutableList<V>> {
        val retVal: HashSet<MutableList<V>> = hashSetOf()

        fun generate(k: Int, list: List<V>) {
            // If only 1 element, just output the array
            if (k == 1) {
                retVal.add(list.toMutableList())
            } else {
                for (i in 0 until k) {

                    generate(k - 1, list)
                    if (k % 2 == 0) {
                        swap(list, i, k - 1)
                    } else {
                        swap(list, 0, k - 1)
                    }
                }
            }
        }

        generate(this.count(), this.toList())
        return retVal
    }

}