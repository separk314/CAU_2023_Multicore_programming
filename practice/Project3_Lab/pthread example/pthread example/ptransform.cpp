#include <iostream>
#include <vector>
#include <algorithm>
#include <ctime>
#include <execution>

bool isPrime(int x) {
    if (x <= 1) return false;
    for (int i = 2; i < x; i++) {
        if (x % i == 0) return false;
    }
    return true;
}

int getPrimeNo(int n) {
    int prime_count = 0;

    for (int i = 2; i <= n; i++) {
        if (isPrime(i)) 
            prime_count++;
    }
    std::cout << "No. of Primes (" << n << ") =" << prime_count << "\n";
    return prime_count;
}

int main()
{
    std::vector<int> A = { 100000, 100100, 100200, 100300, 100400, 
                            100500, 100600, 100700, 100800, 100900,
                           101000, 101100, 101200, 101300, 101400, 
                            101500, 101600, 101700, 101800, 101900 };
    std::vector<int> B;
    B.resize(A.size()); // 벡터 B의 크기를 A와 같도록 조정


    auto start_time = std::chrono::high_resolution_clock::now();
    // A의 각 원소에 대해 getPrimeNo 함수를 적용하고, 결과를 B에 저장
    std::transform(A.begin(), A.end(), B.begin(), getPrimeNo);
    auto end_time = std::chrono::high_resolution_clock::now();

    // 시간, 결과 출력
    auto time_diff = end_time - start_time;
    std::cout << "transform time:" << time_diff / std::chrono::milliseconds(1) << "ms\n";
    for (int i = 0; i < B.size(); i++) 
        std::cout << B[i] << "\n";


    start_time = std::chrono::high_resolution_clock::now();
    // 위의 과정을 parallel하게 다시 수행
    std::transform(std::execution::par, A.begin(), A.end(), B.begin(), getPrimeNo);
    end_time = std::chrono::high_resolution_clock::now();
    
    // 시간, 결과 출력
    time_diff = end_time - start_time;
    std::cout << "parallel transform time:" << time_diff / std::chrono::milliseconds(1) << "ms\n";
    for (int i = 0; i < B.size(); i++) 
        std::cout << B[i] << "\n";

    system("pause");    // 콘솔 창이 바로 닫히지 않도록 프로그램 실행을 일시 중지.
    return 0;
}