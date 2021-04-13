package algorithms

import Solution

interface IAlgorithm {
    val name: String
    fun search(initSolution: Solution, timeConstraint: Int = 0) : Solution
}