cmake_minimum_required(VERSION 3.18.1)
project("ffnativo")
add_library(ffnativo SHARED native-lib.cpp)
target_link_libraries(ffnativo log android)
