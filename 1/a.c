#include "matrix.h"
#include <errno.h> /* for ENOSYS */
#include <stdlib.h> /* for malloc() and free() and rand() */
#include <stdio.h> /* for fprintf() and fscanf() */
#include <string.h> /* for strtok() */

int matrix_allocate(matrix_t *m, int rows, int columns) {
    if (!m) return -1; // 传入的矩阵指针为空

    m->rows = rows;
    m->columns = columns;

    m->content = (int **)malloc(rows * sizeof(int*));
    if (!m->content) return -1; // 内存分配失败

    for (int i = 0; i < rows; i++) {
        m->content[i] = (int *)malloc(columns * sizeof(int));
        if (!m->content[i]) {
            // 如果某一行的分配失败，需要清理之前已分配的内存
            for (int j = 0; j < i; j++) {
                free(m->content[j]);
            }
            free(m->content);
            return -1;
        }
    }

    return 0; // 分配成功
}

void matrix_free(matrix_t *m) {
    if (!m || !m->content) return;

    for (int i = 0; i < m->rows; i++) {
        free(m->content[i]);
    }
    free(m->content);

    m->rows = 0;
    m->columns = 0;
}

void matrix_init_n(matrix_t *m, int n) {
    // 检查m是否为NULL
    if (!m) return;

    // 使用双重循环遍历矩阵的每一个元素
    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            m->content[i][j] = n; // 将当前元素的值设置为n
        }
    }
}

void matrix_init_zeros(matrix_t *m) {
    // 使用matrix_init_n函数初始化矩阵的所有元素为0
    matrix_init_n(m, 0);
}

int matrix_init_identity(matrix_t *m) {
    if (!m || !m->content) return -1; // 检查矩阵或其内容是否为空
    
    if (m->rows != m->columns) return -1; // 同位矩阵必须是正方形

    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            if (i == j) {
                m->content[i][j] = 1;
            } else {
                m->content[i][j] = 0;
            }
        }
    }
    return 0; // 初始化成功
}

void matrix_init_rand(matrix_t *m, int val_min, int val_max) {
    if (!m || !m->content) return;

    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            m->content[i][j] = val_min + rand() % (val_max - val_min + 1);
        }
    }
}

int matrix_equal(matrix_t *m1, matrix_t *m2) {
    // 检查矩阵是否为NULL
    if (!m1 || !m2) return 0; // 如果有一个矩阵为NULL，则返回0表示不相等

    // 检查矩阵的行数和列数
    if (m1->rows != m2->rows || m1->columns != m2->columns) return 0; // 行数或列数不相等，返回0

    // 检查矩阵的具体内容
    for (int i = 0; i < m1->rows; i++) {
        for (int j = 0; j < m1->columns; j++) {
            if (m1->content[i][j] != m2->content[i][j]) return 0; // 如果发现不相等的元素，返回0
        }
    }

    return 1; // 所有检查都通过，返回1表示两个矩阵相等
}

int matrix_sum(matrix_t *m1, matrix_t *m2, matrix_t *result) {
    // 检查矩阵是否为NULL
    if (!m1 || !m2 || !result) return -1;  // 任何一个矩阵为NULL则返回错误

    // 检查矩阵的行数和列数是否相等
    if (m1->rows != m2->rows || m1->columns != m2->columns) return -1; // 不同维度的矩阵无法相加，返回错误

    // 为结果矩阵分配内存
    if (matrix_allocate(result, m1->rows, m1->columns) != 0) return -1; // 内存分配失败，返回错误

    // 计算矩阵的和
    for (int i = 0; i < m1->rows; i++) {
        for (int j = 0; j < m1->columns; j++) {
            result->content[i][j] = m1->content[i][j] + m2->content[i][j]; // 对应元素相加
        }
    }

    return 0; // 矩阵相加成功，返回0
}

int matrix_scalar_product(matrix_t *m, int scalar, matrix_t *result) {
    if (!m || !m->content || !result) return -EINVAL;  // 检查输入矩阵和结果矩阵是否有效

    // 为结果矩阵分配内存，并确保其与输入矩阵的尺寸相同
    if (matrix_allocate(result, m->rows, m->columns) != 0) return -ENOMEM;

    // 遍历每个矩阵元素并将其乘以给定的标量
    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            result->content[i][j] = m->content[i][j] * scalar;
        }
    }

    return 0;  // 返回成功
}

int matrix_transposition(matrix_t *m, matrix_t *result) {
    if (!m || !m->content || !result) return -EINVAL;  // 检查输入矩阵和结果矩阵是否有效

    // 为结果矩阵分配内存，但注意其行列与输入矩阵相反
    if (matrix_allocate(result, m->columns, m->rows) != 0) return -ENOMEM;

    // 遍历输入矩阵的每个元素，并将其设置为结果矩阵的转置位置
    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            result->content[j][i] = m->content[i][j];
        }
    }

    return 0;  // 返回成功
}

int matrix_product(matrix_t *m1, matrix_t *m2, matrix_t *result) {
    // 检查矩阵是否合法
    if (!m1 || !m2 || !result) return -EINVAL;

    // 两矩阵能够相乘的条件是第一个矩阵的列数等于第二个矩阵的行数
    if (m1->columns != m2->rows) return -EINVAL;

    // 为结果矩阵分配内存
    if (matrix_allocate(result, m1->rows, m2->columns) != 0) return -ENOMEM;

    // 开始计算两个矩阵的乘积
    for (int i = 0; i < m1->rows; i++) {
        for (int j = 0; j < m2->columns; j++) {
            result->content[i][j] = 0; // 初始化结果为0
            for (int k = 0; k < m1->columns; k++) {
                result->content[i][j] += m1->content[i][k] * m2->content[k][j];
            }
        }
    }

    return 0;
}

int matrix_dump_file(matrix_t *m, char *output_file) {
    // 检查传入的矩阵是否为空
    if (!m) return -EINVAL; // 无效参数
    
    // 使用fopen尝试打开文件以写入
    FILE *file = fopen(output_file, "w");
    if (!file) return -1; // 如果无法打开文件，返回错误代码

    // 遍历矩阵的每一行和每一列，写入文件
    for (int i = 0; i < m->rows; i++) {
        for (int j = 0; j < m->columns; j++) {
            // 使用fprintf将每个整数写入文件，并在它们之间添加空格
            fprintf(file, "%d ", m->content[i][j]);
        }
        fprintf(file, "%d\n", m->content[i][m->columns - 1]); // 每行的最后一个数字后面没有空格
    }

    fclose(file); // 关闭文件
    return 0; // 成功返回
}

int matrix_allocate_and_init_file(matrix_t *m, char *input_file) {
    // 检查传入的矩阵是否为空
    if (!m) return -EINVAL; // 无效参数
    
    // 使用fopen尝试打开文件以读取
    FILE *file = fopen(input_file, "r");
    if (!file) return -1; // 如果无法打开文件，返回错误代码
    
    // 读取矩阵的行数和列数
    int rows = 0;
    int columns = 0;
    char line[1000]; // 假设每行不超过1000个字符
    
    // 使用fgets从文件中读取一行数据，每次最多读取sizeof(line)个字符
    while (fgets(line, sizeof(line), file)) {
        // 每次成功读取一行，就增加行数计数
        rows++;

        // 只在读取第一行时统计列数
        if (rows == 1) {
            char temp[1000]; // 为了不修改原始的行数据，我们使用一个临时数组
            strncpy(temp, line, 1000); // 将读取的一行数据复制到临时数组中
            
            // 使用strtok函数来分割字符串，这里以空格为分隔符来分割字符串
            // 首次调用strtok时，传递待分割的字符串
            char *token = strtok(temp, " ");
            while (token) {
                // 每找到一个空格分隔的字符串，列数就加1
                columns++;

                // 继续查找下一个分隔的字符串，这里传递NULL作为strtok的第一个参数，
                // 表示继续之前的字符串分割操作
                token = strtok(NULL, " ");
            }
        }
    }

    // 为矩阵分配内存
    if (matrix_allocate(m, rows, columns) != 0) {
        fclose(file);
        return -ENOMEM; // 内存不足
    }

    // 重置文件指针并开始读取内容
    fseek(file, 0, SEEK_SET);
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            fscanf(file, "%d", &(m->content[i][j]));
        }
    }

    fclose(file); // 关闭文件
    return 0; // 成功返回
}

