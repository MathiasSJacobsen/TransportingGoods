package algorithms

import Solution

interface IAlgorithm {
    val name: String
    fun search(initSolution: Solution) : Solution
}