/*
#include <iostream>
#include <vector>
#include <algorithm>
#include <ctime>
#include <execution>

int main()
{
	std::vector<int> vec(100000000);	// 정수 벡터 생성

	// 벡터를 랜덤 숫자로 채운다.
	std::srand(unsigned(std::time(nullptr)));	// 난수 발생기 초기화
	std::generate(vec.begin(), vec.end(), std::rand);	// 벡터에 난수 채우기

	auto start_time = std::chrono::high_resolution_clock::now();
	std::sort(vec.begin(), vec.end());	// 벡터 정렬
	auto end_time = std::chrono::high_resolution_clock::now();

	// 측정 시간 출력
	auto time_diff = end_time - start_time;
	std::cout << "sorting time: " <<
		time_diff / std::chrono::milliseconds(1) << "ms to run.\n";


	// 벡터에 다시 무작위 숫자를 채운다.
	std::srand(unsigned(std::time(nullptr)));
	std::generate(vec.begin(), vec.end(), std::rand);

	start_time = std::chrono::high_resolution_clock::now();
	std::sort(std::execution::par, vec.begin(), vec.end());	// 병렬로 정렬
	end_time = std::chrono::high_resolution_clock::now();

	// 측정 시간 출력
	time_diff = end_time - start_time;
	std::cout << "parallel sorting time: " <<
		time_diff / std::chrono::milliseconds(1) << "ms to run.\n";


	system("pause");
	return 0;
}
*/