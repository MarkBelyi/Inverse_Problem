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
    /*println("Augmented Matrix")
    for (row in augmentedMatrix) {
        println(row.joinToString(", "))
    }
    println()*/
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

// Transposed matrix
fun transpose(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val transposed = Array(matrix[0].size) { DoubleArray(matrix.size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            transposed[j][i] = matrix[i][j] // 11 -> 11; 12 -> 21; 13 -> 31; and so on
        }
    }
    return transposed
}

// Multiply matrix A by vector y
fun matrixVectorMultiply(A: Array<DoubleArray>, y: DoubleArray): DoubleArray {
    val result = DoubleArray(A.size)
    for (i in A.indices) {
        for (j in A[0].indices) {
            result[i] += A[i][j] * y[j] // we multiply every row of matrix by vector
        }

        /*

        | a  b  c |     | x |   | ax + by + cz |
        | d  e  f | *   | y | = | dx + ey + fz |
        | g  h  i |     | z |   | gx + hy + iz |

        */

    }
    return result
}


// Multiply two matrix
fun matrixMultiply(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray> {
    val result = Array(A.size) { DoubleArray(B[0].size) }
    for (i in A.indices) {
        for (j in B[0].indices) {
            for (k in A[0].indices) {
                result[i][j] += A[i][k] * B[k][j] // every row of first matrix multiply by every column of second
            }
        }
    }
    return result
}


// Tichonov Regularization
fun tichonovRegularization(A: Array<DoubleArray>, y: DoubleArray, lambda: Double): DoubleArray {
    val AT = transpose(matrix = A)
    val AT_A = matrixMultiply(A = AT, B = A)

    // Add regularization parameter for stabilizing (λI)
    for (i in AT_A.indices) {
        AT_A[i][i] += lambda // (A^T * A + λI)
    }

    // (A^T * A + λI)^(-1)
    val AT_A_Inv = inverseMatrix(matrix = AT_A)

    if (AT_A_Inv == null) {
        throw IllegalArgumentException("Could not find the inverse matrix for (A^T * A + λI)")
    }

    // (A^T * A + λI)^(-1) * A^T * y
    val AT_y = matrixVectorMultiply(A = AT, y = y)
    return matrixVectorMultiply(A = AT_A_Inv, y = AT_y)
}




// I remember that you said that we can use ready-made functions. I just remembered what is what
fun main(){

    // Matrix "A"
    val A = arrayOf(
        doubleArrayOf(1.0, 2.0),
        doubleArrayOf(2.0, 4.001)
    )
    // Vector "y"
    val y = doubleArrayOf(3.0, 6.001)
    // Different lambda values
    val lambdas = listOf(0.1, 0.01, 0.001, 0.0001, 0.00001)



    // Direct Solution => x = A^(-1) * y
    // Find Inverse Matrix
    val A_Inv = inverseMatrix(A)

    println("Inverse Matrix")
    for (row in A_Inv!!) {
        println(row.joinToString(", "))
    }
    println()

    if (A_Inv != null) {

        val directSolution = matrixVectorMultiply(A_Inv, y) // A^(-1) * y

        println(message = "Direct solution (without regularization):")
        println(message = directSolution.joinToString(", "))
        println()

    } else {

        println(message = "The inverse matrix could not be found.")
    }



    //Solution with Tichonov regularization for different values of lambda
    for (lambda in lambdas) {

        val regSolution = tichonovRegularization(A = A, y = y, lambda = lambda)

        println("Solution with Tichonov regularization, lambda = $lambda:")
        println(regSolution.joinToString(", "))
        println()

    }

    /*
    As lambda becomes larger, the regularization has effect:
    the solution becomes more stable to the degeneracy of the original matrix A.
    As lambda decreases,
    the regularization becomes weaker and the solution approaches the direct solution.
    This can be useful, when the determinant is close to zero.
    */

}