package algorithms.operations.forExam

import Solution
import algorithms.operations.Operation

class RandomRemovalGreedyInsertion : Operation()
{
    override fun operation(solution: Solution): Solution {
        val instance = solution.instance
        val solutionWithIndependentRoutes = solutionWithIndependentRoutes(solution)
        val removedCalls = removeCalls(solution)
        val copy = solution.arr.toMutableList()
        if (removedCalls.isNullOrEmpty()){
            return solution
        }
        copy.removeAll(removedCalls)
        var copySol = Solution(solution.instance, copy)

        var best = removedCalls.map { bestInsert(copySol, it) }.toMutableList()
        for (i in 0 until best.size){
            val minCost = best.minByOrNull { it.cost }!!
            val temp = solutionWithIndependentRoutes(copySol)
            temp[minCost.routeIndx].add(minCost.insert1, minCost.call)
            temp[minCost.routeIndx].add(minCost.insert2, minCost.call)
            copySol = Solution(solution.instance, fromIndependentRoutesToSolution(temp))

            best.remove(minCost)
            best = best.map {
                if (it.routeIndx == minCost.routeIndx){
                    bestInsert(copySol, it.call)
                } else {
                    it
                }
            }.toMutableList()
        }

        return copySol
    }


}