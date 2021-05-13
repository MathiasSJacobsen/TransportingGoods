package algorithms

import Solution

interface IAlgorithm {
    val name: String
    fun search(initSolution: Solution, timeConstraint: Double = 0.0) : Solution
}