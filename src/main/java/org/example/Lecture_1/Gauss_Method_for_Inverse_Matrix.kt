package org.example.Lecture_1

import kotlin.math.abs

fun determinant(matrix: Array<DoubleArray>): Double {
    return when (matrix.size) {
        2 -> matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]  // 2x2
        3 -> matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0])  // 3x3
        else -> throw IllegalArgumentException("Sorry, only matriz with size: 3x3 and 2x2")
    }
}

fun inverseMatrix(matrix: Array<DoubleArray>): Array<DoubleArray>? {// We put in this function our array[][] (matrix) and return also matrix

    //Gauss Method
    val size = matrix.size // predict that we are working with the quadratic matrix and function size for example will return 3 (cause it return amount of row)
    // println(size) // I have checked it

    //Here we checked if det = 0, then we reject request/ Otherwise all okay/ instead if it's right size of the matrix
    val det = determinant(matrix)
    if (abs(det) < 1e-9) {
        println("Determinant of this matrix equals or close to the zero! So, we cant find the inverse matrix")
        return null
    }

    /*
        [*][*][*]/[1][1][1]
        [*][*][*]/[1][1][1] <- augmented matrix (column: size * 2; row: size)
        [*][*][*]/[1][1][1]
    */
    val augmentedMatrix = Array(size) {DoubleArray(2 * size)}


    // we need to заполнить matrix on left side by our matrix and the right side with identity matrix
    for (i in 0 until size){
        for(j in 0 until size){
            augmentedMatrix[i][j] = matrix[i][j] // our left part with given matrix
        }
        augmentedMatrix[i][i + size] = 1.0 // our right part with identity matrix
    }
    // and so our augmented matrix will be 3 by 6 (row: 3, column: 6)

    //check that everything allright
    for (row in augmentedMatrix) {
        println(row.joinToString(", "))
    }
    /*
        4.0, 7.0, 5.0, 1.0, 0.0, 0.0
        2.0, 6.0, 9.0, 0.0, 1.0, 0.0 <- yeah that's okay
        4.0, 6.0, 7.0, 0.0, 0.0, 1.0
    */

    //Direct of the Gaussian method
    for (i in 0 until size) {

        // Diagonal element should be equal to "1", so we need divide by this element
        val diagElement = augmentedMatrix[i][i]

        for (j in 0 until 2 * size) { // 'cause this is our matrix + identity matrix we will use "2 * size"/ cause our matrix is quadratic
            augmentedMatrix[i][j] /= diagElement // every diagonal element must be 1
        }

        // Reset the elements in the rows in the current column to zero
        for (k in 0 until size) {
            if (k != i) {
                val factor = augmentedMatrix[k][i] // not diagonal number
                for (j in 0 until 2 * size) { // like in the previous loop
                    augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j] // "nullarize" or reset value for arr[k][i]
                } // it's easy to see this, but in reality it's more complex
            }
        } // -> example
        // [2][1]
        // [5][3]
        // =>
        // [1][0.5]
        // [5][3]
        // =>
        // 2'nd row = 2'nd row - 5 * 1'st row
        // =>
        // [1][0.5]
        // [0][0.5]
    } // This example represent step by step dividing row to zero value

    // Our inverse matrix in right side <= or => our changed identity matrix
    val inverseMatrix = Array(size) { DoubleArray(size) } // initialize future answer like arr[][]
    for (i in 0 until size) {
        for (j in 0 until size) {
            inverseMatrix[i][j] = augmentedMatrix[i][j + size]
        }
    }
    /*
        *, *, *, -0.26086956521739113, -0.4130434782608696, 0.7173913043478262
        *, *, *, 0.47826086956521735, 0.17391304347826086, -0.5652173913043479
        *, *, *, -0.2608695652173913, 0.08695652173913045, 0.2173913043478261
                 |            This is our inverse matrix                      |
    */

    return inverseMatrix
    //end
}

fun main(){
    val matrix = arrayOf(
        doubleArrayOf(4.0, 7.0, 5.0),
        doubleArrayOf(2.0, 6.0, 9.0),
        doubleArrayOf(4.0, 6.0, 7.0)
    )

    val inverse = inverseMatrix(matrix) //

    if (inverse != null) {
        println("Inverse Matrix:")
        for (row in inverse) {
            println(row.joinToString(", "))
        }
    } else {
        println("The inverse matrix could not be found.")
    }

}